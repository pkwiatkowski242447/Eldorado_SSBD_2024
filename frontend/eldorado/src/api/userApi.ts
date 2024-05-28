import {apiWithConfig} from "@/api/api.config.ts";

export const usersApi = {
    getSelf: async () => {
        return await apiWithConfig.get(`/accounts/self`);
    },
}