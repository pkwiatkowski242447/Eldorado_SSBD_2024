package pl.lodz.p.it.ssbd2024.ssbd03.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.filters.JWTAuthenticationFilter;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.filters.JWTRequiredFilter;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.roles.RolesMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.UnsupportedRoleException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true
)
public class SecurityConfig {
    @Value("${base.app.url}")
    private String app_url;

    private final AuthenticationProvider authenticationProvider;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final JWTRequiredFilter jwtRequiredFilter;
    private final RolesMapper rolesMapper;

    @Autowired
    public SecurityConfig(AuthenticationProvider authenticationProvider,
                          JWTAuthenticationFilter jwtAuthenticationFilter,
                          JWTRequiredFilter jwtRequiredFilter,
                          RolesMapper rolesMapper) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtRequiredFilter = jwtRequiredFilter;
        this.rolesMapper = rolesMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> corsConfigurationSource())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtRequiredFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .anonymous((anon) -> {
                    try {
                        anon.authorities(
                                rolesMapper.getAuthoritiesAsStrings(Roles.ANONYMOUS)
                                        .stream()
                                        .map(SimpleGrantedAuthority::new)
                                        .collect(Collectors.toList())
                        );
                    } catch (UnsupportedRoleException ex) {
                        log.error("Unable to get authorities of role {}", Roles.ANONYMOUS);
                        anon.authorities("ROLE_ANONYMOUS");
                    }
                })
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/**").permitAll()
                );

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        corsConfiguration.setAllowedHeaders(List.of(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.IF_MATCH,
                HttpHeaders.ACCEPT));
        corsConfiguration.setAllowedOriginPatterns(List.of(app_url));
        corsConfiguration.addExposedHeader("Access-Token");
        corsConfiguration.addExposedHeader("Uid");
        corsConfiguration.addExposedHeader("ETag");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
