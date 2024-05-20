package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Data Transfer Object used to authenticate.
 *
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountLoginDTO {

    @Schema(description = "String identifier used to authenticate to the application", example = "boleslawchrobry", requiredMode = Schema.RequiredMode.REQUIRED)
    private String login;

    @Schema(description = "Secret string of characters used to authenticate to the application", example = "P@ssw0rd!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Schema(description = "String of characters that specify language setting in the user browser.", example = "pl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String language;

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the AccountLoginDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("login", login)
                .toString();
    }
}
