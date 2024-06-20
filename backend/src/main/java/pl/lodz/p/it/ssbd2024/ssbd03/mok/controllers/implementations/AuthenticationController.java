package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.RollbackException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.token.AccessAndRefreshTokensDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.authentication.AuthenticationLoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.authentication.AuthenticationCodeDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.token.RefreshTokenDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationDatabaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationOptimisticLockException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.InvalidLoginAttemptException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.status.AccountBlockedByAdminException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.status.AccountBlockedByFailedLoginAttemptsException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.status.AccountNotActivatedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.status.AccountSuspendedException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces.AuthenticationControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.AuthenticationServiceInterface;

import java.time.LocalDateTime;
import java.util.Base64;

/**
 * Controller used for authentication in the system.
 */
@Slf4j
@RestController
@LoggerInterceptor
@RequestMapping("/api/v1/auth")
@TxTracked
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationBaseException.class)
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

    // Login methods

    @Override
    @RolesAllowed(Authorities.LOGIN)
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {
            InvalidLoginAttemptException.class, AccountNotActivatedException.class,
            AccountBlockedByAdminException.class, AccountBlockedByFailedLoginAttemptsException.class,
            AccountSuspendedException.class
    })
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> loginUsingCredentials(String proxyChain, AuthenticationLoginDTO accountLoginDTO,
                                                   HttpServletRequest request) throws ApplicationBaseException {
        String sourceAddress = getSourceAddress(proxyChain, request);
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accountLoginDTO.getLogin(),
                    accountLoginDTO.getPassword()));

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
        } catch (AccountExpiredException expiredException) {
            throw new AccountSuspendedException();
        } catch (AuthenticationException authenticationException) {
            throw new InvalidLoginAttemptException();
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed(Authorities.LOGIN)
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {
            AccountNotActivatedException.class, AccountBlockedByAdminException.class,
            AccountBlockedByFailedLoginAttemptsException.class, 
    })
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> loginUsingAuthenticationCode(String proxyChain, AuthenticationCodeDTO authenticationCodeDTO,
                                                          HttpServletRequest request) throws ApplicationBaseException {
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

    // Refresh user session method

    @Override
    @RolesAllowed({Authorities.REFRESH_SESSION})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> refreshUserSession(String proxyChain, RefreshTokenDTO refreshTokenDTO,
                                                HttpServletRequest request) throws ApplicationBaseException {
        String sourceAddress = getSourceAddress(proxyChain, request);
        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        AccessAndRefreshTokensDTO accessAndRefreshTokensDTO = this.authenticationService.refreshUserSession(refreshTokenDTO.getRefreshToken(), userLogin);
        log.info("User: {} refreshed their session in the application at: {} from IPv4: {}",
                userLogin, LocalDateTime.now(), sourceAddress);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(accessAndRefreshTokensDTO);
    }

    // Logout method

    @Override
    @RolesAllowed({Authorities.LOGOUT})
    public ResponseEntity<?> logout(String proxyChain, HttpServletRequest request, HttpServletResponse response) {
        String sourceAddress = getSourceAddress(proxyChain, request);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        log.info("User: {} successfully logged out from the application at {} from IPv4: {}, ending their session in the application.",
                userName, LocalDateTime.now(), sourceAddress);
        return ResponseEntity.noContent().build();
    }

    // Fetch car image to present on login page

    @Override
    @RolesAllowed({Authorities.LOGIN})
    public ResponseEntity<?> getRandomImage() {
        String apiURL = "https://random-image-pepebigotes.vercel.app/api/random-image";
        Response response = RestAssured.given().when().get(apiURL);

        byte[] byteArray = response.getBody().asByteArray();
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(new String(Base64.getEncoder().encode(byteArray)));
    }

    // Other private methods

    /**
     * This method is used to extract "real" IP address of the user from the HttpServletRequest object.
     * It is requited since user request is forwarded to the application server by proxy.
     *
     * @param proxyChain X-Forwarded-For header content, if present or null otherwise.
     * @param request    HttpServletRequest object, associated with user request.
     * @return This method returns the actual IPv4 address of the user. If the X-Forwarded-For header is empty or not
     * present (basically null) then IPv4 address is extracted from IP packet as source address, which will be proxy.
     */
    @RolesAllowed({Authorities.LOGIN, Authorities.LOGOUT, Authorities.REFRESH_SESSION})
    private String getSourceAddress(String proxyChain, HttpServletRequest request) {
        if (proxyChain != null) {
            return proxyChain.indexOf(',') == -1 ? proxyChain : proxyChain.split(",")[0];
        } else {
            return request.getRemoteAddr();
        }
    }
}
