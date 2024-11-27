package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.RollbackException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import pl.lodz.p.it.ssbd2024.ssbd03.config.metrics.CustomMetrics;
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
import java.util.concurrent.Callable;

/**
 * Controller used for authentication in the system.
 */
@Slf4j
@RestController
@LoggerInterceptor
@RequestMapping("/api/v1/auth")
@TxTracked
@RequiredArgsConstructor
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

    private final CustomMetrics customMetrics;

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
        customMetrics.recordLoginUsingCredentialsRequest();
        return customMetrics.timeLoginUsingCredentialsRequest(() -> {
              LoginUsingCredentials login = new LoginUsingCredentials(proxyChain, request, accountLoginDTO);
              return login.call();
        });
    }

    public class LoginUsingCredentials implements Callable<ResponseEntity<?>> {

        String proxyChain;
        AuthenticationLoginDTO accountLoginDTO;
        HttpServletRequest request;

        public LoginUsingCredentials(String proxyChain,
                                     HttpServletRequest request,
                                     AuthenticationLoginDTO accountLoginDTO) {
            this.proxyChain = proxyChain;
            this.request = request;
            this.accountLoginDTO = accountLoginDTO;
        }

        @Override
        public ResponseEntity<?> call() throws ApplicationBaseException {
            String sourceAddress = getSourceAddress(proxyChain, request);
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accountLoginDTO.getLogin(),
                        accountLoginDTO.getPassword()));

                AccessAndRefreshTokensDTO accessAndRefreshTokensDTO = authenticationService.registerSuccessfulLoginAttempt(accountLoginDTO.getLogin(), false,
                        sourceAddress, accountLoginDTO.getLanguage());

                if (accessAndRefreshTokensDTO != null) {
                    log.info("User: {} successfully authenticated during one factor authentication in the application, starting session at {} from IPv4: {}",
                            accountLoginDTO.getLogin(), LocalDateTime.now(), sourceAddress);
                    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(accessAndRefreshTokensDTO);
                }
                log.info("User: {} successfully authenticated in the first step of multifactor authentication at {} from IPv4: {}",
                        accountLoginDTO.getLogin(), LocalDateTime.now(), sourceAddress);
            } catch (BadCredentialsException badCredentialsException) {
                authenticationService.registerUnsuccessfulLoginAttemptWithIncrement(accountLoginDTO.getLogin(), sourceAddress);
                log.error("Authentication to user account with login: {} at {} from IPv4: {} was not successful. Cause: invalid login credentials.",
                        accountLoginDTO.getLogin(), LocalDateTime.now(), sourceAddress);
                throw new InvalidLoginAttemptException();
            } catch (DisabledException disabledException) {
                authenticationService.registerUnsuccessfulLoginAttemptWithoutIncrement(accountLoginDTO.getLogin(), sourceAddress);
                log.error("Authentication to user account with login: {} at {} from IPv4: {} was not successful. Cause: User account has not been activated.",
                        accountLoginDTO.getLogin(), LocalDateTime.now(), sourceAddress);
                throw new AccountNotActivatedException();
            } catch (LockedException lockedException) {
                authenticationService.registerUnsuccessfulLoginAttemptWithoutIncrement(accountLoginDTO.getLogin(), sourceAddress);
                Account account = authenticationService.findByLogin(accountLoginDTO.getLogin()).orElseThrow(InvalidLoginAttemptException::new);
                if (account.getBlockedTime() == null) {
                    log.error("Authentication to user account with login: {} at {} from IPv4: {} was not successful. Cause: User account has been blocked by the admin.",
                            accountLoginDTO.getLogin(), LocalDateTime.now(), sourceAddress);
                    throw new AccountBlockedByAdminException();
                } else {
                    log.error("Authentication to user account with login: {} at {} from IPv4: {} was not successful. Cause: User account has been blocked by logging unsuccessfully {} amount of time.",
                            accountLoginDTO.getLogin(), LocalDateTime.now(), sourceAddress, loginFailedAttemptMaxCount);
                    throw new AccountBlockedByFailedLoginAttemptsException();
                }
            } catch (AccountExpiredException expiredException) {
                throw new AccountSuspendedException();
            } catch (AuthenticationException authenticationException) {
                throw new InvalidLoginAttemptException();
            }

            return ResponseEntity.noContent().build();
        }
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
        customMetrics.recordLoginUsingAuthCodeRequest();
        return customMetrics.timeLoginUsingAuthCodeRequest(() -> {
            LoginUsingAuthCode login = new LoginUsingAuthCode(proxyChain, request, authenticationCodeDTO);
            return login.call();
        });
    }

    public class LoginUsingAuthCode implements Callable<ResponseEntity<?>> {

        String proxyChain;
        AuthenticationCodeDTO authenticationCodeDTO;
        HttpServletRequest request;

        public LoginUsingAuthCode(String proxyChain,
                                  HttpServletRequest request,
                                  AuthenticationCodeDTO authenticationCodeDTO) {
            this.proxyChain = proxyChain;
            this.request = request;
            this.authenticationCodeDTO = authenticationCodeDTO;
        }

        @Override
        public ResponseEntity<?> call() throws ApplicationBaseException {
            String sourceAddress = getSourceAddress(proxyChain, request);
            try {
                authenticationService.loginUsingAuthenticationCode(authenticationCodeDTO.getUserLogin(), authenticationCodeDTO.getAuthCodeValue());
            } catch (ApplicationBaseException applicationBaseException) {
                log.error("Authentication to user account with login: {} at {} from IPv4: {} in the second step of multifactor authentication was not successful.",
                        authenticationCodeDTO.getUserLogin(), LocalDateTime.now(), sourceAddress);
                authenticationService.registerUnsuccessfulLoginAttemptWithoutIncrement(authenticationCodeDTO.getUserLogin(), sourceAddress);
                throw applicationBaseException;
            }
            AccessAndRefreshTokensDTO accessAndRefreshTokensDTO = authenticationService.registerSuccessfulLoginAttempt(authenticationCodeDTO.getUserLogin(), true,
                    sourceAddress, authenticationCodeDTO.getLanguage());
            log.info("User: {} successfully authenticated during two factor authentication in the application, starting session at {} from IPv4: {}",
                    SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now(), sourceAddress);

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(accessAndRefreshTokensDTO);
        }
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
    public ResponseEntity<?> logout(String proxyChain, HttpServletRequest request, HttpServletResponse response)
            throws ApplicationBaseException {
        customMetrics.recordLogoutRequest();
        return customMetrics.timeLogoutRequest(() -> {
           Logout logout = new Logout(proxyChain, request, response);
           return logout.call();
        });
    }

    public class Logout implements Callable<ResponseEntity<?>> {

        String proxyChain;
        HttpServletRequest request;
        HttpServletResponse response;

        public Logout(String proxyChain,
                      HttpServletRequest request,
                      HttpServletResponse response) {
            this.proxyChain = proxyChain;
            this.request = request;
            this.response = response;
        }

        @Override
        public ResponseEntity<?> call() {
            String sourceAddress = getSourceAddress(proxyChain, request);
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            log.info("User: {} successfully logged out from the application at {} from IPv4: {}, ending their session in the application.",
                    userName, LocalDateTime.now(), sourceAddress);

            return ResponseEntity.noContent().build();
        }
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

    @Override
    @RolesAllowed({Authorities.SWITCH_USER_LEVEL})
    public ResponseEntity<?> changeUserLevel(String level) throws ApplicationBaseException {
        authenticationService.changeUserLevel(level);

        return ResponseEntity.noContent().build();
    }
}
