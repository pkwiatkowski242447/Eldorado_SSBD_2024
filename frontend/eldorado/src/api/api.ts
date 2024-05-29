import {API_TEST_URL, apiWithConfig, DEFAULT_HEADERS, TIMEOUT_IN_MS} from "./api.config";
import {UserLevelType} from "@/types/Users.ts";
import axios from "axios";

export const api = {
    logIn: (login: string, password: string) => {
        let language = window.navigator.language;
        language = language.substring(0, 2)
        return apiWithConfig.post('/auth/login-credentials', {login, password, language})
    },

    refreshSession: (refreshToken: string) => {
        return apiWithConfig.post('/auth/refresh-session', {refreshToken})
    },

    logIn2fa: (userLogin: string, authCodeValue: string) => {
        let language = window.navigator.language;
        language = language.substring(0, 2)
        return apiWithConfig.post('/auth/login-auth-code', {userLogin, authCodeValue, language})
    },

    logOut: () => {
        return apiWithConfig.post('/auth/logout');
    },

    changePasswordSelf: (oldPassword: string, newPassword: string) => {
        return apiWithConfig.patch('/accounts/change-password/self', {
            oldPassword: oldPassword,
            newPassword: newPassword
        })
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

    registerStaff: (login: string, password: string, firstName: string, lastName: string, email: string, phoneNumber: string, language: string) => {
        return apiWithConfig.post('/register/staff', {
            login,
            password,
            firstName,
            lastName,
            email,
            phoneNumber,
            language
        })
    },

    registerAdmin: (login: string, password: string, firstName: string, lastName: string, email: string, phoneNumber: string, language: string) => {
        return apiWithConfig.post('/register/admin', {
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

    resetPasswordByUser: (token: string, password: string) => {
        return apiWithConfig.post(`/accounts/change-password/${token}`, {
            password
        })
    },

    resetPasswordByAdmin: (id: string) => {
        return apiWithConfig.post(`/accounts/reset-password/${id}`)
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

    modifyAccountUser: (login: string, version: number, userLevelsDto: UserLevelType[], name: string,
                        lastname: string, phoneNumber: string, twoFactorAuth: boolean, etag: string) => {
        const cleanedEtag = etag.replace(/^"|"$/g, '');
        //this is necessary because backend requires the etag to be without quotes
        const test = apiWithConfig.put('/accounts', {
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
        console.log(test)
        return test
    },

    changeEmailSelf: (email: string) => {
        return apiWithConfig.patch('/accounts/change-email-self', {
            email: email
        })
    },

    changeEmailUser: (id: string, email: string) => {
        return apiWithConfig.patch(`/accounts/${id}/change-email`, {
            email: email
        })
    },

    confirmEmail: (token: string) => {
        return axios.create({
            baseURL: API_TEST_URL,
            timeout: TIMEOUT_IN_MS,
            headers: DEFAULT_HEADERS,
        }).post(`/accounts/confirm-email/${token}`)
    },

    resendEmailConfirmation: () => {
        return apiWithConfig.post('/accounts/resend-email-confirmation')
    },

    getAccounts: (details: string) => {
        return apiWithConfig.get('/accounts' + details)
    },

    removeLevelClient: (id: string) => {
        return apiWithConfig.post(`/accounts/${id}/remove-level-client`)
    },

    removeLevelStaff: (id: string) => {
        return apiWithConfig.post(`/accounts/${id}/remove-level-staff`)
    },

    removeLevelAdmin: (id: string) => {
        return apiWithConfig.post(`/accounts/${id}/remove-level-admin`)
    },

    addLevelClient: (id: string) => {
        return apiWithConfig.post(`/accounts/${id}/add-level-client`)
    },

    addLevelStaff: (id: string) => {
        return apiWithConfig.post(`/accounts/${id}/add-level-staff`)
    },

    addLevelAdmin: (id: string) => {
        return apiWithConfig.post(`/accounts/${id}/add-level-admin`)
    },

    getAccountById: (id: string) => {
        return apiWithConfig.get(`/accounts/${id}`)
    },

    blockAccount: (id: string) => {
        return apiWithConfig.post(`/accounts/${id}/block`, {}, {
            headers: {
                'accept': 'text/plain'
            }
        });
    },

    unblockAccount: (id: string) => {
        return apiWithConfig.post(`/accounts/${id}/unblock`, {}, {
            headers: {
                'accept': 'text/plain'
            }
        });
    },
}