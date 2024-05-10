package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.validation;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountBaseException;

public class AccountValidationException extends AccountBaseException {
    public AccountValidationException() {
    }

    public AccountValidationException(String message) {
        super(message);
    }

    public AccountValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountValidationException(Throwable cause) {
        super(cause);
    }
}
