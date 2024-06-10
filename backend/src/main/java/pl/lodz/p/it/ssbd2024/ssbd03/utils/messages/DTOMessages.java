package pl.lodz.p.it.ssbd2024.ssbd03.utils.messages;

public class DTOMessages {

    // RefreshTokenDTO Messages ----------------------------------------------------------------

    public static final String RT_REFRESH_TOKEN_BLANK = "bean.validation.refresh.token.dto.refresh.token.blank";
    public static final String RT_REFRESH_TOKEN_TOO_SHORT = "bean.validation.refresh.token.dto.refresh.token.too.short";
    public static final String RT_REFRESH_TOKEN_TOO_LONG = "bean.validation.refresh.token.dto.refresh.token.too.long";
    public static final String RT_REFRESH_TOKEN_NOT_JWT = "bean.validation.refresh.token.dto.refresh.token.not.jwt";

    // AccountLoginDTO Messages ----------------------------------------------------------------

    public static final String LOGIN_BLANK = "bean.validation.account.login.dto.login.blank";
    public static final String LOGIN_REGEX_NOT_MET = "bean.validation.account.login.dto.login.regex.not.met";
    public static final String LOGIN_TOO_SHORT = "bean.validation.account.login.dto.login.too.short";
    public static final String LOGIN_TOO_LONG = "bean.validation.account.login.dto.login.too.long";

    public static final String PASSWORD_BLANK = "bean.validation.account.login.dto.password.blank";
    public static final String PASSWORD_REGEX_NOT_MET = "bean.validation.account.login.dto.password.regex.not.met";
    public static final String PASSWORD_TOO_SHORT = "bean.validation.account.login.dto.password.too.short";
    public static final String PASSWORD_TOO_LONG = "bean.validation.account.login.dto.password.too.long";

    public static final String LANGUAGE_BLANK = "bean.validation.account.login.dto.language.blank";
    public static final String LANGUAGE_SIZE_INVALID = "bean.validation.account.login.dto.language.size.invalid";
    public static final String LANGUAGE_REGEX_NOT_MET = "bean.validation.account.login.dto.language.regex.not.met";

    // ParkingCreateDTO Messages ---------------------------------------------------------------
    public static final String PARKING_ENUM_INVALID = "bean.validation.parking.enum.invalid";

    // SectorCreateDTO Messages ---------------------------------------------------------------
    public static final String SECTOR_ENUM_INVALID = "bean.validation.sector.enum.invalid";
}
