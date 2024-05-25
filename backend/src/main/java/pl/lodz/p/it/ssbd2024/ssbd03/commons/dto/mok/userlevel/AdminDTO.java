package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.userlevel;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Data transfer object used for representing Admin user level.
 */
@Getter @Setter
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
    public AdminDTO(UUID id, Long version) {
        super(id, version, "ADMIN");
    }

}
