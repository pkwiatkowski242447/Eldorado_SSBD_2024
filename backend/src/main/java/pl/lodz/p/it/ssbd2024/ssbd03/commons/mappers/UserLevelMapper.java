package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AdminDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.ClientDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.StaffDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.UserLevelDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Admin;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Staff;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mapper.MapperUnexpectedClientTypeException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mapper.MapperUnexpectedUserLevelException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

//TODO javadoc -> Michal

public class UserLevelMapper {
    public static UserLevel toUserLevel(UserLevelDTO userLevelDTO)
            throws MapperUnexpectedUserLevelException, MapperUnexpectedClientTypeException {
        return switch (userLevelDTO) {
            case ClientDTO clientDTO -> {
                Client client = new Client();
                client.setType(switch (clientDTO.getClientType().toUpperCase()) {
                    case "BASIC" -> Client.ClientType.BASIC;
                    case "STANDARD" -> Client.ClientType.STANDARD;
                    case "PREMIUM" -> Client.ClientType.PREMIUM;
                    default -> throw new MapperUnexpectedClientTypeException(I18n.UNEXPECTED_CLIENT_TYPE);
                });
                yield client;
            }
            case StaffDTO staffDTO -> new Staff();
            case AdminDTO adminDTO -> new Admin();
            default -> throw new MapperUnexpectedUserLevelException(I18n.UNEXPECTED_USER_LEVEL);
        };
    }

    public static UserLevelDTO toUserLevelDTO(UserLevel userLevel) throws MapperUnexpectedUserLevelException {
        return switch (userLevel) {
            case Client client -> new ClientDTO(userLevel.getId(), client.getVersion(), client.getType().toString());
            case Staff staff -> new StaffDTO(userLevel.getId(), staff.getVersion());
            case Admin admin -> new AdminDTO(userLevel.getId().toString(), admin.getVersion());
            default -> throw new MapperUnexpectedUserLevelException(I18n.UNEXPECTED_USER_LEVEL);
        };
    }
}
