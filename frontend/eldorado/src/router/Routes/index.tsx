import {Route, Routes} from "react-router-dom";
// import {AdminRoutes, ClientRoutes, PublicRoutes, STAFFRoutes} from "../routes";
import PublicLayout from "../../components/layouts/PublicLayout";
import {useAccount} from "../../hooks/useAccount";
// import AdminLayout from "../../components/layouts/AdminLayout";
// import ClientLayout from "../../components/layouts/ClientLayout";
import {useEffect} from "react";
import {PublicRoutes} from "../routes";
// import PageNotFound from "../../pages/PageNotFound";
// import ResourceAdminLayout from "../../components/layouts/ResourceAdminLayout";

export const RoutesComponents = () => {
    const {isAuthenticated} = useAccount();
    useEffect(() => {
        // getCurrentAccount();
    }, []);
    return (
        <Routes>
            {PublicRoutes.map(({path, Component}) => (
                <Route key={path} path={path} element={
                    <PublicLayout>
                        <Component/>
                    </PublicLayout>}/>
            ))}

            {/*{isAuthenticated && accountType===AccountTypeEnum.ADMIN && AdminRoutes.map(({path, Component}) => (*/}
            {/*    <Route key={path} path={path} element={*/}
            {/*        <AdminLayout>*/}
            {/*            <Component/>*/}
            {/*        </AdminLayout>}/>*/}
            {/*))}*/}

            {/*{isAuthenticated && accountType===AccountTypeEnum.CLIENT && ClientRoutes.map(({path, Component}) => (*/}
            {/*    <Route key={path} path={path} element={*/}
            {/*        <ClientLayout>*/}
            {/*            <Component/>*/}
            {/*        </ClientLayout>}/>*/}
            {/*))}*/}

            {/*{isAuthenticated && accountType===AccountTypeEnum.STAFF && STAFFRoutes.map(({path, Component}) => (*/}
            {/*    <Route key={path} path={path} element={*/}
            {/*        <ResourceAdminLayout>*/}
            {/*            <Component/>*/}
            {/*        </ResourceAdminLayout>}/>*/}
            {/*))}*/}

            {/*{!isAuthenticated && PublicRoutes.map(({path, Component}) => (*/}
            {/*    <Route key={path} path={path} element={*/}
            {/*        <PublicLayout>*/}
            {/*            <Component/>*/}
            {/*        </PublicLayout>}/>*/}
            {/*))}*/}


            {/*{!isAuthenticated &&*/}
            {/*    <Route path="*" element={*/}
            {/*        <PublicLayout>*/}
            {/*            <PageNotFound/>*/}
            {/*        </PublicLayout>*/}
            {/*    }/>}*/}

            {/*{isAuthenticated && accountType===AccountTypeEnum.ADMIN &&*/}
            {/*    <Route path="*" element={*/}
            {/*        <AdminLayout>*/}
            {/*            <PageNotFound/>*/}
            {/*        </AdminLayout>*/}
            {/*        }/>}*/}

            {/*{isAuthenticated && accountType===AccountTypeEnum.STAFF &&*/}
            {/*    <Route path="*" element={*/}
            {/*        <ResourceAdminLayout>*/}
            {/*            <PageNotFound/>*/}
            {/*        </ResourceAdminLayout>*/}
            {/*    }/>}*/}

            {/*{isAuthenticated && accountType===AccountTypeEnum.CLIENT &&*/}
            {/*    <Route path="*" element={*/}
            {/*        <ClientLayout>*/}
            {/*            <PageNotFound/>*/}
            {/*        </ClientLayout>*/}
            {/*    }/>}*/}
        </Routes>
    )
}