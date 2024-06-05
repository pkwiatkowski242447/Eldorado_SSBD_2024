package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class InvalidLoginAttemptException extends AccountBaseException {
    public InvalidLoginAttemptException() {
        super(I18n.INVALID_LOGIN_ATTEMPT_EXCEPTION);
    }
}
