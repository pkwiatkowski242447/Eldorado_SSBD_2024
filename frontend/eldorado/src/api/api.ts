// import {ApiResponseType} from "../types/ApiResponseType";
import {apiWithConfig} from "./api.config";
import {ApiResponseType} from "@/types/ApiResponseType.ts";
// import {AccessToken} from "../types/TokenPayload";

export const api = {
    logIn: (login: string, password: string) => {
        return apiWithConfig.post('/auth/login', { login, password })
    },
    logOut: (): ApiResponseType<never> => {
        return apiWithConfig.post('/auth/logout')
    },
    registerClient: (login:string, password:string, firstName:string, lastName:string, email:string, phoneNumber:string, language:string) => {
        return apiWithConfig.post('/register/client',{login,password, firstName, lastName, email, phoneNumber, language})
    },
    activateAccount: (token: string) => {
        return apiWithConfig.post(`/accounts/activate-account/${token}`)
    }

}