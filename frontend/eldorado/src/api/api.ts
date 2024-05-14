import {apiWithConfig} from "./api.config";
import {UserLevelType} from "@/types/Users.ts";

export const api = {
    logIn: (login: string, password: string) => {
        return apiWithConfig.post('/auth/login', {login, password})
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
                        lastname: string, phoneNumber: string, accountLanguage: string, etag: string) => {
        const cleanedEtag = etag.replace(/^"|"$/g, '');
        //this is necessary because backend requires etag to be without quotes
        return apiWithConfig.put('/accounts/self', {
            login: login,
            version: version,
            userLevelsDto: userLevelsDto,
            name: name,
            lastname: lastname,
            phoneNumber: phoneNumber,
            accountLanguage: accountLanguage
        }, {
            headers: {
                'If-Match': cleanedEtag
            }
        })
    },
}