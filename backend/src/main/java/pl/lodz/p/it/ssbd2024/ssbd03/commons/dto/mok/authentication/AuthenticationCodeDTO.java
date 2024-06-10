package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mok.AccountsConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AccountMessages;

/**
 * Data Transfer Object used for the second step in multifactor
 * authentication, when code is passed to authenticate user.
 *
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@LoggerInterceptor
public class AuthenticationCodeDTO {

    @NotBlank(message = AccountMessages.LOGIN_BLANK)
    @Pattern(regexp = AccountsConsts.LOGIN_REGEX, message = AccountMessages.LOGIN_REGEX_NOT_MET)
    @Size(min = AccountsConsts.LOGIN_MIN_LENGTH, message = AccountMessages.LOGIN_TOO_SHORT)
    @Size(max = AccountsConsts.LOGIN_MAX_LENGTH, message = AccountMessages.LOGIN_TOO_LONG)
    @Schema(description = "String identifier used to authenticate to the application", example = "boleslawchrobry", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userLogin;
    @NotBlank(message = AccountMessages.AUTH_CODE_BLANK)
    @Pattern(regexp = AccountsConsts.AUTH_CODE_REGEX, message = AccountMessages.AUTH_CODE_REGEX_NOT_MET)
    @Size(min = AccountsConsts.AUTH_CODE_MIN_LENGTH, message = AccountMessages.AUTH_CODE_TOO_SHORT)
    @Size(max = AccountsConsts.AUTH_CODE_MAX_LENGTH, message = AccountMessages.AUTH_CODE_TOO_LONG)
    @Schema(description = "Code used to authenticate the user", example = "12345678", requiredMode = Schema.RequiredMode.REQUIRED)
    private String authCodeValue;
    @NotBlank(message = AccountMessages.LANGUAGE_BLANK)
    @Pattern(regexp = AccountsConsts.LANGUAGE_REGEX, message = AccountMessages.LANGUAGE_REGEX_NOT_MET)
    @Schema(description = "Language used natively in user browser", example = "pl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String language;

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the AuthenticationCodeDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("Login: ", userLogin)
                .toString();
    }
}
