package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class ClientDTO extends UserLevelDTO {
    private String clientType;

    public ClientDTO(String roleName, String clientType) {
        super(roleName);
        this.clientType = clientType;
    }
}
