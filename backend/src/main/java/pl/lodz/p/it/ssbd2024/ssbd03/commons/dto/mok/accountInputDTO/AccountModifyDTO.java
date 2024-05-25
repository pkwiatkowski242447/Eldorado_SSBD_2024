package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.AccountSignableDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.userlevel.UserLevelDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mok.AccountsConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AccountMessages;

import java.util.Set;

/**
 * Data transfer object used in editing user account;
 */
@Getter @Setter
@NoArgsConstructor
public class AccountModifyDTO extends AccountSignableDTO {
    @NotBlank(message = AccountMessages.NAME_BLANK)
    @Pattern(regexp = AccountsConsts.NAME_REGEX, message = AccountMessages.NAME_REGEX_NOT_MET)
    @Size(min = AccountsConsts.NAME_MIN_LENGTH, message = AccountMessages.NAME_TOO_SHORT)
    @Size(max = AccountsConsts.NAME_MAX_LENGTH, message = AccountMessages.NAME_TOO_LONG)
    @Schema(description = "First name of the user", example = "Boleslaw", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @NotBlank(message = AccountMessages.LASTNAME_BLANK)
    @Pattern(regexp = AccountsConsts.LASTNAME_REGEX, message = AccountMessages.LASTNAME_REGEX_NOT_MET)
    @Size(min = AccountsConsts.LASTNAME_MIN_LENGTH, message = AccountMessages.LASTNAME_TOO_SHORT)
    @Size(max = AccountsConsts.LASTNAME_MAX_LENGTH, message = AccountMessages.LASTNAME_TOO_LONG)
    @Schema(description = "Last name of the user", example = "Chrobry", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastname;
    @NotBlank(message = AccountMessages.PHONE_NUMBER_BLANK)
    @Pattern(regexp = AccountsConsts.PHONE_NUMBER_REGEX, message = AccountMessages.PHONE_NUMBER_REGEX_NOT_MET)
    @Schema(description = "Phone number of the user", example = "111222333", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phoneNumber;
    @NotNull(message = AccountMessages.TWO_FACTOR_AUTH_NULL)
    @Schema(description = "Boolean flag indicating whether 2FA is enabled.", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean twoFactorAuth;

    /**
     * All arguments constructor
     * @param login String identifier linked with Account.
     * @param version Number of object version.
     * @param userLevelsDto Set of account user levels.
     * @param name First name of the user.
     * @param lastname Last name of the user.
     * @param phoneNumber Phone number of the user.
     * @param twoFactorAuth Boolean flag indicating whether 2FA is enabled.
     */
    public AccountModifyDTO(String login, Long version, Set<UserLevelDTO> userLevelsDto,
                            String name, String lastname, String phoneNumber, Boolean twoFactorAuth) {
        super(login, version, userLevelsDto);
        this.name = name;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.twoFactorAuth = twoFactorAuth;
    }

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the AccountModifyDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
