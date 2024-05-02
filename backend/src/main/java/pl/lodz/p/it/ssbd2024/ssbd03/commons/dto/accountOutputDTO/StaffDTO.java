package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString(callSuper = true)
public class StaffDTO extends UserLevelDTO {

    public StaffDTO() {
        super("STAFF");
    }

    public StaffDTO(String roleName) {
        super(roleName);
    }
}