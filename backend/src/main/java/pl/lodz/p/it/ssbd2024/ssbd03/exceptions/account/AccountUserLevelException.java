package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account;

public class AccountUserLevelException extends Exception {
    public AccountUserLevelException(String message) {
        super(message);
    }

    public AccountUserLevelException(String message, Throwable cause) {
        super(message, cause);
    }
}
