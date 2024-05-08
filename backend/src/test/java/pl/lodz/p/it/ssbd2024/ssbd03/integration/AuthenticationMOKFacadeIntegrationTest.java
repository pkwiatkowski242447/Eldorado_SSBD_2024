package pl.lodz.p.it.ssbd2024.ssbd03.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import pl.lodz.p.it.ssbd2024.ssbd03.TestcontainersConfig;
import pl.lodz.p.it.ssbd2024.ssbd03.config.webconfig.WebConfig;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AuthenticationFacade;

import java.util.Optional;
import java.util.UUID;

@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
@ExtendWith(SpringExtension.class)
public class AuthenticationMOKFacadeIntegrationTest extends TestcontainersConfig {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    //INITIAL DATA
    @Autowired
    private AuthenticationFacade authenticationFacade;

    private UUID accountIdNo1 = UUID.fromString("b3b8c2ac-21ff-434b-b490-aa6d717447c0");
    private String accountLoginNo2 = "jerzybem";
    private String accountFirstNameNo2 = "Jerzy";
    private String accountLastNameNo2 = "Bem";
    private String accountEmailNo2 = "jbem@example.com";
    private String accountLanguageNo1 = "PL";
    private String accountPhoneNumberNo2 = "111111111";

    private String accountPasswordNo1 = "$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa";


    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAndRefreshTestPositive() {
        Optional<Account> account = authenticationFacade.findAndRefresh(accountIdNo1);

        Assertions.assertTrue(account.isPresent());

        Assertions.assertEquals(accountLoginNo2, account.get().getLogin());
        Assertions.assertEquals(accountPasswordNo1, account.get().getPassword());
        Assertions.assertEquals(accountFirstNameNo2, account.get().getName());
        Assertions.assertEquals(accountLastNameNo2, account.get().getLastname());
        Assertions.assertEquals(accountEmailNo2, account.get().getEmail());
        Assertions.assertEquals(accountPhoneNumberNo2, account.get().getPhoneNumber());
        Assertions.assertEquals(accountLanguageNo1, account.get().getAccountLanguage());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findTestPositive() {
        Optional<Account> account = authenticationFacade.findAndRefresh(accountIdNo1);

        Assertions.assertTrue(account.isPresent());

        Assertions.assertEquals(accountLoginNo2, account.get().getLogin());
        Assertions.assertEquals(accountPasswordNo1, account.get().getPassword());
        Assertions.assertEquals(accountFirstNameNo2, account.get().getName());
        Assertions.assertEquals(accountLastNameNo2, account.get().getLastname());
        Assertions.assertEquals(accountEmailNo2, account.get().getEmail());
        Assertions.assertEquals(accountPhoneNumberNo2, account.get().getPhoneNumber());
        Assertions.assertEquals(accountLanguageNo1, account.get().getAccountLanguage());
    }


}
