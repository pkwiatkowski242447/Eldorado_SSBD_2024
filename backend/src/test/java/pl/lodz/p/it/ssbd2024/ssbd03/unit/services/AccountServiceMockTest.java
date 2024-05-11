package pl.lodz.p.it.ssbd2024.ssbd03.unit.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountAlreadyBlockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountAlreadyUnblockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.old.AccountEmailChangeException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.old.AccountEmailNullException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountIdNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.read.TokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.IllegalOperationException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations.AccountService;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations.TokenService;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceMockTest {
    @Mock
    private TokenService tokenService;
    @Mock
    private MailProvider mailProvider;
    @Mock
    private JWTProvider jwtProvider;
    @Mock
    private TokenFacade tokenFacade;
    @Mock
    private AccountMOKFacade accountMOKFacade;
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
    void confirmEmailTestSuccessful() throws AccountEmailChangeException, AccountEmailNullException, AccountNotFoundException {
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
    void confirmEmailTestTokenInvalid() throws AccountEmailChangeException, AccountEmailNullException, AccountNotFoundException {
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
    void confirmEmailTestTokenNotInDatabase() throws AccountEmailChangeException, AccountEmailNullException, AccountNotFoundException {
        String tokenVal = "TU9DSyBUT0tFTg==";
        String decodedTokenVal = new String(Base64.getDecoder().decode(tokenVal));
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");

        when(tokenFacade.findByTokenValue(decodedTokenVal)).thenReturn(Optional.empty());
        when(accountMOKFacade.find(account.getId())).thenReturn(Optional.of(account));

        assertFalse(accountService.confirmEmail(tokenVal));
    }

    @Test
    void changeEmailTestSuccessful() throws AccountEmailChangeException, AccountNotFoundException {

        Account a = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        a.setAccountLanguage("pl");
        String newEmail = "new@email.com";

        when(accountMOKFacade.find(any())).thenReturn(Optional.of(a));
        when(tokenService.createEmailConfirmationToken(a, newEmail)).thenReturn("TOKEN");
        doNothing().when(mailProvider).sendEmailConfirmEmail(eq(a.getName()), eq(a.getLastname()), eq(newEmail), any(), eq(a.getAccountLanguage()));

        accountService.changeEmail(UUID.randomUUID(), newEmail);
        Mockito.verify(mailProvider, Mockito.times(1)).sendEmailConfirmEmail(eq(a.getName()), eq(a.getLastname()), eq(newEmail), any(), eq(a.getAccountLanguage()));
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
    void activateAccountTestWhenAccountObjectIsNotFound() {
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
    void blockAccountTestSuccessful() throws AccountAlreadyBlockedException, IllegalOperationException, AccountNotFoundException {

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
    void unblockAccountTestSuccessful() throws AccountNotFoundException, AccountAlreadyUnblockedException {

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
    void getAccountByLoginTest() {

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
    void getAccountByIdTest() {

        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        UUID id = UUID.randomUUID();

        when(accountMOKFacade.findAndRefresh(id)).thenReturn(Optional.of(account));

        assertEquals(account, accountService.getAccountById(id).orElse(null));

        Mockito.verify(accountMOKFacade, Mockito.times(1)).findAndRefresh(id);
    }
}