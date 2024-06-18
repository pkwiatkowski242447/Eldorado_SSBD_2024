package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Admin;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Staff;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AbstractEntityUnitTest {

    private static AbstractEntity client;
    private static AbstractEntity staff;
    private static AbstractEntity admin;
    private static AbstractEntity abstractEntity;

    @BeforeAll
    public static void setUp() throws Exception {
        client = new Client();
        staff = new Staff();
        admin = new Admin();
        abstractEntity = new Client(20L);

        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(client, UUID.randomUUID());
        id.set(staff, UUID.randomUUID());
        id.set(admin, UUID.randomUUID());
        id.setAccessible(false);

        Field version = AbstractEntity.class.getDeclaredField("version");
        version.setAccessible(true);
        version.set(client, 1L);
        version.set(staff, 2L);
        version.set(admin, 3L);
        version.setAccessible(false);
    }

    @Test
    public void abstractEntitySetIdTestPositive() throws Exception {
        UUID idBefore = client.getId();
        UUID newId = UUID.randomUUID();

        assertNotNull(idBefore);
        assertNotNull(newId);

        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(client, newId);
        id.setAccessible(false);

        UUID idAfter = client.getId();

        assertNotNull(idAfter);

        assertNotEquals(idBefore, newId);
        assertNotEquals(idBefore, idAfter);
        assertEquals(idAfter, newId);
    }

    @Test
    public void abstractEntitySetVersionTestPositive() throws Exception {
        Long versionBefore = client.getVersion();
        Long newVersion = 10L;

        assertNotNull(versionBefore);
        assertNotNull(newVersion);

        Field version = AbstractEntity.class.getDeclaredField("version");
        version.setAccessible(true);
        version.set(client, newVersion);
        version.setAccessible(false);

        Long versionAfter = client.getVersion();

        assertNotNull(versionAfter);

        assertNotEquals(versionBefore, newVersion);
        assertNotEquals(versionBefore, versionAfter);
        assertEquals(versionAfter, newVersion);
    }

    @Test
    public void abstractEntityConstructorWithVersionTestPositive() {
        assertNotNull(abstractEntity);
        assertEquals(abstractEntity.getVersion(), 20L);
    }

    @Test
    public void abstractEntityToStringTestPositive() throws Exception {
        String result = client.toString();
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
        assertTrue(result.contains("AbstractEntity"));
    }
}
