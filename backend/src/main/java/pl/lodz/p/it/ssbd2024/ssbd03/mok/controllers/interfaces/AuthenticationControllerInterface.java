package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AuthenticationCodeDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountLoginDTO;
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
     * @param accountLoginDTO User's credentials.
     * @param request         HTTP Request in which the credentials.
     * @return In case of successful logging in returns HTTP 204 NO CONTENT is returned.
     * If any problems occur returns HTTP 400 BAD REQUEST with the problem description.
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     *                                  layer of facade and service components in the application.
     */
    ResponseEntity<?> loginUsingCredentials(AccountLoginDTO accountLoginDTO, HttpServletRequest request) throws ApplicationBaseException;

    /**
     * Allows an unauthenticated user to perform the second step in multifactor authentication,
     * which is to provide authentication code, which was sent to the users e-mail address.
     *
     * @param AuthenticationCodeDTO Data transfer object containing authentication code.
     * @param request               HTTP Request in which the credentials.
     * @return In case of successful logging in returns HTTP 200 OK is returned.
     * If any problems occur returns HTTP 400 BAD REQUEST with the problem description.
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     *                                  layer of facade and service components in the application.
     */
    ResponseEntity<?> loginUsingAuthenticationCode(AuthenticationCodeDTO AuthenticationCodeDTO, HttpServletRequest request) throws ApplicationBaseException;

    /**
     * Allows authenticated user to log out from the system.
     *
     * @param request  HTTP Request in which the credentials
     * @param response HTTP Response object, associated with the response.
     * @return         It returns an HTTP response with 204 NO CONTENT if logout was successful.
     */
    ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response);
}
