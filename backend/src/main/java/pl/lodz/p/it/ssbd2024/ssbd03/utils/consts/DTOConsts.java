package pl.lodz.p.it.ssbd2024.ssbd03.utils.consts;

public class DTOConsts {

    // Refresh token constants
    public static final int REFRESH_TOKEN_MIN_LENGTH = 32;
    public static final int REFRESH_TOKEN_MAX_LENGTH = 256;

    // Login constants
    public static final int LOGIN_MIN_LENGTH = 4;
    public static final int LOGIN_MAX_LENGTH = 32;
    public static final String LOGIN_REGEX = "^[a-zA-Z0-9_-]{4,32}$";

    // Password constants
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 32;
    public static final String PASSWORD_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,32}$";

    // Language constants
    public static final int LANGUAGE_LENGTH = 2;
    public static final String LANGUAGE_REGEX = "^([A-Z]{2}|[a-z]{2})$";

    // Parking city constants
    public static final String CITY_REGEX = "^([a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*$";
    public static final int CITY_MIN_LENGTH = 2;
    public static final int CITY_MAX_LENGTH = 50;

    // Parking zip-code constants
    public static final String ZIP_CODE_REGEX = "^\\d{2}-\\d{3}$";
    public static final int ZIP_CODE_MIN_LENGTH = 6;
    public static final int ZIP_CODE_MAX_LENGTH = 6;

    // Parking street constants
    public static final String STREET_REGEX = "^[A-Za-z0-9.-]${5, 50}";
    public static final int STREET_MIN_LENGTH = 2;
    public static final int STREET_MAX_LENGTH = 50;

    // Make reservation constants
    public static final String UUID_REGEX = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";
    public static final String DATE_REGEX = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])T([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9].\\d{3}Z$";
}
