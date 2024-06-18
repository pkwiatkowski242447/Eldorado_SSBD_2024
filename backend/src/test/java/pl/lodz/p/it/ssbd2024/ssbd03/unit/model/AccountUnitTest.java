package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AccountUnitTest {

    //ACCOUNT DATA
    private static final String ACCOUNT_LOGIN_NO_1 = "AccountLoginNo1";
    private static final String ACCOUNT_PASSWORD_NO_1 = "$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa";
    private static final String ACCOUNT_NAME_NO_1 = "AccountName1";
    private static final String ACCOUNT_LASTNAME_NO_1 = "AccountLastnameNo1";
    private static final String ACCOUNT_EMAIL_NO_1 = "accountNo1@email.com";
    private static final String ACCOUNT_PHONE_NUMBER_NO_1 = "000000000";

    private static final int UNSUCCESSFUL_LOGIN_COUNTER = 2;
    private static final LocalDateTime LAST_UNSUCCESSFUL_LOGIN_TIME = LocalDateTime.now().plusHours(12);
    private static final String LAST_UNSUCCESSFUL_LOGIN_IP = "172.16.0.1";
    private static final LocalDateTime LAST_SUCCESSFUL_LOGIN_TIME = LocalDateTime.now().minusHours(12);
    private static final String LAST_SUCCESSFUL_LOGIN_IP = "192.168.0.1";

    private static final String LOGIN_NO1 = "ExampleLogin";
    private static final String PASSWORD_NO1 = "ExamplePassword";
    private static final String FIRST_NAME_NO1 = "Firstname";
    private static final String LAST_NAME_NO1 = "Lastname";
    private static final String EMAIL_NO1 = "email@sb.pl";
    private static final String PHONE_NUMBER_NO1 = "123456789";
    private static final String ACCOUNT_LANGUAGE_NO1 = "PL";

    private static Account exampleAccount;
    private static Account testAccount;
    private static Set<UserLevel> userLevels = new HashSet<>();

    @BeforeAll
    public static void setUp() throws Exception {
        exampleAccount = new Account(LOGIN_NO1,
                PASSWORD_NO1,
                FIRST_NAME_NO1,
                LAST_NAME_NO1,
                EMAIL_NO1,
                PHONE_NUMBER_NO1);

        exampleAccount.blockAccount(false);
        exampleAccount.setAccountLanguage(ACCOUNT_LANGUAGE_NO1);

        Set<String> pastPasswords = new HashSet<>();
        pastPasswords.add("ExamplePasswordNo1");
        pastPasswords.add("ExamplePasswordNo2");
        pastPasswords.add("ExamplePasswordNo3");

        UserLevel client = new Client();
        client.setAccount(exampleAccount);

        UserLevel admin = new Admin();
        admin.setAccount(exampleAccount);

        userLevels.add(client);
        userLevels.add(admin);

        exampleAccount.addUserLevel(client);
        exampleAccount.addUserLevel(admin);

        ActivityLog activityLog = new ActivityLog();
        activityLog.setUnsuccessfulLoginCounter(UNSUCCESSFUL_LOGIN_COUNTER);
        activityLog.setLastSuccessfulLoginTime(LAST_SUCCESSFUL_LOGIN_TIME);
        activityLog.setLastSuccessfulLoginIp(LAST_SUCCESSFUL_LOGIN_IP);
        activityLog.setLastUnsuccessfulLoginTime(LAST_UNSUCCESSFUL_LOGIN_TIME);
        activityLog.setLastUnsuccessfulLoginIp(LAST_UNSUCCESSFUL_LOGIN_IP);

        exampleAccount.setActivityLog(activityLog);

        testAccount = new Account(LOGIN_NO1,
                PASSWORD_NO1,
                FIRST_NAME_NO1,
                LAST_NAME_NO1,
                EMAIL_NO1,
                PHONE_NUMBER_NO1);

        testAccount.blockAccount(false);
        testAccount.setAccountLanguage(ACCOUNT_LANGUAGE_NO1);

        client = new Client();
        client.setAccount(testAccount);

        admin = new Admin();
        admin.setAccount(testAccount);

        userLevels.add(client);
        userLevels.add(admin);

        testAccount.addUserLevel(client);
        testAccount.addUserLevel(admin);

        activityLog = new ActivityLog();
        activityLog.setUnsuccessfulLoginCounter(UNSUCCESSFUL_LOGIN_COUNTER);
        activityLog.setLastSuccessfulLoginTime(LAST_SUCCESSFUL_LOGIN_TIME);
        activityLog.setLastSuccessfulLoginIp(LAST_SUCCESSFUL_LOGIN_IP);
        activityLog.setLastUnsuccessfulLoginTime(LAST_UNSUCCESSFUL_LOGIN_TIME);
        activityLog.setLastUnsuccessfulLoginIp(LAST_UNSUCCESSFUL_LOGIN_IP);

        testAccount.setActivityLog(activityLog);
    }

    //ACTIVITY LOG DATA

    @Test
    public void getActivityLogForAccountWithNotNullActivityLogPositiveTest() {
        Account testAccount1 = new Account(ACCOUNT_LOGIN_NO_1, ACCOUNT_PASSWORD_NO_1, ACCOUNT_NAME_NO_1, ACCOUNT_LASTNAME_NO_1,
                ACCOUNT_EMAIL_NO_1, ACCOUNT_PHONE_NUMBER_NO_1);

        assertNotNull(testAccount1.getActivityLog());
        assertNull(testAccount1.getActivityLog().getLastSuccessfulLoginIp());
        assertNull(testAccount1.getActivityLog().getLastUnsuccessfulLoginTime());
        assertNull(testAccount1.getActivityLog().getLastUnsuccessfulLoginIp());

        LocalDateTime lastSuccessfulLoginTimeNo1 = LocalDateTime.of(2002, 10, 12, 10, 20, 30);
        LocalDateTime lastUnsuccessfulLoginTimeNo1 = LocalDateTime.of(2002, 10, 12, 9, 20, 30);

        String lastSuccessfulLoginIPNo1 = "10.10.10.10";
        String lastUnsuccessfulLoginIPNo1 = "11.11.11.11";

        testAccount1.getActivityLog().setLastSuccessfulLoginTime(lastSuccessfulLoginTimeNo1);
        testAccount1.getActivityLog().setLastUnsuccessfulLoginTime(lastUnsuccessfulLoginTimeNo1);

        testAccount1.getActivityLog().setLastSuccessfulLoginIp(lastSuccessfulLoginIPNo1);
        testAccount1.getActivityLog().setLastUnsuccessfulLoginIp(lastUnsuccessfulLoginIPNo1);

        assertNotNull(testAccount1.getActivityLog());
        assertEquals(lastSuccessfulLoginTimeNo1, testAccount1.getActivityLog().getLastSuccessfulLoginTime());
        assertEquals(lastSuccessfulLoginIPNo1, testAccount1.getActivityLog().getLastSuccessfulLoginIp());
        assertEquals(lastUnsuccessfulLoginTimeNo1, testAccount1.getActivityLog().getLastUnsuccessfulLoginTime());
        assertEquals(lastUnsuccessfulLoginIPNo1, testAccount1.getActivityLog().getLastUnsuccessfulLoginIp());
    }

    @Test
    public void getActivityLogForAccountWithNotNullActivityLogPositiveLog() {
        Account testAccount1 = new Account(ACCOUNT_LOGIN_NO_1, ACCOUNT_PASSWORD_NO_1, ACCOUNT_NAME_NO_1, ACCOUNT_LASTNAME_NO_1,
                ACCOUNT_EMAIL_NO_1, ACCOUNT_PHONE_NUMBER_NO_1);

        testAccount1.setActivityLog(null);

        assertNotNull(testAccount1.getActivityLog());
        assertNull(testAccount1.getActivityLog().getLastSuccessfulLoginIp());
        assertNull(testAccount1.getActivityLog().getLastUnsuccessfulLoginTime());
        assertNull(testAccount1.getActivityLog().getLastUnsuccessfulLoginIp());
    }

    @Test
    public void isEnabledReturnsTrueTest() {
        Account testAccount1 = new Account(ACCOUNT_LOGIN_NO_1, ACCOUNT_PASSWORD_NO_1, ACCOUNT_NAME_NO_1, ACCOUNT_LASTNAME_NO_1,
                ACCOUNT_EMAIL_NO_1, ACCOUNT_PHONE_NUMBER_NO_1);

        testAccount1.setBlocked(false);
        testAccount1.setActive(true);

        assertTrue(testAccount1.isEnabled());

        testAccount1.setBlocked(false);
        testAccount1.setActive(false);

        assertFalse(testAccount1.isEnabled());

        testAccount1.setBlocked(true);
        testAccount1.setActive(true);

        assertFalse(testAccount1.isEnabled());
    }

    @Test
    public void isEnabledReturnsFalseTest() {
        Account testAccount1 = new Account(ACCOUNT_LOGIN_NO_1, ACCOUNT_PASSWORD_NO_1, ACCOUNT_NAME_NO_1, ACCOUNT_LASTNAME_NO_1,
                ACCOUNT_EMAIL_NO_1, ACCOUNT_PHONE_NUMBER_NO_1);

        testAccount1.setBlocked(true);
        testAccount1.setActive(false);

        assertFalse(testAccount1.isEnabled());
    }

    @Test
    public void equalsReturnsFalseTest() {
        Account testAccount1 = new Account(ACCOUNT_LOGIN_NO_1, ACCOUNT_PASSWORD_NO_1, ACCOUNT_NAME_NO_1, ACCOUNT_LASTNAME_NO_1,
                ACCOUNT_EMAIL_NO_1, ACCOUNT_PHONE_NUMBER_NO_1);

        assertFalse(testAccount1.equals(null));
        assertFalse(testAccount1.equals("String"));
    }

    // Added tests

    @Test
    public void accountNoArgsConstructorTestPositive() {
        Account testAccount = new Account();
        assertNotNull(testAccount);
    }

    @Test
    public void accountAllGettersTestPositive() {
        assertEquals(exampleAccount.getLogin(), LOGIN_NO1);
        assertEquals(exampleAccount.getPassword(), PASSWORD_NO1);
        assertEquals(exampleAccount.getEmail(), EMAIL_NO1);
        assertEquals(exampleAccount.getName(), FIRST_NAME_NO1);
        assertEquals(exampleAccount.getLastname(), LAST_NAME_NO1);
        assertEquals(exampleAccount.getPhoneNumber(), PHONE_NUMBER_NO1);
        assertEquals(exampleAccount.getAccountLanguage(), ACCOUNT_LANGUAGE_NO1);

        assertFalse(exampleAccount.getSuspended());
        assertFalse(exampleAccount.getActive());
        assertTrue(exampleAccount.getBlocked());
        assertTrue(exampleAccount.getTwoFactorAuth());

        assertNotNull(exampleAccount.getBlockedTime());

        assertFalse(exampleAccount.getUserLevels().isEmpty());
        assertEquals(2, exampleAccount.getUserLevels().size());
        assertTrue(exampleAccount.getUserLevels().stream().anyMatch(userLevel -> userLevel instanceof Client));
        assertTrue(exampleAccount.getUserLevels().stream().anyMatch(userLevel -> userLevel instanceof Admin));

        assertEquals(exampleAccount.getActivityLog().getUnsuccessfulLoginCounter(), UNSUCCESSFUL_LOGIN_COUNTER);
        assertEquals(exampleAccount.getActivityLog().getLastSuccessfulLoginTime(), LAST_SUCCESSFUL_LOGIN_TIME);
        assertEquals(exampleAccount.getActivityLog().getLastSuccessfulLoginIp(), LAST_SUCCESSFUL_LOGIN_IP);
        assertEquals(exampleAccount.getActivityLog().getLastUnsuccessfulLoginTime(), LAST_UNSUCCESSFUL_LOGIN_TIME);
        assertEquals(exampleAccount.getActivityLog().getLastUnsuccessfulLoginIp(), LAST_UNSUCCESSFUL_LOGIN_IP);
    }

    @Test
    public void accountSetPasswordTestPositive() {
        String passwordBefore = testAccount.getPassword();
        String newPassword = "P@ssw0rd!123!";

        assertNotNull(passwordBefore);
        assertNotNull(newPassword);

        testAccount.setPassword(newPassword);

        String passwordAfter = testAccount.getPassword();

        assertNotEquals(passwordBefore, newPassword);
        assertNotEquals(passwordBefore, passwordAfter);
        assertEquals(newPassword, passwordAfter);
    }

    @Test
    public void accountSetSuspendedTestPositive() {
        boolean suspendedBefore = testAccount.getSuspended();
        boolean newSuspended = !suspendedBefore;

        testAccount.setSuspended(newSuspended);

        boolean suspendedAfter = testAccount.getSuspended();

        assertNotEquals(suspendedBefore, newSuspended);
        assertNotEquals(suspendedBefore, suspendedAfter);
        assertEquals(newSuspended, suspendedAfter);
    }

    @Test
    public void accountSetActiveTestPositive() {
        boolean activeBefore = testAccount.getActive();
        boolean newActive = !activeBefore;

        testAccount.setActive(newActive);

        boolean activeAfter = testAccount.getActive();

        assertNotEquals(activeBefore, newActive);
        assertNotEquals(activeBefore, activeAfter);
        assertEquals(newActive, activeAfter);
    }

    @Test
    public void accountSetBlockedTestPositive() {
        boolean blockedBefore = testAccount.getBlocked();
        boolean newBlocked = !blockedBefore;

        testAccount.setBlocked(newBlocked);

        boolean blockedAfter = testAccount.getBlocked();

        assertNotEquals(blockedBefore, newBlocked);
        assertNotEquals(blockedBefore, blockedAfter);
        assertEquals(newBlocked, blockedAfter);
    }

    @Test
    public void accountSetTwoFactorAuthTestPositive() {
        boolean twoFactorAuthBefore = testAccount.getTwoFactorAuth();
        boolean newTwoFactorAuth = !twoFactorAuthBefore;

        testAccount.setTwoFactorAuth(newTwoFactorAuth);

        boolean twoFactorAuthAfter = testAccount.getTwoFactorAuth();

        assertNotEquals(twoFactorAuthBefore, newTwoFactorAuth);
        assertNotEquals(twoFactorAuthBefore, twoFactorAuthAfter);
        assertEquals(newTwoFactorAuth, twoFactorAuthAfter);
    }

    @Test
    public void accountSetFirstNameTestPositive() {
        String nameBefore = testAccount.getName();
        String newName = "NewFirstName";

        assertNotNull(nameBefore);
        assertNotNull(newName);

        testAccount.setName(newName);

        String nameAfter = testAccount.getName();

        assertNotNull(nameAfter);

        assertNotEquals(nameBefore, newName);
        assertNotEquals(nameBefore, nameAfter);
        assertEquals(newName, nameAfter);
    }

    @Test
    public void accountSetLastNameTestPositive() {
        String lastNameBefore = testAccount.getLastname();
        String newLastName = "NewLastName";

        assertNotNull(lastNameBefore);
        assertNotNull(newLastName);

        testAccount.setLastname(newLastName);

        String lastNameAfter = testAccount.getLastname();

        assertNotNull(lastNameAfter);

        assertNotEquals(lastNameBefore, newLastName);
        assertNotEquals(lastNameBefore, lastNameAfter);
        assertEquals(newLastName, lastNameAfter);
    }

    @Test
    public void accountSetEmailTestPositive() {
        String emailBefore = testAccount.getEmail();
        String newEmail = "NewEmail";

        assertNotNull(emailBefore);
        assertNotNull(newEmail);

        testAccount.setEmail(newEmail);

        String lastNameAfter = testAccount.getEmail();

        assertNotNull(lastNameAfter);

        assertNotEquals(emailBefore, newEmail);
        assertNotEquals(emailBefore, lastNameAfter);
        assertEquals(newEmail, lastNameAfter);
    }

    @Test
    public void accountSetAccountLanguageTestPositive() {
        String accountLanguageBefore = testAccount.getAccountLanguage();
        String newAccountLanguage = "NewAccountLanguage";

        assertNotNull(accountLanguageBefore);
        assertNotNull(newAccountLanguage);

        testAccount.setAccountLanguage(newAccountLanguage);

        String accountLanguageAfter = testAccount.getAccountLanguage();

        assertNotNull(accountLanguageAfter);

        assertNotEquals(accountLanguageBefore, newAccountLanguage);
        assertNotEquals(accountLanguageBefore, accountLanguageAfter);
        assertEquals(newAccountLanguage, accountLanguageAfter);
    }

    @Test
    public void accountSetPhoneNumberTestPositive() {
        String phoneNumberBefore = testAccount.getPhoneNumber();
        String newPhoneNumber = "987654321";

        assertNotNull(phoneNumberBefore);
        assertNotNull(newPhoneNumber);

        testAccount.setPhoneNumber(newPhoneNumber);

        String phoneNumberAfter = testAccount.getPhoneNumber();

        assertNotNull(phoneNumberAfter);

        assertNotEquals(phoneNumberBefore, newPhoneNumber);
        assertNotEquals(phoneNumberBefore, phoneNumberAfter);
        assertEquals(newPhoneNumber, phoneNumberAfter);
    }
}
