package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.request;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Used to specify an Exception related with handling incorrect request.
 */
public class InvalidRequestHeaderException extends ApplicationBaseException {
    public InvalidRequestHeaderException() {
    }

    public InvalidRequestHeaderException(String message) {
        super(message);
    }

    public InvalidRequestHeaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestHeaderException(Throwable cause) {
        super(cause);
    }
}
