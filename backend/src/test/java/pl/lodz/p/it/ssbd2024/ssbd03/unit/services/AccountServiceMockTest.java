package pl.lodz.p.it.ssbd2024.ssbd03.unit.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationOptimisticLockException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountUserLevelException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountAlreadyBlockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountAlreadyUnblockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountEmailAlreadyTakenException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountSameEmailException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.integrity.UserLevelMissingException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountEmailNullException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountIdNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.resetOwnPassword.CurrentPasswordAndNewPasswordAreTheSameException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.resetOwnPassword.IncorrectPasswordException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountBlockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountNotActivatedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.TokenNotValidException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.read.TokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.IllegalOperationException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.UserLevelFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations.AccountService;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;

import java.lang.reflect.Field;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@SecurityTestExecutionListeners
public class AccountServiceMockTest {
    @Mock
    private MailProvider mailProvider;
    @Mock
    private JWTProvider jwtProvider;
    @Mock
    private TokenFacade tokenFacade;
    @Mock
    private AccountMOKFacade accountMOKFacade;
    @Mock
    private UserLevelFacade userLevelFacade;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private AccountService accountService;

    @Test
    void registerClientTestSuccessful() throws ApplicationBaseException {
        String password = "P@ssw0rd!";
        String testToken = "TestTokenValue";

        doNothing().when(accountMOKFacade).create(any(Account.class));
        when(encoder.encode(password)).thenReturn(new BCryptPasswordEncoder().encode(password));
        when(jwtProvider.generateActionToken(any(Account.class), anyInt(), any())).thenReturn(testToken);

        accountService.registerClient("Testowy", password, "Imie",
                "Nazwisko", "test@example.com", "123123123", "pl");

        Mockito.verify(accountMOKFacade, Mockito.times(1)).create(any(Account.class));
    }

    @Test
    void registerStaffTestSuccessful() throws ApplicationBaseException {
        String password = "P@ssw0rd!";
        String testToken = "TestTokenValue";

        doNothing().when(accountMOKFacade).create(any(Account.class));
        when(encoder.encode(password)).thenReturn(new BCryptPasswordEncoder().encode(password));
        doNothing().when(mailProvider).sendRegistrationConfirmEmail(any(), any(), any(), any(), any());
        when(jwtProvider.generateActionToken(any(Account.class), anyInt(), any())).thenReturn(testToken);

        accountService.registerStaff("Testowy", password, "Imie",
                "Nazwisko", "test@example.com", "123123123", "pl");
        Mockito.verify(accountMOKFacade, Mockito.times(1)).create(any(Account.class));
    }

    @Test
    void registerAdminTestSuccessful() throws ApplicationBaseException {
        String password = "P@ssw0rd!";
        String testToken = "TestTokenValue";

        doNothing().when(accountMOKFacade).create(any(Account.class));
        when(encoder.encode(password)).thenReturn(new BCryptPasswordEncoder().encode(password));
        doNothing().when(mailProvider).sendRegistrationConfirmEmail(any(), any(), any(), any(), any());
        when(jwtProvider.generateActionToken(any(Account.class), anyInt(), any())).thenReturn(testToken);

        accountService.registerAdmin("Testowy", password, "Imie",
                "Nazwisko", "test@example.com", "123123123", "pl");
        Mockito.verify(accountMOKFacade, Mockito.times(1)).create(any(Account.class));
    }

    @Test
    void confirmEmailTestSuccessful() throws ApplicationBaseException {
        String tokenVal = "TU9DSyBUT0tFTg==";
        String decodedTokenVal = new String(Base64.getDecoder().decode(tokenVal));
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Token token = new Token(decodedTokenVal, account, Token.TokenType.CONFIRM_EMAIL);

        when(tokenFacade.findByTokenValue(decodedTokenVal)).thenReturn(Optional.of(token));
        when(accountMOKFacade.find(account.getId())).thenReturn(Optional.of(account));
        when(jwtProvider.isTokenValid(decodedTokenVal, account)).thenReturn(true);
        when(jwtProvider.extractEmail(decodedTokenVal)).thenReturn("new@email.com");
        doNothing().when(accountMOKFacade).edit(account);
        doNothing().when(tokenFacade).remove(token);

        assertTrue(accountService.confirmEmail(tokenVal));
    }

    @Test
    void confirmEmailTestTokenInvalid() throws ApplicationBaseException {
        String tokenVal = "TU9DSyBUT0tFTg==";
        String decodedTokenVal = new String(Base64.getDecoder().decode(tokenVal));
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Token token = new Token(decodedTokenVal, account, Token.TokenType.CONFIRM_EMAIL);

        when(tokenFacade.findByTokenValue(decodedTokenVal)).thenReturn(Optional.of(token));
        when(accountMOKFacade.find(account.getId())).thenReturn(Optional.of(account));
        when(jwtProvider.isTokenValid(decodedTokenVal, account)).thenReturn(false);

        assertFalse(accountService.confirmEmail(tokenVal));
    }

    @Test
    void confirmEmailTestTokenNotInDatabase() {
        String tokenVal = "TU9DSyBUT0tFTg==";
        String decodedTokenVal = new String(Base64.getDecoder().decode(tokenVal));

        when(tokenFacade.findByTokenValue(decodedTokenVal)).thenReturn(Optional.empty());

        assertThrows(TokenNotFoundException.class, () -> accountService.confirmEmail(tokenVal));
    }

