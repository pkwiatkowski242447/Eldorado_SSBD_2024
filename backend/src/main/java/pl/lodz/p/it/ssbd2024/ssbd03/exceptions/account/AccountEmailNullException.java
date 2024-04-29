package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account;

/**
 * Used to specify an Exception related with null value of the email address.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
public class AccountEmailNullException extends Exception
{
    public AccountEmailNullException(String message) {
        super(message);
    }

    public AccountEmailNullException(String message, Throwable cause) {
        super(message, cause);
    }
}
