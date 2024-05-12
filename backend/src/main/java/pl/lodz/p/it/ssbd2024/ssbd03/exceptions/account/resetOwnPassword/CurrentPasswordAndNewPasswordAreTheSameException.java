package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.resetOwnPassword;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class CurrentPasswordAndNewPasswordAreTheSameException extends ResetOwnPasswordException {
    public CurrentPasswordAndNewPasswordAreTheSameException() {
        super(I18n.SET_NEW_PASSWORD_IS_THE_SAME_AS_CURRENT_ONE);
    }
}
