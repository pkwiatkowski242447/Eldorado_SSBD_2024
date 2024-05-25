package pl.lodz.p.it.ssbd2024.ssbd03.unit.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.token.AccessAndRefreshTokensDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.ActivityLog;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountAuthenticationException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.InvalidLoginAttemptException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountBlockedByAdminException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountBlockedByFailedLoginAttemptsException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountNotActivatedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.TokenNotValidException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.read.TokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AuthenticationFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenAuthFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations.AuthenticationService;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.TokenProvider;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@SecurityTestExecutionListeners
public class AuthenticationServiceMockTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private JWTProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailProvider mailProvider;

    @Mock
    private TokenAuthFacade tokenFacade;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void loginUsingAuthenticationCodeTestNegativeAccountNotFound() throws Exception {
        String exampleLogin = "ExampleLogin";
        String exampleCode = "ExampleCode";
        when(authenticationFacade.findByLogin(exampleLogin)).thenReturn(Optional.empty());
        assertThrows(InvalidLoginAttemptException.class, () -> authenticationService.loginUsingAuthenticationCode(exampleLogin, exampleCode));
    }

    @Test
    public void loginUsingAuthenticationCodeTestNegativeAccountNotActive() throws Exception {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        String exampleCode = "ExampleCode";
        account.setActive(false);
        when(authenticationFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        assertThrows(AccountNotActivatedException.class, () -> authenticationService.loginUsingAuthenticationCode(account.getLogin(), exampleCode));
    }

    @Test
    public void loginUsingAuthenticationCodeTestNegativeAccountBlockedByAdmin() throws Exception {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        account.setActive(true);
        String exampleCode = "ExampleCode";
        account.blockAccount(true);
        when(authenticationFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        assertThrows(AccountBlockedByAdminException.class, () -> authenticationService.loginUsingAuthenticationCode(account.getLogin(), exampleCode));
    }

    @Test
    public void loginUsingAuthenticationCodeTestNegativeAccountBlockedByLoggingIncorrectlyTooMuchTimes() throws Exception {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        account.setActive(true);
        String exampleCode = "ExampleCode";
        account.blockAccount(false);
        when(authenticationFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        assertThrows(AccountBlockedByFailedLoginAttemptsException.class, () -> authenticationService.loginUsingAuthenticationCode(account.getLogin(), exampleCode));
    }

    @Test
    public void loginUsingAuthenticationCodeTestNegativeTokenWithAuthCodeNotFound() throws Exception {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        account.setActive(true);
        String exampleCode = "ExampleCode";
        when(authenticationFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        when(tokenFacade.findByTypeAndAccount(Token.TokenType.MULTI_FACTOR_AUTHENTICATION_CODE, account.getId())).thenReturn(Optional.empty());
        assertThrows(TokenNotFoundException.class, () -> authenticationService.loginUsingAuthenticationCode(account.getLogin(), exampleCode));
    }

    @Test
    public void loginUsingAuthenticationCodeTestNegativeTokenInvalid() throws Exception {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        String exampleCode = "ExampleCode";
        Token token = new Token(exampleCode, account, Token.TokenType.MULTI_FACTOR_AUTHENTICATION_CODE);
        account.setActive(true);
        when(authenticationFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        when(tokenFacade.findByTypeAndAccount(Token.TokenType.MULTI_FACTOR_AUTHENTICATION_CODE, account.getId())).thenReturn(Optional.of(token));
        when(jwtProvider.isMultiFactorAuthTokenValid(token.getTokenValue())).thenReturn(false);
        assertThrows(TokenNotValidException.class, () -> authenticationService.loginUsingAuthenticationCode(account.getLogin(), exampleCode));
    }

    @Test
    public void loginUsingAuthenticationCodeTestNegativeGivenAuthCodeDoesNotMatchOriginal() throws ApplicationBaseException {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        String exampleCode = "ExampleCode";
        Token token = new Token(exampleCode, account, Token.TokenType.MULTI_FACTOR_AUTHENTICATION_CODE);
        account.setActive(true);
        when(authenticationFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        when(tokenFacade.findByTypeAndAccount(Token.TokenType.MULTI_FACTOR_AUTHENTICATION_CODE, account.getId())).thenReturn(Optional.of(token));
        when(jwtProvider.extractHashedCodeValueFromToken(token.getTokenValue())).thenReturn(exampleCode);
        when(jwtProvider.isMultiFactorAuthTokenValid(token.getTokenValue())).thenReturn(true);
        when(passwordEncoder.matches(exampleCode, token.getTokenValue())).thenReturn(false);
        assertThrows(TokenNotValidException.class, () -> authenticationService.loginUsingAuthenticationCode(account.getLogin(), exampleCode));
    }

    @Test
    public void loginUsingAuthenticationCodeTestPositive() throws ApplicationBaseException {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        String exampleCode = "ExampleCode";
        Token token = new Token(exampleCode, account, Token.TokenType.MULTI_FACTOR_AUTHENTICATION_CODE);
        account.setActive(true);
        when(authenticationFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        when(tokenFacade.findByTypeAndAccount(Token.TokenType.MULTI_FACTOR_AUTHENTICATION_CODE, account.getId())).thenReturn(Optional.of(token));
        when(jwtProvider.isMultiFactorAuthTokenValid(token.getTokenValue())).thenReturn(true);
        when(jwtProvider.extractHashedCodeValueFromToken(token.getTokenValue())).thenReturn(exampleCode);
        when(passwordEncoder.matches(exampleCode, token.getTokenValue())).thenReturn(true);
        doNothing().when(tokenFacade).remove(token);

        authenticationService.loginUsingAuthenticationCode(account.getLogin(), exampleCode);
        verify(tokenFacade, times(1)).remove(token);
    }

    @Test
    public void findByLoginTestPositive() throws Exception {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        when(authenticationFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));

        Optional<Account> optionalAccount = authenticationService.findByLogin(account.getLogin());

        assertTrue(optionalAccount.isPresent());
        assertEquals(optionalAccount.get(), account);
    }

    @Test
    public void registerUnsuccessfulLoginAttemptWithoutIncrementTestNegativeAccountNotFound() throws Exception {
        String exampleLogin = "ExampleLogin";
        String exampleIpAddress = "ExampleIpAddress";
        when(authenticationFacade.findByLogin(exampleLogin)).thenReturn(Optional.empty());

        assertThrows(InvalidLoginAttemptException.class, () -> authenticationService.registerUnsuccessfulLoginAttemptWithoutIncrement(exampleLogin, exampleIpAddress));
    }

    @Test
    public void registerUnsuccessfulLoginAttemptWithoutIncrementTestPositive() throws ApplicationBaseException {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        String exampleIpAddress = "ExampleIpAddress";
        when(authenticationFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        doNothing().when(authenticationFacade).edit(account);

        authenticationService.registerUnsuccessfulLoginAttemptWithoutIncrement(account.getLogin(), exampleIpAddress);

        verify(authenticationFacade, times(1)).edit(account);
    }

    @Test
    public void registerUnsuccessfulLoginAttemptWithIncrementTestNegativeAccountNotFound() throws Exception {
        String exampleLogin = "ExampleLogin";
        String exampleIpAddress = "ExampleIpAddress";
        when(authenticationFacade.findByLogin(exampleLogin)).thenReturn(Optional.empty());

        assertThrows(InvalidLoginAttemptException.class, () -> authenticationService.registerUnsuccessfulLoginAttemptWithIncrement(exampleLogin, exampleIpAddress));
    }

    @Test
    public void registerUnsuccessfulLoginAttemptWithIncrementTestPositiveWhenEmailIsNotSent() throws ApplicationBaseException {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        String exampleIpAddress = "ExampleIpAddress";
        when(authenticationFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        doNothing().when(authenticationFacade).edit(account);

        authenticationService.registerUnsuccessfulLoginAttemptWithIncrement(account.getLogin(), exampleIpAddress);

        verify(authenticationFacade, times(1)).edit(account);
    }

    @Test
    public void registerUnsuccessfulLoginAttemptWithIncrementTestPositiveWhenEmailIsSent() throws ApplicationBaseException {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        ActivityLog activityLog = account.getActivityLog();
        activityLog.setUnsuccessfulLoginCounter(5);
        account.setActivityLog(activityLog);
        String exampleIpAddress = "ExampleIpAddress";
        when(authenticationFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        doNothing().when(authenticationFacade).edit(account);
        doNothing().when(mailProvider).sendBlockAccountInfoEmail(account.getName(),
                account.getLastname(),
                account.getEmail(),
                account.getAccountLanguage(),
                false);

        authenticationService.registerUnsuccessfulLoginAttemptWithIncrement(account.getLogin(), exampleIpAddress);

        verify(authenticationFacade, times(1)).edit(account);
        verify(mailProvider, times(1)).sendBlockAccountInfoEmail(account.getName(),
                account.getLastname(),
                account.getEmail(),
                account.getAccountLanguage(),
                false);
    }

    @Test
    public void registerSuccessfulLoginAttemptFor1FATestNegativeAccountNotFound() throws Exception {
        String exampleLogin = "ExampleLogin";
        String exampleIpAddress = "ExampleIpAddress";
        String exampleLanguage = "ExampleLanguage";
        when(authenticationFacade.findByLogin(exampleLogin)).thenReturn(Optional.empty());

        assertThrows(InvalidLoginAttemptException.class, () -> authenticationService.registerSuccessfulLoginAttempt(exampleLogin, false, exampleIpAddress, exampleLanguage));
    }

    @Test
    public void registerSuccessfulLoginAttemptFor1FATestPositiveWhenNotConfirmed() throws ApplicationBaseException {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        account.setTwoFactorAuth(false);
        String exampleIpAddress = "ExampleIpAddress";
        String exampleLanguage = "ExampleLanguage";
        String exampleAccessToken = "ExampleAccessToken";
        String exampleRefreshToken = "ExampleRefreshToken";
        Token refreshTokenObject = new Token(exampleRefreshToken, account, Token.TokenType.REFRESH_TOKEN);
        account.setAccountLanguage(exampleLanguage);
        when(authenticationFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        when(tokenProvider.generateRefreshToken(account)).thenReturn(refreshTokenObject);
        doNothing().when(authenticationFacade).edit(account);
        when(jwtProvider.generateJWTToken(account)).thenReturn(exampleAccessToken);

        AccessAndRefreshTokensDTO accessAndRefreshTokensDTO = authenticationService.registerSuccessfulLoginAttempt(account.getLogin(), false, exampleIpAddress, exampleLanguage);

        assertEquals(accessAndRefreshTokensDTO.getAccessToken(), exampleAccessToken);
        assertEquals(accessAndRefreshTokensDTO.getRefreshToken(), exampleRefreshToken);
    }

    @Test
    public void registerSuccessfulLoginAttemptFor1FATestPositiveWhenConfirmed() throws ApplicationBaseException {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        account.setTwoFactorAuth(false);
        String exampleIpAddress = "ExampleIpAddress";
        String exampleLanguage = "ExampleLanguage";
        String exampleAccessToken = "ExampleAccessToken";
        String exampleRefreshToken = "ExampleRefreshToken";
        Token refreshTokenObject = new Token(exampleRefreshToken, account, Token.TokenType.REFRESH_TOKEN);
        account.setAccountLanguage(exampleLanguage);
        when(authenticationFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        when(tokenProvider.generateRefreshToken(account)).thenReturn(refreshTokenObject);
        doNothing().when(authenticationFacade).edit(account);
        when(jwtProvider.generateJWTToken(account)).thenReturn(exampleAccessToken);

        AccessAndRefreshTokensDTO accessAndRefreshTokensDTO = authenticationService.registerSuccessfulLoginAttempt(account.getLogin(), true, exampleIpAddress, exampleLanguage);

        assertEquals(accessAndRefreshTokensDTO.getAccessToken(), exampleAccessToken);
        assertEquals(accessAndRefreshTokensDTO.getRefreshToken(), exampleRefreshToken);
    }

    @Test
    public void registerSuccessfulLoginAttemptFor2FATestNegativeAccountNotFound() throws Exception {
        String exampleLogin = "ExampleLogin";
        String exampleIpAddress = "ExampleIpAddress";
        String exampleLanguage = "ExampleLanguage";
        when(authenticationFacade.findByLogin(exampleLogin)).thenReturn(Optional.empty());

        assertThrows(InvalidLoginAttemptException.class, () -> authenticationService.registerSuccessfulLoginAttempt(exampleLogin, false, exampleIpAddress, exampleLanguage));
    }

    @Test
    public void registerSuccessfulLoginAttemptFor2FATestPositive() throws ApplicationBaseException, NoSuchAlgorithmException {
        authenticationService.generateKey();
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        String exampleIpAddress = "ExampleIpAddress";
        String exampleLanguage = "ExampleLanguage";
        account.setAccountLanguage(exampleLanguage);
        when(authenticationFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));

        AccessAndRefreshTokensDTO accessAndRefreshTokensDTO = authenticationService.registerSuccessfulLoginAttempt(account.getLogin(), false, exampleIpAddress, exampleLanguage);
        assertNull(accessAndRefreshTokensDTO);
    }

    @Test
    public void refreshUserSessionTestPositive() throws ApplicationBaseException {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        String exampleRefreshTokenValue = "exampleRefreshTokenNo1";
        String newExampleRefreshTokenValue = "newExampleRefreshTokenNo1";
        String newExampleAccessTokenValue = "newExampleAccessTokenNo1";
        Token refreshTokenObject = new Token(exampleRefreshTokenValue, account, Token.TokenType.REFRESH_TOKEN);
        Token newRefreshTokenObject = new Token(newExampleRefreshTokenValue, account, Token.TokenType.REFRESH_TOKEN);

        when(authenticationFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        when(tokenFacade.findByTokenValue(exampleRefreshTokenValue)).thenReturn(Optional.of(refreshTokenObject));
        when(jwtProvider.isTokenValid(exampleRefreshTokenValue, account)).thenReturn(true);
        when(tokenProvider.generateRefreshToken(account)).thenReturn(newRefreshTokenObject);
        when(jwtProvider.generateJWTToken(account)).thenReturn(newExampleAccessTokenValue);
        doNothing().when(tokenFacade).remove(refreshTokenObject);
        doNothing().when(tokenFacade).create(newRefreshTokenObject);

        AccessAndRefreshTokensDTO accessAndRefreshTokensDTO = authenticationService.refreshUserSession(exampleRefreshTokenValue, account.getLogin());
        assertNotNull(accessAndRefreshTokensDTO);
        assertEquals(newExampleAccessTokenValue, accessAndRefreshTokensDTO.getAccessToken());
        assertEquals(newExampleRefreshTokenValue, accessAndRefreshTokensDTO.getRefreshToken());
    }

    @Test
    public void refreshUserSessionTestNegativeAccountNotFound() throws Exception {
        String exampleLogin = "exampleLoginNo1";
        String exampleRefreshTokenValue = "exampleRefreshTokenNo1";
        when(authenticationFacade.findByLogin(exampleLogin)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> authenticationService.refreshUserSession(exampleRefreshTokenValue, exampleLogin));
    }

    @Test
    public void refreshUserSessionTestNegativeTokenNotFound() throws Exception {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        String exampleRefreshTokenValue = "exampleRefreshTokenNo1";
        when(authenticationFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        when(tokenFacade.findByTokenValue(exampleRefreshTokenValue)).thenReturn(Optional.empty());

        assertThrows(TokenNotFoundException.class, () -> authenticationService.refreshUserSession(exampleRefreshTokenValue, account.getLogin()));
    }

    @Test
    public void refreshUserSessionTestNegativeAccountCouldNotAuthenticate() throws Exception {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        account.setBlocked(true);
        String exampleRefreshTokenValue = "exampleRefreshTokenNo1";
        Token refreshTokenObject = new Token(exampleRefreshTokenValue, account, Token.TokenType.REFRESH_TOKEN);
        when(authenticationFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        when(tokenFacade.findByTokenValue(exampleRefreshTokenValue)).thenReturn(Optional.of(refreshTokenObject));

        assertThrows(AccountAuthenticationException.class, () -> authenticationService.refreshUserSession(exampleRefreshTokenValue, account.getLogin()));
    }

    @Test
    public void refreshUserSessionTestNegativeTokenNotValid() throws Exception {
        Account account = new Account("exampleLogin", "examplePassword", "exampleFirstname", "exampleLastname", "exampleEmail", "examplePhoneNumber");
        String exampleRefreshTokenValue = "exampleRefreshTokenNo1";
        Token refreshTokenObject = new Token(exampleRefreshTokenValue, account, Token.TokenType.REFRESH_TOKEN);
        when(authenticationFacade.findByLogin(account.getLogin())).thenReturn(Optional.of(account));
        when(tokenFacade.findByTokenValue(exampleRefreshTokenValue)).thenReturn(Optional.of(refreshTokenObject));
        when(jwtProvider.isTokenValid(exampleRefreshTokenValue, account)).thenReturn(false);

        assertThrows(TokenNotValidException.class, () -> authenticationService.refreshUserSession(exampleRefreshTokenValue, account.getLogin()));
    }
}
