package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.resetOwnPassword;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class PasswordPreviouslyUsedException extends ResetOwnPasswordException {
    public PasswordPreviouslyUsedException() {
        super(I18n.PASSWORD_PREVIOUSLY_USED);
    }
}
