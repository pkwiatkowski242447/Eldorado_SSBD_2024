import {useNavigate, useParams} from 'react-router-dom';
import {api} from '@/api/api.ts';
import {useToast} from '@/components/ui/use-toast.ts';
import {Button} from "@/components/ui/button.tsx";
import eldoLogo from "@/assets/eldorado.png";
import {Card, CardDescription, CardHeader, CardTitle,} from "@/components/ui/card"
import {useAccount} from "@/hooks/useAccount.ts";
import {useTranslation} from "react-i18next";
import handleApiError from "@/components/HandleApiError.ts";

function ConfirmEmailChangePage() {
    const {token} = useParams<{ token: string }>();
    const decodedToken = decodeURIComponent(token!);
    const {toast} = useToast();
    const navigate = useNavigate();
    const {t} = useTranslation();

    const {getCurrentAccount} = useAccount();

    function onClickButton() {
        // console.log(decodedToken)
        if (localStorage.getItem('token')!==null) {
            api.confirmEmail(decodedToken!)
                .then(() => {
                    toast({
                        title: t("confirmEmailPage.popUp.confirmEmailOK.title"),
                        description: t("confirmEmailPage.popUp.confirmEmailOK.loggedIn.text"),
                        action: (
                            <div>
                                <Button onClick={() => {
                                    navigate('/home', {replace: true});
                                    getCurrentAccount();
                                }}>
                                    {t("confirmEmailPage.popUp.confirmEmailOK.loggedIn.button")}
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
        } else {
            api.confirmEmail(decodedToken!)
                .then(() => {
                    toast({
                        title: t("confirmEmailPage.popUp.confirmEmailOK.title"),
                        description: t("confirmEmailPage.popUp.confirmEmailOK.loggedIn.text"),
                        action: (
                            <div>
                                <Button onClick={() => {
                                    navigate('/login', {replace: true});
                                    getCurrentAccount();
                                }}>
                                    {t("confirmEmailPage.popUp.confirmEmailOK.loggedOut.button")}
                                </Button>
                            </div>
                        ),
                    });
                })
                .catch((error) => {
                    handleApiError(error);
                });
        }
    }

    return (
        <div>
            <img src={eldoLogo} alt="Eldorado" className="mx-auto h-auto w-1/2"/>
            <Card>
                <CardHeader>
                    <CardTitle>{t("confirmEmailPage.title")}</CardTitle>
                    <CardDescription>{t("confirmEmailPage.info")}</CardDescription>
                    <Button onClick={onClickButton} className='mx-auto h-auto w-auto'>{t("confirmEmailPage.button")}</Button>
                </CardHeader>
            </Card>
        </div>
    );
}

export default ConfirmEmailChangePage