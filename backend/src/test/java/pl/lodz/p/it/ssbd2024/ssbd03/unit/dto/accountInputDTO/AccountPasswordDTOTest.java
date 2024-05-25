package pl.lodz.p.it.ssbd2024.ssbd03.unit.dto.accountInputDTO;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO.AccountPasswordDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
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
    public void accountPasswordDTOPasswordSetterTestPositive() {
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

    @Test
    public void accountPasswordDTOToStringTestPositive() {
        String result = accountPasswordDTONo1.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
        log.debug("Test result: {}", result);
    }
}
