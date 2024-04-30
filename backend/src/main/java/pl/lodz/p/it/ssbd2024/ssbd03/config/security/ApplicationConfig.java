package pl.lodz.p.it.ssbd2024.ssbd03.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AuthenticationFacade;

import java.util.Properties;

@Configuration
@PropertySource("classpath:mail.properties")
@EnableAspectJAutoProxy
public class ApplicationConfig {

    @Value("${mail.sender.username}")
    private String senderUsername;

    @Value("${mail.sender.password}")
    private String senderPassword;

    @Value("${mail.smtp.auth}")
    private boolean smtpAuth;

    @Value("${mail.smtp.ssl.enable}")
    private boolean smtpSsl;

    @Value("${mail.smtp.ssl.trust}")
    private String smtpSslTrust;

    @Value("${mail.smtp.starttls.enable}")
    private boolean smtpTls;

    @Value("${mail.smtp.host}")
    private String smtpHost;

    @Value("${mail.smtp.port}")
    private int smtpPort;

    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public ApplicationConfig(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return login -> {
            try {
                Account account = authenticationFacade.findByLogin(login).orElseThrow(() -> new AccountNotFoundException("Account with given login could not be found!"));
                return new User(account.getLogin(),
                        account.getPassword(),
                        account.getActive(),
                        true,
                        true,
                        true,
                        account.getUserLevels().stream().map(userLevel -> new SimpleGrantedAuthority("ROLE_" + userLevel.getClass().getSimpleName().toUpperCase())).toList());
            } catch (AccountNotFoundException exception) {
                throw new RuntimeException(exception.getMessage(), exception);
            }
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

    @Bean(name = "mvcHandlerMappingIntrospector")
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

    @Bean
    public JavaMailSenderImpl javaMailSender() {
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", smtpAuth);
        mailProperties.put("mail.smtp.starttls.enable", smtpTls);
        mailProperties.put("mail.smtp.ssl.enable", smtpSsl);
        mailProperties.put("mail.smtp.ssl.trust", smtpSslTrust);
        mailProperties.put("mail.smtp.host", smtpHost);
        mailProperties.put("mail.smtp.port", smtpPort);

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setJavaMailProperties(mailProperties);
        mailSender.setUsername(this.senderUsername);
        mailSender.setPassword(this.senderPassword);
        return mailSender;
    }
}
