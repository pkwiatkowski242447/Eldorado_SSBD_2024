package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.userlevel;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.UserLevelMessages;

import java.util.UUID;

/**
 * Data transfer object used for representing UserLevel entity.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "roleName")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClientDTO.class, name = "CLIENT"),
        @JsonSubTypes.Type(value = StaffDTO.class, name = "STAFF"),
        @JsonSubTypes.Type(value = AdminDTO.class, name = "ADMIN"),
}
)
@Getter @Setter
@LoggerInterceptor
public abstract class UserLevelDTO {
    //TODO validation, certainly...
    @Schema(description = "UUID identifier linked with user level", example = "73538016-095a-4564-965c-9a17c9ded334", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;
    @NotNull(message = UserLevelMessages.VERSION_NULL)
    @Min(value = 0, message = UserLevelMessages.VERSION_LESS_THAN_ZERO)
    @Schema(description = "Number of object version", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long version;
    @NotBlank(message = UserLevelMessages.ROLE_NAME_BLANK)
    @Schema(description = "Name of user level representing by this object", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleName;

    /**
     * No arguments constructor, which setting role name based on class name with default pattern
     */
    public UserLevelDTO() {
        this.roleName = this.getClass().getSimpleName().toUpperCase().replace("DTO", "");
    }

    /**
     * Construct object with setting roleName property.
     * @param roleName Name of user level representing by this object.
     */
    public UserLevelDTO(String roleName) {
        this.roleName = roleName;
    }

    /**
     * All arguments constructor
     * @param id UUID identifier linked with user level.
     * @param version Number of object version.
     * @param roleName Name of user level representing by this object.
     */
    public UserLevelDTO(UUID id, Long version, String roleName) {
        this.id = id;
        this.version = version;
        this.roleName = roleName;
    }

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the UserLevelDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("Id: ", id)
                .append("Version: ", version)
                .toString();
    }
}
