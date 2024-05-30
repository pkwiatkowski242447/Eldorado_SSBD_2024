import {RolesEnum} from "./TokenPayload";
import roles from "../../roles.json";

export enum AccountTypeEnum {
    ADMIN = "Admin",
    CLIENT = "Client",
    STAFF = "Staff",
}

console.log(roles);

export interface UserLevelType {
    id: string
    roleName: RolesEnum
    version: number
}

export function localDateTimeToDate(localDateTime: number[]): string {
    if (localDateTime) {
        const [year, month, day, hour, minute, second, nanosecond] = localDateTime;
        const millisecond = Math.floor(nanosecond / 1000000);
        const date = new Date(year, month - 1, day, hour, minute, second, millisecond);
        return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
    } else {
        return new Date(0).toLocaleString();
    }
}

export interface UserType {
    accountLanguage: string
    active: boolean
    blocked: boolean
    creationDate: string | null
    email: string
    id: string
    lastname: string
    login: string
    name: string
    token: string
    phoneNumber: string
    userLevelsDto: UserLevelType[]
    activeUserLevel: UserLevelType | null
    suspended: boolean
    version: number
    twoFactorAuth: boolean
    lastSuccessfulLoginTime: string | null
    lastUnsuccessfulLoginTime: string | null
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
    suspended: boolean
    lastSuccessfulLoginTime: string | null
    lastUnsuccessfulLoginTime: string | null
    userLevels: AccountTypeEnum[]
}

export interface UserHistoryType {
    id: string;
    version: number;
    login: string;
    suspended: boolean;
    active: boolean;
    blocked: boolean;
    twoFactorAuth: boolean;
    blockedTime: null | string;
    lastSuccessfulLoginTime: number[];
    lastUnsuccessfulLoginTime: number[];
    accountLanguage: string;
    lastSuccessfulLoginIp: string;
    lastUnsuccessfulLoginIp: null | string;
    phoneNumber: string;
    lastname: string;
    name: string;
    email: string;
    operationType: string;
    modificationTime: number[];
    modifiedBy: string;
}
