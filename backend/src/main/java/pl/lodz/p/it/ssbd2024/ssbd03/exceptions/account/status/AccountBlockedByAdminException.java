package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class AccountBlockedByAdminException extends AccountBlockedException {
    public AccountBlockedByAdminException() {
        super(I18n.ACCOUNT_BLOCKED_BY_ADMIN);
    }
}
