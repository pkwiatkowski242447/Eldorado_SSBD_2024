package pl.lodz.p.it.ssbd2024.ssbd03.unit.controller.mok;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.webconfig.SpringWebInitializer;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations.AccountController;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations.AccountService;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;

import java.lang.reflect.Field;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = SpringWebInitializer.class)
public class AccountControllerMockTest {
    static private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private AccountService accountService;
    @Mock
    private JWTProvider jwtProvider;
    @InjectMocks
    private AccountController accountController;
    private MockMvc mockMvc;

    private Account testAccount;
    private final String testId = "58089158-6d4f-42b1-946d-95d159a3ea2c";

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
        testAccount = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        testAccount.setAccountLanguage("pl");
        Field idField = AbstractEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(testAccount, UUID.fromString(testId));
        idField.setAccessible(false);
    }

    @Test
    public void getUserByIdTestSuccessful() throws Exception {
        when(accountService.getAccountById(any())).thenReturn(testAccount);
        when(jwtProvider.generateObjectSignature(any())).thenReturn("SIGNATURE");
        mockMvc.perform(
                        get("/api/v1/accounts/{id}", testId)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andExpect(header().string("ETag", "\"SIGNATURE\""));
    }

    @Test
    public void getUserByIdTestAccountNotFound() throws Exception {
        when(accountService.getAccountById(any())).thenThrow(AccountNotFoundException.class);

        mockMvc.perform(
                        get("/api/v1/accounts/{id}", testId)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUserByIdTestAccountInvalidId() throws Exception {
        mockMvc.perform(
                        get("/api/v1/accounts/{id}", "invalididhere")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllUsersTestSuccessfulEmpty() throws Exception {
        int pageNumber = 0;
        int pageSize = 3;
        when(accountService.getAllAccounts(pageNumber, pageSize)).thenReturn(new ArrayList<>());
        mockMvc.perform(
                        get("/api/v1/accounts", testId)
                                .param("pageNumber", Integer.toString(pageNumber))
                                .param("pageSize", Integer.toString(pageSize))
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }
    @Test
    public void getAllUsersTestSuccessful() throws Exception {
        Account testAccount1 = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        Account testAccount2 = new Account("login", "TestPassword", "firstName", "lastName", "test@email.com", "123123123");
        int pageNumber = 0;
        int pageSize = 3;
        when(accountService.getAllAccounts(pageNumber, pageSize)).thenReturn(List.of(testAccount,testAccount1,testAccount2));
        mockMvc.perform(
                        get("/api/v1/accounts", testId)
                                .param("pageNumber", Integer.toString(pageNumber))
                                .param("pageSize", Integer.toString(pageSize))
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }


    @Test
    public void resendEmailConfirmationTestSuccessful() throws Exception {
        mockMvc.perform(
                        post("/api/v1/accounts/resend-email-confirmation")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    public void forgetAccountPasswordTestSuccessful() throws Exception {
        String email = "test@email.com";
        AccountEmailDTO emailDTO = new AccountEmailDTO(email);

        doNothing().when(accountService).forgetAccountPassword(email);

        mockMvc.perform(
                        post("/api/v1/accounts/forgot-password")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(emailDTO)))
                .andExpect(status().isNoContent());
        verify(accountService, times(1)).forgetAccountPassword(email);
    }

    @Test
    public void resetAccountPasswordTestSuccessful() throws Exception {
        when(accountService.getAccountById(UUID.fromString(testId))).thenReturn(testAccount);
        doNothing().when(accountService).forgetAccountPassword(testAccount.getEmail());

        mockMvc.perform(
                        post("/api/v1/accounts/reset-password/{id}", testId)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    public void resetAccountPasswordTestAccountNotFound() throws Exception {
        when(accountService.getAccountById(UUID.fromString(testId))).thenThrow(AccountNotFoundException.class);

        mockMvc.perform(
                        post("/api/v1/accounts/reset-password/{id}", testId)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addClientUserLevelTestSuccessful() throws Exception {

        doNothing().when(accountService).addClientUserLevel(testId);

        mockMvc.perform(
                        post("/api/v1/accounts/{id}/add-level-client", testId)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    public void addStaffUserLevelTestSuccessful() throws Exception {

        doNothing().when(accountService).addStaffUserLevel(testId);

        mockMvc.perform(
                        post("/api/v1/accounts/{id}/add-level-staff", testId)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    public void addAdminUserLevelTestSuccessful() throws Exception {

        doNothing().when(accountService).addAdminUserLevel(testId);

        mockMvc.perform(
                        post("/api/v1/accounts/{id}/add-level-admin", testId)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    public void removeClientUserLevelTestSuccessful() throws Exception {

        doNothing().when(accountService).removeClientUserLevel(testId);

        mockMvc.perform(
                        post("/api/v1/accounts/{id}/remove-level-client", testId)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    public void removeStaffUserLevelTestSuccessful() throws Exception {

        doNothing().when(accountService).removeStaffUserLevel(testId);

        mockMvc.perform(
                        post("/api/v1/accounts/{id}/remove-level-staff", testId)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    public void removeAdminUserLevelTestSuccessful() throws Exception {

        doNothing().when(accountService).removeAdminUserLevel(testId);

        mockMvc.perform(
                        post("/api/v1/accounts/{id}/remove-level-admin", testId)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    public void changeEmailTestSuccessful() throws Exception {
        String email = "test@email.com";
        AccountEmailDTO emailDTO = new AccountEmailDTO(email);

        doNothing().when(accountService).changeEmail(UUID.fromString(testId), email);

        mockMvc.perform(
                        patch("/api/v1/accounts/{id}/change-email", testId)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(emailDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser("login")
    public void changeEmailSelfTestSuccessful() throws Exception {
        String email = "test@email.com";
        AccountEmailDTO emailDTO = new AccountEmailDTO(email);

        when(accountService.getAccountByLogin(testAccount.getLogin())).thenReturn(testAccount);
        doNothing().when(accountService).changeEmail(UUID.fromString(testId), email);

        mockMvc.perform(
                        patch("/api/v1/accounts/change-email-self", testId)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(emailDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void confirmEmailTestSuccessful() throws Exception {
        String token = "TOKEN VALUE";

        when(accountService.confirmEmail(token)).thenReturn(true);

        mockMvc.perform(
                        post("/api/v1/accounts/confirm-email/{token}", token)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    public void confirmEmailTestTokenInvalidOrExpired() throws Exception {
        String token = "TOKEN VALUE";

        when(accountService.confirmEmail(token)).thenReturn(false);

        mockMvc.perform(
                        post("/api/v1/accounts/confirm-email/{token}", token)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}
