export interface AccessToken{
    accessToken: string
}

export enum RolesEnum{
    ADMIN = "ADMIN",
    STAFF = "STAFF",
    CLIENT = "CLIENT"
}

export interface TokenPayload {
    sub: string;
    account_id: string;
    user_levels: RolesEnum[];
    iat: number;
    exp: number;
    iss: string;
}