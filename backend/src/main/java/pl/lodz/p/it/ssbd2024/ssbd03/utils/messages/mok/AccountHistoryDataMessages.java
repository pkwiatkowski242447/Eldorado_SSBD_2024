package pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok;

public class AccountHistoryDataMessages {

    public static final String ID_NULL = "bean.validation.account.history.id.null";
    public static final String ID_REGEX_NOT_MET = "bean.validation.account.history.id.regex.not.met";

    public static final String LOGIN_BLANK = "bean.validation.account.history.login.blank";
    public static final String LOGIN_REGEX_NOT_MET = "bean.validation.account.history.login.regex.not.met";
    public static final String LOGIN_TOO_SHORT = "bean.validation.account.history.login.too.short";
    public static final String LOGIN_TOO_LONG = "bean.validation.account.history.login.too.long";

    public static final String PASSWORD_BLANK = "bean.validation.account.history.password.blank";
    public static final String PASSWORD_INVALID_LENGTH = "bean.validation.account.history.password.invalid.length";

    public static final String SUSPENDED_NULL = "bean.validation.account.history.suspended.flag.null";
    public static final String ACTIVE_NULL = "bean.validation.account.history.active.flag.null";
    public static final String BLOCKED_NULL = "bean.validation.account.history.blocked.flag.null";
    public static final String TWO_FACTOR_AUTH_NULL = "bean.validation.account.history.two.factor.auth.flag.null";

    public static final String NAME_BLANK = "bean.validation.account.history.first.name.blank";
    public static final String NAME_REGEX_NOT_MET = "bean.validation.account.history.first.name.regex.not.met";
    public static final String NAME_TOO_SHORT = "bean.validation.account.history.first.name.too.short";
    public static final String NAME_TOO_LONG = "bean.validation.account.history.first.name.too.long";

    public static final String LASTNAME_BLANK = "bean.validation.account.history.last.name.blank";
    public static final String LASTNAME_REGEX_NOT_MET = "bean.validation.account.history.last.name.regex.not.met";
    public static final String LASTNAME_TOO_SHORT = "bean.validation.account.history.last.name.too.short";
    public static final String LASTNAME_TOO_LONG = "bean.validation.account.history.last.name.too.long";

    public static final String EMAIL_CONSTRAINT_NOT_MET = "bean.validation.account.history.email.constraint.not.met";
    public static final String EMAIL_TOO_SHORT = "bean.validation.account.history.email.too.short";
    public static final String EMAIL_TOO_LONG = "bean.validation.account.history.email.too.long";

    public static final String LANGUAGE_BLANK = "bean.validation.account.history.language.blank";
    public static final String LANGUAGE_REGEX_NOT_MET = "bean.validation.account.history.language.regex.not.met";

    public static final String PHONE_NUMBER_BLANK = "bean.validation.account.history.phone.number.blank";
    public static final String PHONE_NUMBER_REGEX_NOT_MET = "bean.validation.account.history.phone.number.regex.not.met";

    public static final String VERSION_NULL = "bean.validation.account.history.version.null";
    public static final String VERSION_LESS_THAN_ZERO = "bean.validation.account.history.version.less.than.zero";

    public static final String LAST_SUCCESSFUL_LOGIN_IP_NOT_VALID = "bean.validation.account.history.successful.login.ip.not.valid";
    public static final String LAST_UNSUCCESSFUL_LOGIN_IP_NOT_VALID = "bean.validation.account.history.unsuccessful.login.ip.not.valid";

    public static final String MODIFICATION_TIME_NULL = "bean.validation.account.history.modification.time.null";
    public static final String MODIFICATION_TIME_FUTURE = "bean.validation.account.history.modification.time.from.future";
}
