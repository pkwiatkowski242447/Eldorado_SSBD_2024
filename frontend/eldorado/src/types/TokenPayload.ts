export interface AccessToken{
    accessToken: string
}

export enum RolesEnum{
    ADMIN = "ROLE_ADMIN",
    STAFF = "ROLE_STAFF",
    CLIENT = "ROLE_CLIENT"
}

export interface Authority{
    authority: RolesEnum
}
export interface TokenPayload {
    sub: string;
    account_id: string;
    user_levels: RolesEnum[];
    iat: number;
    exp: number;
    iss: string;
}