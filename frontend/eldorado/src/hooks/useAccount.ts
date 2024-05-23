import {useAccountState} from "../context/AccountContext";
import {useNavigate} from "react-router-dom";
import {api} from "../api/api";
import {Pathnames} from "../router/pathnames";
import {usersApi} from "@/api/userApi.ts";
import {useEffect, useState} from "react";
import {useToast} from "@/components/ui/use-toast.ts";
import {RolesEnum} from "@/types/TokenPayload.ts";
import handleApiError from "@/components/HandleApiError.ts";

export const useAccount = () => {

    const navigate = useNavigate()
    const {account, setAccount} = useAccountState()
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
    const {toast} = useToast()

    useEffect(() => {
        const storedAccount = localStorage.getItem('account');
        if (storedAccount) {
            setAccount(JSON.parse(storedAccount));
        }
        if (account?.token) {
            window.localStorage.setItem('token', account.token);
        }
    }, [account?.token, setAccount]);

    const navigateToMainPage = () => {
        navigate(Pathnames.public.login)
    }

    const logOut = async () => {
        try {
            await api.logOut()
        } catch (e) {
            console.log(e)
        } finally {
            localStorage.removeItem('token')
            localStorage.removeItem('refreshToken')
            localStorage.removeItem('account')
            localStorage.removeItem('etag')
            setAccount(null)
            navigateToMainPage()
        }
    }
    const logIn = async (login: string, password: string) => {
        try {
            const response = await api.logIn(login, password);
            if (response.status === 200) {
                const accessToken = response.data.accessToken;
                const refreshToken = response.data.refreshToken;
                localStorage.setItem('token', accessToken);
                localStorage.setItem('refreshToken', refreshToken);
                await getCurrentAccount()
                setIsAuthenticated(true);
                navigate(Pathnames.public.home)
            } else if (response.status === 204) {
                navigate(`/login/2fa/${login}`);
            }
        } catch (error) {
            //@ts-expect-error idk
            handleApiError(error);
            if (isAuthenticated) await logOut();
        } finally { /* empty */
        }
    }

    const logIn2fa = async (userLogin: string, authCodeValue: string) => {
        try {
            const response = await api.logIn2fa(userLogin, authCodeValue);
            if (response.status === 200) {
                const accessToken = response.data.accessToken;
                const refreshToken = response.data.refreshToken;
                localStorage.setItem('token', accessToken);
                localStorage.setItem('refreshToken', refreshToken);
                console.log(accessToken)
                console.log(refreshToken)
                await getCurrentAccount()
                setIsAuthenticated(true);
                navigate(Pathnames.public.home)
            }
        } catch (e) {
            toast({
                variant: "destructive",
                description: "Something went wrong. Please try again later.",
            })
            // console.log(e);
            if (isAuthenticated) await logOut();
        } finally { /* empty */
        }
    }

    const getCurrentAccount = async () => {
        try {
            const tokenRaw = localStorage.getItem("token");
            if (tokenRaw && tokenRaw !== 'null') {
                const token = await usersApi.getSelf();
                window.localStorage.setItem('etag', token.headers['etag']);
                let activeUserLevel = token.data.userLevelsDto[0];
                if (!account?.activeUserLevel) {
                    for (const userLevel of token.data.userLevelsDto) {
                        if (userLevel.roleName === RolesEnum.ADMIN) {
                            activeUserLevel = userLevel;
                            break;
                        } else if (userLevel.roleName === RolesEnum.STAFF) {
                            activeUserLevel = userLevel;
                        }
                    }
                } else {
                    activeUserLevel = account.activeUserLevel;
                }
                const user = {
                    accountLanguage: token.data.accountLanguage,
                    active: token.data.active,
                    blocked: token.data.blocked,
                    creationDate: token.data.creationDate,
                    email: token.data.email,
                    id: token.data.id,
                    lastname: token.data.lastname,
                    login: token.data.login,
                    name: token.data.name,
                    token: tokenRaw,
                    phone: token.data.phone,
                    userLevelsDto: token.data.userLevelsDto,
                    activeUserLevel: activeUserLevel,
                    verified: token.data.verified,
                    version: token.data.version,
                    twoFactorAuth: token.data.twoFactorAuth,
                };
                setAccount(user);
                localStorage.setItem('account', JSON.stringify(user));
            }
        } catch (e) {
            if (account !== null) {
                alert('Unable to get current account!');
                await logOut();
            }
        }
    };

    return {
        account,
        isAuthenticated,
        logIn,
        getCurrentAccount,
        logOut,
        logIn2fa
    }
}