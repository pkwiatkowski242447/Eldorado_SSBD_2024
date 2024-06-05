package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.conflict;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class AccountAlreadyUnblockedException extends AccountConflictException {

    public AccountAlreadyUnblockedException() {
        super(I18n.ACCOUNT_ALREADY_UNBLOCKED);
    }
}