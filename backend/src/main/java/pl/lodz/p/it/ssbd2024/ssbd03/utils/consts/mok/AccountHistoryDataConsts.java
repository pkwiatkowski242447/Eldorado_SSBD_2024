package pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mok;

public class AccountHistoryDataConsts {

    public static final String ID_REGEX = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    public static final int LOGIN_MIN_LENGTH = 4;
    public static final int LOGIN_MAX_LENGTH = 32;
    public static final String LOGIN_REGEX = "^[a-zA-Z0-9_-]{4,32}$";

    public static final int PASSWORD_LENGTH = 60;

    public static final int NAME_MIN_LENGTH = 2;
    public static final int NAME_MAX_LENGTH = 32;
    public static final String NAME_REGEX = "^[a-zA-Z]{2,32}$";

    public static final int LASTNAME_MIN_LENGTH = 2;
    public static final int LASTNAME_MAX_LENGTH = 32;
    public static final String LASTNAME_REGEX = "^[a-zA-Z]{2,32}$";

    public static final int EMAIL_MIN_LENGTH = 8;
    public static final int EMAIL_MAX_LENGTH = 32;

    public static final String LANGUAGE_REGEX = "^([A-Z]{2}|[a-z]{2})$";

    public static final String PHONE_NUMBER_REGEX = "^(?<!\\w)(\\(?(\\+|00)?[1-9][0-9]{0,2}\\)?)?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}(?!\\w)$";

    public static final String IPV4_REGEX = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";
}
