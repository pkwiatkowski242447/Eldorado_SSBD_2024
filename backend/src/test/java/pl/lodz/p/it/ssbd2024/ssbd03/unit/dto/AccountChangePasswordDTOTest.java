package pl.lodz.p.it.ssbd2024.ssbd03.unit.dto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountChangePasswordDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
public class AccountChangePasswordDTOTest {

    private String oldPasswordNo1;
    private String newPasswordNo1;

    private AccountChangePasswordDTO accountChangePasswordDTONo1;

    @BeforeEach
    public void init() {
        oldPasswordNo1 = "exampleOldPasswordNo1";
        newPasswordNo1 = "exampleNewPasswordNo1";
        accountChangePasswordDTONo1 = new AccountChangePasswordDTO(oldPasswordNo1, newPasswordNo1);
    }

    @Test
    public void accountChangePasswordDTONoArgsConstructorTestPositive() {
        AccountChangePasswordDTO accountChangePasswordDTO = new AccountChangePasswordDTO();
        assertNotNull(accountChangePasswordDTO);
    }

    @Test
    public void accountChangePasswordDTOAllArgsConstructorAndGettersTestPositive() {
        AccountChangePasswordDTO accountChangePasswordDTO = new AccountChangePasswordDTO(oldPasswordNo1, newPasswordNo1);
        assertNotNull(accountChangePasswordDTO);
        assertEquals(oldPasswordNo1, accountChangePasswordDTO.getOldPassword());
        assertEquals(newPasswordNo1, accountChangePasswordDTO.getNewPassword());
    }

    @Test
    public void authenticationCodeDTOOldPasswordSetterTestPositive() {
        String oldPasswordBefore = accountChangePasswordDTONo1.getOldPassword();
        assertNotNull(oldPasswordBefore);

        String newOldPassword = "NewOldPasswordNo1";
        accountChangePasswordDTONo1.setOldPassword(newOldPassword);

        String oldPasswordAfter = accountChangePasswordDTONo1.getOldPassword();
        assertNotNull(oldPasswordAfter);

        assertNotEquals(oldPasswordBefore, oldPasswordAfter);
        assertNotEquals(oldPasswordBefore, newOldPassword);
        assertEquals(newOldPassword, oldPasswordAfter);
    }

    @Test
    public void authenticationCodeDTONewPasswordSetterTestPositive() {
        String newPasswordBefore = accountChangePasswordDTONo1.getNewPassword();
        assertNotNull(newPasswordBefore);

        String newNewPassword = "NewNewPasswordNo1";
        accountChangePasswordDTONo1.setNewPassword(newNewPassword);

        String newPasswordAfter = accountChangePasswordDTONo1.getNewPassword();
        assertNotNull(newPasswordAfter);

        assertNotEquals(newPasswordBefore, newPasswordAfter);
        assertNotEquals(newPasswordBefore, newNewPassword);
        assertEquals(newNewPassword, newPasswordAfter);
    }

    @Test
    public void accountChangePasswordDTOToStringTestPositive() {
        String result = accountChangePasswordDTONo1.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
        log.debug("Test result: {}", result);
    }
}
