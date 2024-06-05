package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.read;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an Exception related with not finding user account object
 * by given id address in the database.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
public class AccountIdNotFoundException extends AccountNotFoundException {

    public AccountIdNotFoundException() {
        super(I18n.ACCOUNT_ID_NOT_FOUND);
    }

    public AccountIdNotFoundException(Throwable cause) {
        super(I18n.ACCOUNT_ID_NOT_FOUND, cause);
    }
}
