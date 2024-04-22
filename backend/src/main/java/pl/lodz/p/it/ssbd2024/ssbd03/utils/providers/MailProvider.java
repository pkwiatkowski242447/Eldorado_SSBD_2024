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

/**
 * Component used for sending e-mail messages.
 */
@Slf4j
@Component
@PropertySource(value = "classpath:mail.properties")
public class MailProvider {

    @Value("${mail.sender.email}")
    private String senderEmail;

    private static final Logger logger = LoggerFactory.getLogger(MailProvider.class.getName());

    private final JavaMailSenderImpl mailSender;

    /**
     * Autowired constructor for the component.
     * @param javaMailSender
     */
    @Autowired
    public MailProvider(JavaMailSenderImpl javaMailSender) {
        this.mailSender = javaMailSender;
    }

    /**
     * Sends an account creation confirmation e-mail to the specified e-mail address.
     * @param firstName User's first name.
     * @param lastName User's last name.
     * @param emailReceiver E-mail address to which the message will be sent.
     * @param confirmationURL URL used to confirm the account creation.
     */
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

            logger.info(emailContent);

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

    /**
     * Loads e-mail template from the /resources/templates folder.
     * @param templateName Name of the template file.
     * @return Returns a String containing the loaded template.
     * If an exception occurs while reading the template file it will return everything read up to this point.
     */
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

    /**
     * Loads eimage from the /resources/templates/images folder.
     * @param imageName Name of the image file.
     * @return Returns a String containing the loaded image.
     * If an exception occurs while reading the image file it will return everything read up to this point.
     */
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
