package pl.lodz.p.it.ssbd2024.ssbd03.unit.controller.mok;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller.AccountExceptionResolver;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller.GenericExceptionResolver;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.authentication.AuthenticationLoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.authentication.AuthenticationCodeDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.exception.ExceptionDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.token.AccessAndRefreshTokensDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.token.RefreshTokenDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.webconfig.SpringWebInitializer;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountNotActivatedException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations.AuthenticationController;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations.AuthenticationService;
import org.springframework.security.authentication.BadCredentialsException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = SpringWebInitializer.class)
public class AuthenticationControllerMockTest {

    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthenticationController authenticationController;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
        AccessAndRefreshTokensDTO accessAndRefreshTokensDTO = new AccessAndRefreshTokensDTO("TEST_ACCESS_TOKEN", "TEST_REFRESH_TOKEN");
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(null);
        when(authenticationService.registerSuccessfulLoginAttempt(
                eq("johann13"),
                eq(false),
                anyString(),
                eq("pl"))).thenReturn(accessAndRefreshTokensDTO);
        AuthenticationLoginDTO accountLoginDTO = new AuthenticationLoginDTO("johann13", "H@selk0!", "pl");

        mockMvc.perform(post("/api/v1/auth/login-credentials")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountLoginDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(accessAndRefreshTokensDTO), result.getResponse().getContentAsString()));

        // Verify
        verify(authenticationManager).authenticate((Authentication) argCaptor.capture());

        assertEquals(accountLoginDTO.getLogin(), ((Authentication) argCaptor.getValue()).getName());
        assertEquals(accountLoginDTO.getPassword(), ((Authentication) argCaptor.getValue()).getCredentials());
    }

    @Test
    public void loginUsingCredentialsSuccessfulWithXForwardedFor() throws Exception {
        AccessAndRefreshTokensDTO accessAndRefreshTokensDTO = new AccessAndRefreshTokensDTO("TEST_ACCESS_TOKEN", "TEST_REFRESH_TOKEN");
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(null);
        when(authenticationService.registerSuccessfulLoginAttempt(
                eq("johann13"),
                eq(false),
                anyString(),
                eq("pl"))).thenReturn(accessAndRefreshTokensDTO);
        AuthenticationLoginDTO accountLoginDTO = new AuthenticationLoginDTO("johann13", "H@selk0!", "pl");

        mockMvc.perform(post("/api/v1/auth/login-credentials")
                        .header("X-Forwarded-For", "192.168.0.2, 1050:0000:0000:0000:0005:0600:300c:326b, 10.10.10.10")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountLoginDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(accessAndRefreshTokensDTO), result.getResponse().getContentAsString()));

        // Verify
        verify(authenticationManager).authenticate((Authentication) argCaptor.capture());

        assertEquals(accountLoginDTO.getLogin(), ((Authentication) argCaptor.getValue()).getName());
        assertEquals(accountLoginDTO.getPassword(), ((Authentication) argCaptor.getValue()).getCredentials());
    }

    @Test
    public void loginUsingCredentialsSuccessfulNoContent() throws Exception {
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(null);
        when(authenticationService.registerSuccessfulLoginAttempt(
                eq("johann13"),
                eq(false),
                anyString(),
                eq("pl"))).thenReturn(null);
        AuthenticationLoginDTO accountLoginDTO = new AuthenticationLoginDTO("johann13", "H@selk0!", "pl");

        mockMvc.perform(post("/api/v1/auth/login-credentials")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountLoginDTO)))
                .andExpect(status().isNoContent());

        // Verify
        verify(authenticationManager).authenticate((Authentication) argCaptor.capture());

        assertEquals(accountLoginDTO.getLogin(), ((Authentication) argCaptor.getValue()).getName());
        assertEquals(accountLoginDTO.getPassword(), ((Authentication) argCaptor.getValue()).getCredentials());
    }

    @Test
    public void loginUsingCredentialsFailedBadCredentials() throws Exception {
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(BadCredentialsException.class);
        doNothing().when(authenticationService).registerUnsuccessfulLoginAttemptWithIncrement(
                eq("johann13"),
                anyString());
        AuthenticationLoginDTO accountLoginDTO = new AuthenticationLoginDTO("johann13", "H@selk0!", "pl");

        mockMvc.perform(post("/api/v1/auth/login-credentials")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountLoginDTO)))
                .andExpect(status().is(401))
                .andExpect(result ->
                        assertEquals(
                                mapper.writeValueAsString(new ExceptionDTO(I18n.INVALID_LOGIN_ATTEMPT_EXCEPTION)),
                                result.getResponse().getContentAsString()
                        )
                );

        // Verify
        verify(authenticationService, times(1)).registerUnsuccessfulLoginAttemptWithIncrement(eq("johann13"), anyString());

        verify(authenticationManager).authenticate((Authentication) argCaptor.capture());

        assertEquals(accountLoginDTO.getLogin(), ((Authentication) argCaptor.getValue()).getName());
        assertEquals(accountLoginDTO.getPassword(), ((Authentication) argCaptor.getValue()).getCredentials());
    }

    @Test
    public void loginUsingCredentialsFailedDisabledAccount() throws Exception {
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(DisabledException.class);
        doNothing().when(authenticationService).registerUnsuccessfulLoginAttemptWithoutIncrement(
                eq("johann13"),
                anyString());
        AuthenticationLoginDTO accountLoginDTO = new AuthenticationLoginDTO("johann13", "H@selk0!", "pl");

        mockMvc.perform(post("/api/v1/auth/login-credentials")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountLoginDTO)))
                .andExpect(status().is(400))
                .andExpect(result ->
                        assertEquals(
                                mapper.writeValueAsString(new ExceptionDTO(I18n.ACCOUNT_INACTIVE_EXCEPTION)),
                                result.getResponse().getContentAsString()
                        )
                );

        // Verify
        verify(authenticationService, times(1)).registerUnsuccessfulLoginAttemptWithoutIncrement(eq("johann13"), anyString());

        verify(authenticationManager).authenticate((Authentication) argCaptor.capture());

        assertEquals(accountLoginDTO.getLogin(), ((Authentication) argCaptor.getValue()).getName());
        assertEquals(accountLoginDTO.getPassword(), ((Authentication) argCaptor.getValue()).getCredentials());
    }

    @Test
    public void loginUsingCredentialsFailedLockedByAdminAccount() throws Exception {
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(LockedException.class);
        doNothing().when(authenticationService).registerUnsuccessfulLoginAttemptWithoutIncrement(
                eq("johann13"),
                anyString());

        Account account = new Account();
        account.blockAccount(true);

        when(authenticationService.findByLogin("johann13")).thenReturn(Optional.of(account));
        AuthenticationLoginDTO accountLoginDTO = new AuthenticationLoginDTO("johann13", "H@selk0!", "pl");

        mockMvc.perform(post("/api/v1/auth/login-credentials")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountLoginDTO)))
                .andExpect(status().is(400))
                .andExpect(result ->
                        assertEquals(
                                mapper.writeValueAsString(new ExceptionDTO(I18n.ACCOUNT_BLOCKED_BY_ADMIN)),
                                result.getResponse().getContentAsString()
                        )
                );

        // Verify
        verify(authenticationService, times(1)).registerUnsuccessfulLoginAttemptWithoutIncrement(eq("johann13"), anyString());

        verify(authenticationManager).authenticate((Authentication) argCaptor.capture());

        assertEquals(accountLoginDTO.getLogin(), ((Authentication) argCaptor.getValue()).getName());
        assertEquals(accountLoginDTO.getPassword(), ((Authentication) argCaptor.getValue()).getCredentials());
    }

    @Test
    public void loginUsingCredentialsFailedLockedAccount() throws Exception {
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(LockedException.class);
        doNothing().when(authenticationService).registerUnsuccessfulLoginAttemptWithoutIncrement(
                eq("johann13"),
                anyString());

        Account account = new Account();
        account.blockAccount(false);

        when(authenticationService.findByLogin("johann13")).thenReturn(Optional.of(account));
        AuthenticationLoginDTO accountLoginDTO = new AuthenticationLoginDTO("johann13", "H@selk0!", "pl");

        mockMvc.perform(post("/api/v1/auth/login-credentials")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountLoginDTO)))
                .andExpect(status().is(400))
                .andExpect(result ->
                        assertEquals(
                                mapper.writeValueAsString(new ExceptionDTO(I18n.ACCOUNT_BLOCKED_BY_FAILED_LOGIN_ATTEMPTS)),
                                result.getResponse().getContentAsString()
                        )
                );

        // Verify
        verify(authenticationService, times(1)).registerUnsuccessfulLoginAttemptWithoutIncrement(eq("johann13"), anyString());

        verify(authenticationManager).authenticate((Authentication) argCaptor.capture());

        assertEquals(accountLoginDTO.getLogin(), ((Authentication) argCaptor.getValue()).getName());
        assertEquals(accountLoginDTO.getPassword(), ((Authentication) argCaptor.getValue()).getCredentials());
    }

    @Test
    public void loginUsingCredentialsFailedAuthenticationException() throws Exception {
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(UsernameNotFoundException.class);

        AuthenticationLoginDTO accountLoginDTO = new AuthenticationLoginDTO("johann13", "H@selk0!", "pl");

        mockMvc.perform(post("/api/v1/auth/login-credentials")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountLoginDTO)))
                .andExpect(status().is(401))
                .andExpect(result ->
                        assertEquals(
                                mapper.writeValueAsString(new ExceptionDTO(I18n.INVALID_LOGIN_ATTEMPT_EXCEPTION)),
                                result.getResponse().getContentAsString()
                        )
                );

        verify(authenticationManager).authenticate((Authentication) argCaptor.capture());

        assertEquals(accountLoginDTO.getLogin(), ((Authentication) argCaptor.getValue()).getName());
        assertEquals(accountLoginDTO.getPassword(), ((Authentication) argCaptor.getValue()).getCredentials());
    }

    @WithMockUser(username = "johann13", roles = {"CLIENT"})
    @Test
    public void loginUsingAuthenticationCodeSuccessful() throws Exception {
        AccessAndRefreshTokensDTO accessAndRefreshTokensDTO = new AccessAndRefreshTokensDTO("TEST_ACCESS_TOKEN", "TEST_REFRESH_TOKEN");
        doNothing().when(authenticationService).loginUsingAuthenticationCode(
                eq("johann13"),
                eq("TEST_VALUE"));
        when(authenticationService.registerSuccessfulLoginAttempt(
                eq("johann13"),
                eq(true),
                anyString(),
                eq("pl"))).thenReturn(accessAndRefreshTokensDTO);
        AuthenticationCodeDTO authenticationCodeDTO = new AuthenticationCodeDTO("johann13", "TEST_VALUE", "pl");

        mockMvc.perform(post("/api/v1/auth/login-auth-code")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(authenticationCodeDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(accessAndRefreshTokensDTO), result.getResponse().getContentAsString()));

        // Verify
        verify(authenticationService, times(1)).loginUsingAuthenticationCode(eq("johann13"), eq("TEST_VALUE"));
    }

    @Test
    public void loginUsingAuthenticationCodeFailed() throws Exception {
        doThrow(new AccountNotActivatedException()).when(authenticationService).loginUsingAuthenticationCode(
                eq("johann13"),
                eq("TEST_VALUE"));
        doNothing().when(authenticationService).registerUnsuccessfulLoginAttemptWithoutIncrement(
                eq("johann13"),
                anyString());
        AuthenticationCodeDTO authenticationCodeDTO = new AuthenticationCodeDTO("johann13", "TEST_VALUE", "pl");

        mockMvc.perform(post("/api/v1/auth/login-auth-code")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(authenticationCodeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result ->
                        assertEquals(
                                mapper.writeValueAsString(new ExceptionDTO(I18n.ACCOUNT_INACTIVE_EXCEPTION)),
                                result.getResponse().getContentAsString()
                        )
                );

        // Verify
        verify(authenticationService, times(1)).loginUsingAuthenticationCode(eq("johann13"), eq("TEST_VALUE"));
    }

    @WithMockUser(username = "ExampleAdminNo1", roles = {"ADMIN"})
    @Test
    public void logoutSuccessful() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout"))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "ExampleAdminNo3", roles = {"ADMIN"})
    @Test
    public void refreshUserSessionTestPositive() throws Exception {
        String userLogin = "ExampleAdminNo3";
        String exampleRefreshToken = "exampleRefreshToken";

        String newAccessToken = "newAccessTokenNo1";
        String newRefreshToken = "newRefreshTokenNo1";

        AccessAndRefreshTokensDTO accessAndRefreshTokensDTO = new AccessAndRefreshTokensDTO(newAccessToken, newRefreshToken);

        when(authenticationService.refreshUserSession(exampleRefreshToken, userLogin)).thenReturn(accessAndRefreshTokensDTO);

        mockMvc.perform(post("/api/v1/auth/refresh-session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RefreshTokenDTO(exampleRefreshToken))))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(accessAndRefreshTokensDTO),
                        result.getResponse().getContentAsString())
                );
    }
}
