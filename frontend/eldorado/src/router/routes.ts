import {Pathnames} from "./pathnames";
import {RouteType} from "../types/RouteType";
import LoginPage from "../pages/LoginPage";
import HomePage from "../pages/HomePage.tsx";
import RegisterPage from "@/pages/RegisterPage.tsx";
import ActivateAccountPage from "@/pages/ActivateAccountPage.tsx";
import ForgotPasswordPage from "@/pages/ForgotPasswordPage.tsx";
import ResetPasswordPage from "@/pages/ResetPasswordPage.tsx";
import LandingPage from "@/pages/LandingPage.tsx";
import UserManagementPage from "@/pages/UserManagementPage.tsx";
import AccountSettings from "@/pages/AccountSettings.tsx";
import ChangeUserLevelPage from "@/pages/ChangeUserLevelPage.tsx";
import ConfirmEmailChangePage from "@/pages/ConfirmEmailChangePage.tsx";
import UserAccountSettings from "@/pages/UserAccountSettings.tsx";
import TwoFactorAuthPage from "@/pages/TwoFactorAuthPage.tsx";


export const PublicRoutes: RouteType[] = [
    {
        path: Pathnames.public.landingPage,
        Component: LandingPage
    },
    {
        path: Pathnames.public.login,
        Component: LoginPage
    },
    {
        path: Pathnames.public.twoFactorAuth,
        Component: TwoFactorAuthPage
    },
    {
        path: Pathnames.public.register,
        Component: RegisterPage
    },
    {
        path: Pathnames.public.home,
        Component: HomePage
    },
    {
        path: Pathnames.public.activateAccount,
        Component: ActivateAccountPage
    },
    {
        path: Pathnames.public.forgotPassword,
        Component: ForgotPasswordPage
    },
    {
        path: Pathnames.public.resetPassword,
        Component: ResetPasswordPage
    },
    {
        path: Pathnames.public.confirmEmailChange,
        Component: ConfirmEmailChangePage
    },


]

export const AdminRoutes: RouteType[] = [
    {
        path: Pathnames.public.home,
        Component: HomePage
    },
    {
        path: Pathnames.loggedIn.accountSettings,
        Component: AccountSettings
    },
    {
        path: Pathnames.loggedIn.changeUserLevel,
        Component: ChangeUserLevelPage
    },
    {
        path: Pathnames.admin.userManagement,
        Component: UserManagementPage
    },
    {
        path: Pathnames.admin.userAccountSettings,
        Component: UserAccountSettings
    }
]

export const StaffRoutes: RouteType[] = [
    {
        path: Pathnames.public.home,
        Component: HomePage
    },
    {
        path: Pathnames.loggedIn.accountSettings,
        Component: AccountSettings
    },
    {
        path: Pathnames.loggedIn.changeUserLevel,
        Component: ChangeUserLevelPage
    },
]

export const ClientRoutes: RouteType[] = [
    {
        path: Pathnames.public.home,
        Component: HomePage
    },
    {
        path: Pathnames.loggedIn.accountSettings,
        Component: AccountSettings
    },
    {
        path: Pathnames.loggedIn.changeUserLevel,
        Component: ChangeUserLevelPage
    },
]