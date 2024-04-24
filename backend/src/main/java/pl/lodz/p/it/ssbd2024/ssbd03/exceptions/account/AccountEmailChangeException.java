package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account;

/**
 * Used to specify an Exception related with changing an e-mail of an Account.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
public class AccountEmailChangeException extends Exception
{
    public AccountEmailChangeException(String message) {
        super(message);
    }

    public AccountEmailChangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
