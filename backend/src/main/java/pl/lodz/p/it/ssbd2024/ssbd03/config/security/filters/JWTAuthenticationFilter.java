package pl.lodz.p.it.ssbd2024.ssbd03.config.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.SecurityConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AuthenticationFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.JWTMessages;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTProvider jwtProvider;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public JWTAuthenticationFilter(JWTProvider jwtProvider,
                                   AuthenticationFacade authenticationFacade) {
        this.jwtProvider = jwtProvider;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException, ServletException {
        log.debug("Started JWT Authentication Filter execution...");
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader.isBlank() || !authHeader.startsWith(SecurityConstants.BEARER_PREFIX)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write(JWTMessages.INCORRECT_TOKEN);
            SecurityContextHolder.clearContext();
            return;
        }

        final String jwtToken = authHeader.replaceAll("\\s+", "").substring(6);
        final String userName = jwtProvider.extractUsername(jwtToken);

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.debug("Security context object was empty...");
            final UUID accountId = jwtProvider.extractAccountId(jwtToken);
            Account account = authenticationFacade.find(accountId).orElseThrow();

            if (!jwtProvider.isTokenValid(jwtToken, account)) {
                log.debug("Token was invalid...");
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().write(JWTMessages.INVALID_TOKEN);
                SecurityContextHolder.clearContext();
                return;
            } else {
                log.debug("Adding authentication object back to the SecurityContext...");
                List<SimpleGrantedAuthority> listOfRoles = account.getUserLevels().stream().map(userLevel -> new SimpleGrantedAuthority("ROLE_" + userLevel.getClass().getSimpleName().toUpperCase())).toList();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(account.getLogin(), account.getPassword(), listOfRoles);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        // TODO: Change this paths to something more civilized.

        String path = request.getServletPath();
        log.debug("Path: " + path);
        return path.startsWith("/api/v1/auth") ||
                path.startsWith("/api/v1/register/client") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/favicon.ico");
    }
}
