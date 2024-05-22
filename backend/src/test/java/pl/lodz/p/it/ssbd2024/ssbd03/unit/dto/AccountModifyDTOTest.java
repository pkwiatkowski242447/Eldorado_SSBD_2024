package pl.lodz.p.it.ssbd2024.ssbd03.unit.dto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountInputDTO.AccountModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel.AdminDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel.ClientDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel.StaffDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel.UserLevelDTO;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
public class AccountModifyDTOTest {

    private final String login = "exampleLoginNo1";
    private final Long version = 10L;
    private final Set<UserLevelDTO> listOfUserLevelDTO = new HashSet<>();

    private String nameNo1;
    private String lastnameNo1;
    private String phoneNumberNo1;
    private boolean twoFactorAuthNo1;

    private AccountModifyDTO accountModifyDTONo1;

    @BeforeEach
    public void init() {
        listOfUserLevelDTO.add(new ClientDTO());
        listOfUserLevelDTO.add(new StaffDTO());
        listOfUserLevelDTO.add(new AdminDTO());

        nameNo1 = "exampleNameNo1";
        lastnameNo1 = "exampleLastnameNo1";
        phoneNumberNo1 = "examplePhoneNumberNo1";
        boolean twoFactorAuthNo1 = false;

        accountModifyDTONo1 = new AccountModifyDTO(login, version, listOfUserLevelDTO, nameNo1, lastnameNo1, phoneNumberNo1, twoFactorAuthNo1);
    }

    @Test
    public void accountModifyDTONoArgsConstructorTestPositive() {
        AccountModifyDTO accountModifyDTO = new AccountModifyDTO();
        assertNotNull(accountModifyDTO);
    }

    @Test
    public void accountModifyDTOAllArgsConstructorAndGettersTestPositive() {
        AccountModifyDTO accountModifyDTO = new AccountModifyDTO(login, version, listOfUserLevelDTO, nameNo1, lastnameNo1, phoneNumberNo1, twoFactorAuthNo1);
        assertNotNull(accountModifyDTO);
        assertEquals(login, accountModifyDTO.getLogin());
        assertEquals(version, accountModifyDTO.getVersion());
        assertEquals(listOfUserLevelDTO, accountModifyDTO.getUserLevelsDto());
        assertEquals(nameNo1, accountModifyDTO.getName());
        assertEquals(lastnameNo1, accountModifyDTO.getLastname());
        assertEquals(phoneNumberNo1, accountModifyDTO.getPhoneNumber());
        assertEquals(twoFactorAuthNo1, accountModifyDTO.getTwoFactorAuth());
    }

    @Test
    public void accountModifyDTONameSetterTestPositive() {
        String nameBefore = accountModifyDTONo1.getName();
        assertNotNull(nameBefore);

        String newName = "NewNameNo1";
        accountModifyDTONo1.setName(newName);

        String nameAfter = accountModifyDTONo1.getName();
        assertNotNull(nameAfter);

        assertNotEquals(nameBefore, nameAfter);
        assertNotEquals(nameBefore, newName);
        assertEquals(newName, nameAfter);
    }

    @Test
    public void accountModifyDTOLastNameSetterTestPositive() {
        String lastNameBefore = accountModifyDTONo1.getLastname();
        assertNotNull(lastNameBefore);

        String newLastName = "NewLastNameNo1";
        accountModifyDTONo1.setLastname(newLastName);

        String lastNameAfter = accountModifyDTONo1.getLastname();
        assertNotNull(lastNameAfter);

        assertNotEquals(lastNameBefore, lastNameAfter);
        assertNotEquals(lastNameBefore, newLastName);
        assertEquals(newLastName, lastNameAfter);
    }

    @Test
    public void accountModifyDTOPhoneNumberSetterTestPositive() {
        String phoneNumberBefore = accountModifyDTONo1.getPhoneNumber();
        assertNotNull(phoneNumberBefore);

        String newPhoneNumber = "NewPhoneNumberNo1";
        accountModifyDTONo1.setPhoneNumber(newPhoneNumber);

        String phoneNumberAfter = accountModifyDTONo1.getPhoneNumber();
        assertNotNull(phoneNumberAfter);

        assertNotEquals(phoneNumberBefore, phoneNumberAfter);
        assertNotEquals(phoneNumberBefore, newPhoneNumber);
        assertEquals(newPhoneNumber, phoneNumberAfter);
    }

    @Test
    public void accountModifyDTOTwoFactorAuthSetterTestPositive() {
        boolean twoFactorAuthBefore = accountModifyDTONo1.getTwoFactorAuth();

        boolean newTwoFactorAuth = !twoFactorAuthBefore;
        accountModifyDTONo1.setTwoFactorAuth(newTwoFactorAuth);

        boolean twoFactorAuthAfter = accountModifyDTONo1.getTwoFactorAuth();

        assertNotEquals(twoFactorAuthBefore, twoFactorAuthAfter);
        assertNotEquals(twoFactorAuthBefore, newTwoFactorAuth);
        assertEquals(newTwoFactorAuth, twoFactorAuthAfter);
    }

    @Test
    public void accountModifyDTOToStringTestPositive() {
        String result = accountModifyDTONo1.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
        log.debug("Test result: {}", result);
    }
}
