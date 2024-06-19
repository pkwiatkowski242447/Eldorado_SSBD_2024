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

public class TokenUnitTest {

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

    private static String TOKEN_VALUE_NO1 = "ExampleTokenNo1";
    private static String TOKEN_VALUE_NO2 = "ExampleTokenNo2";

    private static Token.TokenType TOKEN_TYPE_NO1 = Token.TokenType.REGISTER;
    private static Token.TokenType TOKEN_TYPE_NO2 = Token.TokenType.REFRESH_TOKEN;

    private static LocalDateTime CREATION_TIME_NO1 = LocalDateTime.now().minusHours(12);
    private static LocalDateTime CREATION_TIME_NO2 = LocalDateTime.now().minusHours(24);

    private static Account exampleAccount;
    private static Account testAccount;

    private static Token exapmleToken;
    private static Token testToken;

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

        testAccount.setActivityLog(activityLog);

        previousPasswords = Account.class.getDeclaredField("previousPasswords");
        previousPasswords.setAccessible(true);
        previousPasswords.set(testAccount, pastPasswords);
        previousPasswords.setAccessible(false);

        exapmleToken = new Token(TOKEN_VALUE_NO1, exampleAccount, TOKEN_TYPE_NO1);
        testToken = new Token(TOKEN_VALUE_NO2, testAccount, TOKEN_TYPE_NO2);

        Field creationTimeField = Token.class.getDeclaredField("creationTime");
        creationTimeField.setAccessible(true);
        creationTimeField.set(exapmleToken, CREATION_TIME_NO1);
        creationTimeField.set(testToken, CREATION_TIME_NO2);
        creationTimeField.setAccessible(false);

        Field createdByField = Token.class.getDeclaredField("createdBy");
        createdByField.setAccessible(true);
        createdByField.set(exapmleToken, exampleAccount.getLogin());
        createdByField.set(testToken, testAccount.getLogin());
        createdByField.setAccessible(false);
    }

    @Test
    public void tokenNoArgsConstructorTestPositive() {
        Token token = new Token();
        assertNotNull(token);
    }

    @Test
    public void tokenAllArgsConstructorAndGettersTestPositive() {
        assertNotNull(exapmleToken);
        assertEquals(exapmleToken.getTokenValue(), TOKEN_VALUE_NO1);
        assertEquals(exapmleToken.getAccount(), exampleAccount);
        assertEquals(exapmleToken.getType(), TOKEN_TYPE_NO1);
        assertEquals(exapmleToken.getCreationTime(), CREATION_TIME_NO1);
        assertEquals(exapmleToken.getCreatedBy(), exampleAccount.getLogin());
    }

    @WithMockUser(username = "UniqueLogin", roles = {"ADMIN"})
    @Test
    public void tokenBeforePersistingToTheDatabaseTestPositive() throws Exception {
        String userName = "UniqueLogin";
        LocalDateTime creationTimeBefore = testToken.getCreationTime();
        String createdByBefore = testToken.getCreatedBy();

        assertNotNull(creationTimeBefore);
        assertNotNull(createdByBefore);

        Method beforePersistingToTheDatabase = Token.class.getDeclaredMethod("beforePersistingToTheDatabase");
        beforePersistingToTheDatabase.setAccessible(true);
        beforePersistingToTheDatabase.invoke(testToken);
        beforePersistingToTheDatabase.setAccessible(false);

        LocalDateTime creationTimeAfter = testToken.getCreationTime();
        String createdByAfter = testToken.getCreatedBy();
        assertNotNull(creationTimeAfter);
        assertNotNull(createdByAfter);

        assertNotEquals(creationTimeBefore, creationTimeAfter);
        assertNotEquals(createdByBefore, userName);
    }

    @Test
    public void tokenToStringTestPositive() {
        String result = exapmleToken.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
        assertTrue(result.contains("Token"));
    }
}
