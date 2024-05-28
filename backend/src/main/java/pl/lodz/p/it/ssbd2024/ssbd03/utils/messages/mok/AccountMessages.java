package pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok;

public class AccountMessages {

    public static final String LOGIN_BLANK = "bean.validation.account.login.blank";
    public static final String LOGIN_REGEX_NOT_MET = "bean.validation.account.login.regex.not.met";
    public static final String LOGIN_TOO_SHORT = "bean.validation.account.login.too.short";
    public static final String LOGIN_TOO_LONG = "bean.validation.account.login.too.long";

    public static final String PASSWORD_BLANK = "bean.validation.account.password.blank";
    public static final String PASSWORD_INVALID_LENGTH = "bean.validation.account.password.invalid.length";

    public static final String SUSPENDED_NULL = "bean.validation.account.suspended.flag.null";
    public static final String ACTIVE_NULL = "bean.validation.account.active.flag.null";
    public static final String BLOCKED_NULL = "bean.validation.account.blocked.flag.null";
    public static final String TWO_FACTOR_AUTH_NULL = "bean.validation.account.two.factor.auth.flag.null";

    public static final String NAME_BLANK = "bean.validation.account.first.name.blank";
    public static final String NAME_REGEX_NOT_MET = "bean.validation.account.first.name.regex.not.met";
    public static final String NAME_TOO_SHORT = "bean.validation.account.first.name.too.short";
    public static final String NAME_TOO_LONG = "bean.validation.account.first.name.too.long";

    public static final String LASTNAME_BLANK = "bean.validation.account.last.name.blank";
    public static final String LASTNAME_REGEX_NOT_MET = "bean.validation.account.last.name.regex.not.met";
    public static final String LASTNAME_TOO_SHORT = "bean.validation.account.last.name.too.short";
    public static final String LASTNAME_TOO_LONG = "bean.validation.account.last.name.too.long";

    public static final String EMAIL_CONSTRAINT_NOT_MET = "bean.validation.account.email.constraint.not.met";
    public static final String EMAIL_TOO_SHORT = "bean.validation.account.email.too.short";
    public static final String EMAIL_TOO_LONG = "bean.validation.account.email.too.long";

    public static final String USER_LEVEL_NULL = "bean.validation.account.user.level.null";
    public static final String USER_LEVEL_EMPTY = "bean.validation.account.user.level.empty";
    public static final String USER_LEVEL_FULL = "bean.validation.account.user.level.full";

    public static final String LANGUAGE_BLANK = "bean.validation.account.language.blank";
    public static final String LANGUAGE_REGEX_NOT_MET = "bean.validation.account.language.regex.not.met";

    public static final String PHONE_NUMBER_BLANK = "bean.validation.account.phone.number.blank";
    public static final String PHONE_NUMBER_REGEX_NOT_MET = "bean.validation.account.phone.number.regex.not.met";

    public static final String VERSION_NULL = "bean.validation.account.version.null";
    public static final String VERSION_LESS_THAN_ZERO = "bean.validation.account.version.less.than.zero";

    public static final String CREATION_TIMESTAMP_FUTURE = "bean.validation.account.creation.time.from.future";
    public static final String UPDATE_TIMESTAMP_FUTURE = "bean.validation.account.update.time.from.future";
}
