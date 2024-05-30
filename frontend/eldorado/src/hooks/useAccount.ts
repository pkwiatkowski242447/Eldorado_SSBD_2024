import {useAccountState} from "../context/AccountContext";
import {useNavigate} from "react-router-dom";
import {api} from "../api/api";
import {Pathnames} from "../router/pathnames";
import {usersApi} from "@/api/userApi.ts";
import {useEffect} from "react";
import {useToast} from "@/components/ui/use-toast.ts";
import {RolesEnum} from "@/types/TokenPayload.ts";
import handleApiError from "@/components/HandleApiError.ts";
import {localDateTimeToDate, UserType} from "@/types/Users.ts";
import {useTranslation} from "react-i18next";

export const useAccount = () => {

    const {account, setAccount} = useAccountState()
    const isAuthenticated = !!account
    const {t} = useTranslation();
    const navigate = useNavigate()
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

                const resetStatusResponse = await api.getPasswordAdminResetStatus();
                if (resetStatusResponse.data) {
                    logOut()
                    toast({
                        variant: "destructive",
                        description: t("general.adminInvokedPasswordReset")
                    })
                } else {
                    await getCurrentAccount()
                    navigate(Pathnames.public.home)
                }
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

                const resetStatusResponse = await api.getPasswordAdminResetStatus();
                if (resetStatusResponse.data) {
                    logOut()
                    toast({
                        variant: "destructive",
                        description: "Reset password operation was requested by an administrator. Please click the link in the email you received to continue."
                    })
                } else {
                    await getCurrentAccount()
                    navigate(Pathnames.public.home)
                }
            }
        } catch (e) {
            toast({
                variant: "destructive",
                description: "Something went wrong. Please try again later.",
            })
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
                const storedUserLevel = localStorage.getItem('chosenUserLevel');

                if (storedUserLevel) {
                    const chosenLevel = token.data.userLevelsDto.find((userLevel: {
                        roleName: string;
                    }) => userLevel.roleName === storedUserLevel);
                    if (chosenLevel) {
                        activeUserLevel = chosenLevel;
                    }

                } else if (!account?.activeUserLevel) {
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

                let creationDate = null;
                let lastSuccessfulLoginTime = null;
                let lastUnsuccessfulLoginTime = null;

                if (token.data.creationDate) {
                    creationDate = localDateTimeToDate(token.data.creationDate);
                }
                if (token.data.lastSuccessfulLoginTime) {
                    lastSuccessfulLoginTime = localDateTimeToDate(token.data.lastSuccessfulLoginTime);
                }

                if (token.data.lastUnsuccessfulLoginTime) {
                    lastUnsuccessfulLoginTime = localDateTimeToDate(token.data.lastUnsuccessfulLoginTime);
                }

                const user: UserType = {
                    accountLanguage: token.data.accountLanguage,
                    active: token.data.active,
                    blocked: token.data.blocked,
                    email: token.data.email,
                    id: token.data.id,
                    lastname: token.data.lastname,
                    login: token.data.login,
                    name: token.data.name,
                    token: tokenRaw,
                    phoneNumber: token.data.phoneNumber,
                    userLevelsDto: token.data.userLevelsDto,
                    activeUserLevel: activeUserLevel,
                    suspended: token.data.suspended,
                    version: token.data.version,
                    twoFactorAuth: token.data.twoFactorAuth,
                    lastSuccessfulLoginIp: token.data.lastSuccessfulLoginIp,
                    lastUnsuccessfulLoginIp: token.data.lastUnsuccessfulLoginIp,
                    creationDate: creationDate,
                    lastSuccessfulLoginTime: lastSuccessfulLoginTime,
                    lastUnsuccessfulLoginTime: lastUnsuccessfulLoginTime
                };

                setAccount(user);
                localStorage.setItem('account', JSON.stringify(user));
            }
        } catch (e) {
            //@ts-expect-error idk
            handleApiError(e)
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