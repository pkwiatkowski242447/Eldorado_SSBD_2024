package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.ActivityLog;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationInternalServerErrorException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.InvalidLoginAttemptException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountBlockedByAdminException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountBlockedByFailedLoginAttemptsException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountNotActivatedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.validation.AccountConstraintViolationException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.TokenNotValidException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.read.TokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AuthenticationFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenAuthFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.AuthenticationServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

/**
 * Service managing authentication.
 */
@Slf4j
@Service
@TxTracked
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = AccountConstraintViolationException.class)
public class AuthenticationService implements AuthenticationServiceInterface {

    @Value("${account.maximum.failed.login.attempt.counter}")
    private int failedLoginAttemptMaxVal;


    /**
     * Facade component used for operations on accounts.
     */
    private final AuthenticationFacade authenticationFacade;

    /**
     * Token facade, used for token manipulation in the database.
     */
    private final TokenAuthFacade tokenFacade;

    /**
     * Password encoder, used for generating hashes for tokens containing auth codes.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * MailProvider used for sending emails.
     */
    private final MailProvider mailProvider;

    /**
     * JWT Provider used for generation JWT token, after successful authentication.
     */
    private final JWTProvider jwtProvider;

    final TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator(Duration.of(30, ChronoUnit.SECONDS), 8);
    private Key key;

    /**
     * Autowired constructor for the service.
     *
     * @param authenticationFacade Facade used for reading users accounts information for authentication purposes.
     * @param tokenFacade          Facade used for inserting, deleting and reading token objects from the database.
     * @param passwordEncoder      Component, responsible for generating hashes for given authentication code, and verifying them.
     * @param jwtProvider          Component, responsible for generating JWT tokens with given content, and for given amount of time.
     * @param mailProvider         Component used for sending e-mail messages.
     */
    @Autowired
    public AuthenticationService(AuthenticationFacade authenticationFacade,
                                 TokenAuthFacade tokenFacade,
                                 PasswordEncoder passwordEncoder,
                                 MailProvider mailProvider,
                                 JWTProvider jwtProvider) {
        this.authenticationFacade = authenticationFacade;
        this.tokenFacade = tokenFacade;
        this.passwordEncoder = passwordEncoder;
        this.mailProvider = mailProvider;
        this.jwtProvider = jwtProvider;
    }

    @PostConstruct
    public void generateKey() throws NoSuchAlgorithmException {
        final KeyGenerator keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());

        // Key length should match the length of the HMAC output (160 bits for SHA-1, 256 bits
        // for SHA-256, and 512 bits for SHA-512). Note that while Mac#getMacLength() returns a
        // length in _bytes,_ KeyGenerator#init(int) takes a key length in _bits._
        final int macLengthInBytes = Mac.getInstance(totp.getAlgorithm()).getMacLength();
        keyGenerator.init(macLengthInBytes * 8);

