package pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts;

public class Authorities {

                /*              MOK             */
    // MOK 1
    public static final String REGISTER_CLIENT = "REGISTER_CLIENT";
    // MOK 2
    public static final String LOGIN = "LOGIN";
    // MOK 3
    public static final String RESET_PASSWORD = "RESET_PASSWORD";
    // MOK 4
    public static final String BLOCK_ACCOUNT = "BLOCK_ACCOUNT";
    // MOK 5
    public static final String UNBLOCK_ACCOUNT = "UNBLOCK_ACCOUNT";
    // MOK 6
    public static final String ADD_USER_LEVEL = "ADD_USER_LEVEL";
    // MOK 7
    public static final String REMOVE_USER_LEVEL = "REMOVE_USER_LEVEL";
    // MOK 8
    public static final String CHANGE_OWN_PASSWORD = "CHANGE_OWN_PASSWORD";
    // MOK 9
    public static final String CHANGE_USER_PASSWORD = "CHANGE_USER_PASSWORD";
    // MOK 10
    public static final String CHANGE_OWN_MAIL = "CHANGE_OWN_MAIL";
    // MOK 11
    public static final String CHANGE_USER_MAIL = "CHANGE_USER_MAIL";
    // MOK 12
    public static final String MODIFY_OWN_ACCOUNT = "MODIFY_OWN_ACCOUNT";
    // MOK 13
    public static final String MODIFY_USER_ACCOUNT = "MODIFY_USER_ACCOUNT";
    // MOK 14
    public static final String LOGOUT = "LOGOUT";
    // MOK 15
    public static final String REMOVE_ACCOUNT = "REMOVE_ACCOUNT";
    // MOK 16
    public static final String GET_OWN_ACCOUNT = "GET_OWN_ACCOUNT";
    // MOK 17
    public static final String GET_ALL_USER_ACCOUNTS = "GET_ALL_USER_ACCOUNTS";
    // MOK 18
    public static final String GET_USER_ACCOUNT = "GET_USER_ACCOUNT";
    // MOK 19
    public static final String CHANGE_OWN_PASSWORD_AFTER_ADMINISTRATIVE_CHANGE = "CHANGE_OWN_PASSWORD_AFTER_ADMINISTRATIVE_CHANGE";
    // MOK 20
    public static final String SWITCH_USER_LEVEL = "SWITCH_USER_LEVEL";
    // MOK 21
    public static final String REGISTER_USER = "REGISTER_USER";
    // MOK 22
    public static final String CONFIRM_ACCOUNT_CREATION = "CONFIRM_ACCOUNT_CREATION";
    // MOK 23
    public static final String CONFIRM_EMAIL_CHANGE = "CONFIRM_EMAIL_CHANGE";
    // MOK 24
    public static final String RESEND_EMAIL_CONFIRMATION_MAIL = "RESEND_EMAIL_CONFIRMATION_MAIL";

                /*              MOP             */
    // MOP 1
    public static final String GET_ALL_PARKING = "GET_ALL_PARKING";
    // MOP 2
    public static final String ADD_PARKING = "ADD_PARKING";
    // MOP 3
    public static final String DELETE_PARKING = "DELETE_PARKING";
    // MOP 4
    public static final String EDIT_PARKING = "EDIT_PARKING";
    // MOP 5
    public static final String GET_ALL_SECTORS = "GET_ALL_SECTORS";
    // MOP 6
    public static final String ADD_SECTOR = "ADD_SECTOR";
    // MOP 7
    public static final String DELETE_SECTOR = "DELETE_SECTOR";
    // MOP 8
    public static final String EDIT_SECTOR = "EDIT_SECTOR";
    // MOP 9
    public static final String ACTIVATE_SECTOR = "ACTIVATE_SECTOR";
    // MOP 10
    public static final String DEACTIVATE_SECTOR = "DEACTIVATE_SECTOR";
    // MOP 11
    public static final String GET_ALL_AVAILABLE_PARKING = "GET_ALL_AVAILABLE_PARKING";
    // MOP 12
    public static final String GET_PARKING = "GET_PARKING";
    // MOP 13
    public static final String GET_SECTOR = "GET_SECTOR";
    // MOP 14
    public static final String RESERVE_PARKING_PLACE = "RESERVE_PARKING_PLACE";
    // MOP 15
    public static final String GET_ACTIVE_RESERVATIONS = "GET_ACTIVE_RESERVATIONS";
    // MOP 16
    public static final String GET_HISTORICAL_RESERVATIONS = "GET_HISTORICAL_RESERVATIONS";
    // MOP 17
    public static final String CANCEL_RESERVATION = "CANCEL_RESERVATION";
    // MOP 18
    public static final String ENTER_PARKING_WITHOUT_RESERVATION = "ENTER_PARKING_WITHOUT_RESERVATION";
    // MOP 19
    public static final String EXIT_PARKING = "EXIT_PARKING";
    // MOP 20
    public static final String CHANGE_CLIENT_TYPE = "CHANGE_CLIENT_TYPE";
    // MOP 21
    public static final String ENTER_PARKING_WITH_RESERVATION = "ENTER_PARKING_WITH_RESERVATION";
    // MOP 22
    public static final String GET_ALL_RESERVATIONS = "GET_ALL_RESERVATIONS";
    // MOP 23
    public static final String END_RESERVATION = "END_RESERVATION";

    // Additional

    public static final String REFRESH_SESSION = "REFRESH_SESSION";
    public static final String RESTORE_ACCOUNT_ACCESS = "RESTORE_ACCOUNT_ACCESS";
    public static final String GET_ADMIN_PASSWORD_RESET_STATUS = "RESTORE_ACCOUNT_ACCESS";
    public static final String GET_ACCOUNT_HISTORICAL_DATA = "GET_ACCOUNT_HISTORICAL_DATA";
    public static final String GET_OWN_HISTORICAL_DATA = "GET_OWN_HISTORICAL_DATA";
}
