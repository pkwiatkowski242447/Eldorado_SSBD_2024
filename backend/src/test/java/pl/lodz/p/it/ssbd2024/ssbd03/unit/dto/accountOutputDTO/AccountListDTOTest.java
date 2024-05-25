package pl.lodz.p.it.ssbd2024.ssbd03.unit.dto.accountOutputDTO;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountOutputDTO.AccountListDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
public class AccountListDTOTest {

    private UUID idNo1;
    private String loginNo1;
    private String nameNo1;
    private String lastNameNo1;
    private boolean activeNo1;
    private boolean blockedNo1;
    private boolean verifiedNo1;
    private LocalDateTime lastSuccessfulLoginTimeNo1;
    private LocalDateTime lastUnsuccessfulLoginTimeNo1;
    private List<String> userLevelsNo1;

    private AccountListDTO accountListDTONo1;

    @BeforeEach
    public void init() {
        idNo1 = UUID.randomUUID();
        loginNo1 = "exampleLoginNo1";
        nameNo1 = "exampleNameNo1";
        lastNameNo1 = "exampleLastNameNo1";
        activeNo1 = true;
        blockedNo1 = false;
        verifiedNo1 = true;
        lastSuccessfulLoginTimeNo1 = LocalDateTime.now();
        lastUnsuccessfulLoginTimeNo1 = LocalDateTime.now();
        userLevelsNo1 = List.of("ROLE_ADMIN", "ROLE_STAFF", "ROLE_CLIENT");

        accountListDTONo1 = new AccountListDTO(idNo1, loginNo1, nameNo1, lastNameNo1, activeNo1, blockedNo1, verifiedNo1, lastSuccessfulLoginTimeNo1, lastUnsuccessfulLoginTimeNo1, userLevelsNo1);
    }

    @Test
    public void accountListDTONoArgsConstructorTestPositive() {
        AccountListDTO accountListDTO = new AccountListDTO();
        assertNotNull(accountListDTO);
    }

    @Test
    public void accountListDTOAllArgsConstructorAndGettersTestPositive() {
        AccountListDTO accountListDTO = new AccountListDTO(idNo1, loginNo1, nameNo1, lastNameNo1, activeNo1, blockedNo1, verifiedNo1, lastSuccessfulLoginTimeNo1, lastUnsuccessfulLoginTimeNo1, userLevelsNo1);
        assertNotNull(accountListDTO);
        assertEquals(idNo1, accountListDTO.getId());
        assertEquals(loginNo1, accountListDTO.getLogin());
        assertEquals(nameNo1, accountListDTO.getName());
        assertEquals(lastNameNo1, accountListDTO.getLastName());
        assertEquals(activeNo1, accountListDTO.isActive());
        assertEquals(blockedNo1, accountListDTO.isBlocked());
        assertEquals(verifiedNo1, accountListDTO.isVerified());
        assertEquals(lastSuccessfulLoginTimeNo1, accountListDTO.getLastSuccessfulLoginTime());
        assertEquals(lastUnsuccessfulLoginTimeNo1, accountListDTO.getLastUnsuccessfulLoginTime());
        assertEquals(userLevelsNo1, accountListDTO.getUserLevels());
    }

    @Test
    public void accountListDTOIdSetterTestPositive() {
        UUID idBefore = accountListDTONo1.getId();
        assertNotNull(idBefore);

        UUID newId = UUID.randomUUID();
        accountListDTONo1.setId(newId);

        UUID idAfter = accountListDTONo1.getId();
        assertNotNull(idAfter);

        assertNotEquals(idBefore, idAfter);
        assertNotEquals(idBefore, newId);
        assertEquals(newId, idAfter);
    }

    @Test
    public void accountListDTOUserLoginSetterTestPositive() {
        String userLoginBefore = accountListDTONo1.getLogin();
        assertNotNull(userLoginBefore);

        String newUserLogin = "NewUserLoginNo1";
        accountListDTONo1.setLogin(newUserLogin);

        String userLoginAfter = accountListDTONo1.getLogin();
        assertNotNull(userLoginAfter);

        assertNotEquals(userLoginBefore, userLoginAfter);
        assertNotEquals(userLoginBefore, newUserLogin);
        assertEquals(newUserLogin, userLoginAfter);
    }

    @Test
    public void accountListDTOFirstNameSetterTestPositive() {
        String firstNameBefore = accountListDTONo1.getName();
        assertNotNull(firstNameBefore);

        String newFirstName = "NewFirstNameNo1";
        accountListDTONo1.setName(newFirstName);

        String firstNameAfter = accountListDTONo1.getName();
        assertNotNull(firstNameAfter);

        assertNotEquals(firstNameBefore, firstNameAfter);
        assertNotEquals(firstNameBefore, newFirstName);
        assertEquals(newFirstName, firstNameAfter);
    }

