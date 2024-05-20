package pl.lodz.p.it.ssbd2024.ssbd03.unit.dto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.authentication.AccountLoginDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
public class AccountLoginDTOTest {

    private String loginNo1;
    private String passwordNo1;
    private String languageNo1;

    private AccountLoginDTO accountLoginDTONo1;

    @BeforeEach
    public void init() {
        loginNo1 = "exampleLoginNo1";
        passwordNo1 = "examplePasswordNo1";
        languageNo1 = "exampleLanguageNo1";

        accountLoginDTONo1 = new AccountLoginDTO(loginNo1, passwordNo1, languageNo1);
    }

    @Test
    public void accountLoginDTONoArgsConstructorTestPositive() {
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO();
        assertNotNull(accountLoginDTO);
    }

    @Test
    public void accountLoginDTOAllArgsConstructorAndGettersTestPositive() {
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO(loginNo1, passwordNo1, languageNo1);
        assertNotNull(accountLoginDTO);
        assertEquals(loginNo1, accountLoginDTO.getLogin());
        assertEquals(passwordNo1, accountLoginDTO.getPassword());
        assertEquals(languageNo1, accountLoginDTO.getLanguage());
    }

    @Test
    public void accountLoginDTOUserLoginSetterTestPositive() {
        String userLoginBefore = accountLoginDTONo1.getLogin();
        assertNotNull(userLoginBefore);

        String newUserLogin = "NewUserLoginNo1";
        accountLoginDTONo1.setLogin(newUserLogin);

        String userLoginAfter = accountLoginDTONo1.getLogin();
        assertNotNull(userLoginAfter);

        assertNotEquals(userLoginBefore, userLoginAfter);
        assertNotEquals(userLoginBefore, newUserLogin);
        assertEquals(newUserLogin, userLoginAfter);
    }

    @Test
    public void accountLoginDTOPasswordSetterTestPositive() {
        String passwordBefore = accountLoginDTONo1.getPassword();
        assertNotNull(passwordBefore);

        String newPassword = "NewPasswordNo1";
        accountLoginDTONo1.setPassword(newPassword);

        String passwordAfter = accountLoginDTONo1.getPassword();
        assertNotNull(passwordAfter);

        assertNotEquals(passwordBefore, passwordAfter);
        assertNotEquals(passwordBefore, newPassword);
        assertEquals(newPassword, passwordAfter);
    }

    @Test
    public void accountLoginDTOLanguageSetterTestPositive() {
        String languageBefore = accountLoginDTONo1.getLanguage();
        assertNotNull(languageBefore);

        String newLanguage = "NewLanguageNo1";
        accountLoginDTONo1.setLanguage(newLanguage);

        String languageAfter = accountLoginDTONo1.getLanguage();
        assertNotNull(languageAfter);

        assertNotEquals(languageBefore, languageAfter);
        assertNotEquals(languageBefore, newLanguage);
        assertEquals(newLanguage, languageAfter);
    }

    @Test
    public void accountLoginDTOToStringTestPositive() {
        String result = accountLoginDTONo1.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
        log.debug("Test result: {}", result);
    }
}
