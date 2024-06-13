export const Pathnames = {
    public: {
        landingPage: '/',
        login: '/login',
        twoFactorAuth: '/login/2fa/:login',
        register: '/register',
        home: '/home',
        activateAccount: '/activate-account/:token',
        forgotPassword: '/forgot-password/',
        resetPassword: '/reset-password/:token',
        confirmEmailChange: '/confirm-email/:token',
        restoreAccess: '/restore-access',
        restoreToken: '/restore-token/:token',
        parkingList: '/parking-list',
        parkingInfo: '/parking-list/parking-info/:id',
        enterParkingWithoutReservation: '/enter-parking',
    },
    admin: {
        landingPage: '/',
        userManagement: '/manage-users',
        userAccountSettings: '/manage-users/:id',
        adminCreateUser: '/manage-users/create',
    },
    staff: {
        allReservations: '/all-reservations',
        reservationDetails: '/all-reservations/:id',
        parkingManagement: '/manage-parking',
        parkingManagementInfo: '/manage-parking/:id'
    },
    client: {
        myReservations: '/my-reservations',
        myReservationDetails: '/my-reservations/:id',
        parkingManagement: '/manage-parking',
        parkingManagementInfo: '/manage-parking/:id',
        parkingList: '/parking-list',
        parkingInfo: '/parking-list/parking-info/:id',
        makeReservation: '/make-reservation/:id',
        enterParkingWithoutReservation: '/enter-parking',
    },
    loggedIn: {
        landingPage: '/',
        home: '/home',
        accountSettings: '/account-settings',
        changeUserLevel: '/change-user-level',
        confirmEmailChange: '/confirm-email/:token',
    }
}