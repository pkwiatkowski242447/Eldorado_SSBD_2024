package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.old;

/**
 * Used to specify an Exception related with retrieving an Account from database.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
public class AccountNotFoundException extends Exception {

    public AccountNotFoundException() {
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
}
