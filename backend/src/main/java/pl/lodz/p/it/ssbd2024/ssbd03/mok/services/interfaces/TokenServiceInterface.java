package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Interface used for managing tokens.
 */
public interface TokenServiceInterface {

    /**
     * Creates and persists registration token for the Account.
     *
     * @param account Account for which the token is created.
     * @return Returns newly created registration token value.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by exception handling
     * aspects in the facade layer.
     */
    String createRegistrationToken(Account account) throws ApplicationBaseException;

    /**
     * Creates and persists E-mail confirmation Token for the Account.
     *
     * @param account Account for which the token is created.
     * @return Token's value(JWT).
     * @throws ApplicationBaseException General superclass for all exceptions thrown by exception handling aspects in facade
     * layer.
     */
    String createEmailConfirmationToken(Account account, String email) throws ApplicationBaseException;

    /**
     * Removes token from the database if exists.
     *
     * @param token Confirmation token to be removed
     */
    void removeAccountsEmailConfirmationToken(String token);

    /**
     * Creates and persists password reset token for the Account.
     *
     * @param account Account for which the token is created.
     * @return Returns newly created password reset token value.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by exception handling aspects in facade
     * layer.
     */
    String createPasswordResetToken(Account account) throws ApplicationBaseException;
}
