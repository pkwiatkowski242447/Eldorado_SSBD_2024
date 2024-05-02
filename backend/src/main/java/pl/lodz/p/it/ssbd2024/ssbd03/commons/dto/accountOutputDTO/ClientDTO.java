package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString(callSuper = true)
//@JsonTypeName("CLIENT")
public class ClientDTO extends UserLevelDTO {
    private String clientType;

    public ClientDTO() {
        super("CLIENT");
    }

    public ClientDTO(String clientType) {
        this.clientType = clientType;
    }

    public ClientDTO(String roleName, String clientType) {
        super(roleName);
        this.clientType = clientType;
    }
}
