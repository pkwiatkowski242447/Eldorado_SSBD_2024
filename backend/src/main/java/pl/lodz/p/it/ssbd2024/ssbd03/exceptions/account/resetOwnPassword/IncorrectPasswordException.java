package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.resetOwnPassword;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class IncorrectPasswordException extends ResetOwnPasswordException {
    public IncorrectPasswordException() {
        super(I18n.INCORRECT_PASSWORD);
    }
}
