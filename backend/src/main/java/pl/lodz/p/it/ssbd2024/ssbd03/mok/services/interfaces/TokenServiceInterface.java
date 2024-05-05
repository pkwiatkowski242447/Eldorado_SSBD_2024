package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;

/**
 * Interface used for managing tokens.
 */
public interface TokenServiceInterface {

    /**
     * Creates and persists registration token for the Account.
     *
     * @param account Account for which the token is created.
     * @return Returns newly created registration token value.
     */
    String createRegistrationToken(Account account);

    /**
     * Creates and persists E-mail confirmation Token for the Account.
     *
     * @param account Account for which the token is created.
     * @return Token's value(JWT).
     */
    String createEmailConfirmationToken(Account account, String email);

    /**
     * Removes token from the database if exists.
     *
     * @param token Confirmation token to be removed
     */
    void removeAccountsEmailConfirmationToken(String token);
}
