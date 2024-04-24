package pl.lodz.p.it.ssbd2024.ssbd03.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {

    // Authentication service
    public static final String AUTH_CREDENTIALS_INVALID_EXCEPTION = "authentication.service.account.credentials.not.valid.exception";
    public static final String AUTH_ACCOUNT_LOGIN_NOT_FOUND_EXCEPTION = "authentication.service.account.login.not.found.exception";
    public static final String AUTH_ACTIVITY_LOG_UPDATE_EXCEPTION = "authentication.service.activity.log.update.exception";

    // Account service

    public static final String STAFF_ACCOUNT_CREATION_EXCEPTION = "account.service.staff.account.not.created.exception";
    public static final String ADMIN_ACCOUNT_CREATION_EXCEPTION = "account.service.admin.account.not.created.exception";

    // Authentication controller

    public static final String AUTH_CONTROLLER_ACCOUNT_NOT_ACTIVE = "authentication.controller.account.not.active";
    public static final String AUTH_CONTROLLER_ACCOUNT_BLOCKED = "authentication.controller.account.blocked";

    // Account service
    public static final String ACCOUNT_NOT_FOUND_EXCEPTION = "account.service.account.not.found.exception";
    public static final String ACCOUNT_CONSTRAINT_VALIDATION_EXCEPTION = "account.service.account.constraint.validation.exception";
    public static final String ACCOUNT_SAME_EMAIL_EXCEPTION = "account.service.account.same.email.exception";
    public static final String ACCOUNT_EMAIL_COLLISION_EXCEPTION = "account.service.account.email.collision.exception";

    // Mail provider

    public static final String CONFIRM_REGISTER_GREETING_MESSAGE = "mail.confirm.register.greeting.message";
    public static final String CONFIRM_REGISTER_MESSAGE_SUBJECT = "mail.confirm.register.message.subject";
    public static final String CONFIRM_REGISTER_RESULT_MESSAGE = "mail.confirm.register.result_message";
    public static final String CONFIRM_REGISTER_ACTION_DESCRIPTION = "mail.confirm.register.action_description";
    public static final String CONFIRM_REGISTER_NOTE_TITLE = "mail.confirm.register.note_title";

    public static final String CONFIRM_EMAIL_GREETING_MESSAGE = "mail.confirm.email.greeting.message";
    public static final String CONFIRM_EMAIL_MESSAGE_SUBJECT = "mail.confirm.email.message.subject";
    public static final String CONFIRM_EMAIL_RESULT_MESSAGE = "mail.confirm.email.result_message";
    public static final String CONFIRM_EMAIL_ACTION_DESCRIPTION = "mail.confirm.email.action_description";
    public static final String CONFIRM_EMAIL_NOTE_TITLE = "mail.confirm.email.note_title";
    public static final String AUTO_GENERATED_MESSAGE_NOTE = "mail.auto.generate.message.note";

    public static String getMessage(String messageKey, String language) {
        Locale locale = new Locale.Builder().setLanguage(language).build();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", locale);
        return resourceBundle.getString(messageKey);
    }
}
