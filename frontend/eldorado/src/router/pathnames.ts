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
    },
    admin: {
        landingPage: '/',
        userManagement: '/manage-users',
        userAccountSettings: '/manage-users/:id',
        adminCreateUser: '/manage-users/create',
    },
    staff: {
        allReservations: '/all-reservations',
        parkingManagement: '/manage-parking',
        parkingManagementInfo: '/manage-parking/:id'
    },
    client: {
        myReservations: '/my-reservations',
    },
    loggedIn: {
        landingPage: '/',
        home: '/home',
        accountSettings: '/account-settings',
        changeUserLevel: '/change-user-level',
        confirmEmailChange: '/confirm-email/:token',
    }
}