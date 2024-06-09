package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mok.AccountsConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AccountMessages;

/**
 * Data transfer object used in creating new account.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@LoggerInterceptor
public class AccountRegisterDTO {

    @NotBlank(message = AccountMessages.LOGIN_BLANK)
    @Pattern(regexp = AccountsConsts.LOGIN_REGEX, message = AccountMessages.LOGIN_REGEX_NOT_MET)
    @Size(min = AccountsConsts.LOGIN_MIN_LENGTH, message = AccountMessages.LOGIN_TOO_SHORT)
    @Size(max = AccountsConsts.LOGIN_MAX_LENGTH, message = AccountMessages.LOGIN_TOO_LONG)
    @Schema(description = "String identifier used to authenticate to the application", example = "boleslawchrobry", requiredMode = Schema.RequiredMode.REQUIRED)
    private String login;

    @NotBlank(message = AccountMessages.PASSWORD_BLANK)
    @Size(min = AccountsConsts.PASSWORD_LENGTH, max = AccountsConsts.PASSWORD_LENGTH, message = AccountMessages.PASSWORD_INVALID_LENGTH)
    @Column(name = DatabaseConsts.ACCOUNT_PASSWORD_COLUMN, nullable = false, length = 60)
    @Schema(description = "Secret string of characters used to authenticate to the application", example = "P@ssw0rd!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank(message = AccountMessages.NAME_BLANK)
    @Pattern(regexp = AccountsConsts.NAME_REGEX, message = AccountMessages.NAME_REGEX_NOT_MET)
    @Size(min = AccountsConsts.NAME_MIN_LENGTH, message = AccountMessages.NAME_TOO_SHORT)
    @Size(max = AccountsConsts.NAME_MAX_LENGTH, message = AccountMessages.NAME_TOO_LONG)
    @Schema(description = "First name of the user", example = "Boleslaw", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @NotBlank(message = AccountMessages.LASTNAME_BLANK)
    @Pattern(regexp = AccountsConsts.LASTNAME_REGEX, message = AccountMessages.LASTNAME_REGEX_NOT_MET)
    @Size(min = AccountsConsts.LASTNAME_MIN_LENGTH, message = AccountMessages.LASTNAME_TOO_SHORT)
    @Size(max = AccountsConsts.LASTNAME_MAX_LENGTH, message = AccountMessages.LASTNAME_TOO_LONG)
    @Schema(description = "Last name of the user", example = "Chrobry", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @Email(message = AccountMessages.EMAIL_CONSTRAINT_NOT_MET)
    @Size(min = AccountsConsts.EMAIL_MIN_LENGTH, message = AccountMessages.EMAIL_TOO_SHORT)
    @Size(max = AccountsConsts.EMAIL_MAX_LENGTH, message = AccountMessages.EMAIL_TOO_LONG)
    @Schema(description = "E-mail used by the user", example = "boleslawchrobry@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = AccountMessages.PHONE_NUMBER_BLANK)
    @Pattern(regexp = AccountsConsts.PHONE_NUMBER_REGEX, message = AccountMessages.PHONE_NUMBER_REGEX_NOT_MET)
    @Schema(description = "Phone number of the user", example = "123-456-789", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phoneNumber;

    @NotBlank(message = AccountMessages.LANGUAGE_BLANK)
    @Pattern(regexp = AccountsConsts.LANGUAGE_REGEX, message = AccountMessages.LANGUAGE_REGEX_NOT_MET)
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