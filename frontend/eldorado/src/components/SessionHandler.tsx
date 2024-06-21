import {useCallback, useEffect, useState} from 'react';
import {jwtDecode} from "jwt-decode";
import {api} from "@/api/api.ts";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import {useTranslation} from "react-i18next";
import {useAccount} from "@/hooks/useAccount.ts";
import {toast} from "@/components/ui/use-toast.ts";
import {SessionContext} from '@/context/SessionContext';

const SessionHandler = () => {
    const [isRefreshDialogOpen, setRefreshDialogOpen] = useState(false);
    const [isExpiredDialogOpen, setExpiredDialogOpen] = useState(false);
    const {t} = useTranslation();
    const {logOut} = useAccount();
    const [timeoutId, setTimeoutId] = useState<number | null>(null);
    const {isAuthenticated} = useAccount();

    const resetTimer = useCallback(() => {
        if (timeoutId) {
            clearTimeout(timeoutId);
        }
        checkTokenExpiration();
    }, [timeoutId]);

    const checkTokenExpiration = useCallback(() => {
        if (!isAuthenticated) {
            return;
        }

        const token = localStorage.getItem('token');
        if (token) {
            const decodedToken = jwtDecode<{ exp: number }>(token);
            const currentTime = Date.now() / 1000;
            const expiryTime = decodedToken.exp;

            if (expiryTime) {
                if (currentTime > expiryTime) {
                    setExpiredDialogOpen(true);
                } else {
                    const timeoutDuration = (expiryTime - currentTime - 180) * 1000; // 3 minutes before expiration
                    const id = setTimeout(() => {
                        setRefreshDialogOpen(true);
                    }, timeoutDuration);
                    // @ts-expect-error idk
                    setTimeoutId(id);
                }
            }
        }
    }, [isAuthenticated]);

    const handleRefreshSession = async () => {
        const refreshToken = localStorage.getItem('refreshToken');
        if (refreshToken) {
            try {
                const response = await api.refreshSession(refreshToken);
                await localStorage.setItem('token', response.data.accessToken);
                await localStorage.setItem('refreshToken', response.data.refreshToken);
                toast({
                    title: t("general.refreshSession.popUp1"),
                    description: t("general.refreshSession.popUp2")
                });
                setRefreshDialogOpen(false);
                checkTokenExpiration();
                resetTimer();
            } catch (error) {
                setRefreshDialogOpen(false);
                console.error(error);
                setExpiredDialogOpen(true);
            }
        }
    };

    const handleLogout = () => {
        logOut();
    };

    useEffect(() => {
        checkTokenExpiration();
        return () => {
            if (timeoutId) {
                clearTimeout(timeoutId);
            }
        };
    }, [isAuthenticated]);

    return (
        <>
            <SessionContext.Provider value={{resetTimer}}>
                <AlertDialog open={isRefreshDialogOpen} onOpenChange={setRefreshDialogOpen}>
                    <AlertDialogContent>
                        <AlertDialogTitle>{t("general.sessionAboutToExpire")}</AlertDialogTitle>
                        <AlertDialogDescription>
                            {t("general.sessionAboutToExpire.text")}
                        </AlertDialogDescription>
                        <AlertDialogAction
                            onClick={handleRefreshSession}>{t("general.refreshSession")}</AlertDialogAction>
                        <AlertDialogCancel
                            onClick={() => setRefreshDialogOpen(false)}>{t("general.cancel")}</AlertDialogCancel>
                    </AlertDialogContent>
                </AlertDialog>
                <AlertDialog open={isExpiredDialogOpen} onOpenChange={setExpiredDialogOpen}>
                    <AlertDialogContent>
                        <AlertDialogTitle>{t("general.sessionExpired")}</AlertDialogTitle>
                        <AlertDialogDescription>
                            {t("general.sessionExpired.text")}
                        </AlertDialogDescription>
                        <AlertDialogAction
                            onClick={handleLogout}>{t("general.sessionExpired.text.button")}</AlertDialogAction>
                    </AlertDialogContent>
                </AlertDialog>
            </SessionContext.Provider>
        </>
    );
};

export default SessionHandler;
