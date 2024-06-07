package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.authentication;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

public class AuthenticationBaseException extends ApplicationBaseException {
    public AuthenticationBaseException() {
    }

    public AuthenticationBaseException(String message) {
        super(message);
    }

    public AuthenticationBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationBaseException(Throwable cause) {
        super(cause);
    }
}
