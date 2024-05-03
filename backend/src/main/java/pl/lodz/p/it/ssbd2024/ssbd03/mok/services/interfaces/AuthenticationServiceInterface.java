package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.ActivityLog;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.ActivityLogUpdateException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.AuthenticationAccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.AuthenticationInvalidCredentialsException;

public interface AuthenticationServiceInterface {

    void updateActivityLog(Account account, ActivityLog activityLog) throws ActivityLogUpdateException;
    Account login(String login, String password) throws AuthenticationAccountNotFoundException, AuthenticationInvalidCredentialsException;
    Account findByLogin(String login) throws AuthenticationAccountNotFoundException;
}
