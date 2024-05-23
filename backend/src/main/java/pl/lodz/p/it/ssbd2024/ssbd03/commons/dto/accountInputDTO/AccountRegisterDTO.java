package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountInputDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Data transfer object used in creating new account.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountRegisterDTO {

    @Schema(description = "String identifier used to authenticate to the application", example = "boleslawchrobry", requiredMode = Schema.RequiredMode.REQUIRED)
    private String login;

    @Schema(description = "Secret string of characters used to authenticate to the application", example = "P@ssw0rd!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Schema(description = "First name of the user", example = "Boleslaw", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @Schema(description = "Last name of the user", example = "Chrobry", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @Schema(description = "E-mail used by the user", example = "boleslawchrobry@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Phone number of the user", example = "123-456-789", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phoneNumber;

    @Schema(description = "Language used natively in user browser", example = "pl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String language;

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the AccountRegisterDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("Login: ", login)
                .toString();
    }
}