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
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";

function RestoreTokenPage() {
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
        setIsLoading(true)
        api.restoreAccessToken(decodedToken!)
            .then(() => {
                toast({
                    title: t("restoreTokenPage.popUpOK.restoreOK.title"),
                    description: t("restoreTokenPage.popUpOK.restoreOK.text"),
                    action: (
                        <div>
                            <Button onClick={() => {
                                navigate('/login', {replace: true});
                                getCurrentAccount();
                            }}>
                                {t("restoreTokenPage.popUpOK.restoreOK.button")}
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
        <div className="flex flex-col items-center justify-center">
            <a href="/home" className="flex items-center">
                <img src={eldoLogo} alt="Eldorado" className="mx-auto h-auto w-1/2"/>
                <span className="sr-only">Eldorado</span>
            </a>
            <Card>
                <CardHeader>
                    <CardTitle>{t("restoreTokenPage.title")}</CardTitle>
                    <CardDescription>{t("restoreTokenPage.info")}</CardDescription>
                    <Button onClick={openAlert}
                            className='mx-auto h-auto w-auto' disabled={isLoading}>
                        {isLoading ? (
                            <>
                                <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                            </>
                        ) : (
                            t("restoreTokenPage.button")
                        )}
                    </Button>
                </CardHeader>
            </Card>
            <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                    <AlertDialogDescription>
                        {t("restoreTokenPage.popUp")}
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

export default RestoreTokenPage