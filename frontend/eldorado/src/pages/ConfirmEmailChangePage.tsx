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
import {
    AlertDialog,
    AlertDialogAction, AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";

function ConfirmEmailChangePage() {
    const {token} = useParams<{ token: string }>();
    const decodedToken = decodeURIComponent(token!);
    const {toast} = useToast();
    const navigate = useNavigate();
    const {t} = useTranslation();
    const [isLoading, setIsLoading] = useState(false);
    const {getCurrentAccount} = useAccount();
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);

    function openAlert() {
        setAlertDialogOpen(true);
    }

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
                    setAlertDialogOpen(false)
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
            <a href="/home" className="flex items-center">
                <img src={eldoLogo} alt="Eldorado" className="mx-auto h-auto w-1/2"/>
                <span className="sr-only">Eldorado</span>
            </a>
            <Card>
                <CardHeader>
                    <CardTitle>{t("confirmEmailPage.title")}</CardTitle>
                    <CardDescription>{t("confirmEmailPage.info")}</CardDescription>
                    <Button onClick={openAlert}
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
            <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                    <AlertDialogDescription>
                        {t("confirmEmailPage.popUp")}
                    </AlertDialogDescription>
                    <AlertDialogAction onClick={onClickButton}>
                        {t("general.ok")}
                    </AlertDialogAction>
                    <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                </AlertDialogContent>
            </AlertDialog>
        </div>
    );
}

export default ConfirmEmailChangePage