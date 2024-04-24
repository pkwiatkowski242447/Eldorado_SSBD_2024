package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account;

public class AccountAlreadyBlockedException extends Exception {
    public AccountAlreadyBlockedException() {
        super();
    }

    public AccountAlreadyBlockedException(String message) {
        super(message);
    }
}