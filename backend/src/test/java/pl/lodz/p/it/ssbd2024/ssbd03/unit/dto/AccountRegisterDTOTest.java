package pl.lodz.p.it.ssbd2024.ssbd03.unit.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountRegisterDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AccountRegisterDTOTest {

    private String loginNo1;
    private String passwordNo1;
    private String firstNameNo1;
    private String lastNameNo1;
    private String emailNo1;
    private String phoneNumberNo1;
    private String languageNo1;

    private AccountRegisterDTO accountRegisterDTONo1;

    @BeforeEach
    public void init() {
        loginNo1 = "exampleLogin1";
        passwordNo1 = "examplePassword1";
        firstNameNo1 = "exampleFirstName1";
        lastNameNo1 = "exampleLastName1";
        emailNo1 = "email1@example.com";
        phoneNumberNo1 = "examplePhoneNumber1";
        languageNo1 = "pl";

        accountRegisterDTONo1 = new AccountRegisterDTO(loginNo1, passwordNo1, firstNameNo1, lastNameNo1, emailNo1, phoneNumberNo1, languageNo1);
    }

    @Test
    public void accountRegisterDTONoArgsConstructorTestPositive() {
        AccountRegisterDTO accountRegisterDTO = new AccountRegisterDTO();
        assertNotNull(accountRegisterDTO);
    }

    @Test
    public void accountRegisterDTOAllArgsConstructorAndGettersTestPositive() {
        AccountRegisterDTO accountRegisterDTO = new AccountRegisterDTO(loginNo1, passwordNo1, firstNameNo1, lastNameNo1, emailNo1, phoneNumberNo1, languageNo1);

        assertNotNull(accountRegisterDTO);
        assertEquals(loginNo1, accountRegisterDTO.getLogin());
        assertEquals(passwordNo1, accountRegisterDTO.getPassword());
        assertEquals(firstNameNo1, accountRegisterDTO.getFirstName());
        assertEquals(lastNameNo1, accountRegisterDTO.getLastName());
        assertEquals(emailNo1, accountRegisterDTO.getEmail());
        assertEquals(phoneNumberNo1, accountRegisterDTO.getPhoneNumber());
        assertEquals(languageNo1, accountRegisterDTO.getLanguage());
    }

    @Test
    public void authenticationCodeDTOUserLoginSetterTestPositive() {
        String userLoginBefore = accountRegisterDTONo1.getLogin();
        assertNotNull(userLoginBefore);

        String newUserLogin = "NewUserLoginNo1";
        accountRegisterDTONo1.setLogin(newUserLogin);

        String userLoginAfter = accountRegisterDTONo1.getLogin();
        assertNotNull(userLoginAfter);

        assertNotEquals(userLoginBefore, userLoginAfter);
        assertNotEquals(userLoginBefore, newUserLogin);
        assertEquals(newUserLogin, userLoginAfter);
    }

    @Test
    public void authenticationCodeDTOPasswordSetterTestPositive() {
        String passwordBefore = accountRegisterDTONo1.getPassword();
        assertNotNull(passwordBefore);

        String newPassword = "NewPasswordNo1";
        accountRegisterDTONo1.setPassword(newPassword);

        String passwordAfter = accountRegisterDTONo1.getPassword();
        assertNotNull(passwordAfter);

        assertNotEquals(passwordBefore, passwordAfter);
        assertNotEquals(passwordBefore, newPassword);
        assertEquals(newPassword, passwordAfter);
    }

    @Test
    public void authenticationCodeDTOFirstNameSetterTestPositive() {
        String firstNameBefore = accountRegisterDTONo1.getFirstName();
        assertNotNull(firstNameBefore);

        String newFirstName = "NewFirstNameNo1";
        accountRegisterDTONo1.setFirstName(newFirstName);

        String firstNameAfter = accountRegisterDTONo1.getFirstName();
        assertNotNull(firstNameAfter);

        assertNotEquals(firstNameBefore, firstNameAfter);
        assertNotEquals(firstNameBefore, newFirstName);
        assertEquals(newFirstName, firstNameAfter);
    }

    @Test
    public void authenticationCodeDTOLastNameSetterTestPositive() {
        String lastNameBefore = accountRegisterDTONo1.getLastName();
        assertNotNull(lastNameBefore);

        String newLastName = "NewLastNameNo1";
        accountRegisterDTONo1.setLastName(newLastName);

        String lastNameAfter = accountRegisterDTONo1.getLastName();
        assertNotNull(lastNameAfter);

        assertNotEquals(lastNameBefore, lastNameAfter);
        assertNotEquals(lastNameBefore, newLastName);
        assertEquals(newLastName, lastNameAfter);
    }

    @Test
    public void authenticationCodeDTOEmailSetterTestPositive() {
        String emailBefore = accountRegisterDTONo1.getEmail();
        assertNotNull(emailBefore);

        String newEmail = "NewEmailNo1";
        accountRegisterDTONo1.setEmail(newEmail);

        String emailAfter = accountRegisterDTONo1.getEmail();
        assertNotNull(emailAfter);

        assertNotEquals(emailBefore, emailAfter);
        assertNotEquals(emailBefore, newEmail);
        assertEquals(newEmail, emailAfter);
    }

    @Test
    public void authenticationCodeDTOPhoneNumberSetterTestPositive() {
        String phoneNumberBefore = accountRegisterDTONo1.getPhoneNumber();
        assertNotNull(phoneNumberBefore);

        String newPhoneNumber = "NewEmailNo1";
        accountRegisterDTONo1.setPhoneNumber(newPhoneNumber);

        String phoneNumberAfter = accountRegisterDTONo1.getPhoneNumber();
        assertNotNull(phoneNumberAfter);

        assertNotEquals(phoneNumberBefore, phoneNumberAfter);
        assertNotEquals(phoneNumberBefore, newPhoneNumber);
        assertEquals(newPhoneNumber, phoneNumberAfter);
    }

    @Test
    public void authenticationCodeDTOLanguageSetterTestPositive() {
        String languageBefore = accountRegisterDTONo1.getLanguage();
        assertNotNull(languageBefore);

        String newLanguage = "NewLanguageNo1";
        accountRegisterDTONo1.setLanguage(newLanguage);

        String languageAfter = accountRegisterDTONo1.getLanguage();
        assertNotNull(languageAfter);

        assertNotEquals(languageBefore, languageAfter);
        assertNotEquals(languageBefore, newLanguage);
        assertEquals(newLanguage, languageAfter);
    }
}
