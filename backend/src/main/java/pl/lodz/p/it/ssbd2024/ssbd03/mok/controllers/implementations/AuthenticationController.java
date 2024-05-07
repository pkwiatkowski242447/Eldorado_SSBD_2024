package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountLoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.ActivityLog;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.ActivityLogUpdateException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.AuthenticationAccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.AuthenticationInvalidCredentialsException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces.AuthenticationControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.AuthenticationServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;

import java.time.LocalDateTime;

/**
 * Controller used for authentication in the system.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController implements AuthenticationControllerInterface {

    /**
     * AuthenticationServiceInterface used for authentication purposes.
     */
    private final AuthenticationServiceInterface authenticationService;
    /**
     * JWTProviderInterface used for operations on JWT TOKEN.
     */
    private final JWTProvider jwtProvider;

    /**
     * Autowired constructor for the controller.
     *
     * @param authenticationService Service used for authentication purposes.
     * @param jwtProvider           Component used in order to generate JWT tokens with specified payload.
     */
    @Autowired
    public AuthenticationController(AuthenticationServiceInterface authenticationService,
                                    JWTProvider jwtProvider) {
        this.authenticationService = authenticationService;
        this.jwtProvider = jwtProvider;
    }

    /**
     * Allows an unauthenticated user to log in to the system.
     *
     * @param accountLoginDTO User's credentials.
     * @param request         HTTP Request in which the credentials.
     * @return In case of successful logging in returns HTTP 200 OK and JWT later used to keep track of a session.
     * If any problems occur returns HTTP 400 BAD REQUEST and JSON containing information about the problem.
     */
    @Override
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Log in", description = "This endpoint is used to authenticate in the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication with given credentials was successful."),
            @ApiResponse(responseCode = "400", description = "Given credentials were invalid or some unknown exception occurred while processing the request."),
            @ApiResponse(responseCode = "404", description = "User account with given login could not be found")
    })
    public ResponseEntity<?> login(@RequestBody AccountLoginDTO accountLoginDTO, HttpServletRequest request) {
        try {
            try {
                Account account = this.authenticationService.login(accountLoginDTO.getLogin(), accountLoginDTO.getPassword());
                ActivityLog activityLog = account.getActivityLog();
                String responseMessage;
                if (account.getActive() && !account.getBlocked()) {
                    activityLog.setLastSuccessfulLoginIp(request.getRemoteAddr());
                    activityLog.setLastSuccessfulLoginTime(LocalDateTime.now());
                    activityLog.setUnsuccessfulLoginCounter(0);
                    authenticationService.updateActivityLog(account, activityLog);
                    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jwtProvider.generateJWTToken(account));
                } else if (!account.getActive()) {
                    responseMessage = I18n.AUTH_CONTROLLER_ACCOUNT_NOT_ACTIVE;
                } else {
                    responseMessage = I18n.AUTH_CONTROLLER_ACCOUNT_BLOCKED;
                }
                activityLog.setLastUnsuccessfulLoginIp(request.getRemoteAddr());
                activityLog.setLastUnsuccessfulLoginTime(LocalDateTime.now());
                authenticationService.updateActivityLog(account, activityLog);
                return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(responseMessage);
            } catch (AuthenticationInvalidCredentialsException exception) {
                Account account = this.authenticationService.findByLogin(accountLoginDTO.getLogin());
                ActivityLog activityLog = account.getActivityLog();
                activityLog.setLastUnsuccessfulLoginIp(request.getRemoteAddr());
                activityLog.setLastUnsuccessfulLoginTime(LocalDateTime.now());

                // Increment the number of failed login attempts
                activityLog.setUnsuccessfulLoginCounter(activityLog.getUnsuccessfulLoginCounter() + 1);

                authenticationService.updateActivityLog(account, activityLog);
                return ResponseEntity.badRequest().body(exception.getMessage());
            }
        } catch (AuthenticationAccountNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } catch (ActivityLogUpdateException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    /**
     * This method is used to log out from the application, in a situation
     * when user was previously authenticated.
     *
     * @param request   HttpRequest object, associated with the current request.
     * @param response  HttpResponse object, .
     *
     * @return 200 OK is returned when user is logged out successfully. Otherwise, when any exception is thrown, then
     * 400 BAD REQUEST is returned.
     */
    @Override
    @PostMapping(value = "/logout")
    @Operation(summary = "Log out", description = "This endpoint is used to log out a user from the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logging out previously authenticated user was successful."),
            @ApiResponse(responseCode = "400", description = "Unknown exception occurred while processing the request."),
    })
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(I18n.AUTH_CONTROLLER_ACCOUNT_LOGOUT);
        }
    }
}
