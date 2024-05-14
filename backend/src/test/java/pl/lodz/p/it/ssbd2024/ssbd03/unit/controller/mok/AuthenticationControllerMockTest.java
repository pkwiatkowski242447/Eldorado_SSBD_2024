package pl.lodz.p.it.ssbd2024.ssbd03.unit.controller.mok;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller.AccountExceptionResolver;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller.GenericExceptionResolver;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountLoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AuthenticationCodeDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.webconfig.SpringWebInitializer;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountNotActivatedException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations.AuthenticationController;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations.AuthenticationService;
import org.springframework.security.authentication.BadCredentialsException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = SpringWebInitializer.class)
public class AuthenticationControllerMockTest {

    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;

    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Captor
    private ArgumentCaptor<?> argCaptor;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setControllerAdvice(new AccountExceptionResolver(), new GenericExceptionResolver())
                .build();
    }

    @Test
    public void loginUsingCredentialsSuccessful() throws Exception {
        Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenReturn(null);
        Mockito.when(authenticationService.registerSuccessfulLoginAttempt(
                Mockito.eq("johann13"),
                Mockito.eq(false),
                Mockito.anyString(),
                Mockito.eq("pl"))).thenReturn("TEST_TOKEN");
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("johann13", "H@selk0!", "pl");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login-credentials")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountLoginDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertEquals("TEST_TOKEN", result.getResponse().getContentAsString()));

        // Verify
        Mockito.verify(authenticationManager).authenticate((Authentication) argCaptor.capture());

        assertEquals(accountLoginDTO.getLogin(), ((Authentication)argCaptor.getValue()).getName());
        assertEquals(accountLoginDTO.getPassword(), ((Authentication)argCaptor.getValue()).getCredentials());
    }

    @Test
    public void loginUsingCredentialsSuccessfulNoContent() throws Exception {
        Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenReturn(null);
        Mockito.when(authenticationService.registerSuccessfulLoginAttempt(
                Mockito.eq("johann13"),
                Mockito.eq(false),
                Mockito.anyString(),
                Mockito.eq("pl"))).thenReturn(null);
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("johann13", "H@selk0!", "pl");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login-credentials")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountLoginDTO)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verify
        Mockito.verify(authenticationManager).authenticate((Authentication) argCaptor.capture());

        assertEquals(accountLoginDTO.getLogin(), ((Authentication)argCaptor.getValue()).getName());
        assertEquals(accountLoginDTO.getPassword(), ((Authentication)argCaptor.getValue()).getCredentials());
    }

    @Test
    public void loginUsingCredentialsFailedBadCredentials() throws Exception {
        Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenThrow(BadCredentialsException.class);
        Mockito.doNothing().when(authenticationService).registerUnsuccessfulLoginAttemptWithIncrement(
                Mockito.eq("johann13"),
                Mockito.anyString());
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("johann13", "H@selk0!", "pl");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login-credentials")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountLoginDTO)))
                .andExpect(MockMvcResultMatchers.status().is(401))
                .andExpect(result -> assertEquals(I18n.INVALID_LOGIN_ATTEMPT_EXCEPTION, result.getResponse().getContentAsString()));

        // Verify
        Mockito.verify(authenticationService, Mockito.times(1)).registerUnsuccessfulLoginAttemptWithIncrement(Mockito.eq("johann13"), Mockito.anyString());

        Mockito.verify(authenticationManager).authenticate((Authentication) argCaptor.capture());

        assertEquals(accountLoginDTO.getLogin(), ((Authentication)argCaptor.getValue()).getName());
        assertEquals(accountLoginDTO.getPassword(), ((Authentication)argCaptor.getValue()).getCredentials());
    }

    @Test
    public void loginUsingCredentialsFailedDisabledAccount() throws Exception {
        Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenThrow(DisabledException.class);
        Mockito.doNothing().when(authenticationService).registerUnsuccessfulLoginAttemptWithoutIncrement(
                Mockito.eq("johann13"),
                Mockito.anyString());
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("johann13", "H@selk0!", "pl");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login-credentials")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountLoginDTO)))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(result -> assertEquals(I18n.ACCOUNT_INACTIVE_EXCEPTION, result.getResponse().getContentAsString()));

        // Verify
        Mockito.verify(authenticationService, Mockito.times(1)).registerUnsuccessfulLoginAttemptWithoutIncrement(Mockito.eq("johann13"), Mockito.anyString());

        Mockito.verify(authenticationManager).authenticate((Authentication) argCaptor.capture());

        assertEquals(accountLoginDTO.getLogin(), ((Authentication)argCaptor.getValue()).getName());
        assertEquals(accountLoginDTO.getPassword(), ((Authentication)argCaptor.getValue()).getCredentials());
    }

    @Test
    public void loginUsingCredentialsFailedLockedByAdminAccount() throws Exception {
        Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenThrow(LockedException.class);
        Mockito.doNothing().when(authenticationService).registerUnsuccessfulLoginAttemptWithoutIncrement(
                Mockito.eq("johann13"),
                Mockito.anyString());

        Account account = new Account();
        account.blockAccount(true);

        Mockito.when(authenticationService.findByLogin("johann13")).thenReturn(Optional.of(account));
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("johann13", "H@selk0!", "pl");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login-credentials")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountLoginDTO)))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(result -> assertEquals(I18n.ACCOUNT_BLOCKED_BY_ADMIN, result.getResponse().getContentAsString()));

        // Verify
        Mockito.verify(authenticationService, Mockito.times(1)).registerUnsuccessfulLoginAttemptWithoutIncrement(Mockito.eq("johann13"), Mockito.anyString());

        Mockito.verify(authenticationManager).authenticate((Authentication) argCaptor.capture());

        assertEquals(accountLoginDTO.getLogin(), ((Authentication)argCaptor.getValue()).getName());
        assertEquals(accountLoginDTO.getPassword(), ((Authentication)argCaptor.getValue()).getCredentials());
    }

    @Test
    public void loginUsingCredentialsFailedLockedAccount() throws Exception {
        Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenThrow(LockedException.class);
        Mockito.doNothing().when(authenticationService).registerUnsuccessfulLoginAttemptWithoutIncrement(
                Mockito.eq("johann13"),
                Mockito.anyString());

        Account account = new Account();
        account.blockAccount(false);

        Mockito.when(authenticationService.findByLogin("johann13")).thenReturn(Optional.of(account));
        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("johann13", "H@selk0!", "pl");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login-credentials")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountLoginDTO)))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(result -> assertEquals(I18n.ACCOUNT_BLOCKED_BY_FAILED_LOGIN_ATTEMPTS, result.getResponse().getContentAsString()));

        // Verify
        Mockito.verify(authenticationService, Mockito.times(1)).registerUnsuccessfulLoginAttemptWithoutIncrement(Mockito.eq("johann13"), Mockito.anyString());

        Mockito.verify(authenticationManager).authenticate((Authentication) argCaptor.capture());

        assertEquals(accountLoginDTO.getLogin(), ((Authentication)argCaptor.getValue()).getName());
        assertEquals(accountLoginDTO.getPassword(), ((Authentication)argCaptor.getValue()).getCredentials());
    }

    @Test
    public void loginUsingCredentialsFailedAuthenticationException() throws Exception {
        Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenThrow(UsernameNotFoundException.class);

        AccountLoginDTO accountLoginDTO = new AccountLoginDTO("johann13", "H@selk0!", "pl");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login-credentials")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountLoginDTO)))
                .andExpect(MockMvcResultMatchers.status().is(401))
                .andExpect(result -> assertEquals(I18n.INVALID_LOGIN_ATTEMPT_EXCEPTION, result.getResponse().getContentAsString()));

        Mockito.verify(authenticationManager).authenticate((Authentication) argCaptor.capture());

        assertEquals(accountLoginDTO.getLogin(), ((Authentication)argCaptor.getValue()).getName());
        assertEquals(accountLoginDTO.getPassword(), ((Authentication)argCaptor.getValue()).getCredentials());
    }

    @Test
    public void loginUsingAuthenticationCodeSuccessful() throws Exception {
        Mockito.doNothing().when(authenticationService).loginUsingAuthenticationCode(
                Mockito.eq("johann13"),
                Mockito.eq("TEST_VALUE"));
        Mockito.when(authenticationService.registerSuccessfulLoginAttempt(
                Mockito.eq("johann13"),
                Mockito.eq(true),
                Mockito.anyString(),
                Mockito.eq("pl"))).thenReturn("TEST_TOKEN");
        AuthenticationCodeDTO authenticationCodeDTO = new AuthenticationCodeDTO("johann13", "TEST_VALUE", "pl");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login-auth-code")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(authenticationCodeDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertEquals("TEST_TOKEN", result.getResponse().getContentAsString()));

        // Verify
        Mockito.verify(authenticationService, Mockito.times(1)).loginUsingAuthenticationCode(Mockito.eq("johann13"), Mockito.eq("TEST_VALUE"));
    }

    @Test
    public void loginUsingAuthenticationCodeFailed() throws Exception {
        Mockito.doThrow(new AccountNotActivatedException()).when(authenticationService).loginUsingAuthenticationCode(
                Mockito.eq("johann13"),
                Mockito.eq("TEST_VALUE"));
        Mockito.doNothing().when(authenticationService).registerUnsuccessfulLoginAttemptWithoutIncrement(
                Mockito.eq("johann13"),
                Mockito.anyString());
        AuthenticationCodeDTO authenticationCodeDTO = new AuthenticationCodeDTO("johann13", "TEST_VALUE", "pl");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login-auth-code")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(authenticationCodeDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertEquals(I18n.ACCOUNT_INACTIVE_EXCEPTION, result.getResponse().getContentAsString()));

        // Verify
        Mockito.verify(authenticationService, Mockito.times(1)).loginUsingAuthenticationCode(Mockito.eq("johann13"), Mockito.eq("TEST_VALUE"));
    }

    @Test
    public void logoutSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/logout"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
