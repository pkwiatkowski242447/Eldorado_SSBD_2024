import {UserType} from "../types/Users";
import {useAccountState} from "../context/AccountContext";
import {useNavigate} from "react-router-dom";
import {api} from "../api/api";
import {Pathnames} from "../router/pathnames";
import {usersApi} from "@/api/userApi.ts";
import {useEffect, useState} from "react";
import {useToast} from "@/components/ui/use-toast.ts";
import {RolesEnum} from "@/types/TokenPayload.ts";
import {useTranslation} from "react-i18next";

export const useAccount = () => {

    const navigate = useNavigate()
    const {account, setAccount} = useAccountState()
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
    const {toast} = useToast()
    const {t} = useTranslation();

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
                const token = response.data;
                localStorage.setItem('token', token);
                await getCurrentAccount()
                setIsAuthenticated(true);
                navigate(Pathnames.public.home)
            } else if (response.status === 204) {
                navigate(`/login/2fa/${login}`);
            }
        } catch (error) {
            //@ts-expect-error works tho
            if (error.response && error.response.data) {
                //@ts-expect-error works tho
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
            if (isAuthenticated) await logOut();
        } finally { /* empty */
        }
    }

    const logIn2fa = async (userLogin: string, authCodeValue: string) => {
        try {
            const response = await api.logIn2fa(userLogin, authCodeValue);
            if (response.status === 200) {
                const token = response.data;
                localStorage.setItem('token', token);
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
                if (account?.activeUserLevel == null) {
                    for (const i in token.data.userLevelsDto.length) {
                        if (token.data.userLevelsDto.contains(RolesEnum.ADMIN)) {
                            if (token.data.userLevelsDto[i].roleName === RolesEnum.ADMIN) {
                                activeUserLevel = token.data.userLevelsDto[i];
                                // console.log(activeUserLevel)
                                break;
                            }
                        } else if (token.data.userLevelsDto.contains(RolesEnum.STAFF) && !token.data.userLevelsDto.contains(RolesEnum.ADMIN)) {
                            if (token.data.userLevelsDto[i].roleName === RolesEnum.STAFF) {
                                activeUserLevel = token.data.userLevelsDto[i];
                                // console.log(activeUserLevel)
                                break;
                            }
                        }
                    }
                } else {
                    activeUserLevel = account.activeUserLevel;
                }
                const user: UserType = {
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
                setAccount(user)
                localStorage.setItem('account', JSON.stringify(user));
            }
        } catch (e) {
            if (account !== null) {
                alert('Unable to get current account!');
                await logOut();
            }
        } finally { /* empty */
        }
    }
    return {
        account,
        isAuthenticated,
        logIn,
        getCurrentAccount,
        logOut,
        logIn2fa
    }
}