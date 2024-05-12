package pl.lodz.p.it.ssbd2024.ssbd03.integration.mok;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import pl.lodz.p.it.ssbd2024.ssbd03.TestcontainersConfig;
import pl.lodz.p.it.ssbd2024.ssbd03.config.webconfig.WebConfig;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Admin;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.UserLevelFacade;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
@ExtendWith(SpringExtension.class)
public class UserLevelMOKFacadeIntegrationTest extends TestcontainersConfig {

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
    private UserLevelFacade userLevelFacade;

    private final UUID uuidClientLevelUuid = UUID.fromString("bdb52e59-0054-4ec5-a2af-3e0d2b187ce0");
    private final UUID uuidUserLevelNo1 = UUID.fromString("69507c7f-4c03-4087-85e6-3ae3b6fc2201");
    private final UUID uuidAccountNo1 = UUID.fromString("b3b8c2ac-21ff-434b-b490-aa6d717447c0");
    private final UUID uuidAccountNo2 = UUID.fromString("0ca02f7e-d8e9-45d3-a332-a56015acb822");

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findTestPositive() {
        Optional<UserLevel> userLevel =  userLevelFacade.find(uuidClientLevelUuid);

        Assertions.assertFalse(userLevel.isEmpty());
        Assertions.assertEquals(uuidClientLevelUuid, userLevel.get().getId());
        Assertions.assertEquals(uuidAccountNo1, userLevel.get().getAccount().getId());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAndRefreshTestPositive() {
        Optional<UserLevel> userLevel =  userLevelFacade.findAndRefresh(uuidClientLevelUuid);

        Assertions.assertFalse(userLevel.isEmpty());
        Assertions.assertEquals(uuidClientLevelUuid, userLevel.get().getId());
        Assertions.assertEquals(uuidAccountNo1, userLevel.get().getAccount().getId());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllTestPositive() {
        List<UserLevel> list = userLevelFacade.findAll();

        Assertions.assertNotNull(list);
        Assertions.assertEquals(8, list.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void countTestPositive() {
        teardown();

        int count = userLevelFacade.count();

        Assertions.assertEquals(8, count);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void createAndRemoveTestPositive() {
        UserLevel userLevel = userLevelFacade.find(uuidUserLevelNo1).orElseThrow(NoSuchElementException::new);
        Account account = userLevel.getAccount();

        UserLevel Admin = new Admin();
        Admin.setAccount(account);

        int countBeforeAdd = userLevelFacade.count();
        Assertions.assertEquals(8, countBeforeAdd);

        userLevelFacade.create(Admin);
        int countAfterAdd = userLevelFacade.count();
        Assertions.assertEquals(9, countAfterAdd);

        UUID userLevelToRemove = null;
        List<UserLevel> list = userLevelFacade.findAll();
        for (UserLevel level : list) {
            if (level.getAccount() == account && level.getId() != uuidUserLevelNo1) {
                userLevelToRemove = level.getId();
                userLevelFacade.remove(level);
            }
        }
        int countAfterRemove = userLevelFacade.count();
        Assertions.assertEquals(8, countAfterRemove);

        Optional<UserLevel> deletedUserLevel = userLevelFacade.findAndRefresh(userLevelToRemove);
        Assertions.assertTrue(deletedUserLevel.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void editTestPositive() {
        //pobieram user level staff dla klienta
        UserLevel staffUserLevel = userLevelFacade.find(UUID.fromString("2488831d-c7c4-4f61-b48a-3be87364271f")).orElseThrow(NoSuchElementException::new);
        //UserLevel userLevel = userLevelFacade.find(uuidUserLevelNo1).get();
        //UserLevel userLevel2 = userLevelFacade.find(UUID.fromString("9428fadf-191c-4dd7-8626-01c3e0ff603c")).get();
        //pobieramy drugiego klienta

        //zapisujemy tego klienta
        Account account = staffUserLevel.getAccount(); //michal kowal - klient
        //Account account2 = userLevel2.getAccount(); //jakub koza - klient

        UserLevel Client = new Client();
        //dajemy mu clienta
        Client.setAccount(account);
        //Admin.setAccount(account); // dla kowala admin

        int countBeforeAdd = userLevelFacade.count();
        Assertions.assertEquals(8, countBeforeAdd);

        //dodajemy do bazy
        userLevelFacade.create(Client);
        int countAfterAdd = userLevelFacade.count();
        Assertions.assertEquals(9, countAfterAdd);

        UUID userLevelToEdit = null;
        List<UserLevel> list = userLevelFacade.findAll();
        for (UserLevel level : list) {
            if (level.getAccount() == account && level.getId() != uuidUserLevelNo1) {
                userLevelToEdit = level.getId();
            }
        }

        Client userLevelBeforeEdit = (Client) userLevelFacade.findAndRefresh(userLevelToEdit).orElseThrow(NoSuchElementException::new);
        Assertions.assertEquals(pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client.ClientType.BASIC, userLevelBeforeEdit.getType());
        userLevelBeforeEdit.setType(pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client.ClientType.PREMIUM);
        userLevelFacade.edit(userLevelBeforeEdit);
        Client userLevelAfterEdit = (Client) userLevelFacade.findAndRefresh(userLevelToEdit).orElseThrow(NoSuchElementException::new);
        Assertions.assertEquals(pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client.ClientType.PREMIUM,userLevelAfterEdit.getType());

        userLevelFacade.remove(userLevelAfterEdit);
    }


}
