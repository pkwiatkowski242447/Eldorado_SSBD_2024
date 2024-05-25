package pl.lodz.p.it.ssbd2024.ssbd03.unit.dto.userlevel;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.userlevel.AdminDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.userlevel.ClientDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.userlevel.StaffDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.userlevel.UserLevelDTO;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserLevelDTOTest {

    private UUID id;
    private Long version;

    @BeforeEach
    public void init() {
        id = UUID.randomUUID();
        version = 1L;
    }

    /*                 AdminDTO                 */
    @Test
    public void adminDTONoArgsConstructorTestPositive() {
        AdminDTO adminDTO = new AdminDTO();
        assertNotNull(adminDTO);

        assertEquals("ADMIN", adminDTO.getRoleName());
    }

    @Test
    public void adminDTOAllArgsConstructorTestPositive() {

        AdminDTO adminDTO = new AdminDTO(id, version);
        assertNotNull(adminDTO);

        assertEquals(id, adminDTO.getId());
        assertEquals(version, adminDTO.getVersion());
        assertEquals("ADMIN", adminDTO.getRoleName());
    }

    @Test
    public void adminDTOSettersTestPositive() {
        AdminDTO adminDTO = new AdminDTO();

        // Check value before using setters
        assertNull(adminDTO.getId());
        assertNull(adminDTO.getVersion());
        assertEquals("ADMIN", adminDTO.getRoleName());

        adminDTO.setId(id);
        adminDTO.setVersion(version);
        adminDTO.setRoleName("CHARLIE");

        // Check value after using setters
        assertEquals(id, adminDTO.getId());
        assertEquals(version, adminDTO.getVersion());
        assertEquals("CHARLIE", adminDTO.getRoleName());
    }

    @Test
    public void adminDTOToStringPositive() {
        AdminDTO adminDTO = new AdminDTO(id, version);

        String value = new ToStringBuilder(adminDTO)
                .append("Id: ", adminDTO.getId())
                .append("Version: ", adminDTO.getVersion())
                .toString();

        assertEquals(value, adminDTO.toString());
    }

    /*                 StaffDTO                 */

    @Test
    public void staffDTONoArgsConstructorTestPositive() {
        StaffDTO staffDTO = new StaffDTO();
        assertNotNull(staffDTO);

        assertEquals("STAFF", staffDTO.getRoleName());
    }

    @Test
    public void staffDTOAllArgsConstructorTestPositive() {

        StaffDTO staffDTO = new StaffDTO(id, version);
        assertNotNull(staffDTO);

        assertEquals(id, staffDTO.getId());
        assertEquals(version, staffDTO.getVersion());
        assertEquals("STAFF", staffDTO.getRoleName());
    }

    @Test
    public void staffDTOSettersTestPositive() {
        StaffDTO staffDTO = new StaffDTO();

        // Check value before using setters
        assertNull(staffDTO.getId());
        assertNull(staffDTO.getVersion());
        assertEquals("STAFF", staffDTO.getRoleName());

        staffDTO.setId(id);
        staffDTO.setVersion(version);
        staffDTO.setRoleName("BETA");

        // Check value after using setters
        assertEquals(id, staffDTO.getId());
        assertEquals(version, staffDTO.getVersion());
        assertEquals("BETA", staffDTO.getRoleName());
    }

    @Test
    public void staffDTOToStringPositive() {
        StaffDTO staffDTO = new StaffDTO(id, version);

        String value = new ToStringBuilder(staffDTO)
                .append("Id: ", staffDTO.getId())
                .append("Version: ", staffDTO.getVersion())
                .toString();

        assertEquals(value, staffDTO.toString());
    }

    /*                 ClientDTO                 */

    @Test
    public void ClientDTONoArgsConstructorTestPositive() {
        ClientDTO clientDTO = new ClientDTO();
        assertNotNull(clientDTO);

        assertEquals("CLIENT", clientDTO.getRoleName());
    }

    @Test
    public void clientDTOAllArgsConstructorTestPositive() {

        ClientDTO clientDTO = new ClientDTO(id, version, "BASIC");
        assertNotNull(clientDTO);

        assertEquals(id, clientDTO.getId());
        assertEquals(version, clientDTO.getVersion());
        assertEquals("CLIENT", clientDTO.getRoleName());
        assertEquals("BASIC", clientDTO.getClientType());
    }

    @Test
    public void clientDTOSettersTestPositive() {
        ClientDTO clientDTO = new ClientDTO();

        // Check value before using setters
        assertNull(clientDTO.getId());
        assertNull(clientDTO.getVersion());
        assertEquals("CLIENT", clientDTO.getRoleName());
        assertNull(clientDTO.getClientType());

        clientDTO.setId(id);
        clientDTO.setVersion(version);
        clientDTO.setRoleName("ALFA");
        clientDTO.setClientType("PREMIUM");

        // Check value after using setters
        assertEquals(id, clientDTO.getId());
        assertEquals(version, clientDTO.getVersion());
        assertEquals("ALFA", clientDTO.getRoleName());
        assertEquals("PREMIUM", clientDTO.getClientType());
    }

    @Test
    public void clientDTOToStringPositive() {
        ClientDTO clientDTO = new ClientDTO(id, version, "BASIC");

        String value = new ToStringBuilder(clientDTO)
                .append("Id: ", clientDTO.getId())
                .append("Version: ", clientDTO.getVersion())
                .toString();

        assertEquals(value, clientDTO.toString());
    }

    /*                 CustomUserLevelDTO                 */

    @Test
    public void customUserLevelDTONoArgsConstructorTestPositive() {
        class ManagerDTO extends UserLevelDTO {
        }

        ManagerDTO managerDTO = new ManagerDTO();
        assertNotNull(managerDTO);
        assertEquals("MANAGER", managerDTO.getRoleName());
    }
}