    @Test
    void confirmEmailTestAccountNotFound() throws ApplicationBaseException {
        String tokenVal = "TU9DSyBUT0tFTg==";
        String decodedTokenVal = new String(Base64.getDecoder().decode(tokenVal));
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Token token = new Token(decodedTokenVal, account, Token.TokenType.CONFIRM_EMAIL);

        when(tokenFacade.findByTokenValue(decodedTokenVal)).thenReturn(Optional.of(token));
        when(accountMOKFacade.find(account.getId())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.confirmEmail(tokenVal));
    }

    @Test
    void confirmEmailTestNullEmail() throws ApplicationBaseException {
        String tokenVal = "TU9DSyBUT0tFTg==";
        String decodedTokenVal = new String(Base64.getDecoder().decode(tokenVal));
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Token token = new Token(decodedTokenVal, account, Token.TokenType.CONFIRM_EMAIL);

        when(tokenFacade.findByTokenValue(decodedTokenVal)).thenReturn(Optional.of(token));
        when(accountMOKFacade.find(account.getId())).thenReturn(Optional.of(account));
        when(jwtProvider.isTokenValid(decodedTokenVal, account)).thenReturn(true);
        when(jwtProvider.extractEmail(decodedTokenVal)).thenReturn(null);

        assertThrows(AccountEmailNullException.class, () -> accountService.confirmEmail(tokenVal));
    }

    @Test
    void changeEmailTestSuccessful() throws ApplicationBaseException, NoSuchFieldException, IllegalAccessException {
        Field emailChangeConfirmationPeriodLengthHoursField = AccountService.class.getDeclaredField("emailChangeConfirmationPeriodLengthHours");
        emailChangeConfirmationPeriodLengthHoursField.setAccessible(true);
        emailChangeConfirmationPeriodLengthHoursField.set(accountService, 24);
        emailChangeConfirmationPeriodLengthHoursField.setAccessible(false);

        Account a = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        a.setAccountLanguage("pl");
        String newEmail = "new@email.com";

        when(accountMOKFacade.find(any())).thenReturn(Optional.of(a));
        when(tokenFacade.findByTypeAndAccount(Token.TokenType.CONFIRM_EMAIL, a.getId())).thenReturn(Optional.empty());
        when(jwtProvider.generateEmailToken(a, newEmail, 24)).thenReturn("TOKEN VALUE");
        doNothing().when(tokenFacade).create(any(Token.class));
        doNothing().when(mailProvider).sendEmailConfirmEmail(eq(a.getName()), eq(a.getLastname()), eq(newEmail), any(), eq(a.getAccountLanguage()));

        accountService.changeEmail(UUID.randomUUID(), newEmail);
        Mockito.verify(mailProvider, Mockito.times(1)).sendEmailConfirmEmail(eq(a.getName()), eq(a.getLastname()), eq(newEmail), any(), eq(a.getAccountLanguage()));
    }

    @Test
    void changeEmailTestAccountSameEmail() throws ApplicationBaseException {
        Account a = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        a.setAccountLanguage("pl");
        String newEmail = "test@email.com";

        when(accountMOKFacade.find(any())).thenReturn(Optional.of(a));

        assertThrows(AccountSameEmailException.class, () -> accountService.changeEmail(UUID.randomUUID(), newEmail));
    }

    @Test
    void changeEmailTestAccountEmailAlreadyTaken() throws ApplicationBaseException {
        String newEmail = "new@email.com";
        Account a = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Account a1 = new Account("login1", "TestPassword1", "firstName1", "lastName1", newEmail, "123123124");
        a.setAccountLanguage("pl");
        a1.setAccountLanguage("pl");

        when(accountMOKFacade.find(any())).thenReturn(Optional.of(a));
        when(accountMOKFacade.findByEmail(any())).thenReturn(Optional.of(a1));

        assertThrows(AccountEmailAlreadyTakenException.class, () -> accountService.changeEmail(UUID.randomUUID(), newEmail));
    }

    @Test
    void activateAccountTestSuccessful() throws ApplicationBaseException {
        String tokenVal = "TU9DSyBUT0tFTg==";
        String decodedTokenVal = new String(Base64.getDecoder().decode(tokenVal));
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Token token = new Token(decodedTokenVal, account, Token.TokenType.REGISTER);

        when(tokenFacade.findByTokenValue(decodedTokenVal)).thenReturn(Optional.of(token));
        when(jwtProvider.extractAccountId(decodedTokenVal)).thenReturn(account.getId());
        when(accountMOKFacade.find(account.getId())).thenReturn(Optional.of(account));
        when(jwtProvider.isTokenValid(decodedTokenVal, account)).thenReturn(true);
        doNothing().when(accountMOKFacade).edit(account);
        when(tokenFacade.findByTokenValue(decodedTokenVal)).thenReturn(Optional.of(token));
        doNothing().when(tokenFacade).remove(token);

        assertFalse(account.getActive());
        assertTrue(accountService.activateAccount(tokenVal));
        assertTrue(account.getActive());
    }

    @Test
    void activateAccountTestWithTokenNotBeingBase64String() {
        String tokenVal = "InvalidToken...";
        assertThrows(IllegalArgumentException.class, () -> accountService.activateAccount(tokenVal));
    }

    @Test
    void activateAccountTestWhenTokenObjectIsNotFound() {
        String tokenVal = "TU9DSyBUT0tFTg==";
        String decodedTokenVal = new String(Base64.getDecoder().decode(tokenVal));

        when(tokenFacade.findByTokenValue(decodedTokenVal)).thenReturn(Optional.empty());

        assertThrows(TokenNotFoundException.class, () -> accountService.activateAccount(tokenVal));
    }

