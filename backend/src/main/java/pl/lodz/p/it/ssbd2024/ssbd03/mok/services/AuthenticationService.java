package pl.lodz.p.it.ssbd2024.ssbd03.mok.services;

import org.springframework.beans.factory.annotation.Autowired;
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
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.ActivityLogUpdateException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.AuthenticationAccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.AuthenticationInvalidCredentialsException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AuthenticationFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

@Service
public class AuthenticationService {

    private final AuthenticationFacade authenticationFacade;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(AuthenticationFacade authenticationFacade,
                                 AuthenticationManager authenticationManager) {
        this.authenticationFacade = authenticationFacade;
        this.authenticationManager = authenticationManager;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateActivityLog(Account account, ActivityLog activityLog) throws ActivityLogUpdateException {
        try {
            Account refreshedAccount = authenticationFacade.findAndRefresh(account.getId()).orElseThrow(AccountNotFoundException::new);
            refreshedAccount.setActivityLog(activityLog);
            authenticationFacade.edit(refreshedAccount);
        } catch (AccountNotFoundException exception) {
            throw new ActivityLogUpdateException(I18n.AUTH_ACTIVITY_LOG_UPDATE_EXCEPTION);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Account login(String login, String password) throws AuthenticationAccountNotFoundException, AuthenticationInvalidCredentialsException {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authenticationFacade.findByLogin(login).orElseThrow(() -> new AccountNotFoundException(I18n.AUTH_ACCOUNT_LOGIN_NOT_FOUND_EXCEPTION));
        } catch (AccountNotFoundException exception) {
            throw new AuthenticationAccountNotFoundException(exception.getMessage(), exception);
        } catch (BadCredentialsException exception) {
            throw new AuthenticationInvalidCredentialsException(I18n.AUTH_CREDENTIALS_INVALID_EXCEPTION, exception);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Account findByLogin(String login) throws AuthenticationAccountNotFoundException {
        try {
            return this.authenticationFacade.findByLogin(login).orElseThrow(() -> new AccountNotFoundException(I18n.AUTH_ACCOUNT_LOGIN_NOT_FOUND_EXCEPTION));
        } catch (AccountNotFoundException exception) {
            throw new AuthenticationAccountNotFoundException(exception.getMessage(), exception);
        }
    }
}