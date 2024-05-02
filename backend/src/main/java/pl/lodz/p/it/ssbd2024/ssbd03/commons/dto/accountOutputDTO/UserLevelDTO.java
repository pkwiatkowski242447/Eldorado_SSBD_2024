package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "roleName")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClientDTO.class, name = "CLIENT"),
        @JsonSubTypes.Type(value = StaffDTO.class, name = "STAFF"),
        @JsonSubTypes.Type(value = AdminDTO.class, name = "ADMIN"),
}
)
public abstract class UserLevelDTO {
    private String roleName;

    public UserLevelDTO() {
//        this.roleName = this.getClass().getSimpleName().toUpperCase().replace("DTO", "");
    }

    public UserLevelDTO(String roleName) {
        this.roleName = roleName;
    }
}
