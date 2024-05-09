package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter @Setter
@ToString(callSuper = true)
public class ClientDTO extends UserLevelDTO {
    private String clientType;

    public ClientDTO() {
        super("CLIENT");
    }

    public ClientDTO(UUID id, Long version, String clientType) {
        super(id, version, "CLIENT");
        this.clientType = clientType;
    }
}
