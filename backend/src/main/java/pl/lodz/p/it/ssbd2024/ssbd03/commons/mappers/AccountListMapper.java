package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AccountListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;


public class AccountListMapper {
    static public AccountListDTO toAccountListDTO(Account a) {
        return new AccountListDTO(a.getId(), a.getLogin(), a.getName(),
                a.getLastname(), a.getActive(), a.getBlocked(),
                a.getVerified(), a.getActivityLog().getLastSuccessfulLoginTime(),
                a.getActivityLog().getLastUnsuccessfulLoginTime(),
                a.getUserLevels().stream().map(userLevel -> userLevel.getClass().getSimpleName()).toList());
    }
}
