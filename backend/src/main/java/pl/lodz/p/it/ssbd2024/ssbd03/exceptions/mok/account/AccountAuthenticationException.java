package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class AccountAuthenticationException extends AccountBaseException {

    public AccountAuthenticationException() {
        super(I18n.ACCOUNT_AUTHENTICATION_EXCEPTION);
    }

    public AccountAuthenticationException(String message) {
        super(message);
    }

    public AccountAuthenticationException(Throwable cause) {
        super(I18n.ACCOUNT_AUTHENTICATION_EXCEPTION, cause);
    }

    public AccountAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
