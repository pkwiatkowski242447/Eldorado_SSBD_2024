package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountLoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.ActivityLog;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.ActivityLogUpdateException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.AuthenticationAccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.AuthenticationInvalidCredentialsException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.AuthenticationService;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.JWTService;

import java.time.LocalDateTime;

@Slf4j
@RestController()
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JWTService jwtService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService,
                                    JWTService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody AccountLoginDTO accountLoginDTO, HttpServletRequest request) {
        try {
            try {
                Account account = this.authenticationService.login(accountLoginDTO.getLogin(), accountLoginDTO.getPassword());
                ActivityLog activityLog = account.getActivityLog();
                String responseMessage;
                if (account.getActive() && account.getVerified() && !account.getBlocked()) {
                    activityLog.setLastSuccessfulLoginIp(request.getRemoteAddr());
                    activityLog.setLastSuccessfulLoginTime(LocalDateTime.now());
                    authenticationService.updateActivityLog(account, activityLog);
                    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jwtService.generateJWTToken(account));
                } else if (!account.getActive()) {
                    responseMessage = "Your account has not been activated yet!";
                } else if (account.getBlocked()) {
                    responseMessage = "Your account has been blocked!";
                } else {
                    responseMessage = "Error while logging in.";
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
                authenticationService.updateActivityLog(account, activityLog);
                return ResponseEntity.badRequest().body(exception.getMessage());
            }
        } catch (AuthenticationAccountNotFoundException | ActivityLogUpdateException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping(value = "/test")
    public void testMethod() {
        log.info("TEST ENDPOINT");
    }
}