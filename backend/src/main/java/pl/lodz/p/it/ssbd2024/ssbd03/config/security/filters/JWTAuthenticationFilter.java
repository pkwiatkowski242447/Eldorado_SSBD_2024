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
import org.springframework.web.filter.OncePerRequestFilter;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.SecurityConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.TokenDataExtractionException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AuthenticationFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.utils.JWTMessages;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;

import java.io.IOException;
import java.util.ArrayList;
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

        if (authHeader != null && ! authHeader.isBlank() && authHeader.startsWith(SecurityConstants.BEARER_PREFIX)) {
            String jwtToken = authHeader.replaceAll("\\s+", "").substring(6);

            UUID accountId;
            try {
                accountId = jwtProvider.extractAccountId(jwtToken);
            } catch (TokenDataExtractionException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().write(JWTMessages.INVALID_TOKEN);
                SecurityContextHolder.clearContext();
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                Account account = authenticationFacade.find(accountId).orElse(null);
                if (account == null || !jwtProvider.isTokenValid(jwtToken, account)) {
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    response.getWriter().write(JWTMessages.INVALID_TOKEN);
                    SecurityContextHolder.clearContext();
                    return;
                } else {
                    List<SimpleGrantedAuthority> listOfRoles = new ArrayList<>();
                    for (UserLevel userLevel : account.getUserLevels()) {
                        listOfRoles.add(new SimpleGrantedAuthority("ROLE_" + userLevel.getClass().getSimpleName().toUpperCase()));
                    }
                    listOfRoles.add(new SimpleGrantedAuthority("ROLE_AUTHENTICATED"));
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(account.getLogin(), account.getPassword(), listOfRoles);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
