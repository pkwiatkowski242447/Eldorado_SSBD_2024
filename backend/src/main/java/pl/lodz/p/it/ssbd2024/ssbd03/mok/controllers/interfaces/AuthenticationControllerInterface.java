package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.authentication.AuthenticationCodeDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.authentication.AuthenticationLoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.token.RefreshTokenDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Interface used for managing Authentication process.
 */
public interface AuthenticationControllerInterface {

    /**
     * Allows an unauthenticated user to perform the first step in multifactor authentication,
     * which is to provide credentials to check users identity and for generating the code, which is sent
     * to the users e-mail address.
     *
     * @param proxyChain      X-Forwarded-For header content. If set to null source ip address will be extracted from request.
     * @param accountLoginDTO User's credentials.
     * @param request         HTTP Request in which the credentials.
     * @return In case of successful logging in it returns 200 OK (if the enabled authentication mode is only one factor)
     * or returns HTTP 204 NO CONTENT (in multifactor authentication). When account is blocked or not active
     * then 400 BAD REQUEST is returned. When user credentials are invalid or account is not found 401 UNAUTHORIZED is returned.
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     *                                  layer of facade and service components in the application.
     */
    ResponseEntity<?> loginUsingCredentials(String proxyChain, AuthenticationLoginDTO accountLoginDTO, HttpServletRequest request) throws ApplicationBaseException;

    /**
     * Allows an unauthenticated user to perform the second step in multifactor authentication,
     * which is to provide authentication code, which was sent to the users e-mail address.
     *
     * @param proxyChain            X-Forwarded-For header content. If set to null source ip address will be extracted from request.
     * @param authenticationCodeDTO Data transfer object containing authentication code.
     * @param request               HTTP Request in which the credentials.
     * @return In case of successful logging in returns HTTP 200 OK is returned.
     * If any problems occur returns HTTP 400 BAD REQUEST with the problem description.
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     *                                  layer of facade and service components in the application.
     */
    ResponseEntity<?> loginUsingAuthenticationCode(String proxyChain, AuthenticationCodeDTO authenticationCodeDTO, HttpServletRequest request) throws ApplicationBaseException;

    /**
     * Allows authenticated user to refresh session in the application, after authentication, performed while logging in.
     * After refreshing session new access token and refresh token are generated.
     *
     * @param proxyChain      Header containing IPv4 address of the user.
     * @param refreshTokenDTO Data transfer object containing refresh token, used for refreshing session of authenticated
     *                        user in the application.
     * @param request         HTTP Request in which the credentials.
     * @return Data transfer object containing access token (used for authentication purposes) and refresh token (for
     * refreshing authenticated user session).
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     *                                  layer of facade and service components in the application.
     */
    ResponseEntity<?> refreshUserSession(String proxyChain, RefreshTokenDTO refreshTokenDTO, HttpServletRequest request) throws ApplicationBaseException;

    /**
     * This method is used to log out from the application, in a situation
     * when user was previously authenticated.
     *
     * @param proxyChain X-Forwarded-For header content. If set to null source ip address will be extracted from request.
     * @param request    HttpRequest object, associated with the current request.
     * @param response   HttpResponse object, .
     * @return 204 OK is returned when user is logged out successfully.
     */
    ResponseEntity<?> logout(String proxyChain, HttpServletRequest request, HttpServletResponse response);
}
