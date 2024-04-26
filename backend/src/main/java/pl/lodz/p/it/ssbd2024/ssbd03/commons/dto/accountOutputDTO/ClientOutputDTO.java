package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO;

import lombok.Getter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AccountAbstractOutputDTO;

@Getter
@ToString
public class ClientOutputDTO extends AccountAbstractOutputDTO {
    private String clientType;

    public ClientOutputDTO(String roleName, String clientType) {
        super(roleName);
        this.clientType = clientType;
    }
}
