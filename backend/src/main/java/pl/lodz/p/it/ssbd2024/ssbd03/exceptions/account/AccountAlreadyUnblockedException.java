package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account;

public class AccountAlreadyUnblockedException extends Exception {
    public AccountAlreadyUnblockedException() {
        super();
    }

    public AccountAlreadyUnblockedException(String message) {
        super(message);
    }
}