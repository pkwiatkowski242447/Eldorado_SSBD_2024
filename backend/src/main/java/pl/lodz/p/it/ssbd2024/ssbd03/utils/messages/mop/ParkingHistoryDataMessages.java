package pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mop;

public class ParkingHistoryDataMessages {

    public static final String CITY_REGEX_NOT_MET = "bean.validation.parking.history.city.name.regex.not.met";
    public static final String CITY_NAME_TOO_SHORT = "bean.validation.parking.history.city.name.too.short";
    public static final String CITY_NAME_TOO_LONG = "bean.validation.parking.history.city.name.too.long";

    public static final String ZIP_CODE_REGEX_NOT_MET = "bean.validation.parking.history.zip.code.regex.not.met";
    public static final String ZIP_CODE_INVALID = "bean.validation.parking.history.zip.code.invalid";

    public static final String STREET_REGEX_NOT_MET = "bean.validation.parking.history.street.name.regex.not.met";
    public static final String STREET_NAME_TOO_SHORT = "bean.validation.parking.history.street.name.too.short";
    public static final String STREET_NAME_TOO_LONG = "bean.validation.parking.history.street.name.too.long";

    public static final String CREATION_TIMESTAMP_FUTURE = "bean.validation.parking.history.creation.time.from.future";
    public static final String UPDATE_TIMESTAMP_FUTURE = "bean.validation.parking.history.update.time.from.future";

    public static final String ID_NULL = "bean.validation.parking.history.id.null";

    public static final String VERSION_NULL = "bean.validation.parking.history.version.null";
    public static final String VERSION_LESS_THAN_ZERO = "bean.validation.parking.history.version.less.than.zero";

}