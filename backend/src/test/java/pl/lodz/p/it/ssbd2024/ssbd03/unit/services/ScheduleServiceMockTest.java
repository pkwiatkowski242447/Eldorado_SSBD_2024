package pl.lodz.p.it.ssbd2024.ssbd03.unit.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.AccountHistoryData;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountHistoryDataFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations.ScheduleService;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.TokenProvider;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@SecurityTestExecutionListeners
public class ScheduleServiceMockTest {
    @Mock
    private MailProvider mailProvider;
    @Mock
    private TokenFacade tokenFacade;
    @Mock
    private AccountMOKFacade accountMOKFacade;
    @Mock
    private AccountHistoryDataFacade historyDataFacade;
    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private ScheduleService scheduleService;

    @BeforeEach
    public void setScheduleServiceParameters() throws NoSuchFieldException, IllegalAccessException {
        //Using the default parameters from application properties.
        //They shouldn't have any impact on the tests as they're mocked.
        //I didn't want to raise whole application just to set two string fields.

        Field deleteTimeField = scheduleService.getClass().getDeclaredField("deleteTime");
        deleteTimeField.setAccessible(true);
        deleteTimeField.set(scheduleService, "24");
        Field unblockTimeField = scheduleService.getClass().getDeclaredField("unblockTime");
        unblockTimeField.setAccessible(true);
        unblockTimeField.set(scheduleService, "2");
        Field resendRegistrationConfirmationEmailAfterHoursField = scheduleService.getClass().getDeclaredField("resendRegistrationConfirmationEmailAfterHours");
        resendRegistrationConfirmationEmailAfterHoursField.setAccessible(true);
        resendRegistrationConfirmationEmailAfterHoursField.set(scheduleService, 12);

    }

    @Test
    void deleteNotSuspendedTestSuccessful() throws Exception {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Account account1 = new Account("login1", "TestPassword1", "firstName1", "lastName1", "test1@email.com", "123123124");
        List<Account> accountList = List.of(account, account1);

        when(accountMOKFacade.findAllAccountsMarkedForDeletion(24L, TimeUnit.HOURS)).thenReturn(accountList);
        doNothing().when(tokenFacade).removeByAccount(null);
        doNothing().when(accountMOKFacade).remove(any(Account.class));

        scheduleService.deleteNotActivatedAccounts();

        verify(accountMOKFacade, times(1)).remove(account);
        verify(accountMOKFacade, times(1)).remove(account1);
    }

    @Test
    void deleteNotActiveTestUnsuccessful() throws Exception {
        when(accountMOKFacade.findAllAccountsMarkedForDeletion(24L, TimeUnit.HOURS)).thenThrow(NumberFormatException.class);

        assertDoesNotThrow(() -> scheduleService.deleteNotActivatedAccounts());
    }

    @Test
    void deleteNotActiveTestEmpty() throws Exception {
        List<Account> accountList = new ArrayList<>();

        when(accountMOKFacade.findAllAccountsMarkedForDeletion(24L, TimeUnit.HOURS)).thenReturn(accountList);

        scheduleService.deleteNotActivatedAccounts();

        verify(accountMOKFacade, times(1)).findAllAccountsMarkedForDeletion(24L, TimeUnit.HOURS);
    }

    @Test
    void resendEmailConfirmationEmailTestSuccessful() throws Exception {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Account account1 = new Account("login1", "TestPassword1", "firstName1", "lastName1", "test1@email.com", "123123124");
        account.setAccountLanguage("pl");
        account1.setAccountLanguage("en");
        Token token = new Token("TEST VALUE", account, Token.TokenType.REGISTER);
        Token token1 = new Token("TEST VALUE", account1, Token.TokenType.REGISTER);
        List<Token> tokenList = List.of(token, token1);

        Token newToken = new Token("NewTokenValue", account, Token.TokenType.REGISTER);
        Token newToken1 = new Token("NewTokenValue1", account1, Token.TokenType.REGISTER);

        Field creationDateField = Account.class.getDeclaredField("creationTime");
        creationDateField.setAccessible(true);
        creationDateField.set(account, LocalDateTime.now().minusHours(13));
        creationDateField.set(account1, LocalDateTime.now().minusHours(13));
        creationDateField.setAccessible(false);

        when(tokenFacade.findByTokenType(Token.TokenType.REGISTER)).thenReturn(tokenList);
        doNothing().when(tokenFacade).removeByTypeAndAccount(Token.TokenType.REGISTER, null);
        doReturn(newToken).when(tokenProvider).generateAccountActivationToken(account);
        doReturn(newToken1).when(tokenProvider).generateAccountActivationToken(account1);
        doNothing().when(tokenFacade).create(newToken);
        doNothing().when(tokenFacade).create(newToken1);
        doNothing().when(mailProvider).sendRegistrationConfirmEmail(any(String.class), any(String.class), any(String.class), any(String.class), any(String.class));

        scheduleService.resendConfirmationEmail();

        verify(mailProvider, times(2)).sendRegistrationConfirmEmail(any(String.class), any(String.class), any(String.class), any(String.class), any(String.class));
    }

