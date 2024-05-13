import {Pathnames} from "./pathnames";
import {RouteType} from "../types/RouteType";
// import UsersPage from "../pages/UsersPage";
// import AddUserPage from "../pages/AddUserPage";
// import ClientsPage from "../pages/ClientsPage";
// import CourtsPage from "../pages/CourtsPage";
// import ReservationsPage from "../pages/ReservationsPage";
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
// import RegisterPage from "../pages/RegisterPage";
// import ChangePasswordFormForm from "../components/forms/ChangePasswordForm";
// import ChangePasswordPage from "../pages/ChangePasswordPage";
// import ChangeDetailsPage from "../pages/ChangeDetailsPage";

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
        path: Pathnames.public.register,
        Component: RegisterPage
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
        path: Pathnames.public.home,
        Component: HomePage
    },
    {
        path: Pathnames.public.userManagement,
        Component: UserManagementPage
    },
    {
        path: Pathnames.public.changeUserLevel,
        Component: ChangeUserLevelPage
    },
    {

        path: Pathnames.public.accountSettings,
        Component: AccountSettings
    },

]

// export const AdminRoutes: RouteType[] = [
//     {
//         path: Pathnames.public.home,
//         Component: HomePage
//     },
//     // {
//     //     path: Pathnames.admin.main,
//     //     Component: AdminMainPage
//     // },
// ]

// export const StaffRoutes: RouteType[] = [
//     {
//         path: Pathnames.public.home,
//         Component: HomePage
//     },
//     {
//         path: Pathnames.staff.main,
//         Component: StaffMainPage
//     },
// ]
//
// export const ClientRoutes: RouteType[] = [
//     {
//         path: Pathnames.public.home,
//         Component: HomePage
//     },
//     {
//         path: Pathnames.client.main,
//         Component: ClientMainPage
//     },
// ]