package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account;

public class AccountSameEmailException extends Exception{
    public AccountSameEmailException(String message) {
        super(message);
    }

    public AccountSameEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
