import {useNavigate, useParams} from 'react-router-dom';
import {api} from '@/api/api.ts';
import {useToast} from '@/components/ui/use-toast.ts';
import {Button} from "@/components/ui/button.tsx";
import eldoLogo from "@/assets/eldorado.png";
import {Card, CardDescription, CardHeader, CardTitle,} from "@/components/ui/card"
import {useTranslation} from "react-i18next";
import handleApiError from "@/components/HandleApiError.ts";
import {Loader2} from "lucide-react";
import {useState} from "react";

function ActivateAccountPage() {
    const {token} = useParams<{ token: string }>();
    const decodedToken = decodeURIComponent(token!);
    const {toast} = useToast();
    const navigate = useNavigate();
    const {t} = useTranslation();
    const [isLoading, setIsLoading] = useState(false);

    function onClickButton() {
        setIsLoading(true)
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
            }).finally(() => {
            setIsLoading(false)
        });
    }

    return (
        <div>
            <img src={eldoLogo} alt="Eldorado" className="mx-auto h-auto w-1/2"/>
            <Card>
                <CardHeader>
                    <CardTitle>{t("activateAccountPage.title")}</CardTitle>
                    <CardDescription>{t("activateAccountPage.info")}</CardDescription>
                    <Button onClick={onClickButton} disabled={isLoading}>
                        {isLoading ? (
                            <>
                                <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                            </>
                        ) : (
                            t("activateAccountPage.button")
                        )}
                    </Button>
                </CardHeader>
            </Card>
        </div>
    );
}

export default ActivateAccountPage;