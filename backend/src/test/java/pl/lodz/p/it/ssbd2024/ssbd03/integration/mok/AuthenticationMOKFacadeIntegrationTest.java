package pl.lodz.p.it.ssbd2024.ssbd03.integration.mok;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import pl.lodz.p.it.ssbd2024.ssbd03.TestcontainersConfig;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.config.webconfig.WebConfig;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AuthenticationFacade;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
@ExtendWith(SpringExtension.class)
public class AuthenticationMOKFacadeIntegrationTest extends TestcontainersConfig {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("jdbc.ssbd03.url", () -> String.format("jdbc:postgresql://localhost:%s/ssbd03", postgres.getFirstMappedPort()));
    }

    @AfterEach
    void teardown() {
        ((AtomikosDataSourceBean) webApplicationContext.getBean("dataSourceAdmin")).close();
        ((AtomikosDataSourceBean) webApplicationContext.getBean("dataSourceAuth")).close();
        ((AtomikosDataSourceBean) webApplicationContext.getBean("dataSourceMOP")).close();
        ((AtomikosDataSourceBean) webApplicationContext.getBean("dataSourceMOK")).close();
    }

    //INITIAL DATA
    @Autowired
    private AuthenticationFacade authenticationFacade;

    private final UUID accountIdNo1 = UUID.fromString("b3b8c2ac-21ff-434b-b490-aa6d717447c0");
    private final String accountLoginNo2 = "jerzybem";
    private final String accountFirstNameNo2 = "Jerzy";
    private final String accountLastNameNo2 = "Bem";
    private final String accountEmailNo2 = "jbem@example.com";
    private final String accountLanguageNo1 = "PL";
    private final String accountPhoneNumberNo2 = "111111111";

    private final String accountPasswordNo1 = "$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa";

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Roles.ADMIN})
    public void findAndRefreshTestPositive() {
        Optional<Account> account = authenticationFacade.findAndRefresh(accountIdNo1);

        assertTrue(account.isPresent());

        assertEquals(accountLoginNo2, account.get().getLogin());
        assertEquals(accountPasswordNo1, account.get().getPassword());
        assertEquals(accountFirstNameNo2, account.get().getName());
        assertEquals(accountLastNameNo2, account.get().getLastname());
        assertEquals(accountEmailNo2, account.get().getEmail());
        assertEquals(accountPhoneNumberNo2, account.get().getPhoneNumber());
        assertEquals(accountLanguageNo1, account.get().getAccountLanguage());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Roles.ADMIN})
    public void findTestPositive() {
        Optional<Account> account = authenticationFacade.find(accountIdNo1);

        assertTrue(account.isPresent());

        assertEquals(accountLoginNo2, account.get().getLogin());
        assertEquals(accountPasswordNo1, account.get().getPassword());
        assertEquals(accountFirstNameNo2, account.get().getName());
        assertEquals(accountLastNameNo2, account.get().getLastname());
        assertEquals(accountEmailNo2, account.get().getEmail());
        assertEquals(accountPhoneNumberNo2, account.get().getPhoneNumber());
        assertEquals(accountLanguageNo1, account.get().getAccountLanguage());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findByLoginTestPositive() {
        Optional<Account> account = authenticationFacade.findByLogin(accountLoginNo2);

        assertTrue(account.isPresent());

        assertEquals(accountLoginNo2, account.get().getLogin());
        assertEquals(accountPasswordNo1, account.get().getPassword());
        assertEquals(accountFirstNameNo2, account.get().getName());
        assertEquals(accountLastNameNo2, account.get().getLastname());
        assertEquals(accountEmailNo2, account.get().getEmail());
        assertEquals(accountPhoneNumberNo2, account.get().getPhoneNumber());
        assertEquals(accountLanguageNo1, account.get().getAccountLanguage());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Roles.AUTHENTICATED, Roles.ADMIN})
    public void editTestPositive() {
        Account account = authenticationFacade.find(accountIdNo1).orElseThrow(NoSuchElementException::new);

        assertTrue(account.getActive());
        account.setActive(false);
        authenticationFacade.edit(account); //<--------------------????

        Account account1 = authenticationFacade.findAndRefresh(accountIdNo1).orElseThrow(NoSuchElementException::new);

        assertFalse(account1.getActive());
    }
}
