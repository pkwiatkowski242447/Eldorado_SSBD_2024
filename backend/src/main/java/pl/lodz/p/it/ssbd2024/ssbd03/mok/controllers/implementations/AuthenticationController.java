package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.RollbackException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountLoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AuthenticationCodeDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationDatabaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationOptimisticLockException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.InvalidLoginAttemptException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountBlockedByAdminException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountBlockedByFailedLoginAttemptsException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountNotActivatedException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces.AuthenticationControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.AuthenticationServiceInterface;

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
     * AuthenticationManager used for authenticate user.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Autowired constructor for the controller.
     *
     * @param authenticationService Service used for authentication purposes.
     */
    @Autowired
    public AuthenticationController(AuthenticationServiceInterface authenticationService,
                                    AuthenticationManager authenticationManager) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
    }

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
    @Override
    @PostMapping(value = "/login-credentials", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = { ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class })
    @Operation(summary = "Enter credentials", description = "This endpoint is used to perform first step in multifactor authentication in the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "First step of multifactor authentication was successful. Since user enabled only one factor authentication, then acces token is returned."),
            @ApiResponse(responseCode = "204", description = "First step of multifactor authentication was successful. Now enter authentication code for the second step."),
            @ApiResponse(responseCode = "400", description = "User account is blocked, and therefore could not be used authenticated."),
            @ApiResponse(responseCode = "401", description = "Given credentials were invalid."),
            @ApiResponse(responseCode = "500", description = "Unknown exception occurred during logging attempt.")
    })
    public ResponseEntity<?> loginUsingCredentials(@RequestBody AccountLoginDTO accountLoginDTO, HttpServletRequest request) throws ApplicationBaseException {
        try {
            Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accountLoginDTO.getLogin(),
                    accountLoginDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = this.authenticationService.registerSuccessfulLoginAttempt(accountLoginDTO.getLogin(), false,
                    request.getRemoteAddr(), accountLoginDTO.getLanguage());
            if (accessToken != null) return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(accessToken);
        } catch (BadCredentialsException badCredentialsException) {
            this.authenticationService.registerUnsuccessfulLoginAttemptWithIncrement(accountLoginDTO.getLogin(), request.getRemoteAddr());
            throw new InvalidLoginAttemptException();
        } catch (DisabledException disabledException) {
            this.authenticationService.registerUnsuccessfulLoginAttemptWithoutIncrement(accountLoginDTO.getLogin(), request.getRemoteAddr());
            throw new AccountNotActivatedException();
        } catch (LockedException lockedException) {
            this.authenticationService.registerUnsuccessfulLoginAttemptWithoutIncrement(accountLoginDTO.getLogin(), request.getRemoteAddr());
            Account account = this.authenticationService.findByLogin(accountLoginDTO.getLogin()).orElseThrow(InvalidLoginAttemptException::new);
            if (account.getBlockedTime() != null) {
                throw new AccountBlockedByAdminException();
            } else {
                throw new AccountBlockedByFailedLoginAttemptsException();
            }
        } catch (AuthenticationException authenticationException) {
            throw new InvalidLoginAttemptException();
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Allows an unauthenticated user to perform the second step in multifactor authentication,
     * which is to provide authentication code, which was sent to the users e-mail address.
     *
     * @param authenticationCodeDTO Data transfer object containing authentication code.
     * @param request               HTTP Request in which the credentials.
     * @return In case of successful logging in returns HTTP 200 OK is returned.
     * If any problems occur returns HTTP 400 BAD REQUEST with the problem description.
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     *                                  layer of facade and service components in the application.
     */
    @Override
    @PostMapping(value = "/login-auth-code", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = { ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class })
    @Operation(summary = "Enter authentication code", description = "This endpoint is used to perform second step in multifactor authentication in the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Full authentication process was successful."),
            @ApiResponse(responseCode = "400", description = "User account is blocked, and therefore could not be used authenticated."),
            @ApiResponse(responseCode = "401", description = "Given credentials were invalid."),
            @ApiResponse(responseCode = "500", description = "Unknown exception occurred during logging attempt.")
    })
    public ResponseEntity<?> loginUsingAuthenticationCode(@RequestBody AuthenticationCodeDTO authenticationCodeDTO, HttpServletRequest request) throws ApplicationBaseException {
        try {
            this.authenticationService.loginUsingAuthenticationCode(authenticationCodeDTO.getUserLogin(), authenticationCodeDTO.getAuthCodeValue());
        } catch (ApplicationBaseException applicationBaseException) {
            this.authenticationService.registerUnsuccessfulLoginAttemptWithoutIncrement(authenticationCodeDTO.getUserLogin(), request.getRemoteAddr());
            throw applicationBaseException;
        }
        String accessToken = this.authenticationService.registerSuccessfulLoginAttempt(authenticationCodeDTO.getUserLogin(), true,
                request.getRemoteAddr(), authenticationCodeDTO.getLanguage());
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(accessToken);
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
