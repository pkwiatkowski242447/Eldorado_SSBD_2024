package pl.lodz.p.it.ssbd2024.ssbd03.utils.providers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.EmailTemplateNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.ImageNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.log.MailProviderMessages;

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
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class MailProvider {

    @Value("${mail.sender.email}")
    private String senderEmail;

    private final JavaMailSenderImpl mailSender;

    /**
     * Autowired constructor for the component.
     *
     * @param javaMailSender Component from Spring framework, used to send e-mail messages to certain
     *                       e-mail addresses.
     */
    @Autowired
    public MailProvider(JavaMailSenderImpl javaMailSender) {
        this.mailSender = javaMailSender;
    }

    /**
     * Sends an account creation confirmation e-mail to the specified e-mail address.
     *
     * @param firstName       User's first name.
     * @param lastName        User's last name.
     * @param emailReceiver   E-mail address to which the message will be sent.
     * @param confirmationURL URL used to confirm the account creation.
     * @param language        Language of the message.
     */
    @RolesAllowed({Authorities.REGISTER_CLIENT, Authorities.REGISTER_USER})
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
            this.sendEmail(emailContent, emailReceiver, senderEmail, I18n.getMessage(I18n.CONFIRM_REGISTER_MESSAGE_SUBJECT, language));
        } catch (EmailTemplateNotFoundException | ImageNotFoundException | MessagingException |
                 NullPointerException exception) {
            log.error("Exception of type: {} was throw while sending account activation e-mail message, due to the exception: {} being thrown. Reason: {}",
                    exception.getClass().getSimpleName(), exception.getCause().getClass().getSimpleName(), exception.getMessage());
        }
    }

    /**
     * This method is used to send e-mail message that will contain URL used
     * for restoring access to the account, which was blocked for not being active too long.
     *
     * @param firstName       User's first name.
     * @param lastName        User's last name.
     * @param emailReceiver   E-mail address to which the message will be sent.
     * @param confirmationURL URL used to restore access to the account creation.
     * @param language        Language of the message.
     */
    @RolesAllowed({Authorities.RESTORE_ACCOUNT_ACCESS})
    public void sendAccountAccessRestoreEmailMessage(String firstName, String lastName, String emailReceiver, String confirmationURL, String language) {
        try {
            String logo = this.loadImage("eldorado.png").orElseThrow(() -> new ImageNotFoundException(MailProviderMessages.IMAGE_NOT_FOUND_EXCEPTION));
            String emailContent = this.loadTemplate("link-template.html").orElseThrow(() -> new EmailTemplateNotFoundException(MailProviderMessages.EMAIL_TEMPLATE_NOT_FOUND_EXCEPTION))
                    .replace("$firstname", firstName)
                    .replace("$lastname", lastName)
                    .replace("$greeting_message", I18n.getMessage(I18n.RESTORE_ACCESS_CODE_GREETING_MESSAGE, language))
                    .replace("$result_message", I18n.getMessage(I18n.RESTORE_ACCESS_CODE_RESULT_MESSAGE, language))
                    .replace("$action_description", I18n.getMessage(I18n.RESTORE_ACCESS_CODE_ACTION_DESCRIPTION, language))
                    .replace("$action_link", confirmationURL)
                    .replace("$note_title", I18n.getMessage(I18n.RESTORE_ACCESS_CODE_NOTE_TITLE, language))
                    .replace("$note_message", I18n.getMessage(I18n.AUTO_GENERATED_MESSAGE_NOTE, language))
                    .replace("$eldorado_logo", "data:image/png;base64," + logo);
            this.sendEmail(emailContent, emailReceiver, senderEmail, I18n.getMessage(I18n.RESTORE_ACCESS_CODE_MESSAGE_SUBJECT, language));
        } catch (EmailTemplateNotFoundException | ImageNotFoundException | MessagingException |
                 NullPointerException exception) {
            log.error("Exception of type: {} was throw while sending account access restore message, due to the exception: {} being thrown. Reason: {}",
                    exception.getClass().getSimpleName(), exception.getCause().getClass().getSimpleName(), exception.getMessage());
        }
    }

    /**
     * This method is used to send e-mail message containing confirmation for
     * restoring access to the user account.
     *
     * @param firstName       User's first name.
     * @param lastName        User's last name.
     * @param emailReceiver   E-mail address to which the message will be sent.
     * @param language        Language of the message.
     */
    @RolesAllowed({Authorities.RESTORE_ACCOUNT_ACCESS})
    public void sendAccountAccessRestoreInfoEmail(String firstName, String lastName, String emailReceiver, String language) {
        try {
            String logo = this.loadImage("eldorado.png").orElseThrow(() -> new ImageNotFoundException(MailProviderMessages.IMAGE_NOT_FOUND_EXCEPTION));
            String emailContent = this.loadTemplate("default-template.html").orElseThrow(() -> new EmailTemplateNotFoundException(MailProviderMessages.EMAIL_TEMPLATE_NOT_FOUND_EXCEPTION))
                    .replace("$firstname", firstName)
                    .replace("$lastname", lastName)
                    .replace("$greeting_message", I18n.getMessage(I18n.RESTORE_ACCESS_CONFIRM_GREETING_MESSAGE, language))
                    .replace("$result_message", I18n.getMessage(I18n.RESTORE_ACCESS_CONFIRM_RESULT_MESSAGE, language))
                    .replace("$action_description", I18n.getMessage(I18n.RESTORE_ACCESS_CONFIRM_ACTION_DESCRIPTION, language))
                    .replace("$note_title", I18n.getMessage(I18n.RESTORE_ACCESS_CONFIRM_NOTE_TITLE, language))
                    .replace("$note_message", I18n.getMessage(I18n.AUTO_GENERATED_MESSAGE_NOTE, language))
                    .replace("$eldorado_logo", "data:image/png;base64," + logo);
            this.sendEmail(emailContent, emailReceiver, senderEmail, I18n.getMessage(I18n.RESTORE_ACCESS_CONFIRM_MESSAGE_SUBJECT, language));
        } catch (EmailTemplateNotFoundException | ImageNotFoundException | MessagingException |
                 NullPointerException exception) {
            log.error("Exception of type: {} was throw while sending account access restore e-mail message, due to the exception: {} being thrown. Reason: {}",
                    exception.getClass().getSimpleName(), exception.getCause().getClass().getSimpleName(), exception.getMessage());
        }
    }

    /**
     * Sends an account blocking notification e-mail to the specified e-mail address.
     *
     * @param firstName     User's first name.
     * @param lastName      User's last name.
     * @param emailReceiver E-mail address to which the message will be sent.
     * @param language      Language of the message.
     */
    @RolesAllowed({Authorities.BLOCK_ACCOUNT, Authorities.LOGIN})
    public void sendBlockAccountInfoEmail(String firstName, String lastName, String emailReceiver, String language, boolean adminLock) {
        try {
            String logo = this.loadImage("eldorado.png").orElseThrow(() -> new ImageNotFoundException(MailProviderMessages.IMAGE_NOT_FOUND_EXCEPTION));
            String emailContent = this.loadTemplate("default-template.html").orElseThrow(() -> new EmailTemplateNotFoundException(MailProviderMessages.EMAIL_TEMPLATE_NOT_FOUND_EXCEPTION))
                    .replace("$firstname", firstName)
                    .replace("$lastname", lastName)
                    .replace("$greeting_message", I18n.getMessage(I18n.BLOCK_ACCOUNT_GREETING_MESSAGE, language))
                    .replace("$result_message",
                            I18n.getMessage(adminLock ? I18n.BLOCK_ACCOUNT_RESULT_MESSAGE_ADMIN : I18n.BLOCK_ACCOUNT_RESULT_MESSAGE_AUTO, language))
                    .replace("$action_description",
                            I18n.getMessage(adminLock ? I18n.BLOCK_ACCOUNT_ACTION_DESCRIPTION_ADMIN : I18n.BLOCK_ACCOUNT_ACTION_DESCRIPTION_AUTO, language))
                    .replace("$note_title", I18n.getMessage(I18n.BLOCK_ACCOUNT_NOTE_TITLE, language))
                    .replace("$note_message", I18n.getMessage(I18n.AUTO_GENERATED_MESSAGE_NOTE, language))
                    .replace("$eldorado_logo", "data:image/png;base64," + logo);
            this.sendEmail(emailContent, emailReceiver, senderEmail, I18n.getMessage(I18n.BLOCK_ACCOUNT_MESSAGE_SUBJECT, language));
        } catch (EmailTemplateNotFoundException | ImageNotFoundException | MessagingException |
                 NullPointerException exception) {
            log.error("Exception of type: {} was throw while sending account block e-mail message, due to the exception: {} being thrown. Reason: {}",
                    exception.getClass().getSimpleName(), exception.getCause().getClass().getSimpleName(), exception.getMessage());
        }
    }

    /**
     * Sends an account unblocking notification e-mail to the specified e-mail address.
     *
     * @param firstName     User's first name.
     * @param lastName      User's last name.
     * @param emailReceiver E-mail address to which the message will be sent.
     * @param language      Language of the message.
     */
    @RolesAllowed({Authorities.UNBLOCK_ACCOUNT})
    public void sendUnblockAccountInfoEmail(String firstName, String lastName, String emailReceiver, String language) {
        try {
            String logo = this.loadImage("eldorado.png").orElseThrow(() -> new ImageNotFoundException("Given image could not be found!"));
            String emailContent = this.loadTemplate("default-template.html").orElseThrow(() -> new EmailTemplateNotFoundException("Given email template not found!"))
                    .replace("$firstname", firstName)
                    .replace("$lastname", lastName)
                    .replace("$greeting_message", I18n.getMessage(I18n.UNBLOCK_ACCOUNT_GREETING_MESSAGE, language))
                    .replace("$result_message", I18n.getMessage(I18n.UNBLOCK_ACCOUNT_RESULT_MESSAGE, language))
                    .replace("$action_description", I18n.getMessage(I18n.UNBLOCK_ACCOUNT_ACTION_DESCRIPTION, language))
                    .replace("$note_title", I18n.getMessage(I18n.UNBLOCK_ACCOUNT_NOTE_TITLE, language))
                    .replace("$note_message", I18n.getMessage(I18n.AUTO_GENERATED_MESSAGE_NOTE, language))
                    .replace("$eldorado_logo", "data:image/png;base64," + logo);
            this.sendEmail(emailContent, emailReceiver, senderEmail, I18n.getMessage(I18n.UNBLOCK_ACCOUNT_MESSAGE_SUBJECT, language));
        } catch (EmailTemplateNotFoundException | ImageNotFoundException | MessagingException |
                 NullPointerException exception) {
            log.error("Exception of type: {} was throw while sending account unblock e-mail message, due to the exception: {} being thrown. Reason: {}",
                    exception.getClass().getSimpleName(), exception.getCause().getClass().getSimpleName(), exception.getMessage());
        }
    }

    /**
     * Sends an e-mail change confirmation e-mail to the specified e-mail address.
     *
     * @param firstName       User's first name.
     * @param lastName        User's last name.
     * @param emailReceiver   E-mail address to which the message will be sent.
     * @param confirmationURL URL used to confirm the e-mail address.
     * @param language        Language of the message.
     */
    @RolesAllowed({Authorities.CHANGE_USER_MAIL, Authorities.CHANGE_OWN_MAIL, Authorities.RESEND_EMAIL_CONFIRMATION_MAIL})
    public void sendEmailConfirmEmail(String firstName, String lastName, String emailReceiver, String confirmationURL, String language) {
        try {
            String logo = this.loadImage("eldorado.png").orElseThrow(() -> new ImageNotFoundException(MailProviderMessages.IMAGE_NOT_FOUND_EXCEPTION));
            String emailContent = this.loadTemplate("link-template.html").orElseThrow(() -> new EmailTemplateNotFoundException(MailProviderMessages.EMAIL_TEMPLATE_NOT_FOUND_EXCEPTION))
                    .replace("$firstname", firstName)
                    .replace("$lastname", lastName)
                    .replace("$greeting_message", I18n.getMessage(I18n.CONFIRM_EMAIL_GREETING_MESSAGE, language))
                    .replace("$result_message", I18n.getMessage(I18n.CONFIRM_EMAIL_RESULT_MESSAGE, language))
                    .replace("$action_description", I18n.getMessage(I18n.CONFIRM_EMAIL_ACTION_DESCRIPTION, language))
                    .replace("$action_link", confirmationURL)
                    .replace("$note_title", I18n.getMessage(I18n.CONFIRM_EMAIL_NOTE_TITLE, language))
                    .replace("$note_message", I18n.getMessage(I18n.AUTO_GENERATED_MESSAGE_NOTE, language))
                    .replace("$eldorado_logo", "data:image/png;base64," + logo);
            this.sendEmail(emailContent, emailReceiver, senderEmail, I18n.getMessage(I18n.CONFIRM_EMAIL_MESSAGE_SUBJECT, language));
        } catch (EmailTemplateNotFoundException | ImageNotFoundException | MessagingException |
                 NullPointerException exception) {
            log.error("Exception of type: {} was throw while sending e-mail change confirmation message, due to the exception: {} being thrown. Reason: {}",
                    exception.getClass().getSimpleName(), exception.getCause().getClass().getSimpleName(), exception.getMessage());
        }
    }

    /**
     * Send e-mail message to the e-mail address provided by the unauthenticated user, about changing their account
     * password. Basically, it provides them with a URL to reset their password.
     *
     * @param firstName       User's first name.
     * @param lastName        User's last name.
     * @param emailReceiver   E-mail address to which the message will be sent.
     * @param confirmationURL URL used to confirm the account creation.
     * @param language        Language of the message.
     */
    @RolesAllowed({Authorities.RESET_PASSWORD, Authorities.CHANGE_USER_PASSWORD})
    public void sendPasswordResetEmail(String firstName, String lastName, String emailReceiver, String confirmationURL, String language) {
        try {
            String logo = this.loadImage("eldorado.png").orElseThrow(() -> new ImageNotFoundException(MailProviderMessages.IMAGE_NOT_FOUND_EXCEPTION));
            String emailContent = this.loadTemplate("link-template.html").orElseThrow(() -> new EmailTemplateNotFoundException(MailProviderMessages.EMAIL_TEMPLATE_NOT_FOUND_EXCEPTION))
                    .replace("$firstname", firstName)
                    .replace("$lastname", lastName)
                    .replace("$greeting_message", I18n.getMessage(I18n.PASSWORD_RESET_GREETING_MESSAGE, language))
                    .replace("$result_message", I18n.getMessage(I18n.PASSWORD_RESET_RESULT_MESSAGE, language))
                    .replace("$action_description", I18n.getMessage(I18n.PASSWORD_RESET_ACTION_DESCRIPTION, language))
                    .replace("$action_link", confirmationURL)
                    .replace("$note_title", I18n.getMessage(I18n.PASSWORD_RESET_NOTE_TITLE, language))
                    .replace("$note_message", I18n.getMessage(I18n.AUTO_GENERATED_MESSAGE_NOTE, language))
                    .replace("$eldorado_logo", "data:image/png;base64," + logo);
            this.sendEmail(emailContent, emailReceiver, senderEmail, I18n.getMessage(I18n.PASSWORD_RESET_MESSAGE_SUBJECT, language));
        } catch (EmailTemplateNotFoundException | ImageNotFoundException | MessagingException |
                 NullPointerException exception) {
            log.error("Exception of type: {} was throw while sending password reset e-mail message, due to the exception: {} being thrown. Reason: {}",
                    exception.getClass().getSimpleName(), exception.getCause().getClass().getSimpleName(), exception.getMessage());
        }
    }

    /**
     * Sends e-mail message with authentication code for the second step in multifactor authentication to the e-mail
     * address, attached to user account.
     *
     * @param firstName     User's first name.
     * @param lastName      User's last name.
     * @param authCode      Authentication code used for the second step of multifactor authentication.
     * @param emailReceiver E-mail address to which the message will be sent.
     * @param language      Language of the message.
     */
    @RolesAllowed({Authorities.LOGIN})
    public void sendTwoFactorAuthCode(String firstName, String lastName, String authCode, String emailReceiver, String language) {
        try {
            String logo = this.loadImage("eldorado.png").orElseThrow(() -> new ImageNotFoundException(MailProviderMessages.IMAGE_NOT_FOUND_EXCEPTION));
            String emailContent = this.loadTemplate("code-template.html").orElseThrow(() -> new EmailTemplateNotFoundException(MailProviderMessages.EMAIL_TEMPLATE_NOT_FOUND_EXCEPTION))
                    .replace("$firstname", firstName)
                    .replace("$lastname", lastName)
                    .replace("$greeting_message", I18n.getMessage(I18n.LOGIN_AUTHENTICATION_CODE_GREETING_MESSAGE, language))
                    .replace("$result_message", I18n.getMessage(I18n.LOGIN_AUTHENTICATION_CODE_RESULT_MESSAGE, language))
                    .replace("$action_description", I18n.getMessage(I18n.LOGIN_AUTHENTICATION_CODE_ACTION_DESCRIPTION, language))
                    .replace("$code", authCode)
                    .replace("$note_title", I18n.getMessage(I18n.LOGIN_AUTHENTICATION_CODE_NOTE_TITLE, language))
                    .replace("$note_message", I18n.getMessage(I18n.AUTO_GENERATED_MESSAGE_NOTE, language))
                    .replace("$eldorado_logo", "data:image/png;base64," + logo);
            this.sendEmail(emailContent, emailReceiver, senderEmail, I18n.getMessage(I18n.LOGIN_AUTHENTICATION_CODE_MESSAGE_SUBJECT, language));
        } catch (EmailTemplateNotFoundException | ImageNotFoundException | MessagingException |
                 NullPointerException exception) {
            log.error("Exception of type: {} was throw while sending two factor auth code e-mail message, due to the exception: {} being thrown. Reason: {}",
                    exception.getClass().getSimpleName(), exception.getCause().getClass().getSimpleName(), exception.getMessage());
        }
    }

    /**
     * This method is used to send e-mail message when newly registered user account is being activated.
     *
     * @param firstName     User's first name.
     * @param lastName      User's last name.
     * @param emailReceiver E-mail address to which the message will be sent.
     * @param language      Language of the message.
     */
    @RolesAllowed({Authorities.CONFIRM_ACCOUNT_CREATION})
    public void sendActivationConfirmationEmail(String firstName, String lastName, String emailReceiver, String language) {
        try {
            String logo = this.loadImage("eldorado.png").orElseThrow(() -> new ImageNotFoundException(MailProviderMessages.IMAGE_NOT_FOUND_EXCEPTION));
            String emailContent = this.loadTemplate("default-template.html").orElseThrow(() -> new EmailTemplateNotFoundException(MailProviderMessages.EMAIL_TEMPLATE_NOT_FOUND_EXCEPTION))
                    .replace("$firstname", firstName)
                    .replace("$lastname", lastName)
                    .replace("$greeting_message", I18n.getMessage(I18n.CONFIRM_ACCOUNT_ACTIVATION_GREETING_MESSAGE, language))
                    .replace("$result_message", I18n.getMessage(I18n.CONFIRM_ACCOUNT_ACTIVATION_RESULT_MESSAGE, language))
                    .replace("$action_description", I18n.getMessage(I18n.CONFIRM_ACCOUNT_ACTIVATION_ACTION_DESCRIPTION, language))
                    .replace("$note_title", I18n.getMessage(I18n.CONFIRM_ACCOUNT_ACTIVATION_NOTE_TITLE, language))
                    .replace("$note_message", I18n.getMessage(I18n.AUTO_GENERATED_MESSAGE_NOTE, language))
                    .replace("$eldorado_logo", "data:image/png;base64," + logo);
            this.sendEmail(emailContent, emailReceiver, senderEmail, I18n.getMessage(I18n.CONFIRM_ACCOUNT_ACTIVATION_MESSAGE_SUBJECT, language));
        } catch (EmailTemplateNotFoundException | ImageNotFoundException | MessagingException |
                 NullPointerException exception) {
            log.error("Exception of type: {} was throw while sending successful account activation e-mail message, due to the exception: {} being thrown. Reason: {}",
                    exception.getClass().getSimpleName(), exception.getCause().getClass().getSimpleName(), exception.getMessage());
        }
    }

    /**
     * This method is used to send e-mail notification about newly granted to user account user level.
     *
     * @param firstName     User's first name.
     * @param lastName      User's last name.
     * @param emailReceiver E-mail address to which the message will be sent.
     * @param userLevel     Internationalization key indicating the user level that was granted to the user account.
     * @param language      Language of the message.
     */
    @RolesAllowed({Authorities.ADD_USER_LEVEL})
    public void sendEmailNotificationAboutGrantedUserLevel(String firstName, String lastName, String emailReceiver, String userLevel, String language) {
        try {
            String logo = this.loadImage("eldorado.png").orElseThrow(() -> new ImageNotFoundException(MailProviderMessages.IMAGE_NOT_FOUND_EXCEPTION));
            String emailContent = this.loadTemplate("default-template.html").orElseThrow(() -> new EmailTemplateNotFoundException(MailProviderMessages.EMAIL_TEMPLATE_NOT_FOUND_EXCEPTION))
                    .replace("$firstname", firstName)
                    .replace("$lastname", lastName)
                    .replace("$greeting_message", I18n.getMessage(I18n.ACCESS_LEVEL_GRANTED_GREETING_MESSAGE, language))
                    .replace("$result_message", I18n.getMessage(I18n.ACCESS_LEVEL_GRANTED_RESULT_MESSAGE, language))
                    .replace("$USER_LEVEL", I18n.getMessage(userLevel, language))
                    .replace("$action_description", I18n.getMessage(I18n.ACCESS_LEVEL_GRANTED_ACTION_DESCRIPTION, language))
                    .replace("$note_title", I18n.getMessage(I18n.ACCESS_LEVEL_GRANTED_NOTE_TITLE, language))
                    .replace("$note_message", I18n.getMessage(I18n.AUTO_GENERATED_MESSAGE_NOTE, language))
                    .replace("$eldorado_logo", "data:image/png;base64," + logo);
            this.sendEmail(emailContent, emailReceiver, senderEmail, I18n.getMessage(I18n.ACCESS_LEVEL_GRANTED_MESSAGE_SUBJECT, language));
        } catch (EmailTemplateNotFoundException | ImageNotFoundException | MessagingException |
                 NullPointerException exception) {
            log.error("Exception of type: {} was throw while sending e-mail notification message about granted user level, due to the exception: {} being thrown. Reason: {}",
                    exception.getClass().getSimpleName(), exception.getCause().getClass().getSimpleName(), exception.getMessage());
        }
    }

    /**
     * This method is used to send e-mail notification about revoked user level connected to user account.
     *
     * @param firstName     User's first name.
     * @param lastName      User's last name.
     * @param emailReceiver E-mail address to which the message will be sent.
     * @param userLevel     Internationalization key indicating the user level connected to the account that was revoked.
     * @param language      Language of the message.
     */
    @RolesAllowed({Authorities.REMOVE_USER_LEVEL})
    public void sendEmailNotificationAboutRevokedUserLevel(String firstName, String lastName, String emailReceiver, String userLevel, String language) {
        try {
            String logo = this.loadImage("eldorado.png").orElseThrow(() -> new ImageNotFoundException(MailProviderMessages.IMAGE_NOT_FOUND_EXCEPTION));
            String emailContent = this.loadTemplate("default-template.html").orElseThrow(() -> new EmailTemplateNotFoundException(MailProviderMessages.EMAIL_TEMPLATE_NOT_FOUND_EXCEPTION))
                    .replace("$firstname", firstName)
                    .replace("$lastname", lastName)
                    .replace("$greeting_message", I18n.getMessage(I18n.ACCESS_LEVEL_REVOKED_GREETING_MESSAGE, language))
                    .replace("$result_message", I18n.getMessage(I18n.ACCESS_LEVEL_REVOKED_RESULT_MESSAGE, language))
                    .replace("$USER_LEVEL", I18n.getMessage(userLevel, language))
                    .replace("$action_description", I18n.getMessage(I18n.ACCESS_LEVEL_REVOKED_ACTION_DESCRIPTION, language))
                    .replace("$note_title", I18n.getMessage(I18n.ACCESS_LEVEL_REVOKED_NOTE_TITLE, language))
                    .replace("$note_message", I18n.getMessage(I18n.AUTO_GENERATED_MESSAGE_NOTE, language))
                    .replace("$eldorado_logo", "data:image/png;base64," + logo);
            this.sendEmail(emailContent, emailReceiver, senderEmail, I18n.getMessage(I18n.ACCESS_LEVEL_REVOKED_MESSAGE_SUBJECT, language));
        } catch (EmailTemplateNotFoundException | ImageNotFoundException | MessagingException |
                 NullPointerException exception) {
            log.error("Exception of type: {} was throw while sending e-mail notification message about revoked user level, due to the exception: {} being thrown. Reason: {}",
                    exception.getClass().getSimpleName(), exception.getCause().getClass().getSimpleName(), exception.getMessage());
        }
    }

    /**
     * This method is used to send the user e-mail address.
     *
     * @param emailContent  E-mail content that will be sent to the user e-mail address (in HTML format).
     * @param emailReceiver E-mail address of the user, which the mail is sent to.
     * @param senderEmail   E-mail address of the sender - that is the Eldorado application.
     * @param emailSubject  Topic of the e-mail message.
     * @throws MessagingException Exception thrown while the e-mail message is being sent.
     */
    @RolesAllowed({Authorities.REGISTER_CLIENT, Authorities.REGISTER_USER,
            Authorities.RESTORE_ACCOUNT_ACCESS, Authorities.BLOCK_ACCOUNT,
            Authorities.LOGIN, Authorities.UNBLOCK_ACCOUNT, Authorities.CHANGE_USER_MAIL,
            Authorities.CHANGE_OWN_MAIL, Authorities.RESEND_EMAIL_CONFIRMATION_MAIL,
            Authorities.RESET_PASSWORD, Authorities.CHANGE_USER_PASSWORD,
            Authorities.CONFIRM_ACCOUNT_CREATION, Authorities.ADD_USER_LEVEL,
            Authorities.REMOVE_USER_LEVEL})
    private void sendEmail(String emailContent, String emailReceiver, String senderEmail, String emailSubject) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

        messageHelper.setTo(emailReceiver);

        messageHelper.setSubject(emailSubject);
        messageHelper.setText(emailContent, true);
        messageHelper.setFrom(senderEmail);

        this.mailSender.send(mimeMessage);
    }

    /**
     * Loads e-mail template from the /resources/templates folder.
     *
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
        } catch (IOException | NullPointerException exception) {
            log.error("Exception: {} thrown while reading template: {} from a file. Cause: {}. Make sure that the file containing the template is located in the templates/ folder.",
                    exception.getClass().getSimpleName(), templateName, exception.getMessage());
        }
        return Optional.of(builder.toString());
    }

    /**
     * Loads an image from the /resources/templates/images folder.
     *
     * @param imageName Name of the image file.
     * @return Returns a String containing the loaded image.
     * If an exception occurs while reading the image file it will return everything read up to this point.
     */
    private Optional<String> loadImage(String imageName) {
        StringBuilder builder = new StringBuilder();
        try {
            URL imagePath = getClass().getClassLoader().getResource("templates/images/" + imageName);
            byte[] imageContent = Files.readAllBytes(Path.of(imagePath.toURI()));
            builder.append(new String(Base64.getEncoder().encode(imageContent)));
        } catch (IOException | URISyntaxException | NullPointerException exception) {
            log.error("Exception: {} thrown while reading image: {} from a file. Cause: {}. Make sure that the file containing the image is located in the templates/images/ folder.",
                    exception.getClass().getSimpleName(), imageName, exception.getMessage());
        }
        return Optional.of(builder.toString());
    }
}
