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
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations.TokenService;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class TokenServiceMockTest {

    @Mock
    private TokenFacade tokenFacade;

    @Mock
    private JWTProvider jwtProvider;

    @InjectMocks
    private TokenService tokenService;

    @Test
    void createRegistrationTokenTest(){
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        String tokenValue = "TOKEN VALUE";
        ArgumentCaptor<Token> tokenArgumentCaptor = ArgumentCaptor.captor();
        Token compareToken = new Token(tokenValue, account, Token.TokenType.REGISTER);
        Mockito.when(jwtProvider.generateActionToken(account, 24, ChronoUnit.HOURS)).thenReturn(tokenValue);
        Mockito.doNothing().when(tokenFacade).create(any());

        assertEquals(tokenValue, tokenService.createRegistrationToken(account));
        Mockito.verify(tokenFacade).create(tokenArgumentCaptor.capture());
        Token createdToken = tokenArgumentCaptor.getValue();
        assertEquals(compareToken.getTokenValue(), createdToken.getTokenValue());
        assertEquals(compareToken.getAccount(), createdToken.getAccount());
        assertEquals(compareToken.getType(), createdToken.getType());
    }

    @Test
    void createEmailConfirmationTokenTest(){
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        String newEmail = "new@email.com";
        String tokenValue = "TOKEN VALUE";
        ArgumentCaptor<Token> tokenArgumentCaptor = ArgumentCaptor.captor();
        Token compareToken = new Token(tokenValue, account, Token.TokenType.CONFIRM_EMAIL);
        Mockito.when(jwtProvider.generateEmailToken(account, newEmail,24)).thenReturn(tokenValue);
        Mockito.doNothing().when(tokenFacade).create(any());

        assertEquals(tokenValue, tokenService.createEmailConfirmationToken(account, newEmail));
        Mockito.verify(tokenFacade).create(tokenArgumentCaptor.capture());
        Token createdToken = tokenArgumentCaptor.getValue();
        assertEquals(compareToken.getTokenValue(), createdToken.getTokenValue());
        assertEquals(compareToken.getAccount(), createdToken.getAccount());
        assertEquals(compareToken.getType(), createdToken.getType());
    }

    @Test
    void removeAccountEmailConfirmationTokenTestSuccessful(){
        Account account = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        String tokenValue = "TOKEN VALUE";
        Token token = new Token(tokenValue, account, Token.TokenType.CONFIRM_EMAIL);
        Mockito.when(tokenFacade.findByTypeAndAccount(eq(Token.TokenType.CONFIRM_EMAIL), any())).thenReturn(Optional.of(token));
        Mockito.when(jwtProvider.extractAccountId(tokenValue)).thenReturn(UUID.randomUUID());
        Mockito.doNothing().when(tokenFacade).remove(token);

        tokenService.removeAccountsEmailConfirmationToken(tokenValue);
        Mockito.verify(tokenFacade, Mockito.times(1)).remove(token);
    }

    @Test
    void removeAccountEmailConfirmationTokenTestNoSuchToken(){
        String tokenValue = "TOKEN VALUE";
        Mockito.when(tokenFacade.findByTypeAndAccount(eq(Token.TokenType.CONFIRM_EMAIL), any())).thenReturn(Optional.empty());
        Mockito.when(jwtProvider.extractAccountId(tokenValue)).thenReturn(UUID.randomUUID());

        tokenService.removeAccountsEmailConfirmationToken(tokenValue);
        Mockito.verify(tokenFacade, Mockito.never()).remove(any());
    }
}
