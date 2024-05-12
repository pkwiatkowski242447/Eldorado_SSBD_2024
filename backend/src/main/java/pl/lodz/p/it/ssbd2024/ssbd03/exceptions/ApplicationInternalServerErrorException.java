package pl.lodz.p.it.ssbd2024.ssbd03.exceptions;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class ApplicationInternalServerErrorException extends ApplicationBaseException {

    public ApplicationInternalServerErrorException() {
        super(I18n.INTERNAL_SERVER_ERROR);
    }
}