    @Test
    public void accountListDTOLastNameSetterTestPositive() {
        String lastNameBefore = accountListDTONo1.getLastName();
        assertNotNull(lastNameBefore);

        String newLastName = "NewLastNameNo1";
        accountListDTONo1.setLastName(newLastName);

        String lastNameAfter = accountListDTONo1.getLastName();
        assertNotNull(lastNameAfter);

        assertNotEquals(lastNameBefore, lastNameAfter);
        assertNotEquals(lastNameBefore, newLastName);
        assertEquals(newLastName, lastNameAfter);
    }

    @Test
    public void accountListDTOActiveSetterTestPositive() {
        boolean activeBefore = accountListDTONo1.isActive();

        boolean newActive = !activeBefore;
        accountListDTONo1.setActive(newActive);

        boolean activeAfter = accountListDTONo1.isActive();

        assertNotEquals(activeBefore, activeAfter);
        assertNotEquals(activeBefore, newActive);
        assertEquals(newActive, activeAfter);
    }

    @Test
    public void accountListDTOBlockedSetterTestPositive() {
        boolean blockedBefore = accountListDTONo1.isBlocked();

        boolean newBlocked = !blockedBefore;
        accountListDTONo1.setBlocked(newBlocked);

        boolean blockedAfter = accountListDTONo1.isBlocked();

        assertNotEquals(blockedBefore, blockedAfter);
        assertNotEquals(blockedBefore, newBlocked);
        assertEquals(newBlocked, blockedAfter);
    }

    @Test
    public void accountListDTOVerifiedSetterTestPositive() {
        boolean verifiedBefore = accountListDTONo1.isVerified();

        boolean newVerified = !verifiedBefore;
        accountListDTONo1.setVerified(newVerified);

        boolean verifiedAfter = accountListDTONo1.isVerified();

        assertNotEquals(verifiedBefore, verifiedAfter);
        assertNotEquals(verifiedBefore, newVerified);
        assertEquals(newVerified, verifiedAfter);
    }

    @Test
    public void accountListDTOLastSuccessfulLoginTimeSetterTestPositive() {
        LocalDateTime lastSuccessfulLoginTimeBefore = accountListDTONo1.getLastSuccessfulLoginTime();
        assertNotNull(lastSuccessfulLoginTimeBefore);

        LocalDateTime newLastSuccessfulLoginTime = LocalDateTime.now().plusMinutes(1);
        accountListDTONo1.setLastSuccessfulLoginTime(newLastSuccessfulLoginTime);

        LocalDateTime lastSuccessfulLoginTimeAfter = accountListDTONo1.getLastSuccessfulLoginTime();
        assertNotNull(lastSuccessfulLoginTimeAfter);

        assertNotEquals(lastSuccessfulLoginTimeBefore, lastSuccessfulLoginTimeAfter);
        assertNotEquals(lastSuccessfulLoginTimeBefore, newLastSuccessfulLoginTime);
        assertEquals(newLastSuccessfulLoginTime, lastSuccessfulLoginTimeAfter);
    }

    @Test
    public void accountListDTOLastUnsuccessfulLoginTimeSetterTestPositive() {
        LocalDateTime lastUnsuccessfulLoginTimeBefore = accountListDTONo1.getLastUnsuccessfulLoginTime();
        assertNotNull(lastUnsuccessfulLoginTimeBefore);

        LocalDateTime newLastUnsuccessfulLoginTime = LocalDateTime.now().plusMinutes(1);
        accountListDTONo1.setLastUnsuccessfulLoginTime(newLastUnsuccessfulLoginTime);

        LocalDateTime lastUnsuccessfulLoginTimeAfter = accountListDTONo1.getLastUnsuccessfulLoginTime();
        assertNotNull(lastUnsuccessfulLoginTimeAfter);

        assertNotEquals(lastUnsuccessfulLoginTimeBefore, lastUnsuccessfulLoginTimeAfter);
        assertNotEquals(lastUnsuccessfulLoginTimeBefore, newLastUnsuccessfulLoginTime);
        assertEquals(newLastUnsuccessfulLoginTime, lastUnsuccessfulLoginTimeAfter);
    }

    @Test
    public void accountListDTOUserLevelsSetterTestPositive() {
        List<String> userLevelsBefore = accountListDTONo1.getUserLevels();
        assertNotNull(userLevelsBefore);

        List<String> newUserLevels = List.of("ROLE_ADMIN");
        accountListDTONo1.setUserLevels(newUserLevels);

        List<String> userLevelsAfter = accountListDTONo1.getUserLevels();
        assertNotNull(userLevelsAfter);

        assertNotEquals(userLevelsBefore, userLevelsAfter);
        assertNotEquals(userLevelsBefore, newUserLevels);
        assertEquals(newUserLevels, userLevelsAfter);
    }

    @Test
    public void accountListDTOToStringTestPositive() {
        String result = accountListDTONo1.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
        log.debug("Test result: {}", result);
    }
}
