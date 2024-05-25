import {useNavigate, useParams} from 'react-router-dom';
import {api} from '@/api/api.ts';
import {useToast} from '@/components/ui/use-toast.ts';
import {Button} from "@/components/ui/button.tsx";
import eldoLogo from "@/assets/eldorado.png";
import {Card, CardDescription, CardHeader, CardTitle,} from "@/components/ui/card"
import {useAccount} from "@/hooks/useAccount.ts";
import {useTranslation} from "react-i18next";
import handleApiError from "@/components/HandleApiError.ts";
import {useState} from "react";
import {Loader2} from "lucide-react";

function ConfirmEmailChangePage() {
    const {token} = useParams<{ token: string }>();
    const decodedToken = decodeURIComponent(token!);
    const {toast} = useToast();
    const navigate = useNavigate();
    const {t} = useTranslation();
    const [isLoading, setIsLoading] = useState(false);
    const {getCurrentAccount} = useAccount();

    function onClickButton() {
        if (localStorage.getItem('token') !== null) {
            setIsLoading(true)
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
                    handleApiError(error);
                }).finally(() => {
                setIsLoading(false)
            });
        } else {
            setIsLoading(true)
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
                }).finally(() => {
                setIsLoading(false)
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
                    <Button onClick={onClickButton}
                            className='mx-auto h-auto w-auto' disabled={isLoading}>
                        {isLoading ? (
                            <>
                                <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                            </>
                        ) : (
                            t("confirmEmailPage.button")
                        )}
                    </Button>
                </CardHeader>
            </Card>
        </div>
    );
}

export default ConfirmEmailChangePage