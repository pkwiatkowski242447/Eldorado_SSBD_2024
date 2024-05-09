import {Pathnames} from "./pathnames";
import {RouteType} from "../types/RouteType";
import HomePage from "../pages/HomePage";
// import UsersPage from "../pages/UsersPage";
// import AddUserPage from "../pages/AddUserPage";
// import ClientsPage from "../pages/ClientsPage";
// import CourtsPage from "../pages/CourtsPage";
// import ReservationsPage from "../pages/ReservationsPage";
import LoginPage from "../pages/LoginPage";
import TestPage from "../pages/TestPage";
import RegisterPage from "@/pages/RegisterPage.tsx";
import ActivateAccountPage from "@/pages/ActivateAccountPage.tsx";
// import RegisterPage from "../pages/RegisterPage";
// import ChangePasswordFormForm from "../components/forms/ChangePasswordForm";
// import ChangePasswordPage from "../pages/ChangePasswordPage";
// import ChangeDetailsPage from "../pages/ChangeDetailsPage";

export const PublicRoutes: RouteType[] = [
    {
        path: Pathnames.public.home,
        Component: HomePage
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
        path: Pathnames.public.test,
        Component: TestPage
    },
    {
        path: Pathnames.public.activateAccount,
        Component: ActivateAccountPage
    },

]

export const AdminRoutes: RouteType[] = [
    {
        path: Pathnames.public.home,
        Component: HomePage
    },
    // {
    //     path: Pathnames.admin.main,
    //     Component: AdminMainPage
    // },
]

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