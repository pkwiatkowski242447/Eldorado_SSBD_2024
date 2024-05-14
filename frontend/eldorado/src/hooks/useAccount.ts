import {UserType} from "../types/Users";
import {useAccountState} from "../context/AccountContext";
import {useNavigate} from "react-router-dom";
import {api} from "../api/api";
import {Pathnames} from "../router/pathnames";
import {usersApi} from "@/api/userApi.ts";
import {useEffect, useState} from "react";
import {useToast} from "@/components/ui/use-toast.ts";
import {RolesEnum} from "@/types/TokenPayload.ts";

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
        navigate(Pathnames.public.home)
    }

    const logOut = async () => {
        try {
            await api.logOut()
        } catch (e) {
            console.log(e)
        } finally {
            localStorage.removeItem('token')
            setAccount(null)
            navigateToMainPage()
        }
    }
    const logIn = async (login: string, password: string) => {
        try {
            const token = (await api.logIn(login, password)).data;
            localStorage.setItem('token', token);
            await getCurrentAccount()
            setIsAuthenticated(true);
            navigate(Pathnames.public.home)
        } catch (e) {
            toast({
                variant: "destructive",
                description: "Something went wrong. Please try again later.",
            })
            console.log(e);
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
                                break;
                            }
                        } else if (token.data.userLevelsDto.contains(RolesEnum.STAFF) && !token.data.userLevelsDto.contains(RolesEnum.ADMIN)) {
                            if (token.data.userLevelsDto[i].roleName === RolesEnum.STAFF) {
                                activeUserLevel = token.data.userLevelsDto[i];
                                break;
                            }
                        }
                    }
                } else {
                    activeUserLevel = account.activeUserLevel;
                }
                console.table(token.data)
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
                    userLevels: token.data.userLevelsDto,
                    activeUserLevel: activeUserLevel,
                    verified: token.data.verified,
                    version: token.data.version,
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
    }
}