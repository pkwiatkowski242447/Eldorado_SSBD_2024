package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mok;

import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO.AccountModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountOutputDTO.AccountOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.userlevel.UserLevelDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.mapper.MapperBaseException;

import java.util.HashSet;
import java.util.Set;

/**
 * Used to handle Account entity-DTO mapping.
 */
@LoggerInterceptor
public class AccountMapper {

    /**
     * This method is used to map Account entity to Account output DTO class.
     * @param account Account to map.
     * @return Returns mapped Account DTO class.
     * @throws MapperBaseException Threw when Account contains invalid unhandled user level.
     */
    public static AccountOutputDTO toAccountOutputDto(Account account) throws MapperBaseException {
        Set<UserLevelDTO> list = new HashSet<>();
        for (UserLevel userLevel : account.getUserLevels()) {
            list.add(UserLevelMapper.toUserLevelDTO(userLevel));
        }

        return new AccountOutputDTO(
                account.getLogin(),
                account.getVersion(),
                list,
                account.getId(),
                account.getSuspended(),
                account.getActive(),
                account.getBlocked(),
                account.getTwoFactorAuth(),
                account.getBlockedTime(),
                account.getCreationTime(),
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

    /**
     * This method is used to map modified Account DTO to Account entity class.
     * @param accountModifyDTO Account DTO to map.
     * @return Returns mapped Account entity class.
     * @throws MapperBaseException Threw when Account DTO contains unhandled user level.
     */
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

        account.setTwoFactorAuth(accountModifyDTO.getTwoFactorAuth());

        return account;
    }
}
