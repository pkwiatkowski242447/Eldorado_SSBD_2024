package pl.lodz.p.it.ssbd2024.ssbd03.unit.dto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountEmailDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
public class AccountEmailDTOTest {

    private String emailNo1;

    private AccountEmailDTO accountEmailDTONo1;

    @BeforeEach
    public void init() {
        emailNo1 = "exampleEmailNo1";
        accountEmailDTONo1 = new AccountEmailDTO(emailNo1);
    }

    @Test
    public void accountEmailDTONoArgsConstructorTestPositive() {
        AccountEmailDTO accountEmailDTO = new AccountEmailDTO();
        assertNotNull(accountEmailDTO);
    }

    @Test
    public void accountEmailDTOAllArgsConstructorAndGetterTestPositive() {
        AccountEmailDTO accountEmailDTO = new AccountEmailDTO(emailNo1);
        assertNotNull(accountEmailDTO);
        assertEquals(emailNo1, accountEmailDTO.getEmail());
    }

    @Test
    public void accountEmailDTOEmailSetterTestPositive() {
        String emailBefore = accountEmailDTONo1.getEmail();
        assertNotNull(emailBefore);

        String newEmail = "NewEmailNo1";
        accountEmailDTONo1.setEmail(newEmail);

        String emailAfter = accountEmailDTONo1.getEmail();
        assertNotNull(emailAfter);

        assertNotEquals(emailBefore, emailAfter);
        assertNotEquals(emailBefore, newEmail);
        assertEquals(newEmail, emailAfter);
    }

    @Test
    public void accountEmailDTOToStringTestPositive() {
        String result = accountEmailDTONo1.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
        log.debug("Test result: {}", result);
    }
}
