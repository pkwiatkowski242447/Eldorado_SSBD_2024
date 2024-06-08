package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.conflict;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class AccountSameEmailException  extends AccountConflictException{
    public AccountSameEmailException() {
        super(I18n.ACCOUNT_SAME_EMAIL_EXCEPTION);
    }
}
