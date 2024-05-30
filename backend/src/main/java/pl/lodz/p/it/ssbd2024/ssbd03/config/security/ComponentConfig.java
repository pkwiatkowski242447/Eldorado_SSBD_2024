package pl.lodz.p.it.ssbd2024.ssbd03.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.roles.RolesMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AuthenticationFacade;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@LoggerInterceptor
public class ComponentConfig {

    private final AuthenticationFacade authenticationFacade;
    private final RolesMapper rolesMapper;

    @Autowired
    public ComponentConfig(AuthenticationFacade authenticationFacade,
                           RolesMapper rolesMapper) {
        this.authenticationFacade = authenticationFacade;
        this.rolesMapper = rolesMapper;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return login -> {
            try {
                Account account = authenticationFacade.findByLogin(login).orElseThrow(AccountNotFoundException::new);

                List<SimpleGrantedAuthority> accountAuthorities = new ArrayList<>();
                for (UserLevel userLevel : account.getUserLevels()) {
                    accountAuthorities.addAll(rolesMapper.getAuthorities(userLevel.getClass().getSimpleName().toUpperCase()));
                }

                return new User(account.getLogin(),
                        account.getPassword(),
                        account.getActive(),
                        !account.getSuspended(),
                        true,
                        !account.getBlocked(),
                        accountAuthorities);
            } catch (ApplicationBaseException exception) {
                log.error(exception.getMessage());
            }
            return null;
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
