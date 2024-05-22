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
}
