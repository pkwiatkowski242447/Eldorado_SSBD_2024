package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Used to specify an Exception related to an attempt to perform an illegal action.
 */
public class IllegalOperationException extends ApplicationBaseException {

    public IllegalOperationException(String message) {
        super(message);
    }

    public IllegalOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
