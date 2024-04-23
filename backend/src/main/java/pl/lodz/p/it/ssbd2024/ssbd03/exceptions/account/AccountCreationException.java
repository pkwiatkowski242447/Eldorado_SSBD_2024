package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account;

/**
 * Used to specify an Exception related with creating an Account in the database.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
public class AccountCreationException extends Exception
{
    public AccountCreationException(String message) {
        super(message);
    }

    public AccountCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
