package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.token.AccessAndRefreshTokensDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.Optional;

/**
 * Interface used for managing Authentication
 */
public interface AuthenticationServiceInterface {

    // Login methods

    /**
     * This method is used to perform the second step in multifactor authentication, that is
     * to provide authentication code, used for authenticating user in the application.
     *
     * @param code     String, 8 character long text value, which is the authentication code sent to the users e-mail
     *                 address.
     * @param language Language constant set in the user browser.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by exception handling aspects
     *                                  on facade and service layer components.
     */
    void loginUsingAuthenticationCode(String code, String language) throws ApplicationBaseException;

    // Register successful & unsuccessful login attempt methods

    /**
     * This method is used to register successful login attempt made by the user - if first step of authentication is
     * completed then e-mail message with authentication code is sent to the user. If second step of authentication is
     * completed then JWT token is generated and sent to the user.
     *
     * @param userLogin Login of the user that is trying to authenticate in the application.
     * @param confirmed Boolean flag indicating whether user identity is confirmed - used to send e-mail when first step of multifactor authentication is completed.
     * @param ipAddress Logical IPv4 address, which the user is authenticating from.
     * @param language  Language constant, read as a setting in the user's browser.
     * @return Data transfer object containing a JWT token is returned if user identity is confirmed, that is after the authentication code
     * is entered and validated, and refresh token used to refresh user session. Otherwise, it sends e-mail message containing the
     * authentication code.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by exception handling aspects
     *                                  on facade components.
     */
    AccessAndRefreshTokensDTO registerSuccessfulLoginAttempt(String userLogin, boolean confirmed, String ipAddress, String language) throws ApplicationBaseException;

    /**
     * This method is used to register unsuccessful login attempt made by the user - specifically when credentials
     * entered by the user are invalid (that causes the unsuccessfulLoginCounter to increment, hence the name of the method).
     *
     * @param userLogin Login of the user that is trying to authenticate in the application.
     * @param ipAddress Logical IPv4 address, which the user is authenticating from.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by exception handling aspects
     *                                  on facade components.
     */
    void registerUnsuccessfulLoginAttemptWithIncrement(String userLogin, String ipAddress) throws ApplicationBaseException;

    /**
     * This method is used to register unsuccessful login attempt made by the user - specifically when user account
     * could not be authenticated to other reasons, such as the account being not active or blocked. That should not
     * increase the unsuccessfulLoginCounter as authentication could not be performed.
     *
     * @param userLogin Login of the user that is trying to authenticate in the application.
     * @param ipAddress Logical IPv4 address, which the user is authenticating from.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by exception handling aspects
     *                                  on facade components.
     */
    void registerUnsuccessfulLoginAttemptWithoutIncrement(String userLogin, String ipAddress) throws ApplicationBaseException;

    // Refresh user session method

    /**
     * This method is used to refresh user session in the application using refreshToken sent to the
     * client while logging in.
     *
     * @param refreshToken String identified as refreshToken, used to refresh user session in the application,
     *                     sent to the client during logging in.
     * @param userLogin    Login of the currently authenticated in the application user.
     * @return Data transfer object containing new access token (used for authentication purposes) and refresh token
     * (used for refreshing user session).
     * @throws ApplicationBaseException General superclass for all exceptions thrown by exception handling aspects
     *                                  on facade components.
     */
    AccessAndRefreshTokensDTO refreshUserSession(String refreshToken, String userLogin) throws ApplicationBaseException;

    // Read methods

    /**
     * Retrieves an Account with given login.
     *
     * @param login Login of the Account to be retrieved.
     * @return Returns Account with the specified login.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by exception handling aspects
     *                                  on facade components.
     */
    Optional<Account> findByLogin(String login) throws ApplicationBaseException;
}
