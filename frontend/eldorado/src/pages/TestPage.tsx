import {useAccount} from '../hooks/useAccount';
import {useAccountState} from '../context/AccountContext';
import eldoLogo from "@/assets/eldorado.png";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {Button} from "@/components/ui/button.tsx";

function TestPage() {
    const {account, setAccount} = useAccountState();
    const {logOut} = useAccount();

    console.log(account);

    return (
        <div>
            <div className="flex justify-between items-center">
                <img src={eldoLogo} alt="Eldorado" className="h-auto w-1/2"/>
                <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                        <Button variant="outline">Account</Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent className="w-56">
                        <DropdownMenuLabel>Actions</DropdownMenuLabel>
                        <DropdownMenuSeparator/>
                        <Button onClick={logOut}>Log out</Button>
                    </DropdownMenuContent>
                </DropdownMenu>
            </div>
            <Card>
                <CardHeader>
                    <CardTitle>Hello, {account?.id}</CardTitle>
                    <CardDescription>We can't offer you much (for now)</CardDescription>
                    <CardDescription>Here's some info about your account:</CardDescription>
                </CardHeader>
                <CardContent>
                    <p>ID - {account?.login}</p>
                    <p>Active Role - {account?.activeRole}</p>
                </CardContent>
            </Card>
        </div>
    )
}

export default TestPage