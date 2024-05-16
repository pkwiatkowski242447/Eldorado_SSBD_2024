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


const SiteHeader = () => {
    const {account} = useAccountState();
    const navigator = useNavigate();

    const {logOut} = useAccount();

    function onClickLogout() {
        logOut()
    }

    function onClickSettings() {
        navigator(Pathnames.public.accountSettings);
    }

    function onClickChangeUserLevel() {
        navigator(Pathnames.public.changeUserLevel);
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
            <nav
                className="hidden flex-col gap-6 text-lg font-medium md:flex md:flex-row md:items-center md:gap-5 md:text-sm lg:gap-6">
                <a
                    href="/home"
                    className="flex items-center gap-2 text-lg font-semibold md:text-base"
                >
                    <img src={eldoLogo} alt="Eldorado" className="h-3/4 w-3/4 p-10 pr-8"/>
                    <span className="sr-only">Eldorado</span>
                </a>
                {account?.activeUserLevel.roleName === RolesEnum.ADMIN && (
                    <a
                        href="/manage-users"
                        className="text-muted-foreground transition-colors hover:text-foreground"
                    >
                        Users
                    </a>
                )}
                <a
                    href="#"
                    className="text-muted-foreground transition-colors hover:text-foreground"
                >
                    Parkings
                </a>

            </nav>
            <div className="flex w-full items-right gap-4 md:ml-auto md:gap-2 lg:gap-4 justify-end">
                <div className="flex items-center w-full gap-4 md:ml-auto md:gap-2 lg:gap-4 justify-end">
                    <small className="text-sm font-medium leading-none">Hello, {account?.login}</small>

                    <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                            <Button variant="secondary" size="icon" className="rounded-full">
                                <CircleUser className="h-5 w-5"/>
                                <span className="sr-only">Toggle user menu</span>
                            </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align="end">
                            <DropdownMenuLabel>My Account</DropdownMenuLabel>
                            <DropdownMenuSeparator/>
                            <DropdownMenuItem
                                onClick={onClickSettings}>
                                Settings
                            </DropdownMenuItem>
                            <DropdownMenuSeparator/>
                            <DropdownMenuItem
                                onClick={onClickChangeUserLevel}>
                                Change Access Level
                            </DropdownMenuItem>
                            <DropdownMenuSeparator/>
                            <DropdownMenuItem
                                className="font-bold"
                                onClick={onClickLogout}>
                                Logout
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>

                </div>
            </div>
        </header>
    );
}

export default SiteHeader;