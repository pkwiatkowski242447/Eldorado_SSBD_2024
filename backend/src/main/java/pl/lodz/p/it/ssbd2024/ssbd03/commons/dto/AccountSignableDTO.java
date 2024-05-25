package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel.AdminDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel.ClientDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel.StaffDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel.UserLevelDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mok.AccountsConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AccountMessages;

import java.util.*;

/**
 * Data transfer object used as a basis for Account singed DTOs.
 *
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public abstract class AccountSignableDTO implements SignableDTO {
    @NotBlank(message = AccountMessages.LOGIN_BLANK)
    @Pattern(regexp = AccountsConsts.LOGIN_REGEX, message = AccountMessages.LOGIN_REGEX_NOT_MET)
    @Size(min = AccountsConsts.LOGIN_MIN_LENGTH, message = AccountMessages.LOGIN_TOO_SHORT)
    @Size(max = AccountsConsts.LOGIN_MAX_LENGTH, message = AccountMessages.LOGIN_TOO_LONG)
    @Schema(description = "String identifier linked with Account", example = "boleslawchrobry", requiredMode = Schema.RequiredMode.REQUIRED)
    private String login;
    @NotNull(message = AccountMessages.VERSION_NULL)
    @Min(value = 0, message = AccountMessages.VERSION_LESS_THAN_ZERO)
    @Schema(description = "Number of object version", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long version;
    @NotNull(message = AccountMessages.USER_LEVEL_NULL)
    @Size(min = AccountsConsts.USER_LEVEL_MIN_SIZE, message = AccountMessages.USER_LEVEL_EMPTY)
    @Schema(description = "Set of account user levels", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel.UserLevelDTO> userLevelsDto;

    /**
     * This method returns Account DTO properties (names and values) that should be signed.
     * @return Returns map of properties that should be signed.
     */
    @JsonIgnore
    @Override
    public Map<String, ?> getSigningFields() {
        return Map.ofEntries(
                Map.entry("login", login),
                Map.entry("version", version),
                Map.entry("userLevelsDetails", this.getUserLevelsDetailsAsMap(new ArrayList<>(userLevelsDto)))
        );
    }

    /**
     * This method returns list of signing properties for each passed user level.
     * @param levels Signing Account dto user levels.
     * @return Signing properties for each passed user level.
     */
    private List<Map<String, Object>> getUserLevelsDetailsAsMap(List<pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel.UserLevelDTO> levels) {
        levels.sort(Comparator.comparing(UserLevelDTO::getRoleName, String::compareToIgnoreCase));
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < levels.size(); i++) {
            list.add(new HashMap<>());
            list.get(i).put("id", levels.get(i).getId().toString());
            list.get(i).put("version", levels.get(i).getVersion());
            list.get(i).put("roleName", levels.get(i).getRoleName());
            switch (levels.get(i)) {
                case ClientDTO clientOutputDTO -> list.get(i).put("clientType", clientOutputDTO.getClientType());
                case StaffDTO staffOutputDTO -> {}
                case AdminDTO adminOutputDTO -> {}
                default -> {}
            }
        }

        return list;
    }

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the AccountSignableDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("Login: ", login)
                .append("Version: ", version)
                .toString();
    }
}
