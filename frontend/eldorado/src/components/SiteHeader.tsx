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


const SiteHeader= () => {
    const {account} = useAccountState();
    console.log(account)
    return (
        <header className="sticky top-0 flex h-16 items-center gap-4 border-b bg-background px-4 md:px-6">
            <nav
                className="hidden flex-col gap-6 text-lg font-medium md:flex md:flex-row md:items-center md:gap-5 md:text-sm lg:gap-6">
                <a
                    href="/home"
                    className="flex items-center gap-2 text-lg font-semibold md:text-base"
                >
                    <img src={eldoLogo} alt="Eldorado" className="h-auto w-auto"/>
                    <span className="sr-only">Eldorado</span>
                </a>
                {account?.activeUserLevel.roleName === RolesEnum.ADMIN && (
                    <a
                        href="#"
                        className="text-muted-foreground transition-colors hover:text-foreground"
                    >
                        Users
                    </a>
                )}
                <a
                    href="#"
                    className="text-muted-foreground transition-colors hover:text-foreground"
                >
                    Orders
                </a>
                <a
                    href="#"
                    className="text-muted-foreground transition-colors hover:text-foreground"
                >
                    Products
                </a>
                <a
                    href="#"
                    className="text-muted-foreground transition-colors hover:text-foreground"
                >
                    Customers
                </a>
                <a
                    href="#"
                    className="text-foreground transition-colors hover:text-foreground"
                >
                    Settings
                </a>
            </nav>
            <div className="flex w-full items-right gap-4 md:ml-auto md:gap-2 lg:gap-4 justify-end">
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
                        <DropdownMenuItem>Settings</DropdownMenuItem>
                        <DropdownMenuItem>Support</DropdownMenuItem>
                        <DropdownMenuSeparator/>
                        <DropdownMenuItem>Logout</DropdownMenuItem>
                    </DropdownMenuContent>
                </DropdownMenu>
            </div>
        </header>
    );
}

export default SiteHeader;