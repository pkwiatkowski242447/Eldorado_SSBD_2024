package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    private static UserLevel testUserLevel;

    private static final Client.ClientType CLIENT_TYPE = Client.ClientType.STANDARD;
    private static final Long TOTAL_RESERVATION_HOURS = 100L;

    private static Client clientUserLevel;
    private static Client testClientUserLevel;
    private static Staff staffUserLevel;
    private static Admin adminUserLevel;

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
        exampleUserLevel.setUpdateTime(LocalDateTime.now().minusHours(48));

        testUserLevel = new Admin();
        testUserLevel.setAccount(testAccount);
        testAccount.addUserLevel(testUserLevel);
        exampleUserLevel.setUpdateTime(LocalDateTime.now().minusHours(96));

        clientUserLevel = new Client();
        testClientUserLevel = new Client();
        staffUserLevel = new Staff();
        adminUserLevel = new Admin();

        Field clientTypeField = Client.class.getDeclaredField("type");
        clientTypeField.setAccessible(true);
        clientTypeField.set(clientUserLevel, CLIENT_TYPE);
        clientTypeField.set(testClientUserLevel, CLIENT_TYPE);
        clientTypeField.setAccessible(false);

        Field totalReservationHoursField = Client.class.getDeclaredField("totalReservationHours");
        totalReservationHoursField.setAccessible(true);
        totalReservationHoursField.set(clientUserLevel, TOTAL_RESERVATION_HOURS);
        totalReservationHoursField.set(testClientUserLevel, TOTAL_RESERVATION_HOURS);
        totalReservationHoursField.setAccessible(false);
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

    @WithMockUser(username = "ExampleUser", roles = {"ADMIN"})
    @Test
    public void userLevelBeforePersistingToTheDatabaseTest() throws Exception {
        String userName = "ExampleUser";

        Field creationTimeField = UserLevel.class.getDeclaredField("creationTime");
        creationTimeField.setAccessible(true);
        creationTimeField.set(testUserLevel, LocalDateTime.now().minusHours(36));
        creationTimeField.setAccessible(false);

        Field createdByField = UserLevel.class.getDeclaredField("createdBy");
        createdByField.setAccessible(true);
        createdByField.set(testUserLevel, exampleAccount.getLogin());
        createdByField.setAccessible(false);

        LocalDateTime creationTimeBefore = testUserLevel.getCreationTime();
        assertNotNull(creationTimeBefore);
        String createdByBefore = testUserLevel.getCreatedBy();
        assertNotNull(createdByBefore);

        Method beforePersistingToTheDatabase = UserLevel.class.getDeclaredMethod("beforePersistingToTheDatabase");
        beforePersistingToTheDatabase.setAccessible(true);
        beforePersistingToTheDatabase.invoke(testUserLevel);
        beforePersistingToTheDatabase.setAccessible(false);

        LocalDateTime creationTimeAfter = testUserLevel.getCreationTime();
        assertNotNull(creationTimeAfter);
        String createdByAfter = testUserLevel.getCreatedBy();
        assertNotNull(createdByAfter);

        assertNotEquals(creationTimeBefore, creationTimeAfter);
        assertTrue(creationTimeAfter.isAfter(creationTimeBefore));

        assertNotEquals(createdByBefore, userName);
    }

    @Test
    public void userLevelBeforeUpdatingInTheDatabase() throws Exception {
        LocalDateTime updateTimeBefore = exampleUserLevel.getUpdateTime();
        assertNotNull(updateTimeBefore);

        Method beforeUpdatingInTheDatabase = UserLevel.class.getDeclaredMethod("beforeUpdatingInTheDatabase");
        beforeUpdatingInTheDatabase.setAccessible(true);
        beforeUpdatingInTheDatabase.invoke(exampleUserLevel);
        beforeUpdatingInTheDatabase.setAccessible(false);

        LocalDateTime updateTimeAfter = exampleUserLevel.getUpdateTime();
        assertNotNull(updateTimeAfter);

        assertNotEquals(updateTimeBefore, updateTimeAfter);
    }

    @Test
    public void clientToStringTestPositive() {
        assertNotNull(clientUserLevel);
        String result = clientUserLevel.toString();
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
        assertTrue(result.contains("Client"));
    }

    @Test
    public void clientConstructorWithVersionTestPositive() {
        Long version = 21L;
        Client testClient = new Client(version);
        assertNotNull(testClient);
        assertEquals(testClient.getVersion(), version);
    }

    @Test
    public void clientAllArgsConstructorAndGettersTestPositive() {
        assertNotNull(clientUserLevel);
        assertEquals(clientUserLevel.getType(), CLIENT_TYPE);
        assertEquals(clientUserLevel.getTotalReservationHours(), TOTAL_RESERVATION_HOURS);
    }

    @Test
    public void clientSetClientTypeTestPositive() {
        Client.ClientType clientTypeBefore = testClientUserLevel.getType();
        Client.ClientType newClientType = Client.ClientType.PREMIUM;
        assertNotNull(clientTypeBefore);
        assertNotNull(newClientType);

        testClientUserLevel.setType(newClientType);

        Client.ClientType clientTypeAfter = testClientUserLevel.getType();
        assertNotNull(clientTypeAfter);

        assertNotEquals(clientTypeBefore, newClientType);
        assertNotEquals(clientTypeBefore, clientTypeAfter);
        assertEquals(clientTypeAfter, newClientType);
    }

    @Test
    public void clientSetTotalReservationHoursTestPositive() {
        Long totalReservationHoursBefore = testClientUserLevel.getTotalReservationHours();
        Long newTotalReservationHours = 150L;

        assertNotNull(totalReservationHoursBefore);
        assertNotNull(newTotalReservationHours);

        testClientUserLevel.setTotalReservationHours(newTotalReservationHours);

        Long totalReservationHoursAfter = testClientUserLevel.getTotalReservationHours();

        assertNotNull(totalReservationHoursAfter);

        assertNotEquals(totalReservationHoursBefore, newTotalReservationHours);
        assertNotEquals(totalReservationHoursBefore, totalReservationHoursAfter);
        assertEquals(totalReservationHoursAfter, newTotalReservationHours);
    }

    @Test
    public void staffToStringTestPositive() {
        assertNotNull(staffUserLevel);
        String result = staffUserLevel.toString();
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
        assertTrue(result.contains("Staff"));
    }

    @Test
    public void staffConstructorWithVersionTestPositive() {
        Long version = 22L;
        Staff testStaff = new Staff(version);
        assertNotNull(testStaff);
        assertEquals(testStaff.getVersion(), version);
    }

    @Test
    public void adminToStringTestPositive() {
        assertNotNull(adminUserLevel);
        String result = adminUserLevel.toString();
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
        assertTrue(result.contains("Admin"));
    }

    @Test
    public void adminConstructorWithVersionTestPositive() {
        Long version = 23L;
        Admin testAdmin = new Admin(version);
        assertNotNull(testAdmin);
        assertEquals(testAdmin.getVersion(), version);
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
