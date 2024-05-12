package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class AccountBlockedByFailedLoginAttemptsException extends AccountBlockedException {
    public AccountBlockedByFailedLoginAttemptsException() {
        super(I18n.ACCOUNT_BLOCKED_BY_FAILED_LOGIN_ATTEMPTS);
    }
}
