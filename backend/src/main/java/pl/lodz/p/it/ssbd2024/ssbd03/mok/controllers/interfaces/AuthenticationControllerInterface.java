package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountLoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Interface used for managing Authentication process.
 */
public interface AuthenticationControllerInterface {

    /**
     * Allows an unauthenticated user to log in to the system.
     *
     * @param accountLoginDTO User's credentials.
     * @param request         HTTP Request in which the credentials.
     * @return                It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> login(AccountLoginDTO accountLoginDTO, HttpServletRequest request) throws ApplicationBaseException;

    /**
     * Allows authenticated user to log out from the system.
     *
     * @param request  HTTP Request in which the credentials
     * @param response
     * @return         It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response);
}
