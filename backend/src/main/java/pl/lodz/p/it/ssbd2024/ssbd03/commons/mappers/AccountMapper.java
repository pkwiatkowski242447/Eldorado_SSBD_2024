package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.*;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Staff;

import java.util.List;

public class AccountMapper {
    public static AccountOutputDTO toAccountOutputDto(Account account) {

        List<AccountAbstractOutputDTO> list = account.getUserLevels().stream().map(userLevel ->
                switch (userLevel) {
                    case Client client -> new ClientOutputDTO(client.getClass().getSimpleName().toUpperCase(), client.getType().toString());
                    case Staff staff -> new StaffOutputDTO(staff.getClass().getSimpleName().toUpperCase());
                    default -> new AdminOutputDTO("ADMIN");
                }
        ).toList();

        AccountOutputDTO outputDTO = new AccountOutputDTO(
                account.getId(),
                account.getLogin(),
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
                account.getEmail(),
                list
        );

        return outputDTO;
    }
}
