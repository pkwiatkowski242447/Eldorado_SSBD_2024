package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account;

/**
 * Used to specify an Exception related to repeating the e-mail address in the database.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
public class AccountSameEmailException extends Exception{
    public AccountSameEmailException(String message) {
        super(message);
    }

    public AccountSameEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
