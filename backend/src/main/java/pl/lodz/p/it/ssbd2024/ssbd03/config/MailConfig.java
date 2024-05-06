package pl.lodz.p.it.ssbd2024.ssbd03.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Properties;

@Configuration
public class MailConfig {

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
