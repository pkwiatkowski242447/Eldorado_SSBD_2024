package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.*;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Admin;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Staff;

import java.util.List;

public class AccountMapper {
    public static AccountOutputDTO toAccountOutputDto(Account account) {

        List<UserLevelDTO> list = account.getUserLevels().stream().map(userLevel ->
                switch (userLevel) {
                    case Client client -> new ClientDTO(client.getClass().getSimpleName().toUpperCase(), client.getType().toString());
                    case Staff staff -> new StaffDTO(staff.getClass().getSimpleName().toUpperCase());
                    case Admin admin -> new AdminDTO(admin.getClass().getSimpleName().toUpperCase());
                    default -> throw new IllegalArgumentException("Unexpected userlevel: " + userLevel.getClass().getSimpleName());
                }
        ).toList();

        return new AccountOutputDTO(
                account.getLogin(),
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
}
