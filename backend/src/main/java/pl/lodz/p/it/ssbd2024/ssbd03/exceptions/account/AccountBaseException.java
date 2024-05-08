package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

public class AccountBaseException extends ApplicationBaseException {

    public AccountBaseException() {
    }

    public AccountBaseException(String message) {
        super(message);
    }

    public AccountBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountBaseException(Throwable cause) {
        super(cause);
    }
}
