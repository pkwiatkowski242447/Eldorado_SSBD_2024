package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.conflict;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class AccountAlreadyBlockedException extends AccountConflictException {

    public AccountAlreadyBlockedException() {
        super(I18n.ACCOUNT_ALREADY_BLOCKED);
    }
}