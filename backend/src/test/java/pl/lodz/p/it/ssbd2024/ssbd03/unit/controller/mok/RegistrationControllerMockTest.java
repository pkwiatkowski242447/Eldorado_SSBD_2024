package pl.lodz.p.it.ssbd2024.ssbd03.unit.controller.mok;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller.AccountExceptionResolver;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.exception.controller.GenericExceptionResolver;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO.AccountRegisterDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.webconfig.SpringWebInitializer;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationInternalServerErrorException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.conflict.AccountConflictException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.validation.AccountConstraintViolationException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations.RegistrationController;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations.AccountService;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = SpringWebInitializer.class)
public class RegistrationControllerMockTest {

    @Mock
    private AccountService accountService;
    @InjectMocks
    private RegistrationController registrationController;

    private MockMvc mockMvc;

    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String testLogin = "kgreen";
    private static final String testPassword = "H@sl01!";
    private static final String testFirstname = "Karol";
    private static final String testLastname = "Green";
    private static final String testEmail = "kgreen@example.com";
    private static final String testPhoneNumber = "111222333";
    private static final String testLanguage = "pl";

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(registrationController)
                .setControllerAdvice(new AccountExceptionResolver(), new GenericExceptionResolver())
                .build();
    }

    @Test
    public void registerClientSuccessful() throws Exception {
        Account account = new Account(testLogin, testPassword, testFirstname, testLastname, testEmail, testPhoneNumber);
        Mockito.doReturn(account).when(accountService).registerClient(
                testLogin,
                testPassword,
                testFirstname,
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );
        AccountRegisterDTO accountRegisterDTO = new AccountRegisterDTO(
                testLogin,
                testPassword,
                testFirstname,
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/register/client")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountRegisterDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // Verify
        Mockito.verify(accountService, Mockito.times(1)).registerClient(
                testLogin,
                testPassword,
                testFirstname,
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );
    }

    @Test
    public void registerClientFailedInvalidData() throws Exception {
        Mockito.doThrow(AccountConstraintViolationException.class).when(accountService).registerClient(
                testLogin,
                testPassword,
                "",
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );
        AccountRegisterDTO accountRegisterDTO = new AccountRegisterDTO(
                testLogin,
                testPassword,
                "",
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/register/client")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountRegisterDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verify(accountService, Mockito.times(1)).registerClient(
                testLogin,
                testPassword,
                "",
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );
    }

    @Test
    public void registerClientFailedConflict() throws Exception {
        Mockito.doThrow(AccountConflictException.class).when(accountService).registerClient(
                testLogin,
                testPassword,
                testFirstname,
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );
        AccountRegisterDTO accountRegisterDTO = new AccountRegisterDTO(
                testLogin,
                testPassword,
                testFirstname,
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/register/client")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountRegisterDTO)))
                .andExpect(MockMvcResultMatchers.status().isConflict());

        // Verify
        Mockito.verify(accountService, Mockito.times(1)).registerClient(
                testLogin,
                testPassword,
                testFirstname,
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );
    }

    @Test
    public void registerClientFailedServerError() throws Exception {
        Mockito.doThrow(ApplicationInternalServerErrorException.class).when(accountService).registerClient(
                testLogin,
                testPassword,
                testFirstname,
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );
        AccountRegisterDTO accountRegisterDTO = new AccountRegisterDTO(
                testLogin,
                testPassword,
                testFirstname,
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/register/client")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountRegisterDTO)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());

        // Verify
        Mockito.verify(accountService, Mockito.times(1)).registerClient(
                testLogin,
                testPassword,
                testFirstname,
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );
    }

    @Test
    public void registerClientFailed() throws Exception {
        Mockito.doThrow(AccountConstraintViolationException.class).when(accountService).registerClient(
                testLogin,
                testPassword,
                "",
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );
        AccountRegisterDTO accountRegisterDTO = new AccountRegisterDTO(
                testLogin,
                testPassword,
                "",
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/register/client")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountRegisterDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verify(accountService, Mockito.times(1)).registerClient(
                testLogin,
                testPassword,
                "",
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );
    }

    @Test
    public void registerStaffSuccessful() throws Exception {
        Account account = new Account(testLogin, testPassword, testFirstname, testLastname, testEmail, testPhoneNumber);
        Mockito.doReturn(account).when(accountService).registerStaff(
                testLogin,
                testPassword,
                testFirstname,
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );
        AccountRegisterDTO accountRegisterDTO = new AccountRegisterDTO(
                testLogin,
                testPassword,
                testFirstname,
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/register/staff")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountRegisterDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // Verify
        Mockito.verify(accountService, Mockito.times(1)).registerStaff(
                testLogin,
                testPassword,
                testFirstname,
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );
    }

    @Test
    public void registerAdminSuccessful() throws Exception {
        Account account = new Account(testLogin, testPassword, testFirstname, testLastname, testEmail, testPhoneNumber);
        Mockito.doReturn(account).when(accountService).registerAdmin(
                testLogin,
                testPassword,
                testFirstname,
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );
        AccountRegisterDTO accountRegisterDTO = new AccountRegisterDTO(
                testLogin,
                testPassword,
                testFirstname,
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/register/admin")
                        .contentType(CONTENT_TYPE)
                        .content(mapper.writeValueAsString(accountRegisterDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // Verify
        Mockito.verify(accountService, Mockito.times(1)).registerAdmin(
                testLogin,
                testPassword,
                testFirstname,
                testLastname,
                testEmail,
                testPhoneNumber,
                testLanguage
        );
    }
}
