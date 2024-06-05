package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.read;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.AccountBaseException;

/**
 * Used to specify an Exception related with reading Account object from the database.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
public class AccountReadException extends AccountBaseException {

    public AccountReadException(String message) {
        super(message);
    }

    public AccountReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
