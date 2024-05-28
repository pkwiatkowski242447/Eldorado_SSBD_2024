package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class AccountRestoreAccessException extends ApplicationBaseException {

    public AccountRestoreAccessException() {
        super(I18n.ACCOUNT_RESTORE_ACCESS_EXCEPTION);
    }

    public AccountRestoreAccessException(String message) {
        super(message);
    }

    public AccountRestoreAccessException(Throwable cause) {
        super(I18n.ACCOUNT_RESTORE_ACCESS_EXCEPTION, cause);
    }

    public AccountRestoreAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
