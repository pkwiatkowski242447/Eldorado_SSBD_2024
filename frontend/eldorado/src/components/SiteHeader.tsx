import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Car, CircleUser, Menu} from "lucide-react";
import {useAccountState} from "@/context/AccountContext.tsx";
import {RolesEnum} from "@/types/TokenPayload.ts";
import {Pathnames} from "@/router/pathnames.ts";
import {Link, useNavigate} from "react-router-dom";
import {useAccount} from "@/hooks/useAccount.ts";
import {useTranslation} from "react-i18next";
import {Sheet, SheetContent, SheetTrigger} from "@/components/ui/sheet.tsx";
import eldoLogo from "@/assets/logo_eldorado.svg";

const SiteHeader = () => {
    const {account} = useAccountState();
    const navigate = useNavigate();
    const {t} = useTranslation();
    const accountLS = localStorage.getItem('account');
    const tokenLS = localStorage.getItem('token');
    const {logOut} = useAccount();

    function onClickLogout() {
        logOut();
    }

    function onClickSettings() {
        if (account) {
            navigate(Pathnames.loggedIn.accountSettings);
        }
    }

    function onClickChangeUserLevel() {
        if (account) {
            navigate(Pathnames.loggedIn.changeUserLevel);
        }
    }

    function onClickLogin() {
        navigate(Pathnames.public.login);
    }

    let headerColor = 'bg-gray-200 border-gray-200';
    if (account) {
        switch (account?.activeUserLevel?.roleName) {
            case RolesEnum.ADMIN:
                headerColor = 'bg-red-200 border-red-200';
                break;
            case RolesEnum.STAFF:
                headerColor = 'bg-blue-200 border-blue-200';
                break;
            case RolesEnum.CLIENT:
                headerColor = 'bg-green-200 border-green-200';
                break;
            default:
                headerColor = 'bg-black';
                break;
        }
    }

    return (
        <header
            className={`top-0 flex h-16 items-center gap-4 border-b bg-background px-4 md:px-6 border-2 rounded-xl ${headerColor}`}>
            <nav
                className="hidden flex-col gap-6 text-lg font-medium md:flex md:flex-row md:items-center md:gap-5 md:text-sm lg:gap-6">
                {/*<Link to="/home" className="flex items-center ">*/}
                {/*    <img src={eldoLogo} alt="Eldorado" className={"w-fit h-40"}/>*/}
                {/*    <span className="sr-only">Eldorado</span>*/}
                {/*</Link>*/}
                {account?.activeUserLevel?.roleName === RolesEnum.CLIENT && (
                    <div>
                    <Link to={Pathnames.loggedIn.home} className="flex items-center ">
                        <img src={eldoLogo} alt="Eldorado" className={"w-auto h-96"}/>
                        <span className="sr-only">Eldorado</span>
                    </Link>
                    </div>
                )}
                {account?.activeUserLevel?.roleName === RolesEnum.STAFF && (
                    <Link to={Pathnames.loggedIn.home} className="flex items-center ">
                        <img src={eldoLogo} alt="Eldorado" className={"w-96 h-96"}/>
                        <span className="sr-only">Eldorado</span>
                    </Link>
                )}
                {account?.activeUserLevel?.roleName === RolesEnum.ADMIN && (
                    <Link to={Pathnames.loggedIn.home} className="flex items-center ">
                        <img src={eldoLogo} alt="Eldorado" className={"w-96 h-96"}/>
                        <span className="sr-only">Eldorado</span>
                    </Link>
                )}
                {!account && (
                    <Link to={Pathnames.public.home} className="flex items-center ">
                        <img src={eldoLogo} alt="Eldorado" className={"w-96 h-96"}/>
                        <span className="sr-only">Eldorado</span>
                    </Link>
                )}
                {account?.activeUserLevel?.roleName === RolesEnum.ADMIN && (
                    <Link to={Pathnames.admin.userManagement} className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap">
                        {t("siteHeader.users")}
                    </Link>
                )}
                {account?.activeUserLevel?.roleName === RolesEnum.ADMIN && (
                    <Link to={Pathnames.admin.adminCreateUser}
                          className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap">
                        {t("siteHeader.addUser")}
                    </Link>
                )}
                {account?.activeUserLevel?.roleName === RolesEnum.CLIENT && (
                    <Link to={Pathnames.client.myReservations}
                          className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap">
                        {t("siteHeader.myReservations")}
                    </Link>
                )}
                {account?.activeUserLevel?.roleName === RolesEnum.CLIENT && (
                    <Link to={Pathnames.client.parkingList}
                          className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap">
                        {t("siteHeader.activeParkingList")}
                    </Link>
                )}
                {account?.activeUserLevel?.roleName === RolesEnum.STAFF && (
                    <Link to={Pathnames.staff.allReservations}
                          className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap">
                        {t("siteHeader.allReservations")}
                    </Link>
                )}
                {account?.activeUserLevel?.roleName === RolesEnum.STAFF && (
                    <Link to={Pathnames.staff.parkingManagement}
                          className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap ">
                        {t("siteHeader.manageParking")}
                    </Link>
                )}
                {account?.activeUserLevel?.roleName === RolesEnum.CLIENT && (
                    <Link to={Pathnames.client.enterParkingWithoutReservation}
                          className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap">
                        Enter Parking
                    </Link>
                )}
                {!account && (
                    <Link to={Pathnames.public.enterParkingWithoutReservation}
                          className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap">
                        Enter Parking
                    </Link>
                )}
                {account?.activeUserLevel?.roleName === RolesEnum.CLIENT && (
                    <Link to={Pathnames.client.enterParkingWithReservation}
                          className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap">
                        Enter Reservation
                    </Link>
                )}
                {(!account || account?.activeUserLevel?.roleName === RolesEnum.CLIENT) && (
                    <Link to={Pathnames.public.exitParking}
                          className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap">
                        {t("Exit Parking")}
                    </Link>
                )}
            </nav>
            <Sheet>
                <SheetTrigger asChild>
                    <Button
                        variant="outline"
                        size="icon"
                        className="shrink-0 md:hidden"
                    >
                        <Menu className="h-5 w-5"/>
                        <span className="sr-only">Toggle navigation menu</span>
                    </Button>
                </SheetTrigger>
                <SheetContent side="left">
                    <nav className="grid gap-6 text-lg font-medium whitespace-nowrap">
                        <Link to="/home" className="flex items-center gap-2 text-lg font-semibold md:text-base">
                            <Car></Car>
                            <span className="sr-only">Eldorado</span>
                        </Link>
                        {account?.activeUserLevel?.roleName === RolesEnum.ADMIN && (
                            <Link
                                to="/manage-users"
                                className="text-muted-foreground transition-colors hover:text-gray-500">
                                {t("siteHeader.users")}
                            </Link>

                        )}
                        {account?.activeUserLevel?.roleName === RolesEnum.ADMIN && (
                            <Link to="/manage-users/create"
                                  className="text-muted-foreground transition-colors hover:text-gray-500">
                                {t("siteHeader.addUser")}
                            </Link>
                        )}
                        {account?.activeUserLevel?.roleName === RolesEnum.CLIENT && (
                            <Link to={Pathnames.client.myReservations}
                                  className="text-muted-foreground transition-colors hover:text-gray-500">
                                {t("siteHeader.myReservations")}
                            </Link>
                        )}
                        {account?.activeUserLevel?.roleName === RolesEnum.CLIENT && (
                            <Link to={Pathnames.client.parkingList}
                                  className="text-muted-foreground transition-colors hover:text-gray-500">
                                {t("siteHeader.activeParkingList")}
                            </Link>
                        )}
                        {account?.activeUserLevel?.roleName === RolesEnum.STAFF && (
                            <Link to={Pathnames.staff.allReservations}
                                  className="text-muted-foreground transition-colors hover:text-gray-500">
                                {t("siteHeader.allReservations")}
                            </Link>
                        )}
                        {account?.activeUserLevel?.roleName === RolesEnum.STAFF && (
                            <Link to={Pathnames.staff.parkingManagement}
                                  className="text-muted-foreground transition-colors hover:text-gray-500">
                                {t("siteHeader.manageParking")}
                            </Link>
                        )}
                        {account?.activeUserLevel?.roleName === RolesEnum.CLIENT && (
                            <Link to={Pathnames.client.enterParkingWithoutReservation}
                                  className="text-muted-foreground transition-colors hover:text-gray-500">
                                Enter Parking
                            </Link>
                        )}
                        {!account && (
                            <Link to={Pathnames.public.enterParkingWithoutReservation}
                                  className="text-muted-foreground transition-colors hover:text-gray-500">
                                Enter Parking
                            </Link>
                        )}
                        {account?.activeUserLevel?.roleName === RolesEnum.CLIENT && (
                            <Link to={Pathnames.client.enterParkingWithReservation}
                                  className="text-muted-foreground transition-colors hover:text-gray-500">
                                Enter Reservation
                            </Link>
                        )}
                        {(!account || account?.activeUserLevel?.roleName === RolesEnum.CLIENT) && (
                            <Link to={Pathnames.public.exitParking}
                                  className="text-muted-foreground transition-colors hover:text-gray-500">
                                {t("Exit Parking")}
                            </Link>
                        )}
                    </nav>
                </SheetContent>
            </Sheet>
            <div className="flex w-full items-center gap-4 justify-end">
                {(accountLS && tokenLS) && (
                    <>
                        <small
                            className="text-sm font-medium leading-none">{t("siteHeader.hello")}{account?.login}</small>
                        <DropdownMenu>
                            <DropdownMenuTrigger asChild>
                                <Button variant="secondary" size="icon" className="rounded-full">
                                    <CircleUser className="h-5 w-5"/>
                                    <span className="sr-only">Toggle user menu</span>
                                </Button>
                            </DropdownMenuTrigger>
                            <DropdownMenuContent align="end">
                                <DropdownMenuLabel>{t("siteHeader.myAccount")}</DropdownMenuLabel>
                                <DropdownMenuSeparator/>
                                <DropdownMenuItem onClick={onClickSettings}>
                                    {t("siteHeader.settings")}
                                </DropdownMenuItem>
                                <DropdownMenuSeparator/>
                                <DropdownMenuItem onClick={onClickChangeUserLevel}>
                                    {t("siteHeader.changeLevel")}
                                </DropdownMenuItem>
                                <DropdownMenuSeparator/>
                                <DropdownMenuItem className="font-bold" onClick={onClickLogout}>
                                    {t("siteHeader.logout")}
                                </DropdownMenuItem>
                            </DropdownMenuContent>
                        </DropdownMenu>
                    </>
                )}
                {(!accountLS || !tokenLS) && (
                    <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                            <Button variant="secondary" size="icon" className="rounded-full">
                                <CircleUser className="h-5 w-5"/>
                                <span className="sr-only">Toggle user menu</span>
                            </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align="end">
                            <DropdownMenuItem onClick={onClickLogin} className={"font-bold"}>
                                {t("siteHeader.login")}
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>
                )}
            </div>
        </header>
    );
}

export default SiteHeader;
