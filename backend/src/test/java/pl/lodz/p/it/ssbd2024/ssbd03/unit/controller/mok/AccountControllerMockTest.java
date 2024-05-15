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
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller.AccountExceptionResolver;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller.GenericExceptionResolver;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller.TokenExceptionResolver;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountChangePasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountPasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.webconfig.SpringWebInitializer;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountIdNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.resetOwnPassword.CurrentPasswordAndNewPasswordAreTheSameException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.resetOwnPassword.IncorrectPasswordException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountBlockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountNotActivatedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.TokenNotValidException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.read.TokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations.AccountController;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations.AccountService;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;

import java.lang.reflect.Field;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setControllerAdvice(new GenericExceptionResolver(), new AccountExceptionResolver(), new TokenExceptionResolver()).build();
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
    public void addClientUserLevelTestUnsuccessful() throws Exception {
        String exampleId = "ExampleId";

        mockMvc.perform(post("/api/v1/accounts/{id}/add-level-client", exampleId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
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
    public void addStaffUserLevelTestUnsuccessful() throws Exception {
        String exampleId = "ExampleId";

        mockMvc.perform(post("/api/v1/accounts/{id}/add-level-staff", exampleId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
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
    public void addAdminUserLevelTestUnsuccessful() throws Exception {
        String exampleId = "ExampleId";

        mockMvc.perform(post("/api/v1/accounts/{id}/add-level-admin", exampleId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
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
    public void removeClientUserLevelTestUnsuccessful() throws Exception {
        String exampleId = "ExampleId";

        mockMvc.perform(post("/api/v1/accounts/{id}/remove-level-client", exampleId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
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
    public void removeStaffUserLevelTestUnsuccessful() throws Exception {
        String exampleId = "ExampleId";

        mockMvc.perform(post("/api/v1/accounts/{id}/remove-level-staff", exampleId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
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
    public void removeAdminUserLevelTestUnsuccessful() throws Exception {
        String exampleId = "ExampleId";

        mockMvc.perform(post("/api/v1/accounts/{id}/remove-level-admin", exampleId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
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
    public void activateAccountTestPositive() throws Exception {
        String exampleId = "ExampleId";

        when(accountService.activateAccount(exampleId)).thenReturn(true);

        mockMvc.perform(post("/api/v1/accounts/activate-account/{exampleId}", exampleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void activateAccountTestNegative() throws Exception {
        String exampleId = "ExampleId";

        when(accountService.activateAccount(exampleId)).thenReturn(false);

        mockMvc.perform(post("/api/v1/accounts/activate-account/{exampleId}", exampleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void changeAccountPasswordTestNegativeTokenNotFound() throws Exception {
        String exampleToken = "ExampleToken";
        String examplePassword = "ExamplePassword";
        ObjectMapper objectMapper = new ObjectMapper();
        AccountPasswordDTO accountPasswordDTO = new AccountPasswordDTO(examplePassword);

        doThrow(TokenNotFoundException.class).when(accountService).changeAccountPassword(exampleToken, accountPasswordDTO.getPassword());

        mockMvc.perform(post("/api/v1/accounts/change-password/{token_id}", exampleToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountPasswordDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void changeAccountPasswordTestNegativeTokenNotValid() throws Exception {
        String exampleToken = "ExampleToken";
        String examplePassword = "ExamplePassword";
        ObjectMapper objectMapper = new ObjectMapper();
        AccountPasswordDTO accountPasswordDTO = new AccountPasswordDTO(examplePassword);

        doThrow(TokenNotValidException.class).when(accountService).changeAccountPassword(exampleToken, accountPasswordDTO.getPassword());

        mockMvc.perform(post("/api/v1/accounts/change-password/{token_id}", exampleToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountPasswordDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void changeAccountPasswordTestNegativeAccountIdNotFoundException() throws Exception {
        String exampleToken = "ExampleToken";
        String examplePassword = "ExamplePassword";
        ObjectMapper objectMapper = new ObjectMapper();
        AccountPasswordDTO accountPasswordDTO = new AccountPasswordDTO(examplePassword);

        doThrow(AccountIdNotFoundException.class).when(accountService).changeAccountPassword(exampleToken, accountPasswordDTO.getPassword());

        mockMvc.perform(post("/api/v1/accounts/change-password/{token_id}", exampleToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountPasswordDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void changeAccountPasswordTestNegativeAccountBlockedException() throws Exception {
        String exampleToken = "ExampleToken";
        String examplePassword = "ExamplePassword";
        ObjectMapper objectMapper = new ObjectMapper();
        AccountPasswordDTO accountPasswordDTO = new AccountPasswordDTO(examplePassword);

        doThrow(AccountBlockedException.class).when(accountService).changeAccountPassword(exampleToken, accountPasswordDTO.getPassword());

        mockMvc.perform(post("/api/v1/accounts/change-password/{token_id}", exampleToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountPasswordDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void changeAccountPasswordTestNegativeAccountNotActivatedException() throws Exception {
        String exampleToken = "ExampleToken";
        String examplePassword = "ExamplePassword";
        ObjectMapper objectMapper = new ObjectMapper();
        AccountPasswordDTO accountPasswordDTO = new AccountPasswordDTO(examplePassword);

        doThrow(AccountNotActivatedException.class).when(accountService).changeAccountPassword(exampleToken, accountPasswordDTO.getPassword());

        mockMvc.perform(post("/api/v1/accounts/change-password/{token_id}", exampleToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountPasswordDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void changeAccountPasswordTestPositive() throws Exception {
        String exampleToken = "ExampleToken";
        String examplePassword = "ExamplePassword";
        ObjectMapper objectMapper = new ObjectMapper();
        AccountPasswordDTO accountPasswordDTO = new AccountPasswordDTO(examplePassword);

        doNothing().when(accountService).changeAccountPassword(exampleToken, accountPasswordDTO.getPassword());

        mockMvc.perform(post("/api/v1/accounts/change-password/{token_id}", exampleToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountPasswordDTO)))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "ExampleAdminLogin1", roles = {"ADMIN"})
    @Test
    public void changePasswordSelfTestNegativeAccountNotFound() throws Exception {
        String oldPassword = "ExampleOldPassword1";
        String newPassword = "ExampleNewPassword1";
        String userName = "ExampleAdminLogin1";
        ObjectMapper objectMapper = new ObjectMapper();
        AccountChangePasswordDTO accountChangePasswordDTO = new AccountChangePasswordDTO(oldPassword, newPassword);

        doThrow(AccountNotFoundException.class).when(accountService).changePasswordSelf(accountChangePasswordDTO.getOldPassword(),
                accountChangePasswordDTO.getNewPassword(),
                userName);

        mockMvc.perform(patch("/api/v1/accounts/change-password/self")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountChangePasswordDTO)))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "ExampleAdminLogin2", roles = {"ADMIN"})
    @Test
    public void changePasswordSelfTestNegativeOldPasswordIsNotCorrect() throws Exception {
        String oldPassword = "ExampleOldPassword2";
        String newPassword = "ExampleNewPassword2";
        String userName = "ExampleAdminLogin2";
        ObjectMapper objectMapper = new ObjectMapper();
        AccountChangePasswordDTO accountChangePasswordDTO = new AccountChangePasswordDTO(oldPassword, newPassword);

        doThrow(IncorrectPasswordException.class).when(accountService).changePasswordSelf(accountChangePasswordDTO.getOldPassword(),
                accountChangePasswordDTO.getNewPassword(),
                userName);

        mockMvc.perform(patch("/api/v1/accounts/change-password/self")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountChangePasswordDTO)))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "ExampleAdminLogin3", roles = {"ADMIN"})
    @Test
    public void changePasswordSelfTestNegativeNewPasswordTheSameAsTheOldOne() throws Exception {
        String oldPassword = "ExampleOldPassword3";
        String newPassword = "ExampleNewPassword3";
        String userName = "ExampleAdminLogin3";
        ObjectMapper objectMapper = new ObjectMapper();
        AccountChangePasswordDTO accountChangePasswordDTO = new AccountChangePasswordDTO(oldPassword, newPassword);

        doThrow(CurrentPasswordAndNewPasswordAreTheSameException.class).when(accountService).changePasswordSelf(accountChangePasswordDTO.getOldPassword(),
                accountChangePasswordDTO.getNewPassword(),
                userName);

        mockMvc.perform(patch("/api/v1/accounts/change-password/self")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountChangePasswordDTO)))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "ExampleAdminLogin4", roles = {"ADMIN"})
    @Test
    public void changePasswordSelfTestPositive() throws Exception {
        String oldPassword = "ExampleOldPassword4";
        String newPassword = "ExampleNewPassword4";
        String userName = "ExampleAdminLogin4";
        ObjectMapper objectMapper = new ObjectMapper();
        AccountChangePasswordDTO accountChangePasswordDTO = new AccountChangePasswordDTO(oldPassword, newPassword);

        doNothing().when(accountService).changePasswordSelf(accountChangePasswordDTO.getOldPassword(),
                accountChangePasswordDTO.getNewPassword(),
                userName);

        mockMvc.perform(patch("/api/v1/accounts/change-password/self")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountChangePasswordDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void getAccountsByMatchingLoginFirstNameAndLastNameTestPositiveWithNoEmptyListOfAccounts() throws Exception {
        Account accountNo1 = new Account("exampleLogin1", "examplePassword1", "exampleFirstName1", "exampleLastName1", "exampleEmail1", "examplePhoneNumber1");
        Account accountNo2 = new Account("exampleLogin2", "examplePassword2", "exampleFirstName2", "exampleLastName2", "exampleEmail2", "examplePhoneNumber2");
        Account accountNo3 = new Account("exampleLogin3", "examplePassword3", "exampleFirstName3", "exampleLastName3", "exampleEmail3", "examplePhoneNumber3");
        List<Account> listOfAccounts = List.of(accountNo1, accountNo2, accountNo3);

        when(accountService.getAccountsByMatchingLoginFirstNameAndLastName(anyString(), anyString(), anyString(),
                anyBoolean(), anyBoolean(), anyInt(), anyInt())).thenReturn(listOfAccounts);

        mockMvc.perform(get("/api/v1/accounts//match-login-firstname-and-lastname")
                        .queryParam("login", "ExampleLogin")
                        .queryParam("firstName", "ExampleFirstName")
                        .queryParam("lastName", "ExampleLastName")
                        .queryParam("active", "true")
                        .queryParam("order", "false")
                        .queryParam("pageNumber", "0")
                        .queryParam("pageSize", "5"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAccountsByMatchingLoginFirstNameAndLastNameTestPositiveWhenThereAreNoAccounts() throws Exception {
        List<Account> listOfAccounts = new ArrayList<>();

        when(accountService.getAccountsByMatchingLoginFirstNameAndLastName(anyString(), anyString(), anyString(),
                anyBoolean(), anyBoolean(), anyInt(), anyInt())).thenReturn(listOfAccounts);

        mockMvc.perform(get("/api/v1/accounts//match-login-firstname-and-lastname")
                        .queryParam("login", "ExampleLogin")
                        .queryParam("firstName", "ExampleFirstName")
                        .queryParam("lastName", "ExampleLastName")
                        .queryParam("active", "true")
                        .queryParam("order", "false")
                        .queryParam("pageNumber", "0")
                        .queryParam("pageSize", "5"))
                .andExpect(status().isNoContent());
    }
}
