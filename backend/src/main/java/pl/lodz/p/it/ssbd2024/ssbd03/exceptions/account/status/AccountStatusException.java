package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountBaseException;

/**
 * Used to specify an Exception related with certain status of user account object.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
public class AccountStatusException extends AccountBaseException {
    public AccountStatusException(String message) {
        super(message);
    }

    public AccountStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
