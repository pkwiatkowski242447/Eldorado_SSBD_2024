package pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mok;

public class AccountsConsts {

    public static final int LOGIN_MIN_LENGTH = 4;
    public static final int LOGIN_MAX_LENGTH = 32;
    public static final String LOGIN_REGEX = "^[a-zA-Z0-9_-]{8,32}$";

    public static final int PASSWORD_LENGTH = 60;

    public static final int NAME_MIN_LENGTH = 2;
    public static final int NAME_MAX_LENGTH = 32;
    public static final String NAME_REGEX = "^[a-zA-Z]{2, 32}$";

    public static final int LASTNAME_MIN_LENGTH = 2;
    public static final int LASTNAME_MAX_LENGTH = 32;
    public static final String LASTNAME_REGEX = "^[a-zA-Z]{2, 32}$";

    public static final int EMAIL_MIN_LENGTH = 8;
    public static final int EMAIL_MAX_LENGTH = 32;

    public static final int USER_LEVEL_MIN_SIZE = 1;
    public static final int USER_LEVEL_MAX_SIZE = 3;

    public static final String LANGUAGE_REGEX = "^[A-Za-z]{2,4}([_-][A-Za-z]{4})?([_-]([A-Za-z]{2}|[0-9]{3}))?$";

    public static final String PHONE_NUMBER_REGEX = "^(?<!\\w)(\\(?(\\+|00)?48\\)?)?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}(?!\\w)$";
}
