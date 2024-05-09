package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read;

/**
 * Used to specify an Exception related with not finding user account object in the database.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
public class AccountNotFoundException extends AccountReadException {

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
