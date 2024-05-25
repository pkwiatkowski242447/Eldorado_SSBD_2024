package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.userlevel;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Data transfer object used for representing Staff user level.
 */
@Getter @Setter
public class StaffDTO extends UserLevelDTO {

    /**
     * Construct object with setting superclass roleName property.
     */
    public StaffDTO() {
        super("STAFF");
    }

    /**
     * Construct object with setting superclass id, version, roleName properties.
     * @param id UUID identifier linked with user level.
     * @param version Number of object version.
     */
    public StaffDTO(UUID id, Long version) {
        super(id, version, "STAFF");
    }
}