package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AttributeRecordUnitTest {

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

    private static final String ATTRIBUTE_NAME_NO1 = "Timezone";
    private static final String ATTRIBUTE_NAME_NO2 = "Theme";

    private static final String TIMEZONE_VALUE_NO1 = "GMT+0";
    private static final String TIMEZONE_VALUE_NO2 = "GMT+1";
    private static final String TIMEZONE_VALUE_NO3 = "GMT+2";
    private static final String TIMEZONE_VALUE_NO4 = "GMT+3";
    private static final String TIMEZONE_VALUE_NO5 = "GMT+4";
    private static final String TIMEZONE_VALUE_NO6 = "GMT+5";
    private static final String TIMEZONE_VALUE_NO7 = "GMT+6";
    private static final String TIMEZONE_VALUE_NO8 = "GMT+7";
    private static final String TIMEZONE_VALUE_NO9 = "GMT+8";
    private static final String TIMEZONE_VALUE_NO10 = "GMT+9";
    private static final String TIMEZONE_VALUE_NO11 = "GMT+10";
    private static final String TIMEZONE_VALUE_NO12 = "GMT+11";
    private static final String TIMEZONE_VALUE_NO13 = "GMT+12";

    private static final String THEME_VALUE_NO1 = "Dark";
    private static final String THEME_VALUE_NO2 = "Light";

    private static AttributeValue TIMEZONE_NO1;
    private static AttributeValue TIMEZONE_NO2;
    private static AttributeValue TIMEZONE_NO3;
    private static AttributeValue TIMEZONE_NO4;
    private static AttributeValue TIMEZONE_NO5;
    private static AttributeValue TIMEZONE_NO6;
    private static AttributeValue TIMEZONE_NO7;
    private static AttributeValue TIMEZONE_NO8;
    private static AttributeValue TIMEZONE_NO9;
    private static AttributeValue TIMEZONE_NO10;
    private static AttributeValue TIMEZONE_NO11;
    private static AttributeValue TIMEZONE_NO12;
    private static AttributeValue TIMEZONE_NO13;

    private static AttributeValue THEME_NO1;
    private static AttributeValue THEME_NO2;

    private static AttributeName exampleAttribute;
    private static AttributeName testAttribute;

    private static AttributeRecord exampleRecord;
    private static AttributeRecord testRecord;

    private static Account exampleAccount;
    private static Account testAccount;

    @BeforeAll
    public static void setUp() throws Exception {
        exampleAttribute = new AttributeName(ATTRIBUTE_NAME_NO1);

        TIMEZONE_NO1 = new AttributeValue(TIMEZONE_VALUE_NO1, exampleAttribute);
        TIMEZONE_NO2 = new AttributeValue(TIMEZONE_VALUE_NO2, exampleAttribute);
        TIMEZONE_NO3 = new AttributeValue(TIMEZONE_VALUE_NO3, exampleAttribute);
        TIMEZONE_NO4 = new AttributeValue(TIMEZONE_VALUE_NO4, exampleAttribute);
        TIMEZONE_NO5 = new AttributeValue(TIMEZONE_VALUE_NO5, exampleAttribute);
        TIMEZONE_NO6 = new AttributeValue(TIMEZONE_VALUE_NO6, exampleAttribute);
        TIMEZONE_NO7 = new AttributeValue(TIMEZONE_VALUE_NO7, exampleAttribute);
        TIMEZONE_NO8 = new AttributeValue(TIMEZONE_VALUE_NO8, exampleAttribute);
        TIMEZONE_NO9 = new AttributeValue(TIMEZONE_VALUE_NO9, exampleAttribute);
        TIMEZONE_NO10 = new AttributeValue(TIMEZONE_VALUE_NO10, exampleAttribute);
        TIMEZONE_NO11 = new AttributeValue(TIMEZONE_VALUE_NO11, exampleAttribute);
        TIMEZONE_NO12 = new AttributeValue(TIMEZONE_VALUE_NO12, exampleAttribute);
        TIMEZONE_NO13 = new AttributeValue(TIMEZONE_VALUE_NO13, exampleAttribute);

        testAttribute = new AttributeName(ATTRIBUTE_NAME_NO2);

        THEME_NO1 = new AttributeValue(THEME_VALUE_NO1, testAttribute);
        THEME_NO2 = new AttributeValue(THEME_VALUE_NO2, testAttribute);

        Field listOfAttributeValues = AttributeName.class.getDeclaredField("listOfAttributeValues");
        listOfAttributeValues.setAccessible(true);

        listOfAttributeValues.set(exampleAttribute, List.of(
                TIMEZONE_NO1,
                TIMEZONE_NO2,
                TIMEZONE_NO3,
                TIMEZONE_NO4,
                TIMEZONE_NO5,
                TIMEZONE_NO6,
                TIMEZONE_NO7,
                TIMEZONE_NO8,
                TIMEZONE_NO9,
                TIMEZONE_NO10,
                TIMEZONE_NO11,
                TIMEZONE_NO12,
                TIMEZONE_NO13
        ));

        listOfAttributeValues.set(testAttribute, List.of(
                THEME_NO1,
                THEME_NO2
        ));

        listOfAttributeValues.setAccessible(false);

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

        Field id = AbstractEntity.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(exampleAccount, UUID.randomUUID());
        id.set(testAccount, UUID.randomUUID());
        id.setAccessible(false);

        Field version = AbstractEntity.class.getDeclaredField("version");
        version.setAccessible(true);
        version.set(exampleAccount, 3L);
        version.set(testAccount, 5L);
        version.setAccessible(false);

        exampleRecord = new AttributeRecord(exampleAttribute, TIMEZONE_NO1, Set.of(exampleAccount));
        testRecord = new AttributeRecord(testAttribute, THEME_NO1, Set.of(exampleAccount));
    }

    @Test
    public void attributeRecordNoArgsConstructorTestPositive() {
        AttributeRecord record = new AttributeRecord();
        assertNotNull(record);
    }

    @Test
    public void attributeRecordAllArgsConstructorTestPositive() {
        assertNotNull(exampleRecord);
        assertEquals(exampleRecord.getAttributeName(), exampleAttribute);
        assertEquals(exampleRecord.getAttributeValue(), TIMEZONE_NO1);
        assertFalse(exampleRecord.getSetOfAccounts().isEmpty());
        assertEquals(1, exampleRecord.getSetOfAccounts().size());
    }

    @Test
    public void attributeRecordSetAttributeNameTestPositive() {
        AttributeName attributeNameBefore = testRecord.getAttributeName();
        AttributeName newAttributeName = new AttributeName();

        assertNotNull(attributeNameBefore);
        assertNotNull(newAttributeName);

        testRecord.setAttributeName(newAttributeName);

        AttributeName attributeNameAfter = testRecord.getAttributeName();

        assertNotNull(attributeNameAfter);

        assertNotEquals(attributeNameBefore, newAttributeName);
        assertNotEquals(attributeNameBefore, attributeNameAfter);
        assertEquals(newAttributeName, attributeNameAfter);
    }

    @Test
    public void attributeRecordSetAttributeValueTestPositive() {
        AttributeValue attributeValueBefore = testRecord.getAttributeValue();
        AttributeValue newAttributeValue = new AttributeValue();

        assertNotNull(attributeValueBefore);
        assertNotNull(newAttributeValue);

        testRecord.setAttributeValue(newAttributeValue);

        AttributeValue attributeValueAfter = testRecord.getAttributeValue();

        assertNotNull(attributeValueAfter);

        assertNotEquals(attributeValueBefore, newAttributeValue);
        assertNotEquals(attributeValueBefore, attributeValueAfter);
        assertEquals(newAttributeValue, attributeValueAfter);
    }

    @Test
    public void attributeRecordToStringTestPositive() {
        String result = exampleRecord.toString();
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
        assertTrue(result.contains("AttributeRecord"));
    }
}
