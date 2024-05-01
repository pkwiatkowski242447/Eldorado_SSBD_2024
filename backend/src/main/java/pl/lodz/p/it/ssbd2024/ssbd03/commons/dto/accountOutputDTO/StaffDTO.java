package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class StaffDTO extends UserLevelDTO {
    public StaffDTO(String roleName) {
        super(roleName);
    }
}