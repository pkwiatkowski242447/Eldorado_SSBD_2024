package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel.AdminDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel.ClientDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel.StaffDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel.UserLevelDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Admin;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Staff;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mapper.MapperUnexpectedClientTypeException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mapper.MapperUnexpectedUserLevelException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to handle UserLevel entity-DTO mapping.
 */
public class UserLevelMapper {

    /**
     * This method is used to map UserLevel DTO to UserLevel entity class.
     * @param userLevelDTO UserLevel DTO to map.
     * @return Returns mapped UserLevel entity class.
     * @throws MapperUnexpectedUserLevelException Threw when unexpected UserLevel DTO occurred.
     * @throws MapperUnexpectedClientTypeException Threw when Client user level DTO contains invalid client type.
     */
    public static UserLevel toUserLevel(UserLevelDTO userLevelDTO)
            throws MapperUnexpectedUserLevelException, MapperUnexpectedClientTypeException {
        return switch (userLevelDTO) {
            case ClientDTO clientDTO -> {
                Client client = new Client(clientDTO.getVersion());
                client.setType(switch (clientDTO.getClientType().toUpperCase()) {
                    case "BASIC" -> Client.ClientType.BASIC;
                    case "STANDARD" -> Client.ClientType.STANDARD;
                    case "PREMIUM" -> Client.ClientType.PREMIUM;
                    default -> throw new MapperUnexpectedClientTypeException(I18n.UNEXPECTED_CLIENT_TYPE);
                });
                yield client;
            }
            case StaffDTO staffDTO -> new Staff(staffDTO.getVersion());
            case AdminDTO adminDTO -> new Admin(adminDTO.getVersion());
            default -> throw new MapperUnexpectedUserLevelException(I18n.NO_SUCH_USER_LEVEL_EXCEPTION);
        };
    }

    /**
     * This method is used to map UserLevel entity to UserLevel DTO class.
     * @param userLevel UserLevel to map.
     * @return Returns mapped UserLevel DTO class.
     * @throws MapperUnexpectedUserLevelException Threw when unhandled UserLevel entity occurred.
     */
    public static UserLevelDTO toUserLevelDTO(UserLevel userLevel) throws MapperUnexpectedUserLevelException {
        return switch (userLevel) {
            case Client client -> new ClientDTO(userLevel.getId(), client.getVersion(), client.getType().toString());
            case Staff staff -> new StaffDTO(userLevel.getId(), staff.getVersion());
            case Admin admin -> new AdminDTO(userLevel.getId().toString(), admin.getVersion());
            default -> throw new MapperUnexpectedUserLevelException(I18n.NO_SUCH_USER_LEVEL_EXCEPTION);
        };
    }
}
