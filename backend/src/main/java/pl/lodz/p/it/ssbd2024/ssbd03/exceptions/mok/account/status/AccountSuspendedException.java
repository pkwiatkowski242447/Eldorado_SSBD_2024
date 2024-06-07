package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.status;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an Exception related to the fact that user account has been suspended
 * (and the majority of actions could not be performed).
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
public class AccountSuspendedException extends AccountStatusException {

    public AccountSuspendedException() {
        super(I18n.ACCOUNT_SUSPENDED_EXCEPTION);
    }

    public AccountSuspendedException(String message) {
        super(message);
    }

    public AccountSuspendedException(Throwable cause) {
        super(I18n.ACCOUNT_SUSPENDED_EXCEPTION, cause);
    }

    public AccountSuspendedException(String message, Throwable cause) {
        super(message, cause);
    }
}
