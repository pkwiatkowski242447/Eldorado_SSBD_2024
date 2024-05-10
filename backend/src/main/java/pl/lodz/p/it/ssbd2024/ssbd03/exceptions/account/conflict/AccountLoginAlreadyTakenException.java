package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class AccountLoginAlreadyTakenException extends AccountConflictException {
    public AccountLoginAlreadyTakenException() {
        super(I18n.ACCOUNT_LOGIN_ALREADY_TAKEN);
    }
}
