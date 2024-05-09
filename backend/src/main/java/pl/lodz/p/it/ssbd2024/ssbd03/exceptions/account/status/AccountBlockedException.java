package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an Exception related to the fact that user account has been blocked
 * (and some actions could not be performed).
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
public class AccountBlockedException extends AccountStatusException {

    public AccountBlockedException() {
        super(I18n.ACCOUNT_BLOCKED_EXCEPTION);
    }

    public AccountBlockedException(Throwable cause) {
        super(I18n.ACCOUNT_BLOCKED_EXCEPTION, cause);
    }
}
