package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * Data transfer object used for representing Admin user level.
 */
@Getter @Setter
@ToString(callSuper = true)
public class AdminDTO extends UserLevelDTO {

    /**
     * Construct object with setting superclass roleName property.
     */
    public AdminDTO() {
        super("ADMIN");
    }

    /**
     * Construct object with setting superclass id, version, roleName properties.
     * @param id UUID identifier linked with user level.
     * @param version Number of object version.
     */
    public AdminDTO(String id, Long version) {
        super(UUID.fromString(id), version, "ADMIN");
    }

}
