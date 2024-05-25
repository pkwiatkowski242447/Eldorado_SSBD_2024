"use client"

import eldoLogo from "@/assets/eldorado.png";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu.tsx";
import {Button} from "@/components/ui/button.tsx";
import {CircleUser} from "lucide-react";
import {useAccountState} from "@/context/AccountContext.tsx";
import {RolesEnum} from "@/types/TokenPayload.ts";
import {Pathnames} from "@/router/pathnames.ts";
import {useNavigate} from "react-router-dom";
import {useAccount} from "@/hooks/useAccount.ts";
import {useTranslation} from "react-i18next";

const SiteHeader = () => {
    const {account} = useAccountState();
    const navigate = useNavigate();
    const {t} = useTranslation();

    const {logOut} = useAccount();

    function onClickLogout() {
        logOut();
    }

    function onClickSettings() {
        if (account){
            navigate(Pathnames.loggedIn.accountSettings);
        }
    }

    function onClickChangeUserLevel() {
        if (account){
            navigate(Pathnames.loggedIn.changeUserLevel);
        }
    }

    let headerColor = 'bg-gray-200 border-gray-200';
    if (account) {
        switch (account.activeUserLevel.roleName) {
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
            className={`sticky top-0 flex h-16 items-center gap-4 border-b bg-background px-4 md:px-6 border-2 rounded-xl ${headerColor}`}>
            <nav className="flex items-center gap-4">
                <a href="/home" className="flex items-center">
                    <img src={eldoLogo} alt="Eldorado" className="h-25 w-8/12"/>
                    <span className="sr-only">Eldorado</span>
                </a>
                {account?.activeUserLevel.roleName === RolesEnum.ADMIN && (
                    <Button variant="link" onClick={() => navigate("/manage-users")}
                            className="text-muted-foreground transition-colors hover:text-foreground">
                        {t("siteHeader.users")}
                    </Button>
                )}
                <Button variant="link" onClick={() => navigate("/home")}
                        className="text-muted-foreground transition-colors hover:text-foreground">
                    {t("siteHeader.parkings")}
                </Button>
            </nav>
            <div className="flex w-full items-center gap-4 justify-end">
                <small className="text-sm font-medium leading-none">{t("siteHeader.hello")}{account?.login}</small>
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
            </div>
        </header>
    );
}

export default SiteHeader;
