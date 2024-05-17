import {useNavigate} from "react-router-dom"; // Import useHistory hook
import {Pathnames} from "../router/pathnames";
import {Button} from "@/components/ui/button.tsx";
import eldoLogo from "@/assets/eldorado.png";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import {useTranslation} from "react-i18next";

function LandingPage() {
    const navigate = useNavigate()
    const {t} = useTranslation();

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
                        <CardTitle>{t("landingPage.title")}</CardTitle>
                        <CardDescription>14.06.2024</CardDescription>
                        <CardDescription>{t("landingPage.text")}</CardDescription>
                    </CardHeader>
                    <CardContent>
                        <div className="flex items-center justify-between">
                            <Button onClick={redirectToLoginPage}>{t("landingPage.logIn")}</Button>
                            <Button onClick={redirectToRegisterPage}>{t("landingPage.register")}</Button>
                        </div>
                    </CardContent>
                </Card>
            </div>
        </div>
    );
}

export default LandingPage;