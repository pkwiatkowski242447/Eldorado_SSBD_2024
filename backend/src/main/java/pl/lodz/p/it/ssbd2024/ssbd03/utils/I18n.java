package pl.lodz.p.it.ssbd2024.ssbd03.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {

    // Account exceptions
    public static final String ACCOUNT_CONSTRAINT_VIOLATION = "account.constraint.violation.exception";
    public static final String ACCOUNT_EMAIL_NOT_FOUND = "account.with.given.email.not.found.exception";
    public static final String ACCOUNT_ID_NOT_FOUND = "account.with.given.id.not.found.exception";
    public static final String ACCOUNT_BLOCKED_EXCEPTION = "account.status.blocked.exception";
    public static final String ACCOUNT_INACTIVE_EXCEPTION = "account.status.inactive.exception";

    // Account reset own password exceptions
    public static final String SET_NEW_PASSWORD_IS_THE_SAME_AS_CURRENT_ONE = "account.reset.password.same.passwords.exception";
    public static final String INCORRECT_PASSWORD = "account.reset.password.incorrect.current.password";

    // Account internationalization keys
    public static final String ACCOUNT_LOGIN_ALREADY_TAKEN = "account.login.already.taken.exception";
    public static final String ACCOUNT_EMAIL_ALREADY_TAKEN = "account.email.already.taken.exception";
    public static final String ACCOUNT_BLOCKED = "account.blocked.exception";
    public static final String ACCOUNT_BLOCKED_BY_ADMIN = "account.blocked.by.admin.exception";
    public static final String ACCOUNT_BLOCKED_BY_FAILED_LOGIN_ATTEMPTS = "account.blocked.by.too.many.failed.attempts.exception";
    public static final String ACCOUNT_ALREADY_BLOCKED = "account.already.blocked.exception";
    public static final String ACCOUNT_ALREADY_UNBLOCKED = "account.already.unblocked.exception";

    // Token exceptions
    public static final String TOKEN_VALUE_NOT_FOUND_EXCEPTION = "token.token.value.not.found.exception";
    public static final String TOKEN_NOT_VALID_EXCEPTION = "token.token.value.not.valid.exception";

    // Token internationalization keys
    public static final String TOKEN_VALUE_ALREADY_TAKEN = "token.value.already.taken.exception";

    // Optimistic lock
    public static final String OPTIMISTIC_LOCK_EXCEPTION = "optimistic.lock.exception";

    // Application internal server error
    public static final String INTERNAL_SERVER_ERROR = "application.internal.server.error.exception";

    // Authentication service
    public static final String AUTH_CREDENTIALS_INVALID_EXCEPTION = "authentication.service.account.credentials.not.valid.exception";
    public static final String AUTH_ACCOUNT_LOGIN_NOT_FOUND_EXCEPTION = "authentication.service.account.login.not.found.exception";
    public static final String AUTH_ACTIVITY_LOG_UPDATE_EXCEPTION = "authentication.service.activity.log.update.exception";

    // Account service
    public static final String STAFF_ACCOUNT_CREATION_EXCEPTION = "account.service.staff.account.not.created.exception";
    public static final String ADMIN_ACCOUNT_CREATION_EXCEPTION = "account.service.admin.account.not.created.exception";
    public static final String ADMIN_ACCOUNT_REMOVE_OWN_ADMIN_USER_LEVEL_EXCEPTION = "account.service.admin.remove.own.admin.user.level.exception";
    public static final String UNEXPECTED_USER_LEVEL = "user_level.type.unexpected.exception";
    public static final String ONE_USER_LEVEL = "user_level.one.user.level";
    public static final String UNEXPECTED_CLIENT_TYPE = "user_level.client.client_type.unexpected.exception";
    public static final String USER_LEVEL_DUPLICATED = "user_level.type.duplicated.exception";

    // Authentication controller
    public static final String AUTH_CONTROLLER_ACCOUNT_NOT_ACTIVE = "authentication.controller.account.not.active";
    public static final String AUTH_CONTROLLER_ACCOUNT_BLOCKED = "authentication.controller.account.blocked";
    public static final String AUTH_CONTROLLER_ACCOUNT_LOGOUT = "authentication.controller.account.logout";

    // Account service
    public static final String INVALID_LOGIN_ATTEMPT_EXCEPTION = "account.service.invalid.login.attempt.exception";
    public static final String ACCOUNT_NOT_FOUND_EXCEPTION = "account.service.account.not.found.exception";
    public static final String ACCOUNT_CONSTRAINT_VALIDATION_EXCEPTION = "account.service.account.constraint.validation.exception";
    public static final String ACCOUNT_SAME_EMAIL_EXCEPTION = "account.service.account.same.email.exception";
    public static final String ACCOUNT_EMAIL_COLLISION_EXCEPTION = "account.service.account.email.collision.exception";
    public static final String ACCOUNT_TRY_TO_BLOCK_OWN_EXCEPTION = "account.service.account.try_to_block_own.exception";
    public static final String ACCOUNT_EMAIL_FROM_TOKEN_NULL_EXCEPTION = "account.service.email_from_token_null.exception";
    public static final String TOKEN_NOT_FOUND_EXCEPTION = "token.not.found.exception";

    // Account controller
    public static final String TOKEN_INVALID_OR_EXPIRED = "account.controller.token.invalid.or.expired";
    public static final String UUID_INVALID = "account.controller.uuid.invalid";
    public static final String BAD_UUID_INVALID_FORMAT_EXCEPTION = "account.controller.uuid.invalid.format.exception";
    public static final String MISSING_HEADER_IF_MATCH = "account.controller.missing.header.if_match.exception";

    // JWT
    public static final String DATA_INTEGRITY_COMPROMISED = "controller.data.integrity.compromised.exception";

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

    public static final String PASSWORD_RESET_GREETING_MESSAGE = "mail.reset.password.greeting.message";
    public static final String PASSWORD_RESET_MESSAGE_SUBJECT = "mail.reset.password.message.subject";
    public static final String PASSWORD_RESET_RESULT_MESSAGE = "mail.reset.password.result_message";
    public static final String PASSWORD_RESET_ACTION_DESCRIPTION = "mail.reset.password.action_description";
    public static final String PASSWORD_RESET_NOTE_TITLE = "mail.reset.password.note_title";

    public static final String AUTO_GENERATED_MESSAGE_NOTE = "mail.auto.generate.message.note";

    public static String getMessage(String messageKey, String language) {
        Locale locale = new Locale.Builder().setLanguage(language).build();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", locale);
        return resourceBundle.getString(messageKey);
    }
}
