package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AdminDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.ClientDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.StaffDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.UserLevelDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Admin;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Staff;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;

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
                    ///TODO change exc
                    default -> throw new IllegalArgumentException("Unexpected client type: " + clientDTO.getClientType());
                });
                yield client;
            }
            case StaffDTO staffDTO -> new Staff();
            case AdminDTO adminDTO -> new Admin();
            ///TODO change exc
            default -> throw new IllegalArgumentException("Unexpected userlevel: " + userLevelDTO.getClass().getSimpleName());
        };
    }

    public static UserLevelDTO toUserLevelDTO(UserLevel userLevel) {
        return switch (userLevel) {
            case Client client ->
                    new ClientDTO(client.getClass().getSimpleName().toUpperCase(), client.getType().toString());
            case Staff staff -> new StaffDTO(staff.getClass().getSimpleName().toUpperCase());
            case Admin admin -> new AdminDTO(admin.getClass().getSimpleName().toUpperCase());
            default ->
                    throw new IllegalArgumentException("Unexpected userlevel: " + userLevel.getClass().getSimpleName());
        };
    }
}
