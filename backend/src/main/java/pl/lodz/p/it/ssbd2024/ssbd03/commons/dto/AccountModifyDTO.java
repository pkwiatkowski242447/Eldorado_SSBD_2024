package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AccountSignableDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.UserLevelDTO;

import java.util.Set;

/**
 * Data transfer object used in editing user account;
 */
@Getter @Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class AccountModifyDTO extends AccountSignableDTO {
    @Schema(description = "First name of the user", example = "Boleslaw", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "Last name of the user", example = "Chrobry", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastname;
    @Schema(description = "Phone number of the user", example = "111222333", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phoneNumber;
    @Schema(description = "Language used natively in user browser", example = "pl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String accountLanguage;

    /**
     * All arguments constructor
     * @param login String identifier linked with Account.
     * @param version Number of object version.
     * @param userLevelsDto Set of account user levels.
     * @param name First name of the user.
     * @param lastname Last name of the user.
     * @param phoneNumber Phone number of the user.
     * @param accountLanguage Language used natively in user browser.
     */
    public AccountModifyDTO(String login, Long version, Set<UserLevelDTO> userLevelsDto,
                            String name, String lastname, String phoneNumber, String accountLanguage) {
        super(login, version, userLevelsDto);
        this.name = name;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.accountLanguage = accountLanguage;
    }
}
