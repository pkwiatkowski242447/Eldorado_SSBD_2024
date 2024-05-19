package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AccountSignableDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.UserLevelDTO;

import java.util.Set;

/**
 * Data transfer object used in editing user account;
 */
@Getter @Setter
@NoArgsConstructor
public class AccountModifyDTO extends AccountSignableDTO {
    @Schema(description = "First name of the user", example = "Boleslaw", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "Last name of the user", example = "Chrobry", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastname;
    @Schema(description = "Phone number of the user", example = "111222333", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phoneNumber;
    @Schema(description = "Boolean flag indicating whether 2FA is enabled.", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean twoFactorAuth;

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
                            String name, String lastname, String phoneNumber, boolean twoFactorAuth) {
        super(login, version, userLevelsDto);
        this.name = name;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.twoFactorAuth = twoFactorAuth;
    }
}
