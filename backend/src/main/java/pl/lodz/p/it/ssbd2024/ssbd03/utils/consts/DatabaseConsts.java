package pl.lodz.p.it.ssbd2024.ssbd03.utils.consts;

public class DatabaseConsts {

    // General

    public static final String PK_COLUMN = "id";
    public static final String VERSION_COLUMN = "version";

    public static final String CREATION_TIMESTAMP = "creation_timestamp";
    public static final String UPDATE_TIMESTAMP = "update_timestamp";
    public static final String CREATED_BY = "created_by";
    public static final String UPDATED_BY = "updated_by";

    public static final String TOKEN_TABLE = "token";
    public static final String TOKEN_TOKEN_TYPE_COLUMN = "type";
    public static final String TOKEN_TOKEN_VALUE_COLUMN = "token_value";

    // MOK

    // public.account table

    public static final String ACCOUNT_TABLE = "account";

    public static final String ACCOUNT_LOGIN_COLUMN = "login";
    public static final String ACCOUNT_PASSWORD_COLUMN = "password";
    public static final String ACCOUNT_PAST_PASSWORD_COLUMN = "past_password";
    public static final String ACCOUNT_SUSPENDED_COLUMN = "suspended";
    public static final String ACCOUNT_ACTIVE_COLUMN = "active";
    public static final String ACCOUNT_BLOCKED_COLUMN = "blocked";
    public static final String TWO_FACTOR_AUTH_COLUMN = "two_factor_auth";
    public static final String ACCOUNT_BLOCKED_TIME_COLUMN = "blocked_timestamp";
    public static final String ACCOUNT_LANGUAGE_COLUMN = "language";
    public static final String ACCOUNT_PHONE_NUMBER_COLUMN = "phone_number";

    public static final String ACCOUNT_LAST_SUCCESSFUL_LOGIN_TIME = "last_successful_login_time";
    public static final String ACCOUNT_LAST_SUCCESSFUL_LOGIN_IP = "last_successful_login_ip";
    public static final String ACCOUNT_LAST_UNSUCCESSFUL_LOGIN_TIME = "last_unsuccessful_login_time";
    public static final String ACCOUNT_LAST_UNSUCCESSFUL_LOGIN_IP = "last_unsuccessful_login_ip";
    public static final String UNSUCCESSFUL_LOGIN_COUNTER = "unsuccessful_login_counter";

    // public.account_history_data

    public static final String ACCOUNT_HIST_TABLE = "account_history";

    public static final String ACCOUNT_HIST_ID_COLUMN = "id";
    public static final String ACCOUNT_HIST_VERSION_COLUMN = "version";
    public static final String ACCOUNT_HIST_LOGIN_COLUMN = "login";
    public static final String ACCOUNT_HIST_PASSWORD_COLUMN = "password";
    public static final String ACCOUNT_HIST_SUSPENDED_COLUMN = "suspended";
    public static final String ACCOUNT_HIST_ACTIVE_COLUMN = "active";
    public static final String ACCOUNT_HIST_BLOCKED_COLUMN = "blocked";
    public static final String ACCOUNT_HIST_TWO_FACTOR_AUTH_COLUMN = "two_factor_auth";
    public static final String ACCOUNT_HIST_BLOCKED_TIME_COLUMN = "blocked_time";
    public static final String ACCOUNT_HIST_FIRST_NAME_COLUMN = "first_name";
    public static final String ACCOUNT_HIST_LAST_NAME_COLUMN = "last_name";
    public static final String ACCOUNT_HIST_EMAIL_COLUMN = "email";
    public static final String ACCOUNT_HIST_PHONE_NUMBER_COLUMN = "phone_number";
    public static final String ACCOUNT_HIST_LAST_SUCCESSFUL_LOGIN_TIME_COLUMN = "last_successful_login_time";
    public static final String ACCOUNT_HIST_LAST_SUCCESSFUL_LOGIN_IP_COLUMN = "last_successful_login_ip";
    public static final String ACCOUNT_HIST_LAST_UNSUCCESSFUL_LOGIN_TIME_COLUMN = "last_unsuccessful_login_time";
    public static final String ACCOUNT_HIST_LAST_UNSUCCESSFUL_LOGIN_IP_COLUMN = "last_unsuccessful_login_ip";
    public static final String ACCOUNT_HIST_UNSUCCESSFUL_LOGIN_COUNTER_COLUMN = "unsuccessful_login_counter";
    public static final String ACCOUNT_HIST_LANGUAGE_COLUMN = "language";

