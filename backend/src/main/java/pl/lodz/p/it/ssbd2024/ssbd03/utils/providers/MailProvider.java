package pl.lodz.p.it.ssbd2024.ssbd03.utils.providers;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.EmailTemplateNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.ImageNotFoundException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Component
@PropertySource(value = "classpath:mail.properties")
public class MailProvider {

    @Value("${mail.sender.email}")
    private String senderEmail;

    private static final Logger logger = LoggerFactory.getLogger(MailProvider.class.getName());

    private final JavaMailSenderImpl mailSender;

    @Autowired
    public MailProvider(JavaMailSenderImpl javaMailSender) {
        this.mailSender = javaMailSender;
    }

    public void sendRegistrationConfirmEmail(String firstName, String lastName, String emailReceiver, String confirmationURL) {
        try {
            String logo = this.loadImage("eldorado.png").orElseThrow(() -> new ImageNotFoundException("Given image could not be found!"));
            String emailContent = this.loadTemplate("link-template.html").orElseThrow(() -> new EmailTemplateNotFoundException("Given email template not found!"))
                    .replace("$firstname", firstName)
                    .replace("$lastname", lastName)
                    .replace("$greeting_message", "Hello")
                    .replace("$result_message", "Your account was successfully created!")
                    .replace("$action_description", "In order to activate it, click the the link below.")
                    .replace("$action_link", confirmationURL)
                    .replace("$note_title", "Note")
                    .replace("$note_message", "This e-mail is generated automatically and does not require any responses to it.")
                    .replace("$eldorado_logo", "data:image/png;base64," + logo);


            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

            messageHelper.setTo(emailReceiver);
            messageHelper.setSubject("Activate your account");
            messageHelper.setText(emailContent, true);
            messageHelper.setFrom(senderEmail);

            this.mailSender.send(mimeMessage);
        } catch (EmailTemplateNotFoundException | ImageNotFoundException | MessagingException |
                 NullPointerException exception) {
            logger.error(exception.getMessage(), exception.getCause());
        }
    }

    public void sendBlockAccountInfoEmail(String firstName, String lastName, String emailReceiver) {
        try {
            String logo = this.loadImage("eldorado.png").orElseThrow(() -> new ImageNotFoundException("Given image could not be found!"));
            String emailContent = this.loadTemplate("block-template.html").orElseThrow(() -> new EmailTemplateNotFoundException("Given email template not found!"))
                    .replace("$firstname", firstName)
                    .replace("$lastname", lastName)
                    .replace("$greeting_message", "Hello")
                    .replace("$result_message", "Your account has been blocked!")
                    .replace("$action_description", "TEST TEST.")
                    .replace("$note_title", "Note")
                    .replace("$note_message", "This e-mail is generated automatically and does not require any responses to it.")
                    .replace("$eldorado_logo", "data:image/png;base64," + logo);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

            messageHelper.setTo(emailReceiver);
            messageHelper.setSubject("Your account has been blocked");
            messageHelper.setText(emailContent, true);
            messageHelper.setFrom(senderEmail);

            this.mailSender.send(mimeMessage);
        } catch (EmailTemplateNotFoundException | ImageNotFoundException | MessagingException |
                 NullPointerException exception) {
            logger.error(exception.getMessage(), exception.getCause());
        }
    }

    private Optional<String> loadTemplate(String templateName) {
        StringBuilder builder = new StringBuilder();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/" + templateName);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String placeHolder;
            while ((placeHolder = bufferedReader.readLine()) != null) {
                builder.append(placeHolder);
            }
        } catch (IOException | NullPointerException e) {
            logger.error("Error while reading email message template file", e.getCause());
        }
        return Optional.of(builder.toString());
    }

    private Optional<String> loadImage(String imageName) {
        StringBuilder builder = new StringBuilder();
        try {
            URL imagePath = getClass().getClassLoader().getResource("templates/images/" + imageName);
            BufferedImage image = ImageIO.read(imagePath);
            byte[] imageContent = Files.readAllBytes(Path.of(imagePath.toURI()));
            builder.append(new String(Base64.getEncoder().encode(imageContent)));
        } catch (IOException | URISyntaxException | NullPointerException e) {
            logger.error("Error while reading email message template file", e.getCause());
        }
        return Optional.of(builder.toString());
    }
}
