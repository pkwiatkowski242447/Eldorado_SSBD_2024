package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

public class TokenBaseException extends ApplicationBaseException {

    public TokenBaseException() {
    }

    public TokenBaseException(String message) {
        super(message);
    }

    public TokenBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenBaseException(Throwable cause) {
        super(cause);
    }
}
