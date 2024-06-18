package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.*;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountHistoryDataUnitTest {

    private static final UUID ID_NO1 = UUID.randomUUID();
    private static final Long VERSION_NO1 = 5L;
    private static final String LOGIN_NO1 = "exampleLogin";
    private static final String PASSWORD_NO1 = "examplePassword";

    private static final Boolean SUSPENDED_NO1 = false;
    private static final Boolean ACTIVE_NO1 = true;
    private static final Boolean BLOCKED_NO1 = false;
    private static final Boolean TWO_FACTOR_AUTH_NO1 = true;

    private static final LocalDateTime BLOCKED_TIME_NO1 = LocalDateTime.now().minusHours(12);

    private static final String FIRSTNAME_NO1 = "exampleFirstName";
    private static final String LASTNAME_NO1 = "exampleLastName";
    private static final String EMAIL_NO1 = "exampleEmail@sb.pl";
    private static final String PHONE_NUMBER_NO1 = "examplePhoneNumber";

    private static final int UNSUCCESSFUL_LOGIN_COUNTER = 5;
    private static final LocalDateTime LAST_SUCCESSFUL_LOGIN_TIME_NO1 = LocalDateTime.now().minusHours(24);
    private static final String LAST_SUCCESSFUL_LOGIN_IP_NO1 = "172.16.0.1";
    private static final LocalDateTime LAST_UNSUCCESSFUL_LOGIN_TIME_NO1 = LocalDateTime.now().minusHours(48);
    private static final String LAST_UNSUCCESSFUL_LOGIN_IP_NO1 = "192.168.0.1";

    private static final String LANGUAGE_NO1 = "PL";
    private static final OperationType OPERATION_TYPE = OperationType.REGISTRATION;
    private static final LocalDateTime MODIFICATION_TIME = LocalDateTime.now().minusHours(36);
    private static final Account MODIFIED_BY = new Account(LOGIN_NO1, PASSWORD_NO1, FIRSTNAME_NO1, LASTNAME_NO1, EMAIL_NO1, PHONE_NUMBER_NO1);

    private static AccountHistoryData exampleHistoryData;
    private static AccountHistoryData testHistoryData;
    private static Account exampleAccount;

    @BeforeAll
    public static void setUp() {
        exampleHistoryData = new AccountHistoryData(ID_NO1,
                VERSION_NO1,
                LOGIN_NO1,
                PASSWORD_NO1,
                SUSPENDED_NO1,
                ACTIVE_NO1,
                BLOCKED_NO1,
                TWO_FACTOR_AUTH_NO1,
                BLOCKED_TIME_NO1,
                FIRSTNAME_NO1,
                LASTNAME_NO1,
                EMAIL_NO1,
                PHONE_NUMBER_NO1,
                LAST_SUCCESSFUL_LOGIN_TIME_NO1,
                LAST_SUCCESSFUL_LOGIN_IP_NO1,
                LAST_UNSUCCESSFUL_LOGIN_TIME_NO1,
                LAST_UNSUCCESSFUL_LOGIN_IP_NO1,
                UNSUCCESSFUL_LOGIN_COUNTER,
                LANGUAGE_NO1,
                OPERATION_TYPE,
                MODIFICATION_TIME,
                MODIFIED_BY);

        testHistoryData = new AccountHistoryData(ID_NO1,
                VERSION_NO1,
                LOGIN_NO1,
                PASSWORD_NO1,
                SUSPENDED_NO1,
                ACTIVE_NO1,
                BLOCKED_NO1,
                TWO_FACTOR_AUTH_NO1,
                BLOCKED_TIME_NO1,
                FIRSTNAME_NO1,
                LASTNAME_NO1,
                EMAIL_NO1,
                PHONE_NUMBER_NO1,
                LAST_SUCCESSFUL_LOGIN_TIME_NO1,
                LAST_SUCCESSFUL_LOGIN_IP_NO1,
                LAST_UNSUCCESSFUL_LOGIN_TIME_NO1,
                LAST_UNSUCCESSFUL_LOGIN_IP_NO1,
                UNSUCCESSFUL_LOGIN_COUNTER,
                LANGUAGE_NO1,
                OPERATION_TYPE,
                MODIFICATION_TIME,
                MODIFIED_BY);

        exampleAccount = new Account(LOGIN_NO1,
                PASSWORD_NO1,
                FIRSTNAME_NO1,
                LASTNAME_NO1,
                EMAIL_NO1,
                PHONE_NUMBER_NO1);

        exampleAccount.blockAccount(false);
        exampleAccount.setAccountLanguage(LANGUAGE_NO1);

        Set<String> pastPasswords = new HashSet<>();
        pastPasswords.add("ExamplePasswordNo1");
        pastPasswords.add("ExamplePasswordNo2");
        pastPasswords.add("ExamplePasswordNo3");

        UserLevel client = new Client();
        client.setAccount(exampleAccount);

        UserLevel admin = new Admin();
        admin.setAccount(exampleAccount);

        exampleAccount.addUserLevel(client);
        exampleAccount.addUserLevel(admin);

        ActivityLog activityLog = new ActivityLog();
        activityLog.setUnsuccessfulLoginCounter(UNSUCCESSFUL_LOGIN_COUNTER);
        activityLog.setLastSuccessfulLoginTime(LAST_SUCCESSFUL_LOGIN_TIME_NO1);
        activityLog.setLastSuccessfulLoginIp(LAST_SUCCESSFUL_LOGIN_IP_NO1);
        activityLog.setLastUnsuccessfulLoginTime(LAST_UNSUCCESSFUL_LOGIN_TIME_NO1);
        activityLog.setLastUnsuccessfulLoginIp(LAST_UNSUCCESSFUL_LOGIN_IP_NO1);
    }

    @Test
    public void accountHistoryDataNoArgsConstructorTestPositive() {
        AccountHistoryData accountHistoryData = new AccountHistoryData();
        assertNotNull(accountHistoryData);
    }

    @Test
    public void accountHistoryDataAllArgsConstructorTestPositive() {
        assertEquals(exampleHistoryData.getId(), ID_NO1);
        assertEquals(exampleHistoryData.getVersion(), VERSION_NO1);
        assertEquals(exampleHistoryData.getLogin(), LOGIN_NO1);
        assertEquals(exampleHistoryData.getPassword(), PASSWORD_NO1);
        assertEquals(exampleHistoryData.getSuspended(), SUSPENDED_NO1);
        assertEquals(exampleHistoryData.getActive(), ACTIVE_NO1);
        assertEquals(exampleHistoryData.getBlocked(), BLOCKED_NO1);
        assertEquals(exampleHistoryData.getTwoFactorAuth(), TWO_FACTOR_AUTH_NO1);
        assertEquals(exampleHistoryData.getBlockedTime(), BLOCKED_TIME_NO1);
        assertEquals(exampleHistoryData.getName(), FIRSTNAME_NO1);
        assertEquals(exampleHistoryData.getLastname(), LASTNAME_NO1);
        assertEquals(exampleHistoryData.getEmail(), EMAIL_NO1);
        assertEquals(exampleHistoryData.getPhoneNumber(), PHONE_NUMBER_NO1);
        assertEquals(exampleHistoryData.getLastSuccessfulLoginTime(), LAST_SUCCESSFUL_LOGIN_TIME_NO1);
        assertEquals(exampleHistoryData.getLastSuccessfulLoginIp(), LAST_SUCCESSFUL_LOGIN_IP_NO1);
        assertEquals(exampleHistoryData.getLastUnsuccessfulLoginTime(), LAST_UNSUCCESSFUL_LOGIN_TIME_NO1);
        assertEquals(exampleHistoryData.getLastUnsuccessfulLoginIp(), LAST_UNSUCCESSFUL_LOGIN_IP_NO1);
        assertEquals(exampleHistoryData.getUnsuccessfulLoginCounter(), UNSUCCESSFUL_LOGIN_COUNTER);
        assertEquals(exampleHistoryData.getLanguage(), LANGUAGE_NO1);
        assertEquals(exampleHistoryData.getOperationType(), OPERATION_TYPE);
        assertEquals(exampleHistoryData.getModificationTime(), MODIFICATION_TIME);
        assertEquals(exampleHistoryData.getModifiedBy(), MODIFIED_BY);
    }

    @Test
    public void accountHistorySetSuspendedTestPositive() {
        boolean suspendedBefore = testHistoryData.getSuspended();
        boolean newSuspended = !suspendedBefore;

        testHistoryData.setSuspended(newSuspended);

        boolean suspendedAfter = testHistoryData.getSuspended();

        assertNotEquals(suspendedBefore, newSuspended);
        assertNotEquals(suspendedBefore, suspendedAfter);
        assertEquals(suspendedAfter, newSuspended);
    }

    @Test
    public void accountHistorySetActiveTestPositive() {
        boolean activeBefore = testHistoryData.getActive();
        boolean newActive = !activeBefore;

        testHistoryData.setActive(newActive);

        boolean activeAfter = testHistoryData.getActive();

        assertNotEquals(activeBefore, newActive);
        assertNotEquals(activeBefore, activeAfter);
        assertEquals(activeAfter, newActive);
    }

    @Test
    public void accountHistorySetBlockedTestPositive() {
        boolean blockedBefore = testHistoryData.getBlocked();
        boolean newBlocked = !blockedBefore;

        testHistoryData.setBlocked(newBlocked);

        boolean blockedAfter = testHistoryData.getBlocked();

        assertNotEquals(blockedBefore, newBlocked);
        assertNotEquals(blockedBefore, blockedAfter);
        assertEquals(blockedAfter, newBlocked);
    }

    @Test
    public void accountHistorySetTwoFactorAuthTestPositive() {
        boolean twoFactorAuthBefore = testHistoryData.getTwoFactorAuth();
        boolean newTwoFactorAuth = !twoFactorAuthBefore;

        testHistoryData.setTwoFactorAuth(newTwoFactorAuth);

        boolean twoFactorAuthAfter = testHistoryData.getTwoFactorAuth();

        assertNotEquals(twoFactorAuthBefore, newTwoFactorAuth);
        assertNotEquals(twoFactorAuthBefore, twoFactorAuthAfter);
        assertEquals(twoFactorAuthAfter, newTwoFactorAuth);
    }

    @Test
    public void accountHistorySetBlockedTimeTestPositive() throws Exception {
        LocalDateTime blockedTimeBefore = testHistoryData.getBlockedTime();

        assertNotNull(blockedTimeBefore);
        assertTrue(blockedTimeBefore.isBefore(LocalDateTime.now()));

        Method setModificationTime = testHistoryData.getClass().getDeclaredMethod("setModificationTime");
        setModificationTime.setAccessible(true);
        setModificationTime.invoke(testHistoryData);
        setModificationTime.setAccessible(false);

        LocalDateTime blockedTimeAfter = testHistoryData.getBlockedTime();

        assertNotNull(blockedTimeAfter);
    }

    @Test
    public void accountHistoryCustomConstructorTestPositive() {
        AccountHistoryData accountHistoryData = new AccountHistoryData(exampleAccount, OperationType.LOGIN, MODIFIED_BY);

        assertEquals(exampleAccount.getId(), accountHistoryData.getId());
        assertEquals(exampleAccount.getVersion(), accountHistoryData.getVersion());
        assertEquals(exampleAccount.getLogin(), accountHistoryData.getLogin());
        assertEquals(exampleAccount.getPassword(), accountHistoryData.getPassword());
        assertEquals(exampleAccount.getSuspended(), accountHistoryData.getSuspended());
        assertEquals(exampleAccount.getActive(), accountHistoryData.getActive());
        assertEquals(exampleAccount.getBlocked(), accountHistoryData.getBlocked());
        assertEquals(exampleAccount.getTwoFactorAuth(), accountHistoryData.getTwoFactorAuth());
        assertEquals(exampleAccount.getBlockedTime(), accountHistoryData.getBlockedTime());
        assertEquals(exampleAccount.getName(), accountHistoryData.getName());
        assertEquals(exampleAccount.getLastname(), accountHistoryData.getLastname());
        assertEquals(exampleAccount.getEmail(), accountHistoryData.getEmail());
        assertEquals(exampleAccount.getPhoneNumber(), accountHistoryData.getPhoneNumber());
        assertEquals(exampleAccount.getActivityLog().getLastSuccessfulLoginTime(), accountHistoryData.getLastSuccessfulLoginTime());
        assertEquals(exampleAccount.getActivityLog().getLastSuccessfulLoginIp(), accountHistoryData.getLastSuccessfulLoginIp());
        assertEquals(exampleAccount.getActivityLog().getLastUnsuccessfulLoginTime(), accountHistoryData.getLastUnsuccessfulLoginTime());
        assertEquals(exampleAccount.getActivityLog().getLastUnsuccessfulLoginIp(), accountHistoryData.getLastUnsuccessfulLoginIp());
        assertEquals(exampleAccount.getActivityLog().getUnsuccessfulLoginCounter(), accountHistoryData.getUnsuccessfulLoginCounter());
        assertEquals(exampleAccount.getAccountLanguage(), accountHistoryData.getLanguage());
        assertEquals(OperationType.LOGIN, accountHistoryData.getOperationType());
        assertEquals(MODIFIED_BY, accountHistoryData.getModifiedBy());
    }

    @Test
    public void accountHistoryDataToStringTestPositive() {
        String result = exampleHistoryData.toString();
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
        assertTrue(result.contains(ID_NO1.toString()));
        assertTrue(result.contains(String.valueOf(VERSION_NO1)));
        assertTrue(result.contains(LOGIN_NO1));
    }

    @Test
    public void operationTypeToStringTestPositive() {
        OperationType operationType = OperationType.UNBLOCK;
        String result = operationType.toString();
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
        assertTrue(result.contains("operation.type.unblock"));
    }
}
