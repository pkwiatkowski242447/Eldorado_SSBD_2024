package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DTOConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.DTOMessages;

/**
 * Data Transfer Object used to authenticate.
 *
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationLoginDTO {

    @NotBlank(message = DTOMessages.LOGIN_BLANK)
    @Pattern(regexp = DTOConsts.LOGIN_REGEX, message = DTOMessages.LOGIN_REGEX_NOT_MET)
    @Size(min = DTOConsts.LOGIN_MIN_LENGTH, message = DTOMessages.LOGIN_TOO_SHORT)
    @Size(max = DTOConsts.LOGIN_MAX_LENGTH, message = DTOMessages.LOGIN_TOO_LONG)
    @Schema(description = "String identifier used to authenticate to the application", example = "boleslawchrobry", requiredMode = Schema.RequiredMode.REQUIRED)
    private String login;

    @NotBlank(message = DTOMessages.PASSWORD_BLANK)
    @Pattern(regexp = DTOConsts.PASSWORD_REGEX, message = DTOMessages.PASSWORD_REGEX_NOT_MET)
    @Size(min = DTOConsts.PASSWORD_MIN_LENGTH, message = DTOMessages.PASSWORD_TOO_SHORT)
    @Size(max = DTOConsts.PASSWORD_MAX_LENGTH, message = DTOMessages.PASSWORD_TOO_LONG)
    @Schema(description = "Secret string of characters used to authenticate to the application", example = "P@ssw0rd!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank(message = DTOMessages.LANGUAGE_BLANK)
    @Pattern(regexp = DTOConsts.LANGUAGE_REGEX, message = DTOMessages.LANGUAGE_REGEX_NOT_MET)
    @Size(min = DTOConsts.LANGUAGE_LENGTH, max = DTOConsts.LANGUAGE_LENGTH, message = DTOMessages.LANGUAGE_SIZE_INVALID)
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
