package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.ActivityLog;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.InvalidLoginAttemptException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountBlockedByAdminException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountBlockedByFailedLoginAttemptsException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountNotActivatedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.AuthenticationAccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.AuthenticationInvalidCredentialsException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AuthenticationFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.AuthenticationServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

import java.time.LocalDateTime;

/**
 * Service managing authentication.
 */
@Slf4j
@Service
public class AuthenticationService implements AuthenticationServiceInterface {

    @Value("${account.maximum.failed.login.attempt.counter}")
    private int failedLoginAttemptMaxVal;

    /**
     * Facade component used for operations on accounts.
     */
    private final AuthenticationFacade authenticationFacade;

    /**
     * Facade component used for operations conducted on activity log object inside account object instance.
     */
    private final AccountMOKFacade accountMOKFacade;

    /**
     * AuthenticationManager used for authenticate user.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * MailProvider used for sending emails.
     */
    private final MailProvider mailProvider;

    /**
     * JWT Provider used for generation JWT token, after successful authentication.
     */
    private final JWTProvider jwtProvider;

    /**
     * Autowired constructor for the service.
     *
     * @param authenticationFacade  Facade used for reading users accounts information for authentication purposes.
     * @param authenticationManager Spring Security component, responsible for performing authentication in the application.
     */
    @Autowired
    public AuthenticationService(AuthenticationFacade authenticationFacade,
                                 AccountMOKFacade accountMOKFacade,
                                 AuthenticationManager authenticationManager,
                                 MailProvider mailProvider,
                                 JWTProvider jwtProvider) {
        this.authenticationFacade = authenticationFacade;
        this.accountMOKFacade = accountMOKFacade;
        this.authenticationManager = authenticationManager;
        this.mailProvider = mailProvider;
        this.jwtProvider = jwtProvider;
    }

    /**
     * Authenticates a user in the system.
     *
     * @param login    Login of the Account.
     * @param password Password to the Account.
     * @return Returns an Account with the given credentials.
     * @throws AuthenticationAccountNotFoundException    Threw when there is no Account with given login.
     * @throws AuthenticationInvalidCredentialsException Threw when credentials don't match any account.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String login(String login, String password, String ipAddr) throws ApplicationBaseException {
        String token = "";
        Account account = authenticationFacade.findByLogin(login).orElseThrow(InvalidLoginAttemptException::new);
        try {
            ActivityLog activityLog = account.getActivityLog();
            if (!account.getActive() || account.getBlocked()) {
                activityLog.setLastUnsuccessfulLoginIp(ipAddr);
                activityLog.setLastUnsuccessfulLoginTime(LocalDateTime.now());
                account.setActivityLog(activityLog);
                authenticationFacade.edit(account);
                if (!account.getActive()) {
                    throw new AccountNotActivatedException();
                } else if (account.getBlockedTime() != null) {
                    throw new AccountBlockedByAdminException();
                } else {
                    throw new AccountBlockedByFailedLoginAttemptsException();
                }
            } else if (activityLog.getUnsuccessfulLoginCounter() > this.failedLoginAttemptMaxVal) {
                throw new AccountBlockedByFailedLoginAttemptsException();
            } else {
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                activityLog.setLastSuccessfulLoginIp(ipAddr);
                activityLog.setLastSuccessfulLoginTime(LocalDateTime.now());
                activityLog.setUnsuccessfulLoginCounter(0);
                account.setActivityLog(activityLog);
                authenticationFacade.edit(account);
                token = jwtProvider.generateJWTToken(account);
            }
        } catch (BadCredentialsException exception) {
            Account refreshedAccount = authenticationFacade.findAndRefresh(account.getId()).orElseThrow(InvalidLoginAttemptException::new);
            ActivityLog activityLog = refreshedAccount.getActivityLog();
            activityLog.setLastUnsuccessfulLoginIp(ipAddr);
            activityLog.setLastUnsuccessfulLoginTime(LocalDateTime.now());
            activityLog.setUnsuccessfulLoginCounter(activityLog.getUnsuccessfulLoginCounter() + 1);
            refreshedAccount.setActivityLog(activityLog);
            authenticationFacade.edit(refreshedAccount);
            mailProvider.sendBlockAccountInfoEmail(refreshedAccount.getName(), refreshedAccount.getLastname(),
                    refreshedAccount.getEmail(), account.getAccountLanguage(), false);
            throw new InvalidLoginAttemptException();
        }
        return token;
    }

    /**
     * Retrieves an Account with given login.
     *
     * @param login Login of the Account to be retrieved.
     * @return Returns Account with the specified login.
     * @throws AuthenticationAccountNotFoundException Threw when there is no Account with given login.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Account findByLogin(String login) throws AuthenticationAccountNotFoundException {
        try {
            return this.authenticationFacade.findByLogin(login).orElseThrow(() -> new AccountNotFoundException(I18n.AUTH_ACCOUNT_LOGIN_NOT_FOUND_EXCEPTION));
        } catch (AccountNotFoundException exception) {
            throw new AuthenticationAccountNotFoundException(exception.getMessage(), exception);
        }
    }
}