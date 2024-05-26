import {RolesEnum} from "./TokenPayload";

export enum AccountTypeEnum {
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
    creationDate: Date | null
    email: string
    id: string
    lastname: string
    login: string
    name: string
    token: string
    phoneNumber: string
    userLevelsDto: UserLevelType[]
    activeUserLevel: UserLevelType
    verified: boolean
    version: number
    twoFactorAuth: boolean
    lastSuccessfulLoginTime: Date | null
    lastUnsuccessfulLoginTime: Date | null
    lastSuccessfulLoginIp: string | null
    lastUnsuccessfulLoginIp: string | null
}

export interface ManagedUserType {
    id: string
    login: string
    name: string
    lastName: string
    active: boolean
    blocked: boolean
    verified: boolean
    lastSuccessfulLoginTime: Date | null
    lastUnsuccessfulLoginTime: Date | null
    userLevels: AccountTypeEnum[]
}
