package pl.lodz.p.it.ssbd2024.ssbd03.unit.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountPasswordDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AccountPasswordDTOTest {

    private String passwordNo1;

    private AccountPasswordDTO accountPasswordDTONo1;

    @BeforeEach
    public void init() {
        passwordNo1 = "ExamplePasswordNo1";
        accountPasswordDTONo1 = new AccountPasswordDTO(passwordNo1);
    }

    @Test
    public void accountPasswordDTONoArgsConstructorTestPositive() {
        AccountPasswordDTO accountPasswordDTO = new AccountPasswordDTO();
        assertNotNull(accountPasswordDTO);
    }

    @Test
    public void accountPasswordDTOAllArgsConstructorAndGettersTestPositive() {
        AccountPasswordDTO accountPasswordDTO = new AccountPasswordDTO(passwordNo1);
        assertNotNull(accountPasswordDTO);
        assertEquals(passwordNo1, accountPasswordDTO.getPassword());
    }

    @Test
    public void authenticationCodeDTOPasswordSetterTestPositive() {
        String passwordBefore = accountPasswordDTONo1.getPassword();
        assertNotNull(passwordBefore);

        String newPassword = "NewPasswordNo1";
        accountPasswordDTONo1.setPassword(newPassword);

        String passwordAfter = accountPasswordDTONo1.getPassword();
        assertNotNull(passwordAfter);

        assertNotEquals(passwordBefore, passwordAfter);
        assertNotEquals(passwordBefore, newPassword);
        assertEquals(newPassword, passwordAfter);
    }
}
