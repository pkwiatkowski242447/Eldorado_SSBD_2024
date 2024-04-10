package pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mop;

public class SectorMessages {

    public static final String PARKING_NULL = "Each sector must be assigned to a parking lot.";

    public static final String SECTOR_NAME_INVALID = "Sector name is a sequence of 5 characters.";
    public static final String SECTOR_REGEX_NOT_MET = "Specified sector name does not match required pattern.";

    public static final String SECTOR_TYPE_NULL = "Sector type must be one of the following: COVERED, UNCOVERED, UNDERGROUND.";

    public static final String SECTOR_MAX_PLACES_NULL = "Number of maximum places of a sector could not be null.";
    public static final String SECTOR_MAX_PLACES_NEGATIVE = "Number of maximum places of a sector must be a positive integer or zero.";
    public static final String SECTOR_MAX_PLACES_FULL = "Sector could not have more than 1000 places total.";

    public static final String SECTOR_AVAILABLE_PLACES_NULL = "Number of available places of a sector could not be null.";
    public static final String SECTOR_AVAILABLE_PLACES_NEGATIVE = "Number of available places of a sector must be a positive integer or zero.";

    public static final String SECTOR_WEIGHT_NULL = "Weight of a sector could not be null.";
    public static final String SECTOR_WEIGHT_TOO_SMALL = "Weight of a sector could not be lower than 1.";
    public static final String SECTOR_WEIGHT_TOO_LARGE = "Weight of a sector could not be higher than 100.";
}
