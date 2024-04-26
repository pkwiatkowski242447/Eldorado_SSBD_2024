package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StaffOutputDTO extends AccountAbstractOutputDTO {
    public StaffOutputDTO(String roleName) {
        super(roleName);
    }
}