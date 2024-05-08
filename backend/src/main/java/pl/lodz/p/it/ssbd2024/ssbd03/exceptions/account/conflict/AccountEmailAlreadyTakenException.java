package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class AccountEmailAlreadyTakenException extends AccountConflictException {
    public AccountEmailAlreadyTakenException() {
        super(I18n.ACCOUNT_EMAIL_ALREADY_TAKEN);
    }
}
