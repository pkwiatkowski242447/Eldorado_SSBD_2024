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
    userLevels: UserLevelType[]
    activeUserLevel: UserLevelType
    verified: boolean
}
