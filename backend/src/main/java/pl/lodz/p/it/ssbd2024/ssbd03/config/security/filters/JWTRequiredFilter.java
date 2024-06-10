package pl.lodz.p.it.ssbd2024.ssbd03.config.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.exception.ExceptionDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.SecurityConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@Component
@LoggerInterceptor
public class JWTRequiredFilter extends OncePerRequestFilter {

    private static final String[] WHITELIST = {
            "/api/v1/auth/login-credentials.*",
            "/api/v1/auth/login-auth-code.*",
            "/api/v1/register/client.*",
            "/api/v1/accounts/forgot-password.*",
            "/api/v1/accounts/change-password.*",
            "/api/v1/accounts/activate-account.*",
            "/api/v1/accounts/confirm-email.*",
            "/api/v1/accounts/restore-access.*",
            "/api/v1/accounts/restore-token.*",
            "/v3/api-docs.*",
            "/swagger-ui.*",
            "/swagger-resources.*",
            "/configuration/ui.*",
            "/configuration/security.*",
            "/swagger-ui.html.*",
            "/api/v1/parking/client/sectors/.*",
            "/api/v1/parking/active.*",
            "/api/v1/parking/get.*",
            "/api/v1/parking/.*/enter"
    };

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
        for (String key : WHITELIST) {
            if (request.getRequestURI().matches(key)) return true;
        }
        return false;
    }
}
