package pl.lodz.p.it.ssbd2024.ssbd03.config.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.exception.ExceptionDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.SecurityConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@LoggerInterceptor
public class JWTRequiredFilter extends OncePerRequestFilter {

    private static final String UUID_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    private static final Map<String, String> WHITELIST_MAP = new HashMap<>() {{
        // Other
        put("^/v3/api-docs.*", null);
        put("^/swagger-ui.*", null);
        put("^/swagger-resources.*", null);
        put("^/configuration/ui.*", null);
        put("^/configuration/security.*", null);
        put("^/swagger-ui.html.*", null);
        put("^/favicon.ico.*", null);

        // Endpoints
        put("^/api/v1/auth/login-credentials", HttpMethod.POST.name());
        put("^/api/v1/auth/login-auth-code", HttpMethod.POST.name());
        put("^/api/v1/auth/random-image", HttpMethod.GET.name());
        put("^/api/v1/register/client", HttpMethod.POST.name());
        put("^/api/v1/accounts/forgot-password", HttpMethod.POST.name());
        put("^/api/v1/accounts/change-password/.*", HttpMethod.POST.name());
        put("^/api/v1/accounts/activate-account/.*", HttpMethod.POST.name());
        put("^/api/v1/accounts/confirm-email/.*", HttpMethod.POST.name());
        put("^/api/v1/accounts/restore-access", HttpMethod.POST.name());
        put("^/api/v1/accounts/restore-token/.*", HttpMethod.POST.name());
        put("^/api/v1/parking/sectors/get/%s".formatted(UUID_REGEX), HttpMethod.GET.name());
        put("^/api/v1/parking/active", HttpMethod.GET.name());
        put("^/api/v1/parking/reservations/%s/exit$".formatted(UUID_REGEX), HttpMethod.POST.name());
        put("^/api/v1/parking/%s/enter$".formatted(UUID_REGEX), HttpMethod.POST.name());
        put("^/api/v1/parking/get/%s$".formatted(UUID_REGEX), HttpMethod.GET.name());
        put("^/api/v1/parking/client/sectors/%s$".formatted(UUID_REGEX), HttpMethod.GET.name());

        put("^/api/v1/health/liveness", HttpMethod.GET.name());
        put("^/api/v1/health/readiness", HttpMethod.GET.name());

        put("^/api/v1/dead/liveness", HttpMethod.GET.name());
        put("^/api/v1/metrics.*", null);
        put("^/api/v1/load-generation.*", null);
    }};

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException, ServletException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith(SecurityConstants.BEARER_PREFIX)) {
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try (OutputStream outputStream = response.getOutputStream()) {
                outputStream.write(objectMapper.writeValueAsBytes(new ExceptionDTO(I18n.UNAUTHORIZED_EXCEPTION)));
            }
            SecurityContextHolder.clearContext();
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        for (String key : WHITELIST_MAP.keySet()) {
            String methodName = WHITELIST_MAP.get(key);
//            log.debug("Path in the application: {}", urlPathHelper.getPathWithinApplication(request));
//            log.debug("Key: {}", key);
//            log.debug("Result: {}", urlPathHelper.getPathWithinApplication(request).matches(key));
            if (urlPathHelper.getPathWithinApplication(request).matches(key) &&
                    (methodName == null ||
                    request.getMethod().equals(methodName))) return true;
        }
        return false;
    }
}
