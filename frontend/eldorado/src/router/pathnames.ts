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
    },
    admin: {
        userManagement: '/manage-users',
        userAccountSettings: '/manage-users/:id',
        adminCreateUser: '/manage-users/create',
    },
    staff: {

    },
    client: {

    },
    loggedIn: {
        home: '/home',
        accountSettings: '/account-settings',
        changeUserLevel: '/change-user-level',
    }
}