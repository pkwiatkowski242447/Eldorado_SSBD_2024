package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.read;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Used to specify an Exception related with null value of the email address.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
public class AccountEmailNullException extends ApplicationBaseException
{
    public AccountEmailNullException(String message) {
        super(message);
    }

    public AccountEmailNullException(String message, Throwable cause) {
        super(message, cause);
    }
}
