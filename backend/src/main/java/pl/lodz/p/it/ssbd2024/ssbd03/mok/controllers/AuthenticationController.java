package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers;

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
import pl.lodz.p.it.ssbd2024.ssbd03.entities.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.ActivityLog;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.ActivityLogUpdateException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.AuthenticationAccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.AuthenticationInvalidCredentialsException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.AuthenticationService;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;

import java.time.LocalDateTime;

/**
 * Controller used for authentication in the system.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JWTProvider jwtProvider;
    private final TokenFacade tokenFacade;
    private final MailProvider mailProvider;

    /**
     * Autowired constructor for the controller.
     *
     * @param authenticationService
     * @param jwtProvider
     */
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService,
                                    JWTProvider jwtProvider,
                                    TokenFacade tokenFacade,
                                    MailProvider mailProvider) {
        this.authenticationService = authenticationService;
        this.jwtProvider = jwtProvider;
        this.tokenFacade = tokenFacade;
        this.mailProvider = mailProvider;
    }

    /**
     * Allows an unauthenticated user to log in to the system.
     *
     * @param accountLoginDTO User's credentials.
     * @param request         HTTP Request in which the credentials.
     * @return In case of successful logging in returns HTTP 200 OK and JWT later used to keep track of a session.
     * If any problems occur returns HTTP 400 BAD REQUEST and JSON containing information about the problem.
     */
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
                return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(I18n.getMessage(responseMessage, accountLoginDTO.getLanguage()));
            } catch (AuthenticationInvalidCredentialsException exception) {
                Account account = this.authenticationService.findByLogin(accountLoginDTO.getLogin());
                ActivityLog activityLog = account.getActivityLog();
                activityLog.setLastUnsuccessfulLoginIp(request.getRemoteAddr());
                activityLog.setLastUnsuccessfulLoginTime(LocalDateTime.now());

                // Increment the number of failed login attempts
                activityLog.setUnsuccessfulLoginCounter(activityLog.getUnsuccessfulLoginCounter() + 1);

                authenticationService.updateActivityLog(account, activityLog);
                return ResponseEntity.badRequest().body(I18n.getMessage(exception.getMessage(), account.getAccountLanguage()));
            }
        } catch (AuthenticationAccountNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(I18n.getMessage(exception.getMessage(), accountLoginDTO.getLanguage()));
        } catch (ActivityLogUpdateException exception) {
            return ResponseEntity.badRequest().body(I18n.getMessage(exception.getMessage(), accountLoginDTO.getLanguage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(I18n.getMessage(I18n.AUTH_CONTROLLER_ACCOUNT_LOGOUT, "en"));
        }
    }

    @PostMapping(value = "/resend-email-confirmation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resendEmailConfirmation(@RequestBody AccountLoginDTO accountLoginDTO) {
        try {
            // TODO: Verify credentials
            Account account = this.authenticationService.findByLogin(accountLoginDTO.getLogin());
            Token token = this.tokenFacade.findByTypeAndAccount(Token.TokenType.CONFIRM_EMAIL, account.getId()).orElseThrow();
            String confirmationURL = "http://localhost:8080/api/v1/accounts/email/" + token;
            mailProvider.sendRegistrationConfirmEmail(account.getName(),
                                                      account.getLastname(),
                                                      account.getEmail(),
                                                      confirmationURL,
                                                      account.getAccountLanguage());
            return ResponseEntity.noContent().build();
        } catch (AuthenticationAccountNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(I18n.getMessage(exception.getMessage(), accountLoginDTO.getLanguage()));
        }
    }
}
