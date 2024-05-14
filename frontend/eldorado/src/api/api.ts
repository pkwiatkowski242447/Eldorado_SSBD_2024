import {API_URL, apiWithConfig, DEFAULT_HEADERS, TIMEOUT_IN_MS} from "./api.config";
import {UserLevelType} from "@/types/Users.ts";
import axios from "axios";

export const api = {
    logIn: (login: string, password: string) => {
        let language = window.navigator.language;
        language = language.substring(0, 2)
        return apiWithConfig.post('/auth/login-credentials', {login, password, language})
    },

    logOut: () => {
        const url = '/auth/logout';
        console.log(`Making POST request to: ${url}`);
        return apiWithConfig.post(url);
    },

    registerClient: (login: string, password: string, firstName: string, lastName: string, email: string, phoneNumber: string, language: string) => {
        return apiWithConfig.post('/register/client', {
            login,
            password,
            firstName,
            lastName,
            email,
            phoneNumber,
            language
        })
    },

    activateAccount: (token: string) => {
        return apiWithConfig.post(`/accounts/activate-account/${token}`)
    },

    forgotPassword: (email: string) => {
        return apiWithConfig.post(`/accounts/forgot-password`, {email})
    },

    resetPassword: (token: string, password: string) => {
        return apiWithConfig.post(`/accounts/change-password/${token}`, {
            password
        })
    },

    modifyAccountSelf: (login: string, version: number, userLevelsDto: UserLevelType[], name: string,
                        lastname: string, phoneNumber: string, twoFactorAuth: boolean, etag: string) => {
        const cleanedEtag = etag.replace(/^"|"$/g, '');
        //this is necessary because backend requires etag to be without quotes
        return apiWithConfig.put('/accounts/self', {
            login: login,
            version: version,
            userLevelsDto: userLevelsDto,
            name: name,
            lastname: lastname,
            phoneNumber: phoneNumber,
            twoFactorAuth: twoFactorAuth
        }, {
            headers: {
                'If-Match': cleanedEtag
            }
        })
    },

    changeEmailSelf: (email: string) => {
        return apiWithConfig.patch('/accounts/change-email-self', {
            email: email
        })
    },

    confirmEmail: (token: string) => {
        return axios.create({
            baseURL: API_URL,
            timeout: TIMEOUT_IN_MS,
            headers: DEFAULT_HEADERS,
        }).post(`/accounts/confirm-email/${token}`)
    },
}