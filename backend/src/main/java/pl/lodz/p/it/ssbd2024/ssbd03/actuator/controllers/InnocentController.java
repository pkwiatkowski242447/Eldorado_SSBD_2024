package pl.lodz.p.it.ssbd2024.ssbd03.actuator.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.filters.JWTRequiredFilter;

import java.lang.reflect.Field;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/dead")
@RequiredArgsConstructor
public class InnocentController {

    private final JWTRequiredFilter jWTRequiredFilter;

    @GetMapping(path = "/liveness", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> destroyLiveness() throws NoSuchFieldException, IllegalAccessException {
        Field field_WHITELIST_MAP = JWTRequiredFilter.class.getDeclaredField("WHITELIST_MAP");
        field_WHITELIST_MAP.setAccessible(true);
        @SuppressWarnings("unchecked")
        HashMap<String, String> map = (HashMap<String, String>) field_WHITELIST_MAP.get(jWTRequiredFilter);
        map.remove("^/api/v1/health/liveness");
        field_WHITELIST_MAP.setAccessible(false);


        return ResponseEntity.ok("Liveness destroyed");
    }
}
