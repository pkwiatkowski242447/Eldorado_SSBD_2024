package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminOutputDTO extends AccountAbstractOutputDTO {
    public AdminOutputDTO(String roleName) {
        super(roleName);
    }
}
