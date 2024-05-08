package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter @Setter
@ToString(callSuper = true)
public class AdminDTO extends UserLevelDTO {

    public AdminDTO() {
        super("ADMIN");
    }

    public AdminDTO(String id, String roleName) {
        super(UUID.fromString(id), roleName);
    }

}
