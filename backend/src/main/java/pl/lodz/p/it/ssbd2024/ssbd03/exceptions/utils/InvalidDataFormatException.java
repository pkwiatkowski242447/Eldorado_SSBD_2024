package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Used to specify an Exception related to receiving data in bad format.
 */
public class InvalidDataFormatException extends ApplicationBaseException {
    public InvalidDataFormatException() {
    }

    public InvalidDataFormatException(String message) {
        super(message);
    }
}
