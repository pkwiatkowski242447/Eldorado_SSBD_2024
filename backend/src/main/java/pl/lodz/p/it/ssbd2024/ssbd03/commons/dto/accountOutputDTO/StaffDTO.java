package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter @Setter
@ToString(callSuper = true)
public class StaffDTO extends UserLevelDTO {

    public StaffDTO() {
        super("STAFF");
    }

    public StaffDTO(UUID id, String roleName) {
        super(id, roleName);
    }
}