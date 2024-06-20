package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.annotations.Login;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Staff;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AccountMessages;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountValidationTest {

    private static Validator validator;
    private static final String ACCOUNT_LOGIN_NO_1 = "AccountLoginNo1";
    private static final String ACCOUNT_PASSWORD_NO_1 = "$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa";
    private static final String ACCOUNT_NAME_NO_1 = "Kamilek";
    private static final String ACCOUNT_LASTNAME_NO_1 = "Slimaczek";
    private static final String ACCOUNT_EMAIL_NO_1 = "accountNo1@email.com";
    private static final String ACCOUNT_PHONE_NUMBER_NO_1 = "000000000";

    @BeforeAll
    static void setUp() {
        validator = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator();
    }

    @Test
    void testValidAccountSuccessful() {
        Account account = new Account(
                ACCOUNT_LOGIN_NO_1,
                ACCOUNT_PASSWORD_NO_1,
                ACCOUNT_NAME_NO_1,
                ACCOUNT_LASTNAME_NO_1,
                ACCOUNT_EMAIL_NO_1,
                ACCOUNT_PHONE_NUMBER_NO_1
        );
        account.setAccountLanguage("pl");
        account.addUserLevel(new Staff());

        Set<ConstraintViolation<Account>> constraintViolations = validator.validate(account);
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    void testValidAccountLoginFailed() {
        Account account = new Account(
                "",
                ACCOUNT_PASSWORD_NO_1,
                ACCOUNT_NAME_NO_1,
                ACCOUNT_LASTNAME_NO_1,
                ACCOUNT_EMAIL_NO_1,
                ACCOUNT_PHONE_NUMBER_NO_1
        );
        account.setAccountLanguage("pl");
        account.addUserLevel(new Staff());

        Set<ConstraintViolation<Account>> constraintViolations = validator.validate(account);
        assertFalse(constraintViolations.isEmpty());

        List<String> violationsMessages = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .sorted(Comparator.naturalOrder())
                .toList();

        assertEquals(AccountMessages.LOGIN_BLANK, violationsMessages.get(0));
        assertEquals(AccountMessages.LOGIN_REGEX_NOT_MET,violationsMessages.get(1));
        assertEquals(AccountMessages.LOGIN_TOO_SHORT, violationsMessages.get(2));
    }

    @Test
    void testValidAccountPasswordFailed() {
        Account account = new Account(
                ACCOUNT_LOGIN_NO_1,
                "",
                ACCOUNT_NAME_NO_1,
                ACCOUNT_LASTNAME_NO_1,
                ACCOUNT_EMAIL_NO_1,
                ACCOUNT_PHONE_NUMBER_NO_1
        );
        account.setAccountLanguage("pl");
        account.addUserLevel(new Staff());

        Set<ConstraintViolation<Account>> constraintViolations = validator.validate(account);
        assertFalse(constraintViolations.isEmpty());

        List<String> violationsMessages = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .sorted(Comparator.naturalOrder())
                .toList();

        assertEquals(AccountMessages.PASSWORD_BLANK, violationsMessages.get(0));
        assertEquals(AccountMessages.PASSWORD_INVALID_LENGTH, violationsMessages.get(1));
    }
}