    public static final String ACCOUNT_HIST_OPERATION_TYPE_COLUMN = "operation_type";
    public static final String ACCOUNT_HIST_MODIFIED_BY_COLUMN = "modified_by";
    public static final String ACCOUNT_HIST_MODIFICATION_TIME_COLUMN = "modification_time";

    // public.past_password table

    public static final String PAST_PASSWORD_TABLE = "past_password";
    public static final String PAST_PASSWORD_ACCOUNT_ID_COLUMN = "account_id";
    public static final String PAST_PASSWORD_VALUE_COLUMN = "password";

    // public.personal_data table

    public static final String PERSONAL_DATA_TABLE = "personal_data";

    public static final String PERSONAL_DATA_NAME_COLUMN = "name";
    public static final String PERSONAL_DATA_LASTNAME_COLUMN = "lastname";
    public static final String PERSONAL_DATA_EMAIL_COLUMN = "email";

    // public.user_level table

    public static final String USER_LEVEL_TABLE = "user_level";
    public static final String USER_LEVEL_ACCOUNT_ID_COLUMN = "account_id";

    // public.client_level table

    public static final String CLIENT_DATA_TABLE = "client_data";
    public static final String CLIENT_DATA_TYPE_COLUMN = "type";

    // public.staff_level table

    public static final String STAFF_DATA_TABLE = "staff_data";

    // public.admin_level table

    public static final String ADMIN_DATA_TABLE = "admin_data";

    // Discriminators

    public static final String DISCRIMINATOR_COLUMN = "level";
    public static final String CLIENT_DISCRIMINATOR = "CLIENT";
    public static final String STAFF_DISCRIMINATOR = "STAFF";
    public static final String ADMIN_DISCRIMINATOR = "ADMIN";
    public static final String AUTHENTICATED_DISCRIMINATOR = "AUTHENTICATED";

    // MOP

    // public.parking table

    public static final String PARKING_TABLE = "parking";

    public static final String PARKING_CITY_COLUMN = "city";
    public static final String PARKING_ZIP_CODE_COLUMN = "zip_code";
    public static final String PARKING_STREET_COLUMN = "street";

    // public.parking_event table

    public static final String PARKING_EVENT_TABLE = "parking_event";

    public static final String PARKING_EVENT_RESERVATION_ID_COLUMN = "reservation_id";
    public static final String PARKING_EVENT_DATE_COLUMN = "date";
    public static final String PARKING_EVENT_TYPE_COLUMN = "type";

    // public.reservation table

    public static final String RESERVATION_TABLE = "reservation";

    public static final String RESERVATION_CLIENT_ID_COLUMN = "client_id";
    public static final String RESERVATION_SECTOR_ID_COLUMN = "sector_id";
    public static final String RESERVATION_BEGIN_TIME_COLUMN = "begin_time";
    public static final String RESERVATION_END_TIME_COLUMN = "end_time";

    // public.sector table

    public static final String SECTOR_TABLE = "sector";

    public static final String SECTOR_PARKING_ID_COLUMN = "parking_id";
    public static final String SECTOR_NAME_COLUMN = "name";
    public static final String SECTOR_TYPE_COLUMN = "type";
    public static final String SECTOR_MAX_PLACES_COLUMN = "max_places";
    public static final String SECTOR_AVAILABLE_PLACES_COLUMN = "available_places";
    public static final String SECTOR_WEIGHT_COLUMN = "weight";
}
