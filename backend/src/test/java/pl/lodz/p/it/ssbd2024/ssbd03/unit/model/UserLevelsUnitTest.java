package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserLevelsUnitTest {

    //ACCOUNT DATA
    private static final String LOGIN_NO1 = "ExampleLogin";
    private static final String PASSWORD_NO1 = "ExamplePassword";
    private static final String FIRST_NAME_NO1 = "Firstname";
    private static final String LAST_NAME_NO1 = "Lastname";
    private static final String EMAIL_NO1 = "email@sb.pl";
    private static final String PHONE_NUMBER_NO1 = "123456789";
    private static final String ACCOUNT_LANGUAGE_NO1 = "PL";

    private static final int UNSUCCESSFUL_LOGIN_COUNTER = 2;
    private static final LocalDateTime LAST_UNSUCCESSFUL_LOGIN_TIME = LocalDateTime.now().plusHours(12);
    private static final String LAST_UNSUCCESSFUL_LOGIN_IP = "172.16.0.1";
    private static final LocalDateTime LAST_SUCCESSFUL_LOGIN_TIME = LocalDateTime.now().minusHours(12);
    private static final String LAST_SUCCESSFUL_LOGIN_IP = "192.168.0.1";

    private static final LocalDateTime CREATION_TIME_NO1 = LocalDateTime.now().minusHours(6);
    private static final LocalDateTime CREATION_TIME_NO2 = LocalDateTime.now().minusHours(12);
    private static final LocalDateTime CREATION_TIME_NO3 = LocalDateTime.now().minusHours(18);

    private static final LocalDateTime UPDATE_TIME_NO1 = LocalDateTime.now().minusHours(3);
    private static final LocalDateTime UPDATE_TIME_NO2 = LocalDateTime.now().minusHours(9);
    private static final LocalDateTime UPDATE_TIME_NO3 = LocalDateTime.now().minusHours(12);

    private static Account exampleAccount;
    private static Account testAccount;
    private static UserLevel exampleUserLevel;

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

        Field previousPasswords = Account.class.getDeclaredField("previousPasswords");
        previousPasswords.setAccessible(true);
        previousPasswords.set(exampleAccount, pastPasswords);
        previousPasswords.setAccessible(false);

        UserLevel client = new Client();
        client.setAccount(exampleAccount);

        UserLevel admin = new Admin();
        admin.setAccount(exampleAccount);

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

        testAccount.addUserLevel(client);
        testAccount.addUserLevel(admin);

        activityLog = new ActivityLog();
        activityLog.setUnsuccessfulLoginCounter(UNSUCCESSFUL_LOGIN_COUNTER);
        activityLog.setLastSuccessfulLoginTime(LAST_SUCCESSFUL_LOGIN_TIME);
        activityLog.setLastSuccessfulLoginIp(LAST_SUCCESSFUL_LOGIN_IP);
        activityLog.setLastUnsuccessfulLoginTime(LAST_UNSUCCESSFUL_LOGIN_TIME);
        activityLog.setLastUnsuccessfulLoginIp(LAST_UNSUCCESSFUL_LOGIN_IP);

        exampleUserLevel = new Client();
        exampleUserLevel.setAccount(testAccount);
        testAccount.addUserLevel(exampleUserLevel);
        exampleUserLevel.setUpdateTime(UPDATE_TIME_NO1);
    }

    @Test
    void clientConstructorReturnsNotNull() throws Exception {
        UserLevel userLevel = new Client();
        userLevel.setAccount(testAccount);
        testAccount.addUserLevel(userLevel);

        Field creationTime = UserLevel.class.getDeclaredField("creationTime");
        creationTime.setAccessible(true);
        creationTime.set(userLevel, CREATION_TIME_NO1);
        creationTime.setAccessible(true);

        Field updateTime = UserLevel.class.getDeclaredField("updateTime");
        updateTime.setAccessible(true);
        updateTime.set(userLevel, UPDATE_TIME_NO1);
        updateTime.setAccessible(true);

        Field createdBy = UserLevel.class.getDeclaredField("createdBy");
        createdBy.setAccessible(true);
        createdBy.set(userLevel, testAccount.getLogin());
        createdBy.setAccessible(true);

        assertNotNull(userLevel);
        assertInstanceOf(Client.class, userLevel);
        assertEquals(userLevel.getAccount(), testAccount);
        assertEquals(userLevel.getCreationTime(), CREATION_TIME_NO1);
        assertEquals(userLevel.getUpdateTime(), UPDATE_TIME_NO1);
        assertEquals(userLevel.getCreatedBy(), testAccount.getLogin());
    }

    @Test
    void staffConstructorReturnsNotNull() throws Exception {
        UserLevel userLevel = new Staff();
        userLevel.setAccount(testAccount);
        testAccount.addUserLevel(userLevel);

        Field creationTime = UserLevel.class.getDeclaredField("creationTime");
        creationTime.setAccessible(true);
        creationTime.set(userLevel, CREATION_TIME_NO2);
        creationTime.setAccessible(true);

        Field updateTime = UserLevel.class.getDeclaredField("updateTime");
        updateTime.setAccessible(true);
        updateTime.set(userLevel, UPDATE_TIME_NO2);
        updateTime.setAccessible(true);

        Field createdBy = UserLevel.class.getDeclaredField("createdBy");
        createdBy.setAccessible(true);
        createdBy.set(userLevel, testAccount.getLogin());
        createdBy.setAccessible(true);

        assertNotNull(userLevel);
        assertInstanceOf(Staff.class, userLevel);
        assertEquals(userLevel.getAccount(), testAccount);
        assertEquals(userLevel.getCreationTime(), CREATION_TIME_NO2);
        assertEquals(userLevel.getUpdateTime(), UPDATE_TIME_NO2);
        assertEquals(userLevel.getCreatedBy(), testAccount.getLogin());
    }

    @Test
    void adminConstructorReturnsNotNull() throws Exception {
        UserLevel userLevel = new Admin();
        userLevel.setAccount(exampleAccount);
        exampleAccount.addUserLevel(userLevel);

        Field creationTime = UserLevel.class.getDeclaredField("creationTime");
        creationTime.setAccessible(true);
        creationTime.set(userLevel, CREATION_TIME_NO3);
        creationTime.setAccessible(true);

        Field updateTime = UserLevel.class.getDeclaredField("updateTime");
        updateTime.setAccessible(true);
        updateTime.set(userLevel, UPDATE_TIME_NO3);
        updateTime.setAccessible(true);

        Field createdBy = UserLevel.class.getDeclaredField("createdBy");
        createdBy.setAccessible(true);
        createdBy.set(userLevel, exampleAccount.getLogin());
        createdBy.setAccessible(true);

        assertNotNull(userLevel);
        assertInstanceOf(Admin.class, userLevel);
        assertEquals(userLevel.getAccount(), exampleAccount);
        assertEquals(userLevel.getCreationTime(), CREATION_TIME_NO3);
        assertEquals(userLevel.getUpdateTime(), UPDATE_TIME_NO3);
        assertEquals(userLevel.getCreatedBy(), exampleAccount.getLogin());
    }

    @Test
    public void userLevelSetUpdateTimeTestPositive() {
        LocalDateTime updateTimeBefore = exampleUserLevel.getUpdateTime();
        LocalDateTime newUpdateTime = LocalDateTime.now().plusHours(24);

        assertNotNull(updateTimeBefore);
        assertNotNull(newUpdateTime);

        exampleUserLevel.setUpdateTime(newUpdateTime);

        LocalDateTime updateTimeAfter = exampleUserLevel.getUpdateTime();

        assertNotNull(updateTimeAfter);

        assertNotEquals(updateTimeBefore, updateTimeAfter);
        assertNotEquals(updateTimeBefore, newUpdateTime);
        assertEquals(updateTimeAfter, newUpdateTime);
    }

    @Test
    public void userLevelToStringTestPositive() {
        String result = exampleUserLevel.toString();
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
        if (exampleUserLevel instanceof Client) {
            assertTrue(result.contains("Client"));
        } else if (exampleUserLevel instanceof Staff) {
            assertTrue(result.contains("Staff"));
        } else {
            assertTrue(result.contains("Admin"));
        }
    }
}
