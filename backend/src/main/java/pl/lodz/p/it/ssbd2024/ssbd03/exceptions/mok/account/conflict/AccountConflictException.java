package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.conflict;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.AccountBaseException;

public class AccountConflictException extends AccountBaseException {
    public AccountConflictException() {
    }

    public AccountConflictException(String message) {
        super(message);
    }

    public AccountConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountConflictException(Throwable cause) {
        super(cause);
    }
}
