package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account;

public class AccountEmailChangeException extends Exception
{
    public AccountEmailChangeException(String message) {
        super(message);
    }

    public AccountEmailChangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
