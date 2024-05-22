package pl.lodz.p.it.ssbd2024.ssbd03.unit.dto.accountOutputDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AccountListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AccountOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel.AdminDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel.ClientDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel.StaffDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.userlevel.UserLevelDTO;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountOutputDTOTest {

    private String loginNo1;
    private Long versionNo1;
    private Set<UserLevelDTO> userLevelsNo1;
    private UUID idNo1;
    private boolean verifiedNo1;
    private boolean activeNo1;
    private boolean blockedNo1;
    private boolean twoFactorAuthNo1;
    private LocalDateTime blockedTimeNo1;
    private LocalDateTime creationDateNo1;
    private LocalDateTime lastSuccessfulLoginTimeNo1;
    private LocalDateTime lastUnsuccessfulLoginTimeNo1;
    private String accountLanguageNo1;
    private String lastSuccessfulLoginIpNo1;
    private String lastUnsuccessfulLoginIpNo1;
    private String phoneNumberNo1;
    private String lastnameNo1;
    private String nameNo1;
    private String emailNo1;

    private AccountOutputDTO accountOutputDTO;

    @BeforeEach
    public void init() {
        loginNo1 = "exampleLoginNo1";
        versionNo1 = 0L;
        userLevelsNo1 = Set.of(new AdminDTO(), new StaffDTO(), new ClientDTO());
        idNo1 = UUID.randomUUID();
        verifiedNo1 = true;
        activeNo1 = true;
        blockedNo1 = false;
        twoFactorAuthNo1 = false;
        blockedTimeNo1 = LocalDateTime.now();
        creationDateNo1 = LocalDateTime.now();
        lastSuccessfulLoginTimeNo1 = LocalDateTime.now();
        lastUnsuccessfulLoginTimeNo1 = LocalDateTime.now();
        accountLanguageNo1 = "pl";
        lastSuccessfulLoginIpNo1 = "127.0.0.1";
        lastUnsuccessfulLoginIpNo1 = "127.0.0.1";
        phoneNumberNo1 = "123123123";
        lastnameNo1 = "exampleLastNameNo1";
        nameNo1 = "exampleNameNo1";
        emailNo1 = "exampleEmailNo1@example.com";

        accountOutputDTO = new AccountOutputDTO(
                loginNo1,
                versionNo1,
                userLevelsNo1,
                idNo1,
                verifiedNo1,
                activeNo1,
                blockedNo1,
                twoFactorAuthNo1,
                blockedTimeNo1,
                creationDateNo1,
                lastSuccessfulLoginTimeNo1,
                lastUnsuccessfulLoginTimeNo1,
                accountLanguageNo1,
                lastSuccessfulLoginIpNo1,
                lastUnsuccessfulLoginIpNo1,
                phoneNumberNo1,
                lastnameNo1,
                nameNo1,
                emailNo1
        );
    }

    @Test
    public void accountOutputDTONoArgsConstructorTestPositive() {
        AccountOutputDTO accountOutputDTO = new AccountOutputDTO();
        assertNotNull(accountOutputDTO);
    }

    @Test
    public void accountOutputDTOAllArgsConstructorTestPositive() {
        AccountOutputDTO accountOutputDTO = new AccountOutputDTO(
                loginNo1,
                versionNo1,
                userLevelsNo1,
                idNo1,
                verifiedNo1,
                activeNo1,
                blockedNo1,
                twoFactorAuthNo1,
                blockedTimeNo1,
                creationDateNo1,
                lastSuccessfulLoginTimeNo1,
                lastUnsuccessfulLoginTimeNo1,
                accountLanguageNo1,
                lastSuccessfulLoginIpNo1,
                lastUnsuccessfulLoginIpNo1,
                phoneNumberNo1,
                lastnameNo1,
                nameNo1,
                emailNo1
        );

        assertNotNull(accountOutputDTO);

        assertEquals(loginNo1, accountOutputDTO.getLogin());
        assertEquals(versionNo1, accountOutputDTO.getVersion());
        assertEquals(userLevelsNo1.size(), accountOutputDTO.getUserLevelsDto().size());
        assertEquals(idNo1, accountOutputDTO.getId());
        assertEquals(verifiedNo1, accountOutputDTO.isVerified());
        assertEquals(activeNo1, accountOutputDTO.isActive());
        assertEquals(blockedNo1, accountOutputDTO.isBlocked());
        assertEquals(twoFactorAuthNo1, accountOutputDTO.isTwoFactorAuth());
        assertEquals(blockedTimeNo1, accountOutputDTO.getBlockedTime());
        assertEquals(creationDateNo1, accountOutputDTO.getCreationDate());
        assertEquals(lastSuccessfulLoginTimeNo1, accountOutputDTO.getLastSuccessfulLoginTime());
        assertEquals(lastUnsuccessfulLoginTimeNo1, accountOutputDTO.getLastUnsuccessfulLoginTime());
        assertEquals(accountLanguageNo1, accountOutputDTO.getAccountLanguage());
        assertEquals(lastSuccessfulLoginIpNo1, accountOutputDTO.getLastSuccessfulLoginIp());
        assertEquals(lastUnsuccessfulLoginIpNo1, accountOutputDTO.getLastUnsuccessfulLoginIp());
        assertEquals(phoneNumberNo1, accountOutputDTO.getPhoneNumber());
        assertEquals(lastnameNo1, accountOutputDTO.getLastname());
        assertEquals(nameNo1, accountOutputDTO.getName());
        assertEquals(emailNo1, accountOutputDTO.getEmail());
    }

    @Test
    public void accountOutputDTOSetterTestPositive() {
        // New values
        String loginNo2 = "exampleLoginNo2";
        Long versionNo2 = 0L;
        Set<UserLevelDTO> userLevelsNo2 = Set.of(new StaffDTO(), new ClientDTO());
        UUID idNo2 = UUID.randomUUID();
        boolean verifiedNo2 = !verifiedNo1;
        boolean activeNo2 = !activeNo1;
        boolean blockedNo2 = !blockedNo1;
        boolean twoFactorAuthNo2 = !twoFactorAuthNo1;
        LocalDateTime blockedTimeNo2 = LocalDateTime.now().plusDays(1);
        LocalDateTime creationDateNo2 = LocalDateTime.now().plusDays(1);
        LocalDateTime lastSuccessfulLoginTimeNo2 = LocalDateTime.now().plusDays(1);
        LocalDateTime lastUnsuccessfulLoginTimeNo2 = LocalDateTime.now().plusDays(1);
        String accountLanguageNo2 = "en";
        String lastSuccessfulLoginIpNo2 = "127.0.1.1";
        String lastUnsuccessfulLoginIpNo2 = "127.0.1.1";
        String phoneNumberNo2 = "999888777";
        String lastnameNo2 = "exampleLastNameNo2";
        String nameNo2 = "exampleNameNo2";
        String emailNo2 = "exampleEmailNo2@example.com";

        // Check values before using setters
        assertEquals(loginNo1, accountOutputDTO.getLogin());
        assertEquals(versionNo1, accountOutputDTO.getVersion());
        assertEquals(userLevelsNo1.size(), accountOutputDTO.getUserLevelsDto().size());
        assertEquals(idNo1, accountOutputDTO.getId());
        assertEquals(verifiedNo1, accountOutputDTO.isVerified());
        assertEquals(activeNo1, accountOutputDTO.isActive());
        assertEquals(blockedNo1, accountOutputDTO.isBlocked());
        assertEquals(twoFactorAuthNo1, accountOutputDTO.isTwoFactorAuth());
        assertEquals(blockedTimeNo1, accountOutputDTO.getBlockedTime());
        assertEquals(creationDateNo1, accountOutputDTO.getCreationDate());
        assertEquals(lastSuccessfulLoginTimeNo1, accountOutputDTO.getLastSuccessfulLoginTime());
        assertEquals(lastUnsuccessfulLoginTimeNo1, accountOutputDTO.getLastUnsuccessfulLoginTime());
        assertEquals(accountLanguageNo1, accountOutputDTO.getAccountLanguage());
        assertEquals(lastSuccessfulLoginIpNo1, accountOutputDTO.getLastSuccessfulLoginIp());
        assertEquals(lastUnsuccessfulLoginIpNo1, accountOutputDTO.getLastUnsuccessfulLoginIp());
        assertEquals(phoneNumberNo1, accountOutputDTO.getPhoneNumber());
        assertEquals(lastnameNo1, accountOutputDTO.getLastname());
        assertEquals(nameNo1, accountOutputDTO.getName());
        assertEquals(emailNo1, accountOutputDTO.getEmail());

        // Using setters
        accountOutputDTO.setLogin(loginNo2);
        accountOutputDTO.setVersion(versionNo2);
        accountOutputDTO.setUserLevelsDto(userLevelsNo2);
        accountOutputDTO.setId(idNo2);
        accountOutputDTO.setVerified(verifiedNo2);
        accountOutputDTO.setActive(activeNo2);
        accountOutputDTO.setBlocked(blockedNo2);
        accountOutputDTO.setTwoFactorAuth(twoFactorAuthNo2);
        accountOutputDTO.setBlockedTime(blockedTimeNo2);
        accountOutputDTO.setCreationDate(creationDateNo2);
        accountOutputDTO.setLastSuccessfulLoginTime(lastSuccessfulLoginTimeNo2);
        accountOutputDTO.setLastUnsuccessfulLoginTime(lastUnsuccessfulLoginTimeNo2);
        accountOutputDTO.setAccountLanguage(accountLanguageNo2);
        accountOutputDTO.setLastSuccessfulLoginIp(lastSuccessfulLoginIpNo2);
        accountOutputDTO.setLastUnsuccessfulLoginIp(lastUnsuccessfulLoginIpNo2);
        accountOutputDTO.setPhoneNumber(phoneNumberNo2);
        accountOutputDTO.setLastname(lastnameNo2);
        accountOutputDTO.setName(nameNo2);
        accountOutputDTO.setEmail(emailNo2);

        // Check values after using setters
        assertEquals(loginNo2, accountOutputDTO.getLogin());
        assertEquals(versionNo2, accountOutputDTO.getVersion());
        assertEquals(userLevelsNo2.size(), accountOutputDTO.getUserLevelsDto().size());
        assertEquals(idNo2, accountOutputDTO.getId());
        assertEquals(verifiedNo2, accountOutputDTO.isVerified());
        assertEquals(activeNo2, accountOutputDTO.isActive());
        assertEquals(blockedNo2, accountOutputDTO.isBlocked());
        assertEquals(twoFactorAuthNo2, accountOutputDTO.isTwoFactorAuth());
        assertEquals(blockedTimeNo2, accountOutputDTO.getBlockedTime());
        assertEquals(creationDateNo2, accountOutputDTO.getCreationDate());
        assertEquals(lastSuccessfulLoginTimeNo2, accountOutputDTO.getLastSuccessfulLoginTime());
        assertEquals(lastUnsuccessfulLoginTimeNo2, accountOutputDTO.getLastUnsuccessfulLoginTime());
        assertEquals(accountLanguageNo2, accountOutputDTO.getAccountLanguage());
        assertEquals(lastSuccessfulLoginIpNo2, accountOutputDTO.getLastSuccessfulLoginIp());
        assertEquals(lastUnsuccessfulLoginIpNo2, accountOutputDTO.getLastUnsuccessfulLoginIp());
        assertEquals(phoneNumberNo2, accountOutputDTO.getPhoneNumber());
        assertEquals(lastnameNo2, accountOutputDTO.getLastname());
        assertEquals(nameNo2, accountOutputDTO.getName());
        assertEquals(emailNo2, accountOutputDTO.getEmail());
    }
}
