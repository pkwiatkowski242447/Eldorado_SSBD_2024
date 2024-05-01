package pl.lodz.p.it.ssbd2024.ssbd03.commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.UserLevelDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AdminDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.ClientDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.StaffDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@ToString
@Getter
public class AccountSignableDTO implements SignableDTO {

    private String login;
    //FIXME change type
    private List<UserLevelDTO> userLevelsDto;

    @JsonIgnore
    @Override
    public Map<String, ?> getSigningFields() {
        return Map.ofEntries(
                Map.entry("login", login),
                Map.entry("userLevelsDetails", this.getUserLevelsDetailsAsMap(new ArrayList<>(userLevelsDto)))
        );
    }

    private List<Map<String, Object>> getUserLevelsDetailsAsMap(List<UserLevelDTO> roles) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < roles.size(); i++) {
            list.add(new HashMap<>());
            list.get(i).put("roleName", roles.get(i).getRoleName());
            switch (roles.get(i)) {
                case ClientDTO clientOutputDTO -> list.get(i).put("clientType", clientOutputDTO.getClientType());
                case StaffDTO staffOutputDTO -> {}
                case AdminDTO adminOutputDTO -> {}
                default -> {}
            }
        }

        return list;
    }
}
