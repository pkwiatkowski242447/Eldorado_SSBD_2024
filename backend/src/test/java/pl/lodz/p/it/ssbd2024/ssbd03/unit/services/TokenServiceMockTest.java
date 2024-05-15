package pl.lodz.p.it.ssbd2024.ssbd03.unit.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations.TokenService;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceMockTest {

    @Mock
    private TokenFacade tokenFacade;

    @Mock
    private JWTProvider jwtProvider;

    @InjectMocks
    private TokenService tokenService;

    @Test
    public void createRegistrationTokenTest() throws ApplicationBaseException {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        String tokenValue = "TOKEN VALUE";
        ArgumentCaptor<Token> tokenArgumentCaptor = ArgumentCaptor.captor();
        Token compareToken = new Token(tokenValue, account, Token.TokenType.REGISTER);
        when(jwtProvider.generateActionToken(account, 24, ChronoUnit.HOURS)).thenReturn(tokenValue);
        Mockito.doNothing().when(tokenFacade).create(any());

        assertEquals(tokenValue, tokenService.createRegistrationToken(account));
        Mockito.verify(tokenFacade).create(tokenArgumentCaptor.capture());
        Token createdToken = tokenArgumentCaptor.getValue();
        assertEquals(compareToken.getTokenValue(), createdToken.getTokenValue());
        assertEquals(compareToken.getAccount(), createdToken.getAccount());
        assertEquals(compareToken.getType(), createdToken.getType());
    }

    @Test
    public void createEmailConfirmationTokenTest() throws ApplicationBaseException {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        String newEmail = "new@email.com";
        String tokenValue = "TOKEN VALUE";
        ArgumentCaptor<Token> tokenArgumentCaptor = ArgumentCaptor.captor();
        Token compareToken = new Token(tokenValue, account, Token.TokenType.CONFIRM_EMAIL);
        when(jwtProvider.generateEmailToken(account, newEmail,24)).thenReturn(tokenValue);
        Mockito.doNothing().when(tokenFacade).create(any());

        assertEquals(tokenValue, tokenService.createEmailConfirmationToken(account, newEmail));
        Mockito.verify(tokenFacade).create(tokenArgumentCaptor.capture());
        Token createdToken = tokenArgumentCaptor.getValue();
        assertEquals(compareToken.getTokenValue(), createdToken.getTokenValue());
        assertEquals(compareToken.getAccount(), createdToken.getAccount());
        assertEquals(compareToken.getType(), createdToken.getType());
    }

    @Test
    public void removeAccountEmailConfirmationTokenTestSuccessful() throws ApplicationBaseException {
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        String tokenValue = "TOKEN VALUE";
        Token token = new Token(tokenValue, account, Token.TokenType.CONFIRM_EMAIL);
        when(tokenFacade.findByTypeAndAccount(eq(Token.TokenType.CONFIRM_EMAIL), any())).thenReturn(Optional.of(token));
        when(jwtProvider.extractAccountId(tokenValue)).thenReturn(UUID.randomUUID());
        Mockito.doNothing().when(tokenFacade).remove(token);

        tokenService.removeAccountsEmailConfirmationToken(tokenValue);
        Mockito.verify(tokenFacade, Mockito.times(1)).remove(token);
    }

    @Test
    public void removeAccountEmailConfirmationTokenTestNoSuchToken() throws ApplicationBaseException {
        String tokenValue = "TOKEN VALUE";
        when(tokenFacade.findByTypeAndAccount(eq(Token.TokenType.CONFIRM_EMAIL), any())).thenReturn(Optional.empty());
        when(jwtProvider.extractAccountId(tokenValue)).thenReturn(UUID.randomUUID());

        tokenService.removeAccountsEmailConfirmationToken(tokenValue);
        Mockito.verify(tokenFacade, Mockito.never()).remove(any());
    }

    @Test
    public void createPasswordResetTokenTestPositiveWhenPreviousTokenNotFound() throws ApplicationBaseException {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        String exampleTokenValue = "ExampleTokenValue1";
        Token token = new Token(exampleTokenValue, account, Token.TokenType.RESET_PASSWORD);

        when(tokenFacade.findByTypeAndAccount(Token.TokenType.RESET_PASSWORD, account.getId())).thenReturn(Optional.empty());
        when(jwtProvider.generateActionToken(account, 0, ChronoUnit.MINUTES)).thenReturn(exampleTokenValue);
        doNothing().when(tokenFacade).create(any());

        String tokenValue = tokenService.createPasswordResetToken(account);

        assertEquals(tokenValue, exampleTokenValue);
    }

    @Test
    public void createPasswordResetTokenTestPositiveWhenPreviousTokenFound() throws ApplicationBaseException {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        Token existingToken = new Token("SomeOtherTokenValue", account, Token.TokenType.RESET_PASSWORD);
        String exampleTokenValue = "ExampleTokenValue2";
        Token token = new Token(exampleTokenValue, account, Token.TokenType.RESET_PASSWORD);

        when(tokenFacade.findByTypeAndAccount(Token.TokenType.RESET_PASSWORD, account.getId())).thenReturn(Optional.of(existingToken));
        doNothing().when(tokenFacade).remove(existingToken);
        when(jwtProvider.generateActionToken(account, 0, ChronoUnit.MINUTES)).thenReturn(exampleTokenValue);
        doNothing().when(tokenFacade).create(any());

        String tokenValue = tokenService.createPasswordResetToken(account);

        assertEquals(tokenValue, exampleTokenValue);

        verify(tokenFacade, times(1)).remove(existingToken);
    }
}