    @Test
    void resendEmailConfirmationEmailTestEmpty() throws Exception {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Account account1 = new Account("login1", "TestPassword1", "firstName1", "lastName1", "test1@email.com", "123123124");
        account.setAccountLanguage("pl");
        account1.setAccountLanguage("en");
        Token token = new Token("TEST VALUE", account, Token.TokenType.REGISTER);
        Token token1 = new Token("TEST VALUE", account1, Token.TokenType.REGISTER);
        List<Token> tokenList = List.of(token, token1);

        Field creationDateField = Account.class.getDeclaredField("creationTime");
        creationDateField.setAccessible(true);
        creationDateField.set(account, LocalDateTime.now());
        creationDateField.set(account1, LocalDateTime.now());
        creationDateField.setAccessible(false);

        when(tokenFacade.findByTokenType(Token.TokenType.REGISTER)).thenReturn(tokenList);
        scheduleService.resendConfirmationEmail();
        verify(tokenFacade, times(1)).findByTokenType(Token.TokenType.REGISTER);
        verify(mailProvider, never()).sendRegistrationConfirmEmail(any(String.class), any(String.class), any(String.class), any(String.class), any(String.class));
    }

    @Test
    @WithMockUser(username = "login")
    void unblockAccountTestSuccessful() throws NoSuchFieldException, IllegalAccessException, ApplicationBaseException {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Account account1 = new Account("login1", "TestPassword1", "firstName1", "lastName1", "test1@email.com", "123123124");
        account.setAccountLanguage("pl");
        account1.setAccountLanguage("en");
        account.blockAccount(false);
        account1.blockAccount(false);
        Field blockedTimeField = Account.class.getDeclaredField("blockedTime");
        blockedTimeField.setAccessible(true);
        blockedTimeField.set(account, LocalDateTime.now().minusHours(3));
        blockedTimeField.set(account1, LocalDateTime.now().minusHours(3));
        blockedTimeField.setAccessible(false);
        List<Account> accountList = List.of(account, account1);

        when(accountMOKFacade.findAllBlockedAccountsThatWereBlockedByLoginIncorrectlyCertainAmountOfTimes(2L, TimeUnit.HOURS))
                .thenReturn(accountList);
        doNothing().when(accountMOKFacade).edit(any());
        doNothing().when(mailProvider).sendUnblockAccountInfoEmail(any(), any(), any(), any());
        doNothing().when(historyDataFacade).create(any(AccountHistoryData.class));

        scheduleService.unblockAccount();

        verify(mailProvider, times(2)).sendUnblockAccountInfoEmail(any(), any(), any(), any());
    }

    @Test
    void unblockAccountTestUnsuccessful() throws Exception {
        when(accountMOKFacade.findAllBlockedAccountsThatWereBlockedByLoginIncorrectlyCertainAmountOfTimes(2L, TimeUnit.HOURS))
                .thenThrow(NumberFormatException.class);

        assertDoesNotThrow(() -> scheduleService.unblockAccount());
    }

    @Test
    void unblockAccountTestEmpty() throws Exception {
        List<Account> accountList = new ArrayList<>();

        when(accountMOKFacade.findAllBlockedAccountsThatWereBlockedByLoginIncorrectlyCertainAmountOfTimes(2L, TimeUnit.HOURS))
                .thenReturn(accountList);

        scheduleService.unblockAccount();

        verify(mailProvider, never()).sendUnblockAccountInfoEmail(any(), any(), any(), any());
    }
}
