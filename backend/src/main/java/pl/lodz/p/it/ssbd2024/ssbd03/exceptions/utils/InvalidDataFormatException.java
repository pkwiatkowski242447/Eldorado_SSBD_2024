package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an Exception related to receiving data in bad format.
 */
public class InvalidDataFormatException extends ApplicationBaseException {
    public InvalidDataFormatException() {
        super(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
    }

    public InvalidDataFormatException(String message) {
        super(message);
    }
}
