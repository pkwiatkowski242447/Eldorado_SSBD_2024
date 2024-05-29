package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an Exception related to mapping unsupported security role.
 */
public class UnsupportedRoleException extends ApplicationBaseException {

    public UnsupportedRoleException() {
        super(I18n.UNSUPPORTED_ROLE_EXCEPTION);
    }

    public UnsupportedRoleException(String message) {
        super(message);
    }
}
