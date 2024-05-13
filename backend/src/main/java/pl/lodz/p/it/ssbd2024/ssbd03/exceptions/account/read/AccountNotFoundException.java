package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an Exception related with retrieving an Account from database.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
public class AccountNotFoundException extends AccountBaseException {

    public AccountNotFoundException() {
        super(I18n.ACCOUNT_NOT_FOUND_EXCEPTION);
    }

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(Throwable cause) {
        super(I18n.ACCOUNT_NOT_FOUND_EXCEPTION, cause);
    }

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
