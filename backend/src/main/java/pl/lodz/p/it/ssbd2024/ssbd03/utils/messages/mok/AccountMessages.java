package pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok;

public class AccountMessages {

    public static final String LOGIN_BLANK = "Login could not be null, empty nor consist only of whitespace characters.";
    public static final String LOGIN_REGEX_NOT_MET = "Login can only consist of letters, digits, underscores and hyphens.";
    public static final String LOGIN_TOO_SHORT = "Login should be at least 8 characters long.";
    public static final String LOGIN_TOO_LONG = "Login could not be longer than 20 characters.";

    public static final String PASSWORD_BLANK = "Password could not be null, empty nor consist only of whitespace characters.";
    public static final String PASSWORD_INVALID_LENGTH = "Password hash must be 60 characters long.";

    public static final String VERIFIED_NULL = "Flag indicating whether account is verified could not be null.";
    public static final String ACTIVE_NULL = "Flag indicating whether account is active could not be null.";
    public static final String BLOCKED_NULL = "Flag indicating whether account is blocked could not be null.";

    public static final String NAME_BLANK = "Firstname could not be null, empty nor consist only of whitespace characters.";
    public static final String NAME_REGEX_NOT_MET = "Firstname can only consist of letters.";
    public static final String NAME_TOO_SHORT = "Name should be at least 2 characters long.";
    public static final String NAME_TOO_LONG = "Name could not be longer than 32 characters.";

    public static final String LASTNAME_BLANK = "Lastname could not be null, empty nor consist only of whitespace characters.";
    public static final String LASTNAME_REGEX_NOT_MET = "Lastname can only consist of letters.";
    public static final String LASTNAME_TOO_SHORT = "Lastname should be at least 2 characters long.";
    public static final String LASTNAME_TOO_LONG = "Lastname could not be longer than 32 characters.";

    public static final String EMAIL_NOT_MET = "Specified email address does not match email constraints.";
    public static final String EMAIL_TOO_SHORT = "Email should be at least 8 characters long.";
    public static final String EMAIL_TOO_LONG = "Email could not be longer than 32 characters.";

    public static final String USER_LEVEL_EMPTY = "Each user must have at least one user level.";
    public static final String USER_LEVEL_FULL = "Each user could not have more than three user levels.";

    public static final String LANGUAGE_BLANK = "Specified language setting could not be null, empty nor consist only of whitespace characters.";
    public static final String LANGUAGE_REGEX_NOT_MET = "Specified language setting does not match regex.";

    public static final String PHONE_NUMBER_BLANK = "Phone number could not be null, empty or consist of whitespace characters.";
    public static final String PHONE_NUMBER_REGEX_NOT_MET = "Specified phone number is not a valid phone number.";

    public static final String ACCOUNT_NOT_FOUND_EXCEPTION = "Account with given ID could not be found.";
    public static final String VALIDATION_EXCEPTION = "Invalid field(s).";
    public static final String SAME_EMAIL_EXCEPTION = "Account's Email cannot be changed to the same email.";
}
