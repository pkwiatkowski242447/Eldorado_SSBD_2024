package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an Exception related to the fact that user account has not been activated
 * (and the majority of actions could not be performed).
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
public class AccountNotActivatedException extends AccountStatusException {

    public AccountNotActivatedException() {
        super(I18n.ACCOUNT_INACTIVE_EXCEPTION);
    }

    public AccountNotActivatedException(Throwable cause) {
        super(I18n.ACCOUNT_INACTIVE_EXCEPTION, cause);
    }
}
