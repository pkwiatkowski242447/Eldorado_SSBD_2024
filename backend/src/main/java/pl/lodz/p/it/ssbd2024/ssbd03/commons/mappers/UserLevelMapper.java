package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AdminDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.ClientDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.StaffDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.UserLevelDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Admin;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Staff;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class UserLevelMapper {
    public static UserLevel toUserLevel(UserLevelDTO userLevelDTO) {
        //TODO maybe dynamic mapper??
        return switch (userLevelDTO) {
            case ClientDTO clientDTO -> {
                Client client = new Client();
                client.setType(switch (clientDTO.getClientType().toUpperCase()) {
                    case "CLIENT" -> Client.ClientType.BASIC;
                    case "STANDARD" -> Client.ClientType.STANDARD;
                    case "PREMIUM" -> Client.ClientType.PREMIUM;
                    ///FIXME good exception or not good
                    default -> throw new IllegalArgumentException(I18n.UNEXPECTED_CLIENT_TYPE);
                });
                yield client;
            }
            case StaffDTO staffDTO -> new Staff();
            case AdminDTO adminDTO -> new Admin();
            ///FIXME good exception or not good
            default -> throw new IllegalArgumentException(I18n.UNEXPECTED_USER_LEVEL);
        };
    }

    public static UserLevelDTO toUserLevelDTO(UserLevel userLevel) {
        return switch (userLevel) {
            case Client client ->
                    new ClientDTO(client.getClass().getSimpleName().toUpperCase(), client.getType().toString());
            case Staff staff -> new StaffDTO(staff.getClass().getSimpleName().toUpperCase());
            case Admin admin -> new AdminDTO(admin.getClass().getSimpleName().toUpperCase());
            ///FIXME good exception or not good
            default -> throw new IllegalArgumentException(I18n.UNEXPECTED_USER_LEVEL);
        };
    }
}
