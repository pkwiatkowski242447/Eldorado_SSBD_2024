package pl.lodz.p.it.ssbd2024.ssbd03.unit.controller.mok;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller.AccountExceptionResolver;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller.GenericExceptionResolver;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AccountOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.config.webconfig.SpringWebInitializer;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountAlreadyBlockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountAlreadyUnblockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations.AccountController;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations.AccountService;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = SpringWebInitializer.class)
public class AccountControllerMockTest {
    private static final ObjectMapper mapper = new ObjectMapper();
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
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setControllerAdvice(new GenericExceptionResolver(), new AccountExceptionResolver())
                .build();
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
        when(accountService.getAllAccounts(pageNumber, pageSize)).thenReturn(List.of(testAccount, testAccount1, testAccount2));
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

   @Test
    @WithMockUser("loginAdm")
    public void blockAccountTestSuccessful() throws Exception {
        doNothing().when(accountService).blockAccount(UUID.fromString(testId));
        when(accountService.getAccountById(UUID.fromString(testId))).thenReturn(testAccount);

        mockMvc.perform(
                        post("/api/v1/accounts/{id}/block", testId))
                .andExpect(status().isNoContent());

        verify(accountService, times(1)).blockAccount(UUID.fromString(testId));
    }

    @ParameterizedTest
    @ValueSource(strings = {"   ", "4648eeb1-a120-4724-86d9e2977aa", "4648eeb1-a120-4724-86d9-e2977aa9474de", "4648eeb1-a120-4724-86d9-e2977aa9474X"})
    public void blockAccountTestFailedInvalidId(String id) throws Exception {
        mockMvc.perform(
                        post("/api/v1/accounts/{id}/block", id))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertEquals(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION, result.getResponse().getContentAsString()));
    }

    @Test
    @WithMockUser(username = "login")
    public void blockAccountTestFailedTryToBlockOwnAccount() throws Exception {
        when(accountService.getAccountById(UUID.fromString(testId))).thenReturn(testAccount);

        mockMvc.perform(
                        post("/api/v1/accounts/{id}/block", testId))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertEquals(I18n.ACCOUNT_TRY_TO_BLOCK_OWN_EXCEPTION, result.getResponse().getContentAsString()));
    }

    @Test
    public void blockAccountTestFailedTryBlockBlockedAccount() throws Exception {
        doThrow(new AccountAlreadyBlockedException()).when(accountService).blockAccount(UUID.fromString(testId));

        mockMvc.perform(
                        post("/api/v1/accounts/{id}/block", testId))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertEquals(I18n.ACCOUNT_ALREADY_BLOCKED, result.getResponse().getContentAsString()));

        verify(accountService, times(1)).blockAccount(UUID.fromString(testId));
    }

    @Test
    public void unblockAccountTestSuccessful() throws Exception {
        doNothing().when(accountService).unblockAccount(UUID.fromString(testId));

        mockMvc.perform(
                        post("/api/v1/accounts/{id}/unblock", testId))
                .andExpect(status().isNoContent());

        verify(accountService, times(1)).unblockAccount(UUID.fromString(testId));
    }

    @ParameterizedTest
    @ValueSource(strings = {"   ", "4648eeb1-a120-4724-86d9e2977aa", "4648eeb1-a120-4724-86d9-e2977aa9474de", "4648eeb1-a120-4724-86d9-e2977aa9474X"})
    public void unblockAccountTestFailedInvalidId(String id) throws Exception {
        mockMvc.perform(
                        post("/api/v1/accounts/{id}/unblock", id))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertEquals(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION, result.getResponse().getContentAsString()));
    }

    @Test
    public void unblockAccountTestFailedTryBlockBlockedAccount() throws Exception {
        doThrow(new AccountAlreadyUnblockedException()).when(accountService).unblockAccount(UUID.fromString(testId));

        mockMvc.perform(
                        post("/api/v1/accounts/{id}/unblock", testId))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertEquals(I18n.ACCOUNT_ALREADY_UNBLOCKED, result.getResponse().getContentAsString()));

        verify(accountService, times(1)).unblockAccount(UUID.fromString(testId));
    }

    @Test
    @WithMockUser(username = "login")
    public void getSelfTestSuccessful() throws Exception {
        when(accountService.getAccountByLogin(testAccount.getLogin())).thenReturn(testAccount);
        when(jwtProvider.generateObjectSignature(any())).thenReturn("SIGNATURE");

        mockMvc.perform(
                        get("/api/v1/accounts/self"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string("ETag", "\"SIGNATURE\""))
                .andExpect(result -> {
                            assertEquals(mapper.writeValueAsString(AccountMapper.toAccountOutputDto(testAccount)),
                                    result.getResponse().getContentAsString());
                        }
                );
    }

    @Test
    @WithMockUser(username = "login")
    public void getSelfTestFailedAccountNotFoundInDB() throws Exception {
        when(accountService.getAccountByLogin(testAccount.getLogin())).thenThrow(AccountNotFoundException.class);

        mockMvc.perform(
                        get("/api/v1/accounts/self"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN));
    }

    @Captor
    private ArgumentCaptor<Account> accountCaptor;
    @Captor
    private ArgumentCaptor<AccountModifyDTO> modifyDtoCaptor;

    @Test
    @WithMockUser(username = "login")
    public void modifyAccountSelfTestSuccessful() throws Exception {
        AccountModifyDTO accountModifyDTO = new AccountModifyDTO(
                testAccount.getLogin(),
                testAccount.getVersion(),
                new HashSet<>(),
                "Karol",
                testAccount.getLastname(),
                testAccount.getPhoneNumber(),
                testAccount.getTwoFactorAuth()
        );
        Account account = AccountMapper.toAccount(accountModifyDTO);
        AccountOutputDTO accountOutputDTO = AccountMapper.toAccountOutputDto(account);

        when(jwtProvider.generateObjectSignature(any())).thenReturn("SIGNATURE");
        when(accountService.modifyAccount(any(), eq(testAccount.getLogin()))).thenReturn(account);

        mockMvc.perform(
                        put("/api/v1/accounts/self")
                                .header("If-Match", "SIGNATURE")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(accountModifyDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(result -> {
                            assertEquals(mapper.writeValueAsString(accountOutputDTO),
                                    result.getResponse().getContentAsString());
                        }
                );

        // Verify
        verify(jwtProvider, times(1)).generateObjectSignature(modifyDtoCaptor.capture());
        assertEquals(accountModifyDTO.getLogin(), modifyDtoCaptor.getValue().getLogin());
        assertEquals(accountModifyDTO.getName(), modifyDtoCaptor.getValue().getName());
        assertNotEquals(testAccount.getName(), modifyDtoCaptor.getValue().getName());

        verify(accountService, times(1)).modifyAccount(accountCaptor.capture(), eq(testAccount.getLogin()));
        assertEquals(account.getId(), accountCaptor.getValue().getId());
        assertEquals(account.getLogin(), accountCaptor.getValue().getLogin());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  "})
    public void modifyAccountSelfTestFailedInvalidIfMatch(String ifMatch) throws Exception {
        AccountModifyDTO accountModifyDTO = new AccountModifyDTO(
                testAccount.getLogin(),
                testAccount.getVersion(),
                new HashSet<>(),
                "Karol",
                testAccount.getLastname(),
                testAccount.getPhoneNumber(),
                testAccount.getTwoFactorAuth()
        );

        mockMvc.perform(
                        put("/api/v1/accounts/self")
                                .header("If-Match", ifMatch)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(accountModifyDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertEquals(I18n.MISSING_HEADER_IF_MATCH, result.getResponse().getContentAsString()));
    }

    @Test
    public void modifyAccountSelfTestFailedDataIntegrityCompromised() throws Exception {
        AccountModifyDTO accountModifyDTO = new AccountModifyDTO(
                testAccount.getLogin(),
                testAccount.getVersion(),
                new HashSet<>(),
                "Karol",
                testAccount.getLastname(),
                testAccount.getPhoneNumber(),
                testAccount.getTwoFactorAuth()
        );

        when(jwtProvider.generateObjectSignature(any())).thenReturn("SIGNATURE");

        mockMvc.perform(
                        put("/api/v1/accounts/self")
                                .header("If-Match", "SIGNATURE@@@")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(accountModifyDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertEquals(I18n.DATA_INTEGRITY_COMPROMISED, result.getResponse().getContentAsString()));

        // Verify
        verify(jwtProvider, times(1)).generateObjectSignature(modifyDtoCaptor.capture());
        assertEquals(accountModifyDTO.getLogin(), modifyDtoCaptor.getValue().getLogin());
        assertEquals(accountModifyDTO.getName(), modifyDtoCaptor.getValue().getName());
        assertNotEquals(testAccount.getName(), modifyDtoCaptor.getValue().getName());
    }

    @Test
    public void modifyUserAccountTestSuccessful() throws Exception {
        AccountModifyDTO accountModifyDTO = new AccountModifyDTO(
                testAccount.getLogin(),
                testAccount.getVersion(),
                new HashSet<>(),
                "Karol",
                testAccount.getLastname(),
                testAccount.getPhoneNumber(),
                testAccount.getTwoFactorAuth()
        );
        Account account = AccountMapper.toAccount(accountModifyDTO);
        AccountOutputDTO accountOutputDTO = AccountMapper.toAccountOutputDto(account);

        when(jwtProvider.generateObjectSignature(any())).thenReturn("SIGNATURE");
        when(accountService.modifyAccount(any(), eq(testAccount.getLogin()))).thenReturn(account);

        mockMvc.perform(
                        put("/api/v1/accounts")
                                .header("If-Match", "SIGNATURE")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(accountModifyDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(result -> {
                            assertEquals(mapper.writeValueAsString(accountOutputDTO),
                                    result.getResponse().getContentAsString());
                        }
                );

        // Verify
        verify(jwtProvider, times(1)).generateObjectSignature(modifyDtoCaptor.capture());
        assertEquals(accountModifyDTO.getLogin(), modifyDtoCaptor.getValue().getLogin());
        assertEquals(accountModifyDTO.getName(), modifyDtoCaptor.getValue().getName());
        assertNotEquals(testAccount.getName(), modifyDtoCaptor.getValue().getName());

        verify(accountService, times(1)).modifyAccount(accountCaptor.capture(), eq(testAccount.getLogin()));
        assertEquals(account.getId(), accountCaptor.getValue().getId());
        assertEquals(account.getLogin(), accountCaptor.getValue().getLogin());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  "})
    public void modifyUserAccountTestFailedInvalidIfMatch(String ifMatch) throws Exception {
        AccountModifyDTO accountModifyDTO = new AccountModifyDTO(
                testAccount.getLogin(),
                testAccount.getVersion(),
                new HashSet<>(),
                "Karol",
                testAccount.getLastname(),
                testAccount.getPhoneNumber(),
                testAccount.getTwoFactorAuth()
        );

        mockMvc.perform(
                        put("/api/v1/accounts")
                                .header("If-Match", ifMatch)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(accountModifyDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertEquals(I18n.MISSING_HEADER_IF_MATCH, result.getResponse().getContentAsString()));
    }

    @Test
    public void modifyUserAccountTestFailedDataIntegrityCompromised() throws Exception {
        AccountModifyDTO accountModifyDTO = new AccountModifyDTO(
                testAccount.getLogin(),
                testAccount.getVersion(),
                new HashSet<>(),
                "Karol",
                testAccount.getLastname(),
                testAccount.getPhoneNumber(),
                testAccount.getTwoFactorAuth()
        );

        when(jwtProvider.generateObjectSignature(any())).thenReturn("SIGNATURE");

        mockMvc.perform(
                        put("/api/v1/accounts")
                                .header("If-Match", "SIGNATURE@@@")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(accountModifyDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertEquals(I18n.DATA_INTEGRITY_COMPROMISED, result.getResponse().getContentAsString()));

        // Verify
        verify(jwtProvider, times(1)).generateObjectSignature(modifyDtoCaptor.capture());
        assertEquals(accountModifyDTO.getLogin(), modifyDtoCaptor.getValue().getLogin());
        assertEquals(accountModifyDTO.getName(), modifyDtoCaptor.getValue().getName());
        assertNotEquals(testAccount.getName(), modifyDtoCaptor.getValue().getName());
    }
}
