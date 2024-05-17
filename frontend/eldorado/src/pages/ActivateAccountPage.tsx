import {useNavigate, useParams} from 'react-router-dom';
import {api} from '@/api/api.ts';
import {useToast} from '@/components/ui/use-toast.ts';
import {Button} from "@/components/ui/button.tsx";
import eldoLogo from "@/assets/eldorado.png";
import {Card, CardDescription, CardHeader, CardTitle,} from "@/components/ui/card"
import {useTranslation} from "react-i18next";

function ActivateAccountPage() {
    const {token} = useParams<{ token: string }>();
    const decodedToken = decodeURIComponent(token!);
    const {toast} = useToast();
    const navigate = useNavigate();
    const {t} = useTranslation();

    function onClickButton() {
        // console.log(decodedToken)
        api.activateAccount(decodedToken!)
            .then(() => {
                toast({
                    title: t("activateAccountPage.popUp.title"),
                    description: t("activateAccountPage.popUp.text"),
                    action: (
                        <div>
                            <Button onClick={() => {
                                navigate('/login', {replace: true});
                            }}>
                                Log in
                            </Button>
                        </div>
                    ),
                });
            })
            .catch((error) => {
                if (error.response && error.response.data) {
                    const {message, violations} = error.response.data;
                    const violationMessages = violations.map((violation: string | string[]) => t(violation)).join(", ");

                    toast({
                        variant: "destructive",
                        title: t(message),
                        description: violationMessages,
                    });
                } else {
                    toast({
                        variant: "destructive",
                        description: "Error",
                    });
                }
                // console.log(error.response ? error.response.data : error);
            });
    }

    return (
        <div>
            <img src={eldoLogo} alt="Eldorado" className="mx-auto h-auto w-1/2"/>
            <Card>
                <CardHeader>
                    <CardTitle>{t("activateAccountPage.title")}</CardTitle>
                    <CardDescription>{t("activateAccountPage.info")}</CardDescription>
                    <Button onClick={onClickButton}>{t("activateAccountPage.button")}</Button>
                </CardHeader>
            </Card>
        </div>
    );
}

export default ActivateAccountPage;