import {useNavigate, useParams} from 'react-router-dom';
import {api} from '@/api/api.ts';
import {useToast} from '@/components/ui/use-toast.ts';
import {Button} from "@/components/ui/button.tsx";
import eldoLogo from "@/assets/eldorado.png";
import {Card, CardDescription, CardHeader, CardTitle,} from "@/components/ui/card"
import {useTranslation} from "react-i18next";
import handleApiError from "@/components/HandleApiError.ts";

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
                handleApiError(error);
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