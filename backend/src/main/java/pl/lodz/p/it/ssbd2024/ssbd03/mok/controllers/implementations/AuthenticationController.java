package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.RollbackException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.token.AccessAndRefreshTokensDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.authentication.AuthenticationLoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.authentication.AuthenticationCodeDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.token.RefreshTokenDTO;
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
     * AuthenticationManager used for authenticating user in the application (Spring Security component).
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
     * Allows an unauthenticated user to perform the first step in the multifactor authentication,
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
    @PostMapping(value = "/login-credentials", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Roles.ANONYMOUS})
    @TxTracked
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = AccountConstraintViolationException.class)
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Enter credentials", description = "This endpoint is used to perform first step in multifactor authentication in the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "First step of multifactor authentication was successful. Since user enabled only one factor authentication, then access token is returned."),
            @ApiResponse(responseCode = "204", description = "First step of multifactor authentication was successful. Now enter authentication code for the second step."),
            @ApiResponse(responseCode = "400", description = "User account is blocked, and therefore could not be used authenticated."),
            @ApiResponse(responseCode = "401", description = "Given credentials were invalid."),
            @ApiResponse(responseCode = "500", description = "Unknown exception occurred during logging attempt.")
    })
    public ResponseEntity<?> loginUsingCredentials(@RequestHeader(value = "X-Forwarded-For", required = false) String proxyChain,
                                                   @Valid @RequestBody AuthenticationLoginDTO accountLoginDTO, HttpServletRequest request) throws ApplicationBaseException {
        String sourceAddress = getSourceAddress(proxyChain, request);
        try {
            Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accountLoginDTO.getLogin(),
                    accountLoginDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            AccessAndRefreshTokensDTO accessAndRefreshTokensDTO = this.authenticationService.registerSuccessfulLoginAttempt(accountLoginDTO.getLogin(), false,
                    sourceAddress, accountLoginDTO.getLanguage());
            if (accessAndRefreshTokensDTO != null) {
                log.info("User: {} successfully authenticated during one factor authentication in the application, starting session at {} from IPv4: {}",
                        accountLoginDTO.getLogin(), LocalDateTime.now(), sourceAddress);
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(accessAndRefreshTokensDTO);
            }
            log.info("User: {} successfully authenticated in the first step of multifactor authentication at {} from IPv4: {}",
                    accountLoginDTO.getLogin(), LocalDateTime.now(), sourceAddress);
        } catch (BadCredentialsException badCredentialsException) {
            this.authenticationService.registerUnsuccessfulLoginAttemptWithIncrement(accountLoginDTO.getLogin(), sourceAddress);
            log.error("Authentication to user account with login: {} at {} from IPv4: {} was not successful. Cause: invalid login credentials.",
                    accountLoginDTO.getLogin(), LocalDateTime.now(), sourceAddress);
            throw new InvalidLoginAttemptException();
        } catch (DisabledException disabledException) {
            this.authenticationService.registerUnsuccessfulLoginAttemptWithoutIncrement(accountLoginDTO.getLogin(), sourceAddress);
            log.error("Authentication to user account with login: {} at {} from IPv4: {} was not successful. Cause: User account has not been activated.",
                    accountLoginDTO.getLogin(), LocalDateTime.now(), sourceAddress);
            throw new AccountNotActivatedException();
        } catch (LockedException lockedException) {
            this.authenticationService.registerUnsuccessfulLoginAttemptWithoutIncrement(accountLoginDTO.getLogin(), sourceAddress);
            Account account = this.authenticationService.findByLogin(accountLoginDTO.getLogin()).orElseThrow(InvalidLoginAttemptException::new);
            if (account.getBlockedTime() == null) {
                log.error("Authentication to user account with login: {} at {} from IPv4: {} was not successful. Cause: User account has been blocked by the admin.",
                        accountLoginDTO.getLogin(), LocalDateTime.now(), sourceAddress);
                throw new AccountBlockedByAdminException();
            } else {
                log.error("Authentication to user account with login: {} at {} from IPv4: {} was not successful. Cause: User account has been blocked by logging unsuccessfully {} amount of time.",
                        accountLoginDTO.getLogin(), LocalDateTime.now(), sourceAddress, this.loginFailedAttemptMaxCount);
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
    @PostMapping(value = "/login-auth-code", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
    public ResponseEntity<?> loginUsingAuthenticationCode(@RequestHeader(value = "X-Forwarded-For", required = false) String proxyChain,
                                                          @RequestBody AuthenticationCodeDTO authenticationCodeDTO, HttpServletRequest request) throws ApplicationBaseException {
        String sourceAddress = getSourceAddress(proxyChain, request);
        try {
            this.authenticationService.loginUsingAuthenticationCode(authenticationCodeDTO.getUserLogin(), authenticationCodeDTO.getAuthCodeValue());
        } catch (ApplicationBaseException applicationBaseException) {
            log.error("Authentication to user account with login: {} at {} from IPv4: {} in the second step of multifactor authentication was not successful.",
                    authenticationCodeDTO.getUserLogin(), LocalDateTime.now(), sourceAddress);
            this.authenticationService.registerUnsuccessfulLoginAttemptWithoutIncrement(authenticationCodeDTO.getUserLogin(), sourceAddress);
            throw applicationBaseException;
        }
        AccessAndRefreshTokensDTO accessAndRefreshTokensDTO = this.authenticationService.registerSuccessfulLoginAttempt(authenticationCodeDTO.getUserLogin(), true,
                sourceAddress, authenticationCodeDTO.getLanguage());
        log.info("User: {} successfully authenticated during two factor authentication in the application, starting session at {} from IPv4: {}",
                SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now(), sourceAddress);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(accessAndRefreshTokensDTO);
    }

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
    @Override
    @PostMapping(value = "/refresh-session", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Roles.AUTHENTICATED})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Refresh session", description = "This endpoint is used to refresh authenticated user session in the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session was refreshed successfully and new access and refresh token were generated."),
            @ApiResponse(responseCode = "400", description = "User account is blocked or not active, and therefore could not be used authenticated."),
            @ApiResponse(responseCode = "500", description = "Unknown exception occurred during logging attempt.")
    })
    public ResponseEntity<?> refreshUserSession(@RequestHeader(value = "X-Forwarded-For", required = false) String proxyChain,
                                                @Valid @RequestBody RefreshTokenDTO refreshTokenDTO, HttpServletRequest request) throws ApplicationBaseException {
        String sourceAddress = getSourceAddress(proxyChain, request);
        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        AccessAndRefreshTokensDTO accessAndRefreshTokensDTO = this.authenticationService.refreshUserSession(refreshTokenDTO.getRefreshToken(), userLogin);
        log.info("User: {} refreshed their session in the application at: {} from IPv4: {}",
                userLogin, LocalDateTime.now(), sourceAddress);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(accessAndRefreshTokensDTO);
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
        log.info("User: {} successfully logged out from the application at {} from IPv4: {}, ending their session in the application.",
                userName, LocalDateTime.now(), sourceAddress);
        return ResponseEntity.noContent().build();
    }

    /**
     * This method is used to extract "real" IP address of the user from the HttpServletRequest object.
     * It is requited since user request is forwarded to the application server by proxy.
     *
     * @param proxyChain X-Forwarded-For header content, if present or null otherwise.
     * @param request    HttpServletRequest object, associated with user request.
     * @return This method returns the actual IPv4 address of the user. If the X-Forwarded-For header is empty or not
     * present (basically null) then IPv4 address is extracted from IP packet as source address, which will be proxy.
     */
    private String getSourceAddress(String proxyChain, HttpServletRequest request) {
        if (proxyChain != null) {
            return proxyChain.indexOf(',') == -1 ? proxyChain : proxyChain.split(",")[0];
        } else {
            return request.getRemoteAddr();
        }
    }
}