        key = keyGenerator.generateKey();
    }
    /**
     * This method is used to perform the second step in multifactor authentication, that is
     * to verify the provided authentication code, used for authenticating user in the application.
     *
     * @param login Login of the Account.
     * @param code  8 character long string value, which is the authentication code sent to the users e-mail address.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by exception handling aspects
     *                                  on facade components.
     */
    @Override
    @RolesAllowed({Roles.ANONYMOUS})
    public void loginUsingAuthenticationCode(String login, String code) throws ApplicationBaseException {
        Account account = this.authenticationFacade.findByLogin(login).orElseThrow(InvalidLoginAttemptException::new);
        if (!account.getActive()) {
            throw new AccountNotActivatedException();
        } else if (account.getBlocked() && account.getBlockedTime() == null) {
            throw new AccountBlockedByAdminException();
        } else if (account.getBlocked()) {
            throw new AccountBlockedByFailedLoginAttemptsException();
        }

        Token token = this.tokenFacade.findByTypeAndAccount(Token.TokenType.MULTI_FACTOR_AUTHENTICATION_CODE, account.getId()).orElseThrow(TokenNotFoundException::new);
        String hashedAuthenticationCode;
        try{
            hashedAuthenticationCode = this.jwtProvider.extractHashedCodeValueFromToken(token.getTokenValue());
        } catch (TokenNotValidException exception){
            tokenFacade.remove(token);
            throw exception;
        }
        if (!jwtProvider.isMultiFactorAuthTokenValid(token.getTokenValue()) ||
                !passwordEncoder.matches(code, hashedAuthenticationCode))
            throw new TokenNotValidException();
        this.tokenFacade.remove(token);
    }

    /**
     * This method is used to register successful login attempt made by the user - if first step of authentication is
     * completed then e-mail message with authentication code is sent to the user. If second step of authentication is
     * completed then JWT token is generated and sent to the user.
     *
     * @param userLogin Login of the user that is trying to authenticate in the application.
     * @param confirmed Boolean flag indicating whether user identity is confirmed - used to send e-mail when first step of multifactor authentication is completed.
     * @param ipAddress Logical IPv4 address, which the user is authenticating from.
     * @param language  Language constant, read as a setting in the user's browser.
     * @return String containing a JWT token is returned if user identity is confirmed, that is after the authentication code is entered and
     * validated. Otherwise, it sends e-mail message containing the authentication code.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by exception handling aspects
     *                                  on facade components.
     */
    @Override
    @RolesAllowed({Roles.CLIENT, Roles.STAFF, Roles.ADMIN, Roles.ANONYMOUS})
    public String registerSuccessfulLoginAttempt(String userLogin, boolean confirmed, String ipAddress, String language) throws ApplicationBaseException {
        Account account = this.authenticationFacade.findByLogin(userLogin).orElseThrow(InvalidLoginAttemptException::new);
        if (!confirmed && account.getTwoFactorAuth()) {
            this.generateAndSendEmailMessageWithAuthenticationCode(account);
            return null;
        }
        ActivityLog activityLog = account.getActivityLog();
        activityLog.setLastSuccessfulLoginIp(ipAddress);
        activityLog.setLastSuccessfulLoginTime(LocalDateTime.now());
        activityLog.setUnsuccessfulLoginCounter(0);
        account.setActivityLog(activityLog);
        account.setAccountLanguage(language);
        authenticationFacade.edit(account);
        return jwtProvider.generateJWTToken(account);
    }

    /**
     * This method is used to register unsuccessful login attempt made by the user - specifically when credentials
     * entered by the user are invalid (that causes the unsuccessfulLoginCounter to increment, hence the name of the method).
     *
     * @param userLogin Login of the user that is trying to authenticate in the application.
     * @param ipAddress Logical IPv4 address, which the user is authenticating from.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by exception handling aspects
     *                                  on facade components.
     */
    @Override
    @RolesAllowed({Roles.ANONYMOUS})
    public void registerUnsuccessfulLoginAttemptWithIncrement(String userLogin, String ipAddress) throws ApplicationBaseException {
        Account account = this.authenticationFacade.findByLogin(userLogin).orElseThrow(InvalidLoginAttemptException::new);
        ActivityLog activityLog = account.getActivityLog();
        activityLog.setLastUnsuccessfulLoginIp(ipAddress);
        activityLog.setLastUnsuccessfulLoginTime(LocalDateTime.now());
        activityLog.setUnsuccessfulLoginCounter(activityLog.getUnsuccessfulLoginCounter() + 1);
        account.setActivityLog(activityLog);

        if (!account.getBlocked() && activityLog.getUnsuccessfulLoginCounter() >= this.failedLoginAttemptMaxVal) {
            account.blockAccount(false);
            mailProvider.sendBlockAccountInfoEmail(account.getName(), account.getLastname(),
                    account.getEmail(), account.getAccountLanguage(), false);
        }

        authenticationFacade.edit(account);
    }

    /**
     * This method is used to register unsuccessful login attempt made by the user - specifically when user account
     * could not be authenticated to other reasons, such as the account being not active or blocked. That should not
     * increase the unsuccessfulLoginCounter as authentication could not be performed.
     *
     * @param userLogin Login of the user that is trying to authenticate in the application.
     * @param ipAddress Logical IPv4 address, which the user is authenticating from.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by exception handling aspects
     *                                  on facade components.
     */
    @Override
    @RolesAllowed({Roles.ANONYMOUS})
    public void registerUnsuccessfulLoginAttemptWithoutIncrement(String userLogin, String ipAddress) throws ApplicationBaseException {
        Account account = this.authenticationFacade.findByLogin(userLogin).orElseThrow(InvalidLoginAttemptException::new);
        ActivityLog activityLog = account.getActivityLog();
        activityLog.setLastUnsuccessfulLoginIp(ipAddress);
        activityLog.setLastUnsuccessfulLoginTime(LocalDateTime.now());
        account.setActivityLog(activityLog);
        authenticationFacade.edit(account);
    }

    /**
     * This method is used to generate authentication code for multifactor authentication, and sends it via
     * e-mail message to the e-mail address connected to the account, which user try to authenticate to.
     *
     * @param account User account, which the authentication code is generated for.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by exception handling aspects
     *                                  on facade components.
     */
    @RolesAllowed({Roles.ANONYMOUS})
    private void generateAndSendEmailMessageWithAuthenticationCode(Account account) throws ApplicationBaseException {
        tokenFacade.findByTypeAndAccount(Token.TokenType.MULTI_FACTOR_AUTHENTICATION_CODE, account.getId()).ifPresent(tokenFacade::remove);

        String authCodeValue;
        try {
            authCodeValue = totp.generateOneTimePasswordString(key, Instant.now());
        }catch (InvalidKeyException e) {
            throw new ApplicationInternalServerErrorException();
        }

        String hashedAuthCode = passwordEncoder.encode(authCodeValue);
        String tokenValue = this.jwtProvider.generateMultiFactorAuthToken(hashedAuthCode);

        mailProvider.sendTwoFactorAuthCode(account.getName(),
                account.getLastname(),
                authCodeValue,
                account.getEmail(),
                account.getAccountLanguage());

        Token multiFactorAuthToken = new Token(tokenValue, account, Token.TokenType.MULTI_FACTOR_AUTHENTICATION_CODE);
        this.tokenFacade.create(multiFactorAuthToken);
    }

    /**
     * Retrieves an Account with given login.
     *
     * @param login Login of the Account to be retrieved.
     * @return Returns Account with the specified login.
     */
    @Override
    @RolesAllowed({Roles.ANONYMOUS})
    public Optional<Account> findByLogin(String login) {
        return this.authenticationFacade.findByLogin(login);
    }
}