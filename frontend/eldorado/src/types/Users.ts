import {RolesEnum} from "./TokenPayload";

export enum AccountTypeEnum{
    ADMIN = "Admin",
    CLIENT = "Client",
    STAFF = "Staff",
}

export interface UserLevelType {
    id: string
    roleName: RolesEnum
    version: number
}

export interface UserType {
    accountLanguage: string
    active: boolean
    blocked: boolean
    creationDate: Date
    email: string
    id: string
    lastname: string
    login: string
    name: string
    token: string
    phone: string
    userLevelsDto: UserLevelType[]
    activeUserLevel: UserLevelType
    verified: boolean
    version: number
    twoFactorAuth: boolean
}

export interface ManagedUserType {
    id: string
    login: string
    name: string
    lastName: string
    active: boolean
    blocked: boolean
    verified: boolean
    // lastSuccessfulLoginTime: Date
    // lastUnsuccessfulLoginTime: Date
    userLevels: AccountTypeEnum[]
}
