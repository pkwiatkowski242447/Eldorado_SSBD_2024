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

    // ParkingCreateDTO Messages ----------------------------------------------------------------

    public static final String CITY_BLANK = "bean.validation.parking.create.dto.city.blank";
    public static final String CITY_REGEX_NOT_MET = "bean.validation.parking.create.dto.city.regex.not.met";
    public static final String CITY_TOO_SHORT = "bean.validation.parking.create.dto.city.too.short";
    public static final String CITY_TOO_LONG = "bean.validation.parking.create.dto.city.too.long";

    public static final String ZIP_CODE_BLANK = "bean.validation.parking.create.dto.zip.code.blank";
    public static final String ZIP_CODE_REGEX_NOT_MET = "bean.validation.parking.create.dto.zip.code.regex.not.met";
    public static final String ZIP_CODE_TOO_SHORT = "bean.validation.parking.create.dto.zip.code.too.short";
    public static final String ZIP_CODE_TOO_LONG = "bean.validation.parking.create.dto.zip.code.too.long";

    public static final String STREET_BLANK = "bean.validation.parking.create.dto.street.blank";
    public static final String STREET_REGEX_NOT_MET = "bean.validation.parking.create.dto.street.regex.not.met";
    public static final String STREET_TOO_SHORT = "bean.validation.parking.create.dto.street.too.short";
    public static final String STREET_TOO_LONG = "bean.validation.parking.create.dto.street.too.long";

    // MakeReservationDTO

    public static final String RESERVATION_BEGIN_TIME_INVALID = "bean.validation.reservation.make.dto.begin.time.invalid";
    public static final String RESERVATION_END_TIME_INVALID = "bean.validation.reservation.make.dto.end.time.invalid";
    public static final String SECTOR_UUID_BLANK = "bean.validation.reservation.make.dto.uuid.blank";
    public static final String SECTOR_UUID_REGEX_NOT_MET = "bean.validation.reservation.make.dto.sector.uuid.regex.not.met";
    public static final String BEGIN_TIME_BLANK = "bean.validation.reservation.make.dto.begin.time.blank";
    public static final String BEGIN_TIME_REGEX_NOT_MET = "bean.validation.reservation.make.dto.begin.time.regex.not.met";
    public static final String END_TIME_BLANK = "bean.validation.reservation.make.dto.end.time.blank";
    public static final String END_TIME_REGEX_NOT_MET = "bean.validation.reservation.make.dto.end.time.regex.not.met";
}
