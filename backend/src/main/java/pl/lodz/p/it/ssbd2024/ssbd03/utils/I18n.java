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
    public static final String AUTH_CONTROLLER_ACCOUNT_LOGOUT = "authentication.controller.account.logout";

    // Account service
    public static final String ACCOUNT_NOT_FOUND_EXCEPTION = "account.service.account.not.found.exception";
    public static final String ACCOUNT_CONSTRAINT_VALIDATION_EXCEPTION = "account.service.account.constraint.validation.exception";
    public static final String ACCOUNT_SAME_EMAIL_EXCEPTION = "account.service.account.same.email.exception";
    public static final String ACCOUNT_EMAIL_COLLISION_EXCEPTION = "account.service.account.email.collision.exception";
    public static final String ACCOUNT_ALREADY_BLOCKED_EXCEPTION = "account.service.account.already.blocked.exception";
    public static final String ACCOUNT_ALREADY_UNBLOCKED_EXCEPTION = "account.service.account.already.unblocked.exception";
    public static final String ACCOUNT_TRY_TO_BLOCK_OWN_EXCEPTION = "account.service.account.try_to_block_own.exception";

    // Account controller
    public static final String TOKEN_INVALID_OR_EXPIRED = "account.controller.token.invalid.or.expired";
    public static final String UUID_INVALID = "account.controller.uuid.invalid";
    public static final String BAD_UUID_INVALID_FORMAT_EXCEPTION = "account.controller.uuid.invalid.format.exception";

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
    public static final String ACCOUNT_NOT_FOUND_ACCOUNT_CONTROLLER = "account.controller.account.not.found";

    public static final String BLOCK_ACCOUNT_GREETING_MESSAGE = "mail.block.account.greeting.message";
    public static final String BLOCK_ACCOUNT_MESSAGE_SUBJECT = "mail.block.account.message.subject";
    public static final String BLOCK_ACCOUNT_RESULT_MESSAGE_AUTO = "mail.block.account.result_message_auto";
    public static final String BLOCK_ACCOUNT_RESULT_MESSAGE_ADMIN = "mail.block.account.result_message_admin";
    public static final String BLOCK_ACCOUNT_ACTION_DESCRIPTION_AUTO = "mail.block.account.action_description_auto";
    public static final String BLOCK_ACCOUNT_ACTION_DESCRIPTION_ADMIN = "mail.block.account.action_description_admin";
    public static final String BLOCK_ACCOUNT_NOTE_TITLE = "mail.block.account.note_title";

    public static final String UNBLOCK_ACCOUNT_GREETING_MESSAGE = "mail.unblock.account.greeting.message";
    public static final String UNBLOCK_ACCOUNT_MESSAGE_SUBJECT = "mail.unblock.account.message.subject";
    public static final String UNBLOCK_ACCOUNT_RESULT_MESSAGE = "mail.unblock.account.result_message";
    public static final String UNBLOCK_ACCOUNT_ACTION_DESCRIPTION = "mail.unblock.account.action_description";
    public static final String UNBLOCK_ACCOUNT_NOTE_TITLE = "mail.unblock.account.note_title";

    public static String getMessage(String messageKey, String language) {
        Locale locale = new Locale.Builder().setLanguage(language).build();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", locale);
        return resourceBundle.getString(messageKey);
    }
}
