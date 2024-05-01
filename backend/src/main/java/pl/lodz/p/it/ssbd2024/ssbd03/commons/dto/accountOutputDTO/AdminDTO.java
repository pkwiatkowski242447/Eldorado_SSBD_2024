package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class AdminDTO extends UserLevelDTO {
    public AdminDTO(String roleName) {
        super(roleName);
    }
}
