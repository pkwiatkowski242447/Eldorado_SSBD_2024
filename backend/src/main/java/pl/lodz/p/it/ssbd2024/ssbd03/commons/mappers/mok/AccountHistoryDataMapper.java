package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mok;

import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountOutputDTO.AccountHistoryDataOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.AccountHistoryData;

/**
 * Used to handle AccountHistoryData entity-DTO mapping.
 */
@LoggerInterceptor
public class AccountHistoryDataMapper {

    /**
     * This method is used to map AccountHistoryData entity to AccountHistoryData output DTO class.
     *
     * @param account AccountHistoryData to map.
     * @return Returns mapped AccountHistoryData DTO class.
     */
    public static AccountHistoryDataOutputDTO toAccountHistoryDataOutputDto(AccountHistoryData account) {

        return new AccountHistoryDataOutputDTO(
                account.getId(),
                account.getVersion(),
                account.getLogin(),
                account.getSuspended(),
                account.getActive(),
                account.getBlocked(),
                account.getTwoFactorAuth(),
                account.getBlockedTime(),
                account.getLastSuccessfulLoginTime(),
                account.getLastSuccessfulLoginTime(),
                account.getLanguage(),
                account.getLastSuccessfulLoginIp(),
                account.getLastUnsuccessfulLoginIp(),
                account.getPhoneNumber(),
                account.getLastname(),
                account.getName(),
                account.getEmail(),
                account.getOperationType().toString(),
                account.getModificationTime(),
                account.getModifiedBy() == null ? "" : account.getModifiedBy().getLogin()
        );
    }

}
