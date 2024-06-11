package pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mop;

public class SectorMessages {

    public static final String PARKING_NULL = "bean.validation.sector.parking.null";

    public static final String SECTOR_NAME_INVALID = "bean.validation.sector.name.invalid";
    public static final String SECTOR_NAME_BLANK = "bean.validation.sector.name.blank";
    public static final String SECTOR_REGEX_NOT_MET = "bean.validation.sector.name.regex.not.met";

    public static final String SECTOR_TYPE_NULL = "bean.validation.sector.sector.type.null";

    public static final String SECTOR_MAX_PLACES_NULL = "bean.validation.sector.max.places.null";
    public static final String SECTOR_MAX_PLACES_NEGATIVE = "bean.validation.sector.max.places.negative";
    public static final String SECTOR_MAX_PLACES_FULL = "bean.validation.sector.max.places.full";

    public static final String SECTOR_OCCUPIED_PLACES_NULL = "bean.validation.sector.available.places.null";
    public static final String SECTOR_OCCUPIED_PLACES_NEGATIVE = "bean.validation.sector.available.places.negative";

    public static final String SECTOR_WEIGHT_NULL = "bean.validation.sector.weight.null";
    public static final String SECTOR_WEIGHT_TOO_SMALL = "bean.validation.sector.weight.too.small";
    public static final String SECTOR_WEIGHT_TOO_LARGE = "bean.validation.sector.weight.too.large";

    public static final String SECTOR_ACTIVE_NULL = "bean.validation.sector.active.null";

    public static final String DEACTIVATION_TIME_PAST = "bean.validation.sector.deactivation.time.in.past";

    public static final String CREATION_TIMESTAMP_FUTURE = "bean.validation.sector.creation.time.from.future";
    public static final String UPDATE_TIMESTAMP_FUTURE = "bean.validation.sector.update.time.from.future";
}
