package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.ActivityLog;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.ActivityLogUpdateException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.AuthenticationAccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.AuthenticationInvalidCredentialsException;

/**
 * Interface used for managing Authentication
 */
public interface AuthenticationServiceInterface {

    /**
     * Updates the activity log for the specified Account. When the number of failed logins exceeds the allowed number,
     * the account is blocked and an email notification is sent.
     *
     * @param account     Account which ActivityLog is to be updated.
     * @param activityLog Updated ActivityLog.
     * @throws ActivityLogUpdateException Threw when problem retrieving Account occurs.
     */
    void updateActivityLog(Account account, ActivityLog activityLog) throws ActivityLogUpdateException;

    /**
     * Authenticates a user in the system.
     *
     * @param login    Login of the Account.
     * @param password Password to the Account.
     * @return Returns an Account with the given credentials.
     * @throws AuthenticationAccountNotFoundException    Threw when there is no Account with given login.
     * @throws AuthenticationInvalidCredentialsException Threw when credentials don't match any account.
     */
    Account login(String login, String password) throws AuthenticationAccountNotFoundException, AuthenticationInvalidCredentialsException;

    /**
     * Retrieves an Account with given login.
     *
     * @param login Login of the Account to be retrieved.
     * @return Returns Account with the specified login.
     * @throws AuthenticationAccountNotFoundException Threw when there is no Account with given login.
     */
    Account findByLogin(String login) throws AuthenticationAccountNotFoundException;
}
