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
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.token.AccessAndRefreshTokensDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.ActivityLog;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationInternalServerErrorException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountAuthenticationException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.InvalidLoginAttemptException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountBlockedByAdminException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountBlockedByFailedLoginAttemptsException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountNotActivatedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.validation.AccountConstraintViolationException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.TokenNotValidException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.read.TokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AuthenticationFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenAuthFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.AuthenticationServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.TokenProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
     * Token provider component used for automatic generation of token
     * of given type.
     */
    private final TokenProvider tokenProvider;

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
                                 JWTProvider jwtProvider,
                                 TokenProvider tokenProvider) {
        this.authenticationFacade = authenticationFacade;
        this.tokenFacade = tokenFacade;
        this.passwordEncoder = passwordEncoder;
        this.mailProvider = mailProvider;
        this.jwtProvider = jwtProvider;
        this.tokenProvider = tokenProvider;
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

    // Login methods

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
        try {
            hashedAuthenticationCode = this.jwtProvider.extractHashedCodeValueFromToken(token.getTokenValue());
        } catch (TokenNotValidException exception) {
            tokenFacade.remove(token);
            throw exception;
        }
        if (!jwtProvider.isMultiFactorAuthTokenValid(token.getTokenValue()) ||
                !passwordEncoder.matches(code, hashedAuthenticationCode))
            throw new TokenNotValidException();
        this.tokenFacade.remove(token);
    }

    // Register successful & unsuccessful login attempt methods

    @Override
    @RolesAllowed(Authorities.MOK2)
    public AccessAndRefreshTokensDTO registerSuccessfulLoginAttempt(String userLogin, boolean confirmed, String ipAddress, String language) throws ApplicationBaseException {
        Account account = this.authenticationFacade.findByLogin(userLogin).orElseThrow(InvalidLoginAttemptException::new);
        if (!confirmed && account.getTwoFactorAuth()) {
            this.generateAndSendEmailMessageWithAuthenticationCode(account);
            return null;
        }
        tokenFacade.removeByTypeAndAccount(Token.TokenType.REFRESH_TOKEN, account.getId());

        String accessToken = jwtProvider.generateJWTToken(account);
        Token refreshTokenObject = tokenProvider.generateRefreshToken(account);
        tokenFacade.create(refreshTokenObject);

        ActivityLog activityLog = account.getActivityLog();
        activityLog.setLastSuccessfulLoginIp(ipAddress);
        activityLog.setLastSuccessfulLoginTime(LocalDateTime.now());
        activityLog.setUnsuccessfulLoginCounter(0);
        account.setActivityLog(activityLog);
        account.setAccountLanguage(language);
        authenticationFacade.edit(account);

        return new AccessAndRefreshTokensDTO(accessToken, refreshTokenObject.getTokenValue());
    }

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

    // Refresh user session method

    @RolesAllowed({Roles.AUTHENTICATED})
    public AccessAndRefreshTokensDTO refreshUserSession(String refreshToken, String userLogin) throws ApplicationBaseException {
        // Retrieve user account and token object from database
        Account foundAccount = this.authenticationFacade.findByLogin(userLogin).orElseThrow(AccountNotFoundException::new);
        Token refreshTokenObject = this.tokenFacade.findByTokenValue(refreshToken).orElseThrow(TokenNotFoundException::new);

        // If account is blocked or inactive - then throw exception
        if (!foundAccount.couldAuthenticate()) {
            throw new AccountAuthenticationException();
        }

        // If token is not valid - then throw exception
        if (!jwtProvider.isTokenValid(refreshTokenObject.getTokenValue(), foundAccount)) {
            throw new TokenNotValidException();
        }

        // Generate a new pair of access token and refresh token.
        Token newRefreshTokenObject = this.tokenProvider.generateRefreshToken(foundAccount);
        String newAccessToken = this.jwtProvider.generateJWTToken(foundAccount);

        // Remove old refresh token from database and add new refresh token to database.
        tokenFacade.remove(refreshTokenObject);
        tokenFacade.create(newRefreshTokenObject);

        return new AccessAndRefreshTokensDTO(newAccessToken, newRefreshTokenObject.getTokenValue());
    }

    // Read methods

    @Override
    @RolesAllowed({Roles.ANONYMOUS})
    public Optional<Account> findByLogin(String login) throws ApplicationBaseException {
        return this.authenticationFacade.findByLogin(login);
    }

    // Other private methods

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
        tokenFacade.removeByTypeAndAccount(Token.TokenType.MULTI_FACTOR_AUTHENTICATION_CODE, account.getId());

        String authCodeValue;
        try {
            authCodeValue = totp.generateOneTimePasswordString(key, Instant.now());
        } catch (InvalidKeyException e) {
            throw new ApplicationInternalServerErrorException();
        }

        String hashedAuthCode = passwordEncoder.encode(authCodeValue);
        Token multiFactorAuthToken = this.tokenProvider.generateMultiFactorAuthToken(account, hashedAuthCode);

        mailProvider.sendTwoFactorAuthCode(account.getName(),
                account.getLastname(),
                authCodeValue,
                account.getEmail(),
                account.getAccountLanguage());

        this.tokenFacade.create(multiFactorAuthToken);
    }
}