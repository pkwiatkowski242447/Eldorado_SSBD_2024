package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.ActivityLog;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.ActivityLogUpdateException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.AuthenticationAccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.AuthenticationInvalidCredentialsException;

/**
 * Interface used for managing Authentication
 */
public interface AuthenticationServiceInterface {

    /**
     * Authenticates a user in the system.
     *
     * @param login    Login of the Account.
     * @param password Password to the Account.
     * @return Returns an Account with the given credentials.
     * @throws AuthenticationAccountNotFoundException    Threw when there is no Account with given login.
     * @throws AuthenticationInvalidCredentialsException Threw when credentials don't match any account.
     */
    String login(String login, String password, String ipAddr) throws ApplicationBaseException;

    /**
     * Retrieves an Account with given login.
     *
     * @param login Login of the Account to be retrieved.
     * @return Returns Account with the specified login.
     * @throws AuthenticationAccountNotFoundException Threw when there is no Account with given login.
     */
    Account findByLogin(String login) throws AuthenticationAccountNotFoundException;
}
