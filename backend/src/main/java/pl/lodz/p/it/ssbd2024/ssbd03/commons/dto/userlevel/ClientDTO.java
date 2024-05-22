package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

/**
 * Data transfer object used for representing Client user level.
 */
@Getter @Setter
public class ClientDTO extends UserLevelDTO {
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

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the ClientDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .toString();
    }
}
