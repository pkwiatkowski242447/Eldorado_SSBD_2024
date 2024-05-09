package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.old;

public class AccountCreationException extends Exception {
    public AccountCreationException(String message) {
        super(message);
    }

    public AccountCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
