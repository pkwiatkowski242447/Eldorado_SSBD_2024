import {useNavigate} from "react-router-dom"; // Import useHistory hook
import {Pathnames} from "../router/pathnames";
import {Button} from "@/components/ui/button.tsx";
import eldoLogo from "@/assets/eldorado.png";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card.tsx";

function HomePage() {
    const navigate = useNavigate()

    const redirectToLoginPage = () => {
        navigate(Pathnames.public.login); // Redirect to login page
    };

    const redirectToRegisterPage = () => {
        navigate(Pathnames.public.register); // Redirect to login page
    };

    return (
        <div>
            <img src={eldoLogo} alt="Eldorado" className="mx-auto h-auto w-2/3"/>
            <div>
                <Card className="mx-auto h-auto w-2/3">
                    <CardHeader>
                        <CardTitle>Coming soon.</CardTitle>
                        <CardDescription>14.06.2024</CardDescription>
                        <CardDescription>(hopefully) </CardDescription>
                        <CardDescription>(most likely) </CardDescription>
                    </CardHeader>
                    <CardContent>
                        <div className="flex items-center justify-between">
                            <Button onClick={redirectToLoginPage}>Log in</Button>
                            <Button onClick={redirectToRegisterPage}>Register</Button>
                        </div>
                    </CardContent>
                </Card>
            </div>
        </div>
    );
}

export default HomePage;
