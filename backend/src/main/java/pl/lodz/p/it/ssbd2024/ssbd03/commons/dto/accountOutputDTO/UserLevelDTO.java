package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "roleName")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClientDTO.class, name = "CLIENT"),
        @JsonSubTypes.Type(value = StaffDTO.class, name = "STAFF"),
        @JsonSubTypes.Type(value = AdminDTO.class, name = "ADMIN"),
}
)
@Getter @Setter
public abstract class UserLevelDTO {
    private UUID id;
    private Long version;
    private String roleName;

    public UserLevelDTO() {
        this.roleName = this.getClass().getSimpleName().toUpperCase().replace("DTO", "");
    }

    public UserLevelDTO(String roleName) {
        this.roleName = roleName;
    }

    public UserLevelDTO(UUID id, Long version, String roleName) {
        this.id = id;
        this.version = version;
        this.roleName = roleName;
    }
}
