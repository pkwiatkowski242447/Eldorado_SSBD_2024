package pl.lodz.p.it.ssbd2024.ssbd03.actuator.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.actuator.service.ActuatorService;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;

/**
 * Controller used for checking liveness and readiness of system.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@LoggerInterceptor
@RequestMapping("/api/v1/health")
public class ActuatorController {

    private final ActuatorService actuatorService;

    @GetMapping(path = "/liveness", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> checkLiveness() {
        return ResponseEntity.ok("up");
    }

    @GetMapping(path = "/readiness", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> checkReadiness() {
        if (!actuatorService.checkConnection()) return new ResponseEntity<>(HttpStatusCode.valueOf(503));
        return ResponseEntity.ok("ready");
    }
}
