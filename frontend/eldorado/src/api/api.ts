import {apiWithConfig, apiAnonymous} from "./api.config";
import {UserLevelType} from "@/types/Users.ts";
import {CreateParkingType, CreateSectorType, EditSectorType, ParkingType} from "@/types/Parking.ts";

export const api = {
    logIn: (login: string, password: string) => {
        let language = window.navigator.language;
        language = language.substring(0, 2)
        return apiWithConfig.post('/auth/login-credentials', {login, password, language})
    },

    refreshSession: (refreshToken: string) => {
        return apiWithConfig.post('/auth/refresh-session', {refreshToken})
    },

    logIn2fa: (userLogin: string, authCodeValue: string) => {
        let language = window.navigator.language;
        language = language.substring(0, 2)
        return apiWithConfig.post('/auth/login-auth-code', {userLogin, authCodeValue, language})
    },

    logOut: () => {
        return apiWithConfig.post('/auth/logout');
    },

    changePasswordSelf: (oldPassword: string, newPassword: string) => {
        return apiWithConfig.patch('/accounts/change-password/self', {
            oldPassword: oldPassword,
            newPassword: newPassword
        })
    },

    registerClient: (login: string, password: string, firstName: string, lastName: string, email: string, phoneNumber: string, language: string) => {
        return apiWithConfig.post('/register/client', {
            login,
            password,
            firstName,
            lastName,
            email,
            phoneNumber,
            language
        })
    },

    registerStaff: (login: string, password: string, firstName: string, lastName: string, email: string, phoneNumber: string, language: string) => {
        return apiWithConfig.post('/register/staff', {
            login,
            password,
            firstName,
            lastName,
            email,
            phoneNumber,
            language
        })
    },

    registerAdmin: (login: string, password: string, firstName: string, lastName: string, email: string, phoneNumber: string, language: string) => {
        return apiWithConfig.post('/register/admin', {
            login,
            password,
            firstName,
            lastName,
            email,
            phoneNumber,
            language
        })
    },

    activateAccount: (token: string) => {
        return apiWithConfig.post(`/accounts/activate-account/${token}`)
    },

    forgotPassword: (email: string) => {
        return apiWithConfig.post(`/accounts/forgot-password`, {email})
    },

    resetPasswordByUser: (token: string, password: string) => {
        return apiWithConfig.post(`/accounts/change-password/${token}`, {
            password
        })
    },

    resetPasswordByAdmin: (id: string) => {
        return apiWithConfig.post(`/accounts/reset-password/${id}`)
    },

    modifyAccountSelf: (login: string, version: number, userLevelsDto: UserLevelType[], name: string,
                        lastname: string, phoneNumber: string, twoFactorAuth: boolean, etag: string) => {
        const cleanedEtag = etag.replace(/^"|"$/g, '');
        //this is necessary because backend requires etag to be without quotes
        return apiWithConfig.put('/accounts/self', {
            login: login,
            version: version,
            userLevelsDto: userLevelsDto,
            name: name,
            lastname: lastname,
            phoneNumber: phoneNumber,
            twoFactorAuth: twoFactorAuth
        }, {
            headers: {
                'If-Match': cleanedEtag
            }
        })
    },

    modifyAccountUser: (login: string, version: number, userLevelsDto: UserLevelType[], name: string,
                        lastname: string, phoneNumber: string, twoFactorAuth: boolean, etag: string) => {
        const cleanedEtag = etag.replace(/^"|"$/g, '');
        //this is necessary because backend requires the etag to be without quotes
        const test = apiWithConfig.put('/accounts', {
            login: login,
            version: version,
            userLevelsDto: userLevelsDto,
            name: name,
            lastname: lastname,
            phoneNumber: phoneNumber,
            twoFactorAuth: twoFactorAuth
        }, {
            headers: {
                'If-Match': cleanedEtag
            }
        })
        console.log(test)
        return test
    },

    changeEmailSelf: (email: string) => {
        return apiWithConfig.patch('/accounts/change-email-self', {
            email: email
        })
    },

    changeEmailUser: (id: string, email: string) => {
        return apiWithConfig.patch(`/accounts/${id}/change-email`, {
            email: email
        })
    },

    confirmEmail: (token: string) => {
        return apiAnonymous.post(`/accounts/confirm-email/${token}`)
    },

    resendEmailConfirmation: () => {
        return apiWithConfig.post('/accounts/resend-email-confirmation')
    },

    getAccounts: (details: string) => {
        return apiWithConfig.get('/accounts' + details)
    },

    getAccountsMatchPhraseInAccount: (details: string) => {
        return apiWithConfig.get('/accounts/match-phrase-in-account' + details)
    },

    removeLevelClient: (id: string) => {
        return apiWithConfig.post(`/accounts/${id}/remove-level-client`)
    },

    removeLevelStaff: (id: string) => {
        return apiWithConfig.post(`/accounts/${id}/remove-level-staff`)
    },

    removeLevelAdmin: (id: string) => {
        return apiWithConfig.post(`/accounts/${id}/remove-level-admin`)
    },

    addLevelClient: (id: string) => {
        return apiWithConfig.post(`/accounts/${id}/add-level-client`)
    },

    addLevelStaff: (id: string) => {
        return apiWithConfig.post(`/accounts/${id}/add-level-staff`)
    },

    addLevelAdmin: (id: string) => {
        return apiWithConfig.post(`/accounts/${id}/add-level-admin`)
    },

    getAccountById: (id: string) => {
        return apiWithConfig.get(`/accounts/${id}`)
    },

    blockAccount: (id: string) => {
        return apiWithConfig.post(`/accounts/${id}/block`, {}, {
            headers: {
                'accept': 'text/plain'
            }
        });
    },

    unblockAccount: (id: string) => {
        return apiWithConfig.post(`/accounts/${id}/unblock`, {}, {
            headers: {
                'accept': 'text/plain'
            }
        });
    },

    historyDataSelf: (details: string) => {
        return apiWithConfig.get('/accounts/history-data/self' + details)
    },

    historyDataUser: (id: string, details: string) => {
        return apiWithConfig.get(`/accounts/${id}/history-data` + details)
    },

    restoreAccess: (email: string) => {
        return apiWithConfig.post('/accounts/restore-access', {email})
    },

    restoreAccessToken: (token: string) => {
        return apiWithConfig.post(`/accounts/restore-token/${token}`)
    },

    getPasswordAdminResetStatus: () => {
        return apiWithConfig.get('/accounts/admin-password-reset-status')
    },

    getAllAttributes: () => {
        return apiWithConfig.get('/accounts/attributes?pageNumber=0&pageSize=2')
    },

    getMyAttributes: () => {
        return apiWithConfig.get('/accounts/attributes/account/me/get')
    },

    addAttributes: (attributeName: string, attributeValue: string) => {
        return apiWithConfig.post(`/accounts/attributes/account/me/assign/${attributeName}/${attributeValue}`)
    },

    getParking: (details: string) => {
        return apiWithConfig.get('/parking' + details)
    },

    createParking: (parking: CreateParkingType) => {
        return apiWithConfig.post('/parking', {...parking})
    },

    deleteParking: (parkingId: string) => {
        return apiWithConfig.delete(`/parking/${parkingId}`)
    },

    getParkingById: (parkingId: string) => {
        return apiWithConfig.get(`/parking/get/${parkingId}`)
    },

    getSectorsStaff: (id:string | undefined, details: string) => {
        return apiWithConfig.get(`/parking/${id}/sectors${details}`)
    },

    createSector: (parkingId: string, sector: CreateSectorType) => {
        return apiWithConfig.post(`/parking/${parkingId}/sectors`, {...sector})
    },
    deleteSector: (sectorId: string) => {
        return apiWithConfig.delete(`/parking/sectors/${sectorId}`)
    },
    getSectorById: (sectorId: string) => {
        return apiWithConfig.get(`/parking/sectors/get/${sectorId}`)
    },
    modifySector: (sector: EditSectorType) => {
        const cleanedEtag = sector.signature.replace(/^"|"$/g, '');
        const temp = {
            id: sector.id,
            parkingId: sector.parkingId,
            version: sector.version,
            name: sector.name,
            type: sector.type,
            maxPlaces: sector.maxPlaces,
            weight: sector.weight,
        };
        return apiWithConfig.put('/parking/sectors',
            {...temp},
            {
                headers:
                    {
                        'If-Match':
                        cleanedEtag
                    }
            }
        )
    },
    deactivateSector: (sectorId: string, time: Date) => {
        return apiWithConfig.post(`/parking/sectors/${sectorId}/deactivate`, {deactivationTime: time})
    },
    activateSector: (sectorId: string) => {
        return apiWithConfig.post(`/parking/sectors/${sectorId}/activate`)
    },
    getHistoricalReservationsSelf: (pageNumber: number, pageSize: number) => {
        return apiWithConfig.get(`/reservations/historical/self?pageNumber=${pageNumber}&pageSize=${pageSize}`)
    },

    getActiveReservationsSelf: (pageNumber: number, pageSize: number) => {
        return apiWithConfig.get(`/reservations/active/self?pageNumber=${pageNumber}&pageSize=${pageSize}`)
    },

    getAllReservations: (pageNumber: number, pageSize: number) => {
        return apiWithConfig.get(`/reservations?pageNumber=${pageNumber}&pageSize=${pageSize}`)
    },

    getMyReservationDetails: (id: string, details: string) => {
        return apiWithConfig.get(`/reservations/client/${id}` + details)
    },

    getReservationDetails: (id: string, details: string) => {
        return apiWithConfig.get(`/reservations/staff/${id}` + details)
    },

    getActiveParking: (details: string) => {
        return apiWithConfig.get('/parking/active' + details)
    },

    getParkingInfo: (id: string) => {
        return apiWithConfig.get(`/parking/get/${id}`)
    },

    getParkingSectors: (id: string, details: string) => {
        return apiWithConfig.get(`/parking/client/sectors/${id}` + details)
    },

    getSectorInfo: (id: string) => {
        return apiWithConfig.get(`/parking/sectors/get/${id}`)
    },

    modifyParking: (parking: ParkingType) => {
        const cleanedEtag = parking.signature.replace(/^"|"$/g, '');
        const temp = {
            parkingId: parking.parkingId,
            version: parking.version,
            city: parking.city,
            street: parking.street,
            zipCode: parking.zipCode,
            strategy: parking.strategy,
        };
        return apiWithConfig.put('/parking',
            {...temp},
            {
                headers:
                    {
                        'If-Match':
                        cleanedEtag
                    }
            }
        )
    },

    createReservation: (sectorId: string, beginTime: string, endTime: string) => {
        return apiWithConfig.post('/reservations/make-reservation', {
            sectorId: sectorId,
            beginTime: beginTime,
            endTime: endTime
        })
    },

    cancelReservation: (id: string) => {
        return apiWithConfig.delete(`/reservations/cancel-reservation/${id}`)
    },

    enterParkingWithoutReservation: (parkingId: string) => {
        return apiWithConfig.post(`/parking/${parkingId}/enter`)
    },

    enterParkingWithReservation: (reservationId: string) => {
        return apiWithConfig.post(`/parking/reservations/${reservationId}/enter`)
    },

    exitParking: (id:string, endReservation: boolean, isAuthenticated: boolean) => {
        if (isAuthenticated) {
            return apiWithConfig.post(`/parking/reservations/${id}/exit?end=${endReservation}`)
        }
        return apiAnonymous.post(`/parking/reservations/${id}/exit?end=${endReservation}`)
    },

    getRandomImage: () => {
        return apiWithConfig.get('/auth/random-image')
    },

    getParkingHistory: (id: string, details: string) => {
        return apiWithConfig.get(`/parking/${id}/history-data` + details)
    }
}