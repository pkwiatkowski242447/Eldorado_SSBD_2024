package pl.lodz.p.it.ssbd2024.ssbd03.commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.UserLevelDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AdminDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.ClientDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.StaffDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class AccountSignableDTO implements SignableDTO {

    private String login;
    private Set<UserLevelDTO> userLevelsDto;

    @JsonIgnore
    @Override
    public Map<String, ?> getSigningFields() {
        return Map.ofEntries(
                Map.entry("login", login),
                Map.entry("userLevelsDetails", this.getUserLevelsDetailsAsMap(new ArrayList<>(userLevelsDto)))
        );
    }

    private List<Map<String, Object>> getUserLevelsDetailsAsMap(List<UserLevelDTO> levels) {
        levels.sort(Comparator.comparing(UserLevelDTO::getRoleName, String::compareToIgnoreCase));
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < levels.size(); i++) {
            list.add(new HashMap<>());
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
