// import {clientsApi} from "./clientsApi";
// import {adminsApi} from "./adminsApi";
// import {resAdminsApi} from "./resAdminsApi";
// import {AccountTypeEnum, NewUserType, UserType} from "../types/Users";
import {RolesEnum} from "../types/TokenPayload";

export const usersApi = {
    // getUsers: async (): Promise<UserType[]> => {
    //     let usersCopy: UserType[] = []
    //     try {
    //         let {data} = await adminsApi.getAdmins();
    //         usersCopy = [...usersCopy, ...data]
    //     } catch (error) {
    //         console.log({error})
    //     }
    //
    //     try {
    //         let {data} = await resAdminsApi.getResAdmins();
    //         usersCopy = [...usersCopy, ...data]
    //     } catch (error) {
    //         console.log({error})
    //     }
    //
    //     try {
    //         let {data} = await clientsApi.getClients();
    //         usersCopy = [...usersCopy, ...data]
    //     } catch (error) {
    //         console.log({error})
    //     }
    //     return usersCopy;
    // },
    //
    // activate: (user:UserType) => {
    //     switch (user.userType) {
    //         case "Client":
    //             return clientsApi.activate(user.id);
    //         case "Admin":
    //             return adminsApi.activate(user.id);
    //         case "Staff":
    //             return resAdminsApi.activate(user.id);
    //     }
    // },
    //
    // deactivate: (user:UserType) => {
    //     switch (user.userType) {
    //         case "Client":
    //             return clientsApi.deactivate(user.id);
    //         case "Admin":
    //             return adminsApi.deactivate(user.id);
    //         case "Staff":
    //             return resAdminsApi.deactivate(user.id);
    //     }
    // },
    //
    // modify: (user:UserType) => {
    //     switch (user.userType) {
    //         case "Client":
    //             // @ts-ignore
    //             return clientsApi.modify(user);
    //         case "Admin":
    //             return adminsApi.modify(user);
    //         case "Staff":
    //             return resAdminsApi.modify(user);
    //     }
    // },
    //
    // create: (user:NewUserType) => {
    //     switch (user.userType) {
    //         case "Client":
    //             // @ts-ignore
    //             return clientsApi.create(user);
    //         case "Admin":
    //             return adminsApi.create(user);
    //         case "Staff":
    //             return resAdminsApi.create(user);
    //     }
    // },

    // getMe: (role:RolesEnum) => {
    //     switch (role) {
    //         case RolesEnum.CLIENT:
    //             return clientsApi.getMe();
    //         case RolesEnum.ADMIN:
    //             return adminsApi.getMe();
    //         case RolesEnum.STAFF:
    //             return resAdminsApi.getMe();
    //     }
    // },

    // changePassword: (accountType: AccountTypeEnum, oldPassword:string, newPassword: string, newPasswordConfirm: string) => {
    //     switch (accountType) {
    //         case AccountTypeEnum.CLIENT:
    //             return clientsApi.changePassword(oldPassword, newPassword, newPasswordConfirm);
    //         case AccountTypeEnum.ADMIN:
    //             return adminsApi.changePassword(oldPassword, newPassword, newPasswordConfirm);
    //         case AccountTypeEnum.STAFF:
    //             return resAdminsApi.changePassword(oldPassword, newPassword, newPasswordConfirm);
    //     }
    // },
}