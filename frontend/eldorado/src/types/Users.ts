import {RolesEnum} from "./TokenPayload";

export enum AccountTypeEnum{
    ADMIN = "Admin",
    CLIENT = "Client",
    STAFF = "Staff",
}

export interface UserType {
    // archive:boolean,
    id:string,
    login:string
    token: string
    activeRole: RolesEnum;
    userTypes: RolesEnum[]
}

// export interface ClientType extends UserType{
//     clientTypeName: "normal" | "coach" | "athlete",
//     firstName: string,
//     lastName: string,
//     eTag?: string
// }

export interface NewUserType {
    login:string,
    password:string
    userTypes: AccountTypeEnum[] | null
}

// export interface NewClientType extends NewUserType{
//     clientTypeName: "normal" | "coach" | "athlete",
//     firstName: string,
//     lastName: string
// }