    @Test
    void activateAccountTestWhenAccountObjectIsNotFound() throws ApplicationBaseException {
        String tokenVal = "TU9DSyBUT0tFTg==";
        String decodedTokenVal = new String(Base64.getDecoder().decode(tokenVal));
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Token token = new Token(decodedTokenVal, account, Token.TokenType.REGISTER);

        when(tokenFacade.findByTokenValue(decodedTokenVal)).thenReturn(Optional.of(token));
        when(accountMOKFacade.find(account.getId())).thenReturn(Optional.empty());

        assertThrows(AccountIdNotFoundException.class, () -> accountService.activateAccount(tokenVal));
    }

    @Test
    void activateAccountTestTokenInvalid() throws ApplicationBaseException {
        String tokenVal = "TU9DSyBUT0tFTg==";
        String decodedTokenVal = new String(Base64.getDecoder().decode(tokenVal));
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Token token = new Token(decodedTokenVal, account, Token.TokenType.REGISTER);

        when(tokenFacade.findByTokenValue(decodedTokenVal)).thenReturn(Optional.of(token));
        when(accountMOKFacade.find(account.getId())).thenReturn(Optional.of(account));
        when(jwtProvider.extractAccountId(decodedTokenVal)).thenReturn(account.getId());
        when(jwtProvider.isTokenValid(decodedTokenVal, account)).thenReturn(false);

        assertFalse(account.getActive());
        assertFalse(accountService.activateAccount(tokenVal));
        assertFalse(account.getActive());
    }

    @Test
    void blockAccountTestSuccessful() throws AccountAlreadyBlockedException, IllegalOperationException, AccountNotFoundException, ApplicationBaseException {

        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.findAndRefresh(id)).thenReturn(Optional.of(account));
        doNothing().when(accountMOKFacade).edit(account);
        doNothing().when(mailProvider).sendBlockAccountInfoEmail(account.getName(), account.getLastname(), account.getEmail(), account.getAccountLanguage(), true);

