package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AccountOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.UserLevelDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;

import java.util.Set;
import java.util.stream.Collectors;

public class AccountMapper {
    public static AccountOutputDTO toAccountOutputDto(Account account) {

        Set<UserLevelDTO> list = account.getUserLevels()
                .stream()
                .map(UserLevelMapper::toUserLevelDTO)
                .collect(Collectors.toSet());

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

    public static Account toAccount(AccountModifyDTO accountModifyDTO) {
        Account account = new Account(
                accountModifyDTO.getLogin(),
                null,
                accountModifyDTO.getName(),
                accountModifyDTO.getLastname(),
                null,
                accountModifyDTO.getPhoneNumber(),
                accountModifyDTO.getVersion()
        );

        accountModifyDTO.getUserLevelsDto()
                .stream()
                .map(UserLevelMapper::toUserLevel)
                .forEach(account::addUserLevel);
        return account;
    }
}
