package pl.lodz.p.it.ssbd2024.ssbd03.commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.UserLevelDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AdminDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.ClientDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.StaffDTO;

import java.util.*;

/**
 * Data transfer object used as a basis for Account singed DTOs.
 *
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public abstract class AccountSignableDTO implements SignableDTO {
    @Schema(description = "String identifier linked with Account", example = "boleslawchrobry", requiredMode = Schema.RequiredMode.REQUIRED)
    private String login;
    @Schema(description = "Number of object version", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long version;
    @Schema(description = "Set of account user levels", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<UserLevelDTO> userLevelsDto;

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
    private List<Map<String, Object>> getUserLevelsDetailsAsMap(List<UserLevelDTO> levels) {
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
}
