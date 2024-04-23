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
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.MailProviderMessages;

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

    public void sendRegistrationConfirmEmail(String firstName, String lastName, String emailReceiver, String confirmationURL, String language) {
        try {
            String logo = this.loadImage("eldorado.png").orElseThrow(() -> new ImageNotFoundException(MailProviderMessages.IMAGE_NOT_FOUND_EXCEPTION));
            String emailContent = this.loadTemplate("link-template.html").orElseThrow(() -> new EmailTemplateNotFoundException(MailProviderMessages.EMAIL_TEMPLATE_NOT_FOUND_EXCEPTION))
                    .replace("$firstname", firstName)
                    .replace("$lastname", lastName)
                    .replace("$greeting_message", I18n.getMessage(I18n.CONFIRM_REGISTER_GREETING_MESSAGE, language))
                    .replace("$result_message", I18n.getMessage(I18n.CONFIRM_REGISTER_RESULT_MESSAGE, language))
                    .replace("$action_description", I18n.getMessage(I18n.CONFIRM_REGISTER_ACTION_DESCRIPTION, language))
                    .replace("$action_link", confirmationURL)
                    .replace("$note_title", I18n.getMessage(I18n.CONFIRM_REGISTER_NOTE_TITLE, language))
                    .replace("$note_message", I18n.getMessage(I18n.AUTO_GENERATED_MESSAGE_NOTE, language))
                    .replace("$eldorado_logo", "data:image/png;base64," + logo);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

            messageHelper.setTo(emailReceiver);
            messageHelper.setSubject(I18n.getMessage(I18n.CONFIRM_REGISTER_MESSAGE_SUBJECT, language));
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
            logger.error(MailProviderMessages.EMAIL_TEMPLATE_READ_EXCEPTION, e.getCause());
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
            logger.error(MailProviderMessages.IMAGE_READ_EXCEPTION, e.getCause());
        }
        return Optional.of(builder.toString());
    }
}
