package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.userlevel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.ClientMessages;

import java.util.UUID;

/**
 * Data transfer object used for representing Client user level.
 */
@Getter @Setter
@LoggerInterceptor
public class ClientDTO extends UserLevelDTO {
    @NotNull(message = ClientMessages.CLIENT_TYPE_NULL)
    @Schema(description = "Type of client", example = "BASIC", requiredMode = Schema.RequiredMode.REQUIRED)
    private String clientType;

    /**
     * Construct object with setting superclass roleName property.
     */
    public ClientDTO() {
        super("CLIENT");
    }

    /**
     * Construct object with setting superclass id, version, roleName properties and client type.
     * @param id UUID identifier linked with user level.
     * @param version Number of object version.
     * @param clientType Type of client.
     */
    public ClientDTO(UUID id, Long version, String clientType) {
        super(id, version, "CLIENT");
        this.clientType = clientType;
    }
}
