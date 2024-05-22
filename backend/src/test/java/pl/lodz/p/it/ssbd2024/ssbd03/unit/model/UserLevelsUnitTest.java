package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Admin;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Staff;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;

public class UserLevelsUnitTest {

    @Test
    void clientConstructorReturnsNotNull() {
        UserLevel userLevel = new Client(1L);

        Assertions.assertNotNull(userLevel);
    }

    @Test
    void staffConstructorReturnsNotNull() {
        UserLevel userLevel = new Staff(1L);

        Assertions.assertNotNull(userLevel);
    }

    @Test
    void adminConstructorReturnsNotNull() {
        UserLevel userLevel = new Admin(1L);

        Assertions.assertNotNull(userLevel);
    }
}