        assertFalse(account.getBlocked());
        accountService.blockAccount(id);
        assertTrue(account.getBlocked());
    }

    @Test
    void blockAccountTestBlockedBySystem() throws AccountAlreadyBlockedException, AccountNotFoundException, ApplicationBaseException {

        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UUID id = UUID.randomUUID();
        account.blockAccount(false);

        when(accountMOKFacade.findAndRefresh(id)).thenReturn(Optional.of(account));
        doNothing().when(accountMOKFacade).edit(account);
        doNothing().when(mailProvider).sendBlockAccountInfoEmail(account.getName(), account.getLastname(), account.getEmail(), account.getAccountLanguage(), true);

        assertTrue(account.getBlocked());
        assertNotNull(account.getBlockedTime());

        accountService.blockAccount(id);

        assertTrue(account.getBlocked());
        assertNull(account.getBlockedTime());
    }

    @Test
    void blockAccountTestAccountAlreadyBlocked() {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        account.blockAccount(true);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.findAndRefresh(id)).thenReturn(Optional.of(account));

        assertTrue(account.getBlocked());
        assertThrows(AccountAlreadyBlockedException.class, () -> accountService.blockAccount(id));
        assertTrue(account.getBlocked());
    }

    @Test
    void blockAccountTestAccountNotFound() {
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.findAndRefresh(id)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.blockAccount(id));
    }

    @Test
    void unblockAccountTestSuccessful() throws AccountNotFoundException, AccountAlreadyUnblockedException, ApplicationBaseException {

        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        account.blockAccount(true);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.findAndRefresh(id)).thenReturn(Optional.of(account));
        doNothing().when(accountMOKFacade).edit(account);
        doNothing().when(mailProvider).sendUnblockAccountInfoEmail(account.getName(), account.getLastname(), account.getEmail(), account.getAccountLanguage());

        assertTrue(account.getBlocked());
        accountService.unblockAccount(id);
        assertFalse(account.getBlocked());
    }

    @Test
    void unblockAccountTestAccountAlreadyUnblocked() {

        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.findAndRefresh(id)).thenReturn(Optional.of(account));

        assertFalse(account.getBlocked());
        assertThrows(AccountAlreadyUnblockedException.class, () -> accountService.unblockAccount(id));
        assertFalse(account.getBlocked());
    }

    @Test
    void unblockAccountTestAccountNotFound() {
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.findAndRefresh(id)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.unblockAccount(id));
    }

    @Test
    void getAccountsByMatchingLoginFirstNameAndLastNameTest() {

        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Account account1 = new Account("login1", "TestPassword1", "firstName1", "lastName1", "test1@email.com", "123123124");
        List<Account> accountList = List.of(account, account1);
        String login = "login";
        String firstName = "firstName";
        String lastName = "lastName";
        boolean order = true;
        boolean active = true;
        int pageNumber = 0;
        int pageSize = 5;

        when(accountMOKFacade
                .findAllAccountsByActiveAndLoginAndUserFirstNameAndUserLastNameWithPagination(login, firstName, lastName, active, order, pageNumber, pageSize))
                .thenReturn(accountList);

        var retList = accountService.getAccountsByMatchingLoginFirstNameAndLastName(login, firstName, lastName, active, order, pageNumber, pageSize);

        assertEquals(account, retList.get(0));
        assertEquals(account1, retList.get(1));

        Mockito.verify(accountMOKFacade, Mockito.times(1))
                .findAllAccountsByActiveAndLoginAndUserFirstNameAndUserLastNameWithPagination(login, firstName, lastName, active, order, pageNumber, pageSize);
    }

    @Test
    void getAccountByLoginTest() throws ApplicationBaseException {

        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");

        when(accountMOKFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));

        assertEquals(account, accountService.getAccountByLogin(account.getLogin()));

        Mockito.verify(accountMOKFacade, Mockito.times(1))
                .findByLogin(account.getLogin());
    }

    @Test
    void getAllAccountsTest() {

        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Account account1 = new Account("login1", "TestPassword1", "firstName1", "lastName1", "test1@email.com", "123123124");
        List<Account> accountList = List.of(account, account1);
        int pageNumber = 0;
        int pageSize = 5;

        when(accountMOKFacade
                .findAllAccountsWithPagination(pageNumber, pageSize)).thenReturn(accountList);

        var retList = accountService.getAllAccounts(pageNumber, pageSize);
        assertEquals(account, retList.get(0));
        assertEquals(account1, retList.get(1));

        Mockito.verify(accountMOKFacade, Mockito.times(1))
                .findAllAccountsWithPagination(pageNumber, pageSize);
    }

    @Test
    void getAccountByIdTest() throws ApplicationBaseException {

        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.findAndRefresh(id)).thenReturn(Optional.of(account));

        assertEquals(account, accountService.getAccountById(id));

        Mockito.verify(accountMOKFacade, Mockito.times(1)).findAndRefresh(id);
    }

    @Test
    void addClientUserLevelTestSuccessful() throws ApplicationBaseException {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UserLevel userLevelStaff = new Staff();
        account.addUserLevel(userLevelStaff);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.of(account));
        doNothing().when(accountMOKFacade).edit(account);
        doNothing().when(userLevelFacade).create(any(Client.class));

        accountService.addClientUserLevel(id.toString());
        var userLevels = account.getUserLevels();
        assertTrue(userLevels.stream().anyMatch(userLevel -> userLevel instanceof Client));
    }

    @Test
    void addClientUserLevelTestAccountNotFound() throws ApplicationBaseException {
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.addClientUserLevel(id.toString()));
    }

    @Test
    void addClientUserLevelTestUserLevelDuplicated() throws ApplicationBaseException {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UserLevel userLevelClient = new Client();
        account.addUserLevel(userLevelClient);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.of(account));

        assertThrows(AccountUserLevelException.class, () -> accountService.addClientUserLevel(id.toString()));
    }

    @Test
    void addStaffUserLevelTestSuccessful() throws ApplicationBaseException {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UserLevel userLevelClient = new Client();
        account.addUserLevel(userLevelClient);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.of(account));
        doNothing().when(accountMOKFacade).edit(account);
        doNothing().when(userLevelFacade).create(any(Staff.class));

        accountService.addStaffUserLevel(id.toString());
        var userLevels = account.getUserLevels();
        assertTrue(userLevels.stream().anyMatch(userLevel -> userLevel instanceof Staff));
    }
    @Test
    void addStaffUserLevelTestAccountNotFound() throws ApplicationBaseException {
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.addStaffUserLevel(id.toString()));
    }


    @Test
    void addStaffUserLevelTestUserLevelDuplicated() throws ApplicationBaseException {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UserLevel userLevelStaff = new Staff();
        account.addUserLevel(userLevelStaff);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.of(account));

        assertThrows(AccountUserLevelException.class, () -> accountService.addStaffUserLevel(id.toString()));
    }

    @Test
    void addAdminUserLevelTestSuccessful() throws ApplicationBaseException {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UserLevel userLevelClient = new Client();
        account.addUserLevel(userLevelClient);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.of(account));
        doNothing().when(accountMOKFacade).edit(account);
        doNothing().when(userLevelFacade).create(any(Admin.class));

        accountService.addAdminUserLevel(id.toString());
        var userLevels = account.getUserLevels();
        assertTrue(userLevels.stream().anyMatch(userLevel -> userLevel instanceof Admin));
    }

    @Test
    void addAdminUserLevelTestAccountNotFound() throws ApplicationBaseException {
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.addAdminUserLevel(id.toString()));
    }

    @Test
    void addAdminUserLevelTestUserLevelDuplicated() throws ApplicationBaseException {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UserLevel userLevelAdmin = new Admin();
        account.addUserLevel(userLevelAdmin);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.of(account));

        assertThrows(AccountUserLevelException.class, () -> accountService.addAdminUserLevel(id.toString()));
    }

    @Test
    void removeClientUserLevelTestSuccessful() throws ApplicationBaseException {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UserLevel userLevelStaff = new Staff();
        account.addUserLevel(userLevelStaff);
        UserLevel userLevelClient = new Client();
        account.addUserLevel(userLevelClient);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.of(account));
        doNothing().when(accountMOKFacade).edit(account);
        doNothing().when(userLevelFacade).remove(any(Client.class));

        accountService.removeClientUserLevel(id.toString());
        var userLevels = account.getUserLevels();
        assertTrue(userLevels.stream().noneMatch(userLevel -> userLevel instanceof Client));
    }

    @Test
    void removeClientUserLevelTestAccountNotFound() throws ApplicationBaseException {
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.removeClientUserLevel(id.toString()));
    }

    @Test
    void removeClientUserLevelTestNoSuchUserLevel() throws ApplicationBaseException {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UserLevel userLevelStaff = new Staff();
        account.addUserLevel(userLevelStaff);
        UserLevel userLevelAdmin = new Admin();
        account.addUserLevel(userLevelAdmin);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.of(account));

        assertThrows(AccountUserLevelException.class, () -> accountService.removeClientUserLevel(id.toString()));
    }

    @Test
    void removeClientUserLevelTestOneUserLevelLeft() throws ApplicationBaseException {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UserLevel userLevelClient = new Client();
        account.addUserLevel(userLevelClient);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.of(account));

        assertThrows(AccountUserLevelException.class, () -> accountService.removeClientUserLevel(id.toString()));
    }

    @Test
    void removeStaffUserLevelTestSuccessful() throws ApplicationBaseException {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UserLevel userLevelStaff = new Staff();
        account.addUserLevel(userLevelStaff);
        UserLevel userLevelClient = new Client();
        account.addUserLevel(userLevelClient);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.of(account));
        doNothing().when(accountMOKFacade).edit(account);
        doNothing().when(userLevelFacade).remove(any(Staff.class));

        accountService.removeStaffUserLevel(id.toString());
        var userLevels = account.getUserLevels();
        assertTrue(userLevels.stream().noneMatch(userLevel -> userLevel instanceof Staff));
    }

    @Test
    void removeStaffUserLevelTestAccountNotFound() throws ApplicationBaseException {
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.removeStaffUserLevel(id.toString()));
    }

    @Test
    void removeStaffUserLevelTestNoSuchUserLevel() throws ApplicationBaseException {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UserLevel userLevelClient = new Client();
        account.addUserLevel(userLevelClient);
        UserLevel userLevelAdmin = new Admin();
        account.addUserLevel(userLevelAdmin);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.of(account));

        assertThrows(AccountUserLevelException.class, () -> accountService.removeStaffUserLevel(id.toString()));
    }

    @Test
    void removeStaffUserLevelTestOneUserLevelLeft() throws ApplicationBaseException {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UserLevel userLevelStaff = new Staff();
        account.addUserLevel(userLevelStaff);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.of(account));

        assertThrows(AccountUserLevelException.class, () -> accountService.removeStaffUserLevel(id.toString()));
    }

    @Test
    @WithMockUser(username = "login")
    void removeAdminUserLevelTestSuccessful() throws ApplicationBaseException {
        Account accountActor = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Account accountTarget = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UserLevel userLevelStaff = new Staff();
        accountTarget.addUserLevel(userLevelStaff);
        UserLevel userLevelAdminActor = new Admin();
        accountActor.addUserLevel(userLevelAdminActor);
        UserLevel userLevelAdminTarget = new Admin();
        accountTarget.addUserLevel(userLevelAdminTarget);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.of(accountTarget));
        when(accountMOKFacade.findByLogin(accountActor.getLogin())).thenReturn(Optional.of(accountActor));
        doNothing().when(accountMOKFacade).edit(accountTarget);
        doNothing().when(userLevelFacade).remove(any(Admin.class));

        accountService.removeAdminUserLevel(id.toString());
        var userLevels = accountTarget.getUserLevels();
        assertTrue(userLevels.stream().noneMatch(userLevel -> userLevel instanceof Admin));
    }

    @Test
    void removeAdminUserLevelTestTargetAccountNotFound() throws ApplicationBaseException {
        Account accountActor = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UserLevel userLevelAdminActor = new Admin();
        accountActor.addUserLevel(userLevelAdminActor);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.removeAdminUserLevel(id.toString()));
    }

    @Test
    @WithMockUser(username = "login")
    void removeAdminUserLevelTestActorAccountNotFound() throws ApplicationBaseException {
        Account accountActor = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Account accountTarget = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UserLevel userLevelStaff = new Staff();
        accountTarget.addUserLevel(userLevelStaff);
        UserLevel userLevelAdminActor = new Admin();
        accountActor.addUserLevel(userLevelAdminActor);
        UserLevel userLevelAdminTarget = new Admin();
        accountTarget.addUserLevel(userLevelAdminTarget);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.of(accountTarget));
        when(accountMOKFacade.findByLogin(accountActor.getLogin())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.removeAdminUserLevel(id.toString()));
    }

    @Test
    @WithMockUser(username = "login")
    void removeAdminUserLevelTestAdminRemovesOwnUserLevel() throws ApplicationBaseException {
        Account accountActor = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UserLevel userLevelStaff = new Staff();
        accountActor.addUserLevel(userLevelStaff);
        UserLevel userLevelAdminActor = new Admin();
        accountActor.addUserLevel(userLevelAdminActor);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.of(accountActor));
        when(accountMOKFacade.findByLogin(accountActor.getLogin())).thenReturn(Optional.of(accountActor));

        assertThrows(AccountUserLevelException.class, () -> accountService.removeAdminUserLevel(id.toString()));
    }

    @Test
    @WithMockUser(username = "login")
    void removeAdminUserLevelTestNoSuchUserLevel() throws ApplicationBaseException {
        Account accountActor = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Account accountTarget = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UserLevel userLevelStaff = new Staff();
        accountTarget.addUserLevel(userLevelStaff);
        UserLevel userLevelAdminActor = new Admin();
        accountActor.addUserLevel(userLevelAdminActor);
        UserLevel userLevelClient = new Client();
        accountTarget.addUserLevel(userLevelClient);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.of(accountTarget));
        when(accountMOKFacade.findByLogin(accountActor.getLogin())).thenReturn(Optional.of(accountActor));

        assertThrows(AccountUserLevelException.class, () -> accountService.removeAdminUserLevel(id.toString()));
    }

    @Test
    @WithMockUser(username = "login")
    void removeAdminUserLevelTestOneUserLevel() throws ApplicationBaseException {
        Account accountActor = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Account accountTarget = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UserLevel userLevelAdminActor = new Admin();
        accountActor.addUserLevel(userLevelAdminActor);
        UserLevel userLevelAdminTarget = new Admin();
        accountTarget.addUserLevel(userLevelAdminTarget);
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.find(id)).thenReturn(Optional.of(accountTarget));
        when(accountMOKFacade.findByLogin(accountActor.getLogin())).thenReturn(Optional.of(accountActor));

        assertThrows(AccountUserLevelException.class, () -> accountService.removeAdminUserLevel(id.toString()));
    }

    @Test
    void changePasswordSelfTestSuccessful() throws ApplicationBaseException {
        String currentPassword = "CurrentPassword";
        String newPassword = "NewPassword";
        Account account = new Account("login", currentPassword, "firstName", "lastName", "test@email.com", "123123123");

        when(encoder.encode(newPassword)).thenReturn(newPassword);
        when(accountMOKFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        doNothing().when(accountMOKFacade).edit(account);
        when(encoder.matches(currentPassword, account.getPassword())).thenReturn(true);
        when(encoder.matches(newPassword, account.getPassword())).thenReturn(false);

        accountService.changePasswordSelf(currentPassword, newPassword, account.getLogin());

        assertEquals(newPassword, account.getPassword());
    }

    @Test
    void changePasswordSelfTestAccountNotFound() {
        String currentPassword = "CurrentPassword";
        String newPassword = "NewPassword";

        when(encoder.encode(newPassword)).thenReturn(newPassword);
        when(accountMOKFacade.findByLogin(anyString())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.changePasswordSelf(currentPassword, newPassword, "login"));
    }

    @Test
    void changePasswordSelfTestIncorrectPassword() {
        String incorrectPassword = "IncorrectPassword";
        String currentPassword = "CurrentPassword";
        String newPassword = "NewPassword";
        Account account = new Account("login", currentPassword, "firstName", "lastName", "test@email.com", "123123123");

        when(encoder.encode(newPassword)).thenReturn(newPassword);
        when(accountMOKFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        when(encoder.matches(incorrectPassword, account.getPassword())).thenReturn(false);

        assertThrows(IncorrectPasswordException.class, () -> accountService.changePasswordSelf(incorrectPassword, newPassword, account.getLogin()));

        assertEquals(currentPassword, account.getPassword());
    }

    @Test
    void changePasswordSelfTestCurrentPasswordAndNewPasswordAreTheSame() {
        String currentPassword = "CurrentPassword";
        Account account = new Account("login", currentPassword, "firstName", "lastName", "test@email.com", "123123123");

        when(encoder.encode(currentPassword)).thenReturn(currentPassword);
        when(accountMOKFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        when(encoder.matches(currentPassword, account.getPassword())).thenReturn(true);

        assertThrows(CurrentPasswordAndNewPasswordAreTheSameException.class,
                () -> accountService.changePasswordSelf(currentPassword, currentPassword, account.getLogin()));

        assertEquals(currentPassword, account.getPassword());
    }

    @Test
    void changeAccountPasswordTestSuccessful() throws ApplicationBaseException {
        String tokenVal = "TU9DSyBUT0tFTg==";
        String newPassword = "NewPassword";
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        account.setActive(true);
        Token token = new Token("TOKEN VALUE", account, Token.TokenType.RESET_PASSWORD);

        when(tokenFacade.findByTokenValue(anyString())).thenReturn(Optional.of(token));
        when(jwtProvider.isTokenValid(token.getTokenValue(), account)).thenReturn(true);
        when(accountMOKFacade.findAndRefresh(any())).thenReturn(Optional.of(account));
        when(encoder.encode(newPassword)).thenReturn(newPassword);
        doNothing().when(tokenFacade).remove(token);
        doNothing().when(accountMOKFacade).edit(account);

        accountService.changeAccountPassword(tokenVal, newPassword);
        assertEquals(newPassword, account.getPassword());
    }

    @Test
    void changeAccountPasswordTestTokenValueNotFound() {
        String tokenVal = "TU9DSyBUT0tFTg==";
        String newPassword = "NewPassword";

        when(tokenFacade.findByTokenValue(anyString())).thenReturn(Optional.empty());

        assertThrows(TokenNotFoundException.class, () -> accountService.changeAccountPassword(tokenVal, newPassword));
    }

    @Test
    void changeAccountPasswordTestTokenNotValid() {
        String tokenVal = "TU9DSyBUT0tFTg==";
        String newPassword = "NewPassword";
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Token token = new Token("TOKEN VALUE", account, Token.TokenType.RESET_PASSWORD);

        when(tokenFacade.findByTokenValue(anyString())).thenReturn(Optional.of(token));
        when(jwtProvider.isTokenValid(token.getTokenValue(), account)).thenReturn(false);

        assertThrows(TokenNotValidException.class, () -> accountService.changeAccountPassword(tokenVal, newPassword));
    }

    @Test
    void changeAccountPasswordTestAccountIdNotFound() {
        String tokenVal = "TU9DSyBUT0tFTg==";
        String newPassword = "NewPassword";
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Token token = new Token("TOKEN VALUE", account, Token.TokenType.RESET_PASSWORD);

        when(tokenFacade.findByTokenValue(anyString())).thenReturn(Optional.of(token));
        when(jwtProvider.isTokenValid(token.getTokenValue(), account)).thenReturn(true);
        when(accountMOKFacade.findAndRefresh(any())).thenReturn(Optional.empty());

        assertThrows(AccountIdNotFoundException.class, () -> accountService.changeAccountPassword(tokenVal, newPassword));
    }

    @Test
    void changeAccountPasswordTestAccountBlocked() {
        String tokenVal = "TU9DSyBUT0tFTg==";
        String newPassword = "NewPassword";
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        account.blockAccount(true);
        Token token = new Token("TOKEN VALUE", account, Token.TokenType.RESET_PASSWORD);

        when(tokenFacade.findByTokenValue(anyString())).thenReturn(Optional.of(token));
        when(jwtProvider.isTokenValid(token.getTokenValue(), account)).thenReturn(true);
        when(accountMOKFacade.findAndRefresh(any())).thenReturn(Optional.of(account));

        assertThrows(AccountBlockedException.class, () -> accountService.changeAccountPassword(tokenVal, newPassword));
    }

    @Test
    void changeAccountPasswordTestAccountNotActive() {
        String tokenVal = "TU9DSyBUT0tFTg==";
        String newPassword = "NewPassword";
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Token token = new Token("TOKEN VALUE", account, Token.TokenType.RESET_PASSWORD);

        when(tokenFacade.findByTokenValue(anyString())).thenReturn(Optional.of(token));
        when(jwtProvider.isTokenValid(token.getTokenValue(), account)).thenReturn(true);
        when(accountMOKFacade.findAndRefresh(any())).thenReturn(Optional.of(account));

        assertThrows(AccountNotActivatedException.class, () -> accountService.changeAccountPassword(tokenVal, newPassword));
    }

    @Test
    void forgetAccountPasswordTestSuccessful() throws ApplicationBaseException {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        account.setActive(true);

        when(accountMOKFacade.findByEmail(account.getEmail())).thenReturn(Optional.of(account));
        when(tokenFacade.findByTypeAndAccount(Token.TokenType.RESET_PASSWORD, account.getId())).thenReturn(Optional.empty());
        when(jwtProvider.generateActionToken(eq(account), anyInt(), eq(ChronoUnit.MINUTES))).thenReturn("TOKEN VALUE");
        doNothing().when(tokenFacade).create(any(Token.class));
        doNothing().when(mailProvider).sendPasswordResetEmail(eq(account.getName()), eq(account.getLastname()), eq(account.getEmail()), any(), any());

        accountService.forgetAccountPassword(account.getEmail());

        verify(mailProvider, Mockito.times(1)).sendPasswordResetEmail(eq(account.getName()), eq(account.getLastname()), eq(account.getEmail()), any(), any());
    }

    @Test
    void forgetAccountPasswordTestAccountNotFound() {
        when(accountMOKFacade.findByEmail("email")).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.forgetAccountPassword("email"));
    }

    @Test
    void forgetAccountPasswordTestAccountBlocked() {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        account.setActive(true);
        account.blockAccount(true);

        when(accountMOKFacade.findByEmail(account.getEmail())).thenReturn(Optional.of(account));
        assertThrows(AccountBlockedException.class, () -> accountService.forgetAccountPassword(account.getEmail()));
    }

    @Test
    void forgetAccountPasswordTestAccountAccountNotActivatedException() {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");

        when(accountMOKFacade.findByEmail(account.getEmail())).thenReturn(Optional.of(account));
        assertThrows(AccountNotActivatedException.class, () -> accountService.forgetAccountPassword(account.getEmail()));
    }

    @Test
    void modifyAccountTestSuccessful() throws ApplicationBaseException, NoSuchFieldException, IllegalAccessException {
        String newName = "NewName";
        String newLastname = "NewLastName";
        String newPhoneNumber = "NewPhoneNumber";
        String newLanguage = "pl";
        Account accountOld = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123", 0L);
        Account accountNew = new Account("login", "TestPassword", newName, newLastname, "test@email.com", newPhoneNumber, 0L);
        UserLevel userLevelClientOld = new Client();
        UserLevel userLevelClientNew = new Client();

        Field versionField = AbstractEntity.class.getDeclaredField("version");
        versionField.setAccessible(true);
        versionField.set(userLevelClientNew, 0L);
        versionField.set(userLevelClientOld, 0L);
        versionField.setAccessible(false);

        accountOld.addUserLevel(userLevelClientOld);
        accountNew.addUserLevel(userLevelClientNew);
        accountNew.setAccountLanguage(newLanguage);

        when(accountMOKFacade.findByLogin(accountNew.getLogin())).thenReturn(Optional.of(accountOld));
        doNothing().when(accountMOKFacade).edit(any(Account.class));

        Account resultAccount = accountService.modifyAccount(accountNew, accountNew.getLogin());

        assertEquals(accountOld.getLogin(), resultAccount.getLogin());
        assertEquals(accountOld.getPassword(), resultAccount.getPassword());
        assertEquals(newName, resultAccount.getName());
        assertEquals(newLastname, resultAccount.getLastname());
        assertEquals(accountOld.getEmail(), resultAccount.getEmail());
        assertEquals(newPhoneNumber, resultAccount.getPhoneNumber());
    }

    @Test
    void modifyAccountTestAccountNotFound() throws NoSuchFieldException, IllegalAccessException {
        String newName = "NewName";
        String newLastname = "NewLastName";
        String newPhoneNumber = "NewPhoneNumber";
        String newLanguage = "pl";
        Account accountNew = new Account("login", "TestPassword", newName, newLastname, "test@email.com", newPhoneNumber, 0L);
        UserLevel userLevelClientNew = new Client();

        Field versionField = AbstractEntity.class.getDeclaredField("version");
        versionField.setAccessible(true);
        versionField.set(userLevelClientNew, 0L);
        versionField.setAccessible(false);

        accountNew.addUserLevel(userLevelClientNew);
        accountNew.setAccountLanguage(newLanguage);

        when(accountMOKFacade.findByLogin(accountNew.getLogin())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.modifyAccount(accountNew, accountNew.getLogin()));
    }

    @Test
    void modifyAccountTestOptimisticLockAccountVersion() throws NoSuchFieldException, IllegalAccessException {
        String newName = "NewName";
        String newLastname = "NewLastName";
        String newPhoneNumber = "NewPhoneNumber";
        String newLanguage = "pl";
        Account accountOld = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123", 0L);
        Account accountNew = new Account("login", "TestPassword", newName, newLastname, "test@email.com", newPhoneNumber, 1L);
        UserLevel userLevelClientOld = new Client();
        UserLevel userLevelClientNew = new Client();

        Field versionField = AbstractEntity.class.getDeclaredField("version");
        versionField.setAccessible(true);
        versionField.set(userLevelClientNew, 0L);
        versionField.set(userLevelClientOld, 0L);
        versionField.setAccessible(false);

        accountOld.addUserLevel(userLevelClientOld);
        accountNew.addUserLevel(userLevelClientNew);
        accountNew.setAccountLanguage(newLanguage);

        when(accountMOKFacade.findByLogin(accountNew.getLogin())).thenReturn(Optional.of(accountOld));

        assertThrows(ApplicationOptimisticLockException.class, () -> accountService.modifyAccount(accountNew, accountNew.getLogin()));
    }

    @Test
    void modifyAccountTestOptimisticLockUserLevelVersion() throws NoSuchFieldException, IllegalAccessException {
        String newName = "NewName";
        String newLastname = "NewLastName";
        String newPhoneNumber = "NewPhoneNumber";
        String newLanguage = "pl";
        Account accountOld = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123", 0L);
        Account accountNew = new Account("login", "TestPassword", newName, newLastname, "test@email.com", newPhoneNumber, 0L);
        UserLevel userLevelClientOld = new Client();
        UserLevel userLevelClientNew = new Client();

        Field versionField = AbstractEntity.class.getDeclaredField("version");
        versionField.setAccessible(true);
        versionField.set(userLevelClientNew, 0L);
        versionField.set(userLevelClientOld, 1L);
        versionField.setAccessible(false);

        accountOld.addUserLevel(userLevelClientOld);
        accountNew.addUserLevel(userLevelClientNew);
        accountNew.setAccountLanguage(newLanguage);

        when(accountMOKFacade.findByLogin(accountNew.getLogin())).thenReturn(Optional.of(accountOld));

        assertThrows(ApplicationOptimisticLockException.class, () -> accountService.modifyAccount(accountNew, accountNew.getLogin()));
    }

    @Test
    void modifyAccountTestUserLevelMissing() throws NoSuchFieldException, IllegalAccessException {
        String newName = "NewName";
        String newLastname = "NewLastName";
        String newPhoneNumber = "NewPhoneNumber";
        String newLanguage = "pl";
        Account accountOld = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123", 0L);
        Account accountNew = new Account("login", "TestPassword", newName, newLastname, "test@email.com", newPhoneNumber, 0L);
        UserLevel userLevelClientOld = new Client();
        UserLevel userLevelStaffOld = new Staff();
        UserLevel userLevelClientNew = new Client();

        Field versionField = AbstractEntity.class.getDeclaredField("version");
        versionField.setAccessible(true);
        versionField.set(userLevelClientNew, 0L);
        versionField.set(userLevelClientOld, 0L);
        versionField.set(userLevelStaffOld, 0L);
        versionField.setAccessible(false);

        accountOld.addUserLevel(userLevelClientOld);
        accountOld.addUserLevel(userLevelStaffOld);
        accountNew.addUserLevel(userLevelClientNew);
        accountNew.setAccountLanguage(newLanguage);

        when(accountMOKFacade.findByLogin(accountNew.getLogin())).thenReturn(Optional.of(accountOld));

        assertThrows(UserLevelMissingException.class, () -> accountService.modifyAccount(accountNew, accountNew.getLogin()));
    }

    @Test
    @WithMockUser(username = "login")
    void resendEmailConfirmationTestSuccessful() throws ApplicationBaseException, NoSuchFieldException, IllegalAccessException {
        Field emailChangeConfirmationPeriodLengthHoursField = AccountService.class.getDeclaredField("emailChangeConfirmationPeriodLengthHours");
        emailChangeConfirmationPeriodLengthHoursField.setAccessible(true);
        emailChangeConfirmationPeriodLengthHoursField.set(accountService, 24);
        emailChangeConfirmationPeriodLengthHoursField.setAccessible(false);

        String tokenEmail = "token@email.com";
        String newToken = "NEW TOKEN VALUE";
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Token token = new Token("TOKEN VALUE", account, Token.TokenType.CONFIRM_EMAIL);
        account.setAccountLanguage("pl");

        when(accountMOKFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        when(tokenFacade.findByTypeAndAccount(Token.TokenType.CONFIRM_EMAIL, account.getId())).thenReturn(Optional.of(token));
        when(jwtProvider.extractEmail(token.getTokenValue())).thenReturn(tokenEmail);
        when(jwtProvider.generateEmailToken(account, tokenEmail, 24)).thenReturn(newToken);
        doNothing().when(mailProvider).sendEmailConfirmEmail(eq(account.getName()), eq(account.getLastname()), eq(tokenEmail), anyString(), eq(account.getAccountLanguage()));
        doNothing().when(tokenFacade).edit(token);

        accountService.resendEmailConfirmation();
        Mockito.verify(mailProvider, Mockito.times(1)).sendEmailConfirmEmail(eq(account.getName()), eq(account.getLastname()), eq(tokenEmail), anyString(), eq(account.getAccountLanguage()));
    }

    @Test
    @WithMockUser(username = "login")
    void resendEmailConfirmationTestAccountNotFound() {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        account.setAccountLanguage("pl");

        when(accountMOKFacade.findByLogin(account.getLogin())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class , () -> accountService.resendEmailConfirmation());
    }

    @Test
    @WithMockUser(username = "login")
    void resendEmailConfirmationTestTokenNotFound() {

        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        account.setAccountLanguage("pl");

        when(accountMOKFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        when(tokenFacade.findByTypeAndAccount(Token.TokenType.CONFIRM_EMAIL, account.getId())).thenReturn(Optional.empty());

        assertThrows(TokenNotFoundException.class, () -> accountService.resendEmailConfirmation());
    }
}