import {apiWithConfig} from "./api.config";

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
    }
}