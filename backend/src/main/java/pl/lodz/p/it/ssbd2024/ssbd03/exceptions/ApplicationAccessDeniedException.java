package pl.lodz.p.it.ssbd2024.ssbd03.exceptions;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class ApplicationAccessDeniedException extends ApplicationBaseException {

    public ApplicationAccessDeniedException() {
        super(I18n.ACCESS_DENIED_EXCEPTION);
    }

    public ApplicationAccessDeniedException(String message) {
        super(message);
    }

    public ApplicationAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationAccessDeniedException(Throwable cause) {
        super(I18n.ACCESS_DENIED_EXCEPTION, cause);
    }
}
