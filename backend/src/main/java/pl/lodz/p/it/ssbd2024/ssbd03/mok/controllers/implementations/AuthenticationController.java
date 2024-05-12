package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountLoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces.AuthenticationControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.AuthenticationServiceInterface;

/**
 * Controller used for authentication in the system.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController implements AuthenticationControllerInterface {

    @Value("${logout.exit.page.url}")
    private String exitPageUrl;

    /**
     * AuthenticationServiceInterface used for authentication purposes.
     */
    private final AuthenticationServiceInterface authenticationService;

    /**
     * Autowired constructor for the controller.
     *
     * @param authenticationService Service used for authentication purposes.
     */
    @Autowired
    public AuthenticationController(AuthenticationServiceInterface authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Allows an unauthenticated user to log in to the system.
     *
     * @param accountLoginDTO User's credentials.
     * @param request         HTTP Request in which the credentials.
     * @return In case of successful logging in returns HTTP 200 OK and JWT later used to keep track of a session.
     * If any problems occur returns HTTP 400 BAD REQUEST and JSON containing information about the problem.
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     *                                  layer of facade and service components in the application.
     */
    @Override
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Log in", description = "This endpoint is used to authenticate in the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication with given credentials was successful."),
            @ApiResponse(responseCode = "400", description = "User account is blocked, and therefore could not be used authenticated."),
            @ApiResponse(responseCode = "401", description = "Given credentials were invalid."),
            @ApiResponse(responseCode = "500", description = "Unknown exception occurred during logging attempt.")
    })
    public ResponseEntity<?> login(@RequestBody AccountLoginDTO accountLoginDTO, HttpServletRequest request) throws ApplicationBaseException {
        String token = this.authenticationService.login(accountLoginDTO.getLogin(), accountLoginDTO.getPassword(), request.getRemoteAddr());
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(token);
    }

    /**
     * This method is used to log out from the application, in a situation
     * when user was previously authenticated.
     *
     * @param request  HttpRequest object, associated with the current request.
     * @param response HttpResponse object, .
     * @return 204 OK is returned when user is logged out successfully.
     */
    @Override
    @PostMapping(value = "/logout")
    @Operation(summary = "Log out", description = "This endpoint is used to log out a user from the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Logging out previously authenticated user was successful."),
    })
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.noContent().build();
    }
}
