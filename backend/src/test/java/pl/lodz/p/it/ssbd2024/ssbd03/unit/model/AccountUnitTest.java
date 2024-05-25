package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;

import java.time.LocalDateTime;

public class AccountUnitTest {

    //ACCOUNT DATA
    private static final String ACCOUNT_LOGIN_NO_1 = "AccountLoginNo1";
    private static final String ACCOUNT_PASSWORD_NO_1 = "$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa";
    private static final String ACCOUNT_NAME_NO_1 = "AccountName1";
    private static final String ACCOUNT_LASTNAME_NO_1 = "AccountLastnameNo1";
    private static final String ACCOUNT_EMAIL_NO_1 = "accountNo1@email.com";
    private static final String ACCOUNT_PHONE_NUMBER_NO_1 = "000000000";

    //ACTIVITY LOG DATA


    @Test
    void getActivityLogForAccountWithNotNullActivityLogPositiveTest() {
        Account testAccount1 = new Account(ACCOUNT_LOGIN_NO_1, ACCOUNT_PASSWORD_NO_1, ACCOUNT_NAME_NO_1, ACCOUNT_LASTNAME_NO_1,
                ACCOUNT_EMAIL_NO_1, ACCOUNT_PHONE_NUMBER_NO_1);

        Assertions.assertNotNull(testAccount1.getActivityLog());
        Assertions.assertNull(testAccount1.getActivityLog().getLastSuccessfulLoginIp());
        Assertions.assertNull(testAccount1.getActivityLog().getLastUnsuccessfulLoginTime());
        Assertions.assertNull(testAccount1.getActivityLog().getLastUnsuccessfulLoginIp());

        LocalDateTime lastSuccessfulLoginTimeNo1 = LocalDateTime.of(2002, 10, 12, 10, 20, 30);
        LocalDateTime lastUnsuccessfulLoginTimeNo1 = LocalDateTime.of(2002, 10, 12, 9, 20, 30);

        String lastSuccessfulLoginIPNo1 = "10.10.10.10";
        String lastUnsuccessfulLoginIPNo1 = "11.11.11.11";

        testAccount1.getActivityLog().setLastSuccessfulLoginTime(lastSuccessfulLoginTimeNo1);
        testAccount1.getActivityLog().setLastUnsuccessfulLoginTime(lastUnsuccessfulLoginTimeNo1);

        testAccount1.getActivityLog().setLastSuccessfulLoginIp(lastSuccessfulLoginIPNo1);
        testAccount1.getActivityLog().setLastUnsuccessfulLoginIp(lastUnsuccessfulLoginIPNo1);

        Assertions.assertNotNull(testAccount1.getActivityLog());
        Assertions.assertEquals(lastSuccessfulLoginTimeNo1, testAccount1.getActivityLog().getLastSuccessfulLoginTime());
        Assertions.assertEquals(lastSuccessfulLoginIPNo1, testAccount1.getActivityLog().getLastSuccessfulLoginIp());
        Assertions.assertEquals(lastUnsuccessfulLoginTimeNo1, testAccount1.getActivityLog().getLastUnsuccessfulLoginTime());
        Assertions.assertEquals(lastUnsuccessfulLoginIPNo1, testAccount1.getActivityLog().getLastUnsuccessfulLoginIp());
    }

    @Test
    void getActivityLogForAccountWithNotNullActivityLogPositiveLog() {
        Account testAccount1 = new Account(ACCOUNT_LOGIN_NO_1, ACCOUNT_PASSWORD_NO_1, ACCOUNT_NAME_NO_1, ACCOUNT_LASTNAME_NO_1,
                ACCOUNT_EMAIL_NO_1, ACCOUNT_PHONE_NUMBER_NO_1);

        testAccount1.setActivityLog(null);

        Assertions.assertNotNull(testAccount1.getActivityLog());
        Assertions.assertNull(testAccount1.getActivityLog().getLastSuccessfulLoginIp());
        Assertions.assertNull(testAccount1.getActivityLog().getLastUnsuccessfulLoginTime());
        Assertions.assertNull(testAccount1.getActivityLog().getLastUnsuccessfulLoginIp());
    }

    @Test
    void couldAuthenticateReturnsTrueTest() {
        Account testAccount1 = new Account(ACCOUNT_LOGIN_NO_1, ACCOUNT_PASSWORD_NO_1, ACCOUNT_NAME_NO_1, ACCOUNT_LASTNAME_NO_1,
                ACCOUNT_EMAIL_NO_1, ACCOUNT_PHONE_NUMBER_NO_1);

        testAccount1.setBlocked(false);
        testAccount1.setActive(true);

        Assertions.assertTrue(testAccount1.couldAuthenticate());

        testAccount1.setBlocked(false);
        testAccount1.setActive(false);

        Assertions.assertTrue(testAccount1.couldAuthenticate());

        testAccount1.setBlocked(true);
        testAccount1.setActive(true);

        Assertions.assertTrue(testAccount1.couldAuthenticate());
    }

    @Test
    void couldAuthenticateReturnsFalseTest() {
        Account testAccount1 = new Account(ACCOUNT_LOGIN_NO_1, ACCOUNT_PASSWORD_NO_1, ACCOUNT_NAME_NO_1, ACCOUNT_LASTNAME_NO_1,
                ACCOUNT_EMAIL_NO_1, ACCOUNT_PHONE_NUMBER_NO_1);

        testAccount1.setBlocked(true);
        testAccount1.setActive(false);

        Assertions.assertFalse(testAccount1.couldAuthenticate());
    }

    @Test
    void equalsReturnsFalseTest() {
        Account testAccount1 = new Account(ACCOUNT_LOGIN_NO_1, ACCOUNT_PASSWORD_NO_1, ACCOUNT_NAME_NO_1, ACCOUNT_LASTNAME_NO_1,
                ACCOUNT_EMAIL_NO_1, ACCOUNT_PHONE_NUMBER_NO_1);

        Assertions.assertFalse(testAccount1.equals(null));
        Assertions.assertFalse(testAccount1.equals("String"));

    }
}