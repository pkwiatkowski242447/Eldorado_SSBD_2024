package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mok;

import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountOutputDTO.AccountListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;

@LoggerInterceptor
public class AccountListMapper {
    static public AccountListDTO toAccountListDTO(Account a) {
        return new AccountListDTO(a.getId(), a.getLogin(), a.getName(),
                a.getLastname(), a.getActive(), a.getBlocked(),
                a.getSuspended(), a.getActivityLog().getLastSuccessfulLoginTime(),
                a.getActivityLog().getLastUnsuccessfulLoginTime(),
                a.getUserLevels().stream().map(userLevel -> userLevel.getClass().getSimpleName()).toList());
    }
}
