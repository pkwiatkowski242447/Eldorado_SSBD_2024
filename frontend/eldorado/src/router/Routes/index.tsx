import {Route, Routes} from "react-router-dom";
// import {AdminRoutes, ClientRoutes, PublicRoutes, STAFFRoutes} from "../routes";
import PublicLayout from "../../components/layouts/PublicLayout";
import AdminLayout from "../../components/layouts/AdminLayout";
import ClientLayout from "../../components/layouts/ClientLayout";
import {useEffect} from "react";
import {AdminRoutes, ClientRoutes, PublicRoutes, StaffRoutes} from "../routes";
import NotFoundPage from "@/pages/NotFoundPage.tsx";
import StaffLayout from "@/components/layouts/StaffLayout";
import {useAccount} from "@/hooks/useAccount.ts";
import {RolesEnum} from "@/types/TokenPayload.ts";
import {Pathnames} from "@/router/pathnames.ts";

export const RoutesComponents = () => {
    const {isAuthenticated, account, getCurrentAccount} = useAccount();
    useEffect(() => {
        getCurrentAccount();
    }, []);

    return (
        <Routes>
            {!isAuthenticated &&
                <Route path="*" element={
                    <PublicLayout>
                        <NotFoundPage/>
                    </PublicLayout>
                }/>}

            {isAuthenticated && account?.activeUserLevel?.roleName === RolesEnum.ADMIN && AdminRoutes.map(({
                                                                                                              path,
                                                                                                              Component
                                                                                                          }) => (
                <Route key={path} path={path} element={
                    <AdminLayout>
                        <Component/>
                    </AdminLayout>}/>
            ))}

            {isAuthenticated && account?.activeUserLevel?.roleName
                === RolesEnum.CLIENT && ClientRoutes.map(({path, Component}) => (
                    <Route key={path} path={path} element={
                        <ClientLayout>
                            <Component/>
                        </ClientLayout>}/>
                ))}

            {isAuthenticated && account?.activeUserLevel?.roleName
                === RolesEnum.STAFF && StaffRoutes.map(({path, Component}) => (
                    <Route key={path} path={path} element={
                        <StaffLayout>
                            <Component/>
                        </StaffLayout>}/>
                ))}

            {!isAuthenticated && PublicRoutes.map(({path, Component}) => (
                <Route key={path} path={path} element={
                    <PublicLayout hideHeader={location.pathname !== Pathnames.public.home && location.pathname !== Pathnames.public.enterParkingWithoutReservation && location.pathname !== Pathnames.public.exitParking}>
                        <Component/>
                    </PublicLayout>}/>
            ))}

            {isAuthenticated && account?.activeUserLevel?.roleName === RolesEnum.ADMIN &&
                <Route path="*" element={
                    <AdminLayout>
                        <NotFoundPage/>
                    </AdminLayout>
                }/>}

            {isAuthenticated && account?.activeUserLevel?.roleName === RolesEnum.STAFF &&
                <Route path="*" element={
                    <StaffLayout>
                        <NotFoundPage/>
                    </StaffLayout>
                }/>}

            {isAuthenticated && account?.activeUserLevel?.roleName === RolesEnum.CLIENT &&
                <Route path="*" element={
                    <ClientLayout>
                        <NotFoundPage/>
                    </ClientLayout>
                }/>}
        </Routes>
    )
}