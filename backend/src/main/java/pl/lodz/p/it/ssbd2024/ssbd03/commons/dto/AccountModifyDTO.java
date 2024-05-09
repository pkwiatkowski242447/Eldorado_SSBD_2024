package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AccountSignableDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.UserLevelDTO;

import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class AccountModifyDTO extends AccountSignableDTO {
    private String name;
    private String lastname;
    private String phoneNumber;

    public AccountModifyDTO(String login, Long version, Set<UserLevelDTO> userLevelsDto,
                            String name, String lastname, String phoneNumber) {
        super(login, version, userLevelsDto);
        this.name = name;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
    }
}
