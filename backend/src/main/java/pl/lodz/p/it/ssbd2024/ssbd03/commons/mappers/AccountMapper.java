package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AccountOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.UserLevelDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mapper.MapperBaseException;

import java.util.HashSet;
import java.util.Set;

//TODO javadoc -> Michal

public class AccountMapper {
    public static AccountOutputDTO toAccountOutputDto(Account account) throws MapperBaseException {
        Set<UserLevelDTO> list= new HashSet<>();
        for (UserLevel userLevel : account.getUserLevels()) {
            list.add(UserLevelMapper.toUserLevelDTO(userLevel));
        }

        return new AccountOutputDTO(
                account.getLogin(),
                account.getVersion(),
                list,
                account.getId(),
                account.getVerified(),
                account.getActive(),
                account.getBlocked(),
                account.getBlockedTime(),
                account.getCreationDate(),
                account.getActivityLog().getLastSuccessfulLoginTime(),
                account.getActivityLog().getLastUnsuccessfulLoginTime(),
                account.getAccountLanguage(),
                account.getActivityLog().getLastSuccessfulLoginIp(),
                account.getActivityLog().getLastUnsuccessfulLoginIp(),
                account.getPhoneNumber(),
                account.getLastname(),
                account.getName(),
                account.getEmail()
        );
    }

    public static Account toAccount(AccountModifyDTO accountModifyDTO) throws MapperBaseException {
        Account account = new Account(
                accountModifyDTO.getLogin(),
                null,
                accountModifyDTO.getName(),
                accountModifyDTO.getLastname(),
                null,
                accountModifyDTO.getPhoneNumber(),
                accountModifyDTO.getVersion()
        );

        Set<UserLevel> userLevels = new HashSet<>();
        for (UserLevelDTO userLevelDTO : accountModifyDTO.getUserLevelsDto()) {
            userLevels.add(UserLevelMapper.toUserLevel(userLevelDTO));
        }

        userLevels.forEach(account::addUserLevel);

        return account;
    }
}
