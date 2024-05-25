package pl.lodz.p.it.ssbd2024.ssbd03.unit.dto.authentication;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.authentication.AuthenticationCodeDTO;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class AuthenticationCodeDTOTest {

    private String userLoginNo1;
    private String authCodeValueNo1;
    private String languageNo1;

    private AuthenticationCodeDTO authenticationCodeDTONo1;

    @BeforeEach
    public void init() {
        userLoginNo1 = "ExampleLoginNo1";
        authCodeValueNo1 = "ExampleAuthCodeValueNo1";
        languageNo1 = "ExampleLanguageNo1";

        authenticationCodeDTONo1 = new AuthenticationCodeDTO(userLoginNo1, authCodeValueNo1, languageNo1);
    }

    @Test
    public void authenticationCodeDTONoArgsConstructorTestPositive() {
        AuthenticationCodeDTO authenticationCodeDTO = new AuthenticationCodeDTO();
        assertNotNull(authenticationCodeDTO);
    }

    @Test
    public void authenticationCodeDTOAllArgsConstructorAndGettersTestPositive() {
        AuthenticationCodeDTO authenticationCodeDTO = new AuthenticationCodeDTO(userLoginNo1, authCodeValueNo1, languageNo1);

        assertNotNull(authenticationCodeDTO);
        assertEquals(userLoginNo1, authenticationCodeDTO.getUserLogin());
        assertEquals(authCodeValueNo1, authenticationCodeDTO.getAuthCodeValue());
        assertEquals(languageNo1, authenticationCodeDTO.getLanguage());
    }

    @Test
    public void authenticationCodeDTOUserLoginSetterTestPositive() {
        String userLoginBefore = authenticationCodeDTONo1.getUserLogin();
        assertNotNull(userLoginBefore);

        String newUserLogin = "NewUserLoginNo1";
        authenticationCodeDTONo1.setUserLogin(newUserLogin);

        String userLoginAfter = authenticationCodeDTONo1.getUserLogin();
        assertNotNull(userLoginAfter);

        assertNotEquals(userLoginBefore, userLoginAfter);
        assertNotEquals(userLoginBefore, newUserLogin);
        assertEquals(newUserLogin, userLoginAfter);
    }

    @Test
    public void authenticationCodeDTOAuthCodeValueSetterTestPositive() {
        String authCodeValueBefore = authenticationCodeDTONo1.getAuthCodeValue();
        assertNotNull(authCodeValueBefore);

        String newAuthCodeValue = "NewAuthCodeValueNo1";
        authenticationCodeDTONo1.setAuthCodeValue(newAuthCodeValue);

        String newAuthCodeValueAfter = authenticationCodeDTONo1.getAuthCodeValue();
        assertNotNull(newAuthCodeValueAfter);

        assertNotEquals(authCodeValueBefore, newAuthCodeValueAfter);
        assertNotEquals(authCodeValueBefore, newAuthCodeValue);
        assertEquals(newAuthCodeValue, newAuthCodeValueAfter);
    }

    @Test
    public void authenticationCodeDTOLanguageSetterTestPositive() {
        String languageBefore = authenticationCodeDTONo1.getLanguage();
        assertNotNull(languageBefore);

        String newLanguage = "NewLanguageNo1";
        authenticationCodeDTONo1.setLanguage(newLanguage);

        String languageAfter = authenticationCodeDTONo1.getLanguage();
        assertNotNull(languageAfter);

        assertNotEquals(languageBefore, languageAfter);
        assertNotEquals(languageBefore, newLanguage);
        assertEquals(newLanguage, languageAfter);
    }

    @Test
    public void authenticationCodeDTOToStringTestPositive() {
        String result = authenticationCodeDTONo1.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
        log.debug("Test result: {}", result);
    }
}
