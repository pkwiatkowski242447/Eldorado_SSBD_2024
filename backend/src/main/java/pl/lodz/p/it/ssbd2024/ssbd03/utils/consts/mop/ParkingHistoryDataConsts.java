package pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mop;

public class ParkingHistoryDataConsts {

    public static final String CITY_REGEX = "^([a-zA-Z\\u0080-\\u024FąĄćĆęĘłŁńŃóÓśŚźŹżŻ]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024FąĄćĆęĘłŁńŃóÓśŚźŹżŻ]*$";
    public static final int CITY_MIN_LENGTH = 2;
    public static final int CITY_MAX_LENGTH = 50;

    public static final String ZIP_CODE_REGEX = "^\\d{2}-\\d{3}$";
    public static final int ZIP_CODE_LENGTH = 6;

    public static final String STREET_REGEX = "^[A-Za-ząĄćĆęĘłŁńŃóÓśŚźŹżŻ0-9.-]{5,50}$";
    public static final int STREET_MIN_LENGTH = 2;
    public static final int STREET_MAX_LENGTH = 50;

}
