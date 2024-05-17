package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.RollbackException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountLoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AuthenticationCodeDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationDatabaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationOptimisticLockException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.InvalidLoginAttemptException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountBlockedByAdminException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountBlockedByFailedLoginAttemptsException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountNotActivatedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.validation.AccountConstraintViolationException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces.AuthenticationControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.AuthenticationServiceInterface;

import java.time.LocalDateTime;

/**
 * Controller used for authentication in the system.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController implements AuthenticationControllerInterface {

    @Value("${account.maximum.failed.login.attempt.counter}")
    private int loginFailedAttemptMaxCount;

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
     * @param authenticationManager Spring Security component used to create Authentication object while authenticating
     *                              user in the application.
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
     * @return In case of successful logging in it returns 200 OK (if the enabled authentication mode is only one factor)
     * or returns HTTP 204 NO CONTENT (in multifactor authentication). When account is blocked or not active
     * then 400 BAD REQUEST is returned. When user credentials are invalid or account is not found 401 UNAUTHORIZED is returned.
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     *                                  layer of facade and service components in the application.
     */
    @Override
    @PostMapping(value = "/login-credentials", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Roles.ANONYMOUS})
    @TxTracked
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = AccountConstraintViolationException.class)
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Enter credentials", description = "This endpoint is used to perform first step in multifactor authentication in the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "First step of multifactor authentication was successful. Since user enabled only one factor authentication, then acces token is returned."),
            @ApiResponse(responseCode = "204", description = "First step of multifactor authentication was successful. Now enter authentication code for the second step."),
            @ApiResponse(responseCode = "400", description = "User account is blocked, and therefore could not be used authenticated."),
            @ApiResponse(responseCode = "401", description = "Given credentials were invalid."),
            @ApiResponse(responseCode = "500", description = "Unknown exception occurred during logging attempt.")
    })
    public ResponseEntity<?> loginUsingCredentials(@RequestHeader(value = "X-Forwarded-For", required = false) String proxyChain, @RequestBody AccountLoginDTO accountLoginDTO, HttpServletRequest request) throws ApplicationBaseException {
        String sourceAddress = getSourceAddress(proxyChain, request);
        try {
            Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accountLoginDTO.getLogin(),
                    accountLoginDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = this.authenticationService.registerSuccessfulLoginAttempt(accountLoginDTO.getLogin(), false,
                   sourceAddress, accountLoginDTO.getLanguage());
            if (accessToken != null) {
                log.info("User: %s successfully authenticated during one factor authentication in the application, starting session at %s from IPv4: %s"
                        .formatted(accountLoginDTO.getLogin(), LocalDateTime.now().toString(),sourceAddress));
                return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(accessToken);
            }
            log.debug("User: %s successfully authenticated in the first step of multifactor authentication at %s from IPv4: %s"
                    .formatted(accountLoginDTO.getLogin(), LocalDateTime.now().toString(),sourceAddress));
        } catch (BadCredentialsException badCredentialsException) {
            this.authenticationService.registerUnsuccessfulLoginAttemptWithIncrement(accountLoginDTO.getLogin(),sourceAddress);
            log.error("Authentication to user account with login: %s at %s from IPv4: %s was not successful. Cause: invalid login credentials."
                    .formatted(accountLoginDTO.getLogin(), LocalDateTime.now().toString(),sourceAddress));
            throw new InvalidLoginAttemptException();
        } catch (DisabledException disabledException) {
            this.authenticationService.registerUnsuccessfulLoginAttemptWithoutIncrement(accountLoginDTO.getLogin(),sourceAddress);
            log.error("Authentication to user account with login: %s at %s from IPv4: %s was not successful. Cause: User account has not been activated."
                    .formatted(accountLoginDTO.getLogin(), LocalDateTime.now().toString(),sourceAddress));
            throw new AccountNotActivatedException();
        } catch (LockedException lockedException) {
            this.authenticationService.registerUnsuccessfulLoginAttemptWithoutIncrement(accountLoginDTO.getLogin(),sourceAddress);
            Account account = this.authenticationService.findByLogin(accountLoginDTO.getLogin()).orElseThrow(InvalidLoginAttemptException::new);
            if (account.getBlockedTime() == null) {
                log.error("Authentication to user account with login: %s at %s from IPv4: %s was not successful. Cause: User account has been blocked by the admin."
                        .formatted(accountLoginDTO.getLogin(), LocalDateTime.now().toString(),sourceAddress));
                throw new AccountBlockedByAdminException();
            } else {
                log.error("Authentication to user account with login: %s at %s from IPv4: %s was not successful. Cause: User account has been blocked by logging unsuccessfully %d amount of time."
                        .formatted(accountLoginDTO.getLogin(), LocalDateTime.now().toString(),sourceAddress, this.loginFailedAttemptMaxCount));
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
    @RolesAllowed({Roles.ANONYMOUS})
    @TxTracked
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = AccountConstraintViolationException.class)
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Enter authentication code", description = "This endpoint is used to perform second step in multifactor authentication in the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Full authentication process was successful."),
            @ApiResponse(responseCode = "400", description = "User account is blocked, and therefore could not be used authenticated."),
            @ApiResponse(responseCode = "401", description = "Given credentials were invalid."),
            @ApiResponse(responseCode = "500", description = "Unknown exception occurred during logging attempt.")
    })
    public ResponseEntity<?> loginUsingAuthenticationCode(@RequestHeader(value = "X-Forwarded-For", required = false) String proxyChain, @RequestBody AuthenticationCodeDTO authenticationCodeDTO, HttpServletRequest request) throws ApplicationBaseException {
        String sourceAddress = getSourceAddress(proxyChain, request);
        try {
            this.authenticationService.loginUsingAuthenticationCode(authenticationCodeDTO.getUserLogin(), authenticationCodeDTO.getAuthCodeValue());
        } catch (ApplicationBaseException applicationBaseException) {
            log.error("Authentication to user account with login: %s at %s from IPv4: %s in the second step of multifactor authentication was not successful."
                    .formatted(authenticationCodeDTO.getUserLogin(), LocalDateTime.now(),sourceAddress));
            this.authenticationService.registerUnsuccessfulLoginAttemptWithoutIncrement(authenticationCodeDTO.getUserLogin(),sourceAddress);
            throw applicationBaseException;
        }
        String accessToken = this.authenticationService.registerSuccessfulLoginAttempt(authenticationCodeDTO.getUserLogin(), true,
               sourceAddress, authenticationCodeDTO.getLanguage());
        log.info("User: %s successfully authenticated during two factor authentication in the application, starting session at %s from IPv4: %s"
                .formatted(SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now().toString(),sourceAddress));
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
    @RolesAllowed({Roles.AUTHENTICATED})
    @Operation(summary = "Log out", description = "This endpoint is used to log out a user from the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Logging out previously authenticated user was successful."),
    })
    public ResponseEntity<?> logout(@RequestHeader(value = "X-Forwarded-For", required = false) String proxyChain, HttpServletRequest request, HttpServletResponse response) {
        String sourceAddress = getSourceAddress(proxyChain, request);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        log.info("User: %s successfully logged out from the application at %s from IPv4: %s, ending their session in the application."
                .formatted(userName, LocalDateTime.now().toString(),sourceAddress));
        return ResponseEntity.noContent().build();
    }

    private String getSourceAddress(String proxyChain, HttpServletRequest request){
        if(proxyChain != null) {
            return proxyChain.indexOf(',') == -1 ? proxyChain : proxyChain.split(",")[0];
        } else {
            return request.getRemoteAddr();
        }
    }
}
