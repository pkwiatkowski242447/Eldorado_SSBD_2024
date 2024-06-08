package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.read;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an Exception related with not finding user account object
 * by given e-mail address in the database.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
public class AccountEmailNotFoundException extends AccountNotFoundException {

    public AccountEmailNotFoundException() {
        super(I18n.ACCOUNT_EMAIL_NOT_FOUND);
    }

    public AccountEmailNotFoundException(Throwable cause) {
        super(I18n.ACCOUNT_EMAIL_NOT_FOUND, cause);
    }
}
