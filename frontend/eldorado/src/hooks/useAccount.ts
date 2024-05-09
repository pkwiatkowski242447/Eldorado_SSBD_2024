import {UserType} from "../types/Users";
import {useAccountState} from "../context/AccountContext";
import {useNavigate} from "react-router-dom";
import {api} from "../api/api";
import {Pathnames} from "../router/pathnames";
import {jwtDecode} from "jwt-decode";
import {RolesEnum, TokenPayload} from "../types/TokenPayload";
import {usersApi} from "@/api/userApi.ts";
import {useEffect, useState} from "react";
import {useToast} from "@/components/ui/use-toast.ts";

export const useAccount = () => {

    useEffect(() => {
        const tokenRaw = localStorage.getItem("token");
        if (tokenRaw && tokenRaw !== 'null') {
            const decodedToken: TokenPayload = jwtDecode<TokenPayload>(tokenRaw);
            const user: UserType = {
                id: decodedToken.sub,
                login: decodedToken.account_id,
                token: tokenRaw,
                activeRole: decodedToken.user_levels[0],
                userTypes: decodedToken.user_levels.map((role: RolesEnum) => role),
            };
            setAccount(user);
        }
    }, []);

    const navigate = useNavigate()
    const {account, setAccount} =
        useAccountState()
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
    const accountType: RolesEnum[] | null = account?.userTypes ? account.userTypes : null;
    const {toast} = useToast()

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
            console.log(token);
            localStorage.setItem('token', token);
            const decodedToken: TokenPayload = jwtDecode<TokenPayload>(token);
            const user: UserType = {
                id: decodedToken.sub,
                login: decodedToken.account_id,
                token: token,
                activeRole: decodedToken.user_levels[0],
                userTypes: decodedToken.user_levels.map((role: RolesEnum) => role),
            };
            setAccount(user);
            setIsAuthenticated(true);
            console.log(user)
            navigate(Pathnames.public.test)
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
                const {data} = await usersApi.getSelf();
                setAccount(data)
                console.table(data)
            } else throw new Error("Token is null");
        } catch {
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
        accountType,
        logIn,
        getCurrentAccount,
        logOut,
    }
}