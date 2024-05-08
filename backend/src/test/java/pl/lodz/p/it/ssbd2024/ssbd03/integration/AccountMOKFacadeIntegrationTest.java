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
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import java.util.concurrent.TimeUnit;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
@ExtendWith(SpringExtension.class)
public class AccountMOKFacadeIntegrationTest extends TestcontainersConfig {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    //INITIAL DATA
    @Autowired
    private AccountMOKFacade accountMOKFacade;

    private UUID accountIdNo1 = UUID.fromString("b3b8c2ac-21ff-434b-b490-aa6d717447c0");

    //login
    private String accountLoginNo1 = "loginITNo1";
    private String accountLoginNo2 = "jerzybem";
    private String accountLoginNo3 = "loginITNo2";
    private String accountLoginNo4 = "loginITNo3";

    //password

    private String accountPasswordNo1 = "$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa";

    //name
    private String accountFirstNameNo1 = "firstNameITNo1";
    private String accountFirstNameNo3 = "firstNameITNo2";
    private String accountFirstNameNo4 = "firstNameITNo3";
    private String accountFirstNameNo2 = "Jerzy";

    //last name
    private String accountLastNameNo1 = "lastNameITNo1";
    private String accountLastNameNo3 = "lastNameITNo2";
    private String accountLastNameNo4 = "lastNameITNo3";
    private String accountLastNameNo2 = "Bem";

    //email
    private String accountEmailNo1 = "emailtest@it.com";
    private String accountEmailNo3 = "emailtest2@it.com";
    private String accountEmailNo4 = "emailtest3@it.com";
    private String accountEmailNo2 = "jbem@example.com";

    //phone number
    private String accountPhoneNumberNo1 = "123123123";
    private String accountPhoneNumberNo2 = "111111111";
    private String accountPhoneNumberNo3 = "111111112";
    private String accountPhoneNumberNo4 = "111111115";

    //language
    private String accountLanguageNo1 = "PL";

    //user levels
    private UserLevel accountUserLevelClientNo1 = new Client();
    private UserLevel accountUserLevelStaffNo1 = new Staff();
    private UserLevel accountUserLevelAdminNo1 = new Admin();

    //

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findMethodReturnExistingAccountTestPositive() {
        UserLevel userLevelClientNo1 = new Client();
        UserLevel userLevelStaffNo1 = new Staff();
        UserLevel userLevelAdminNo1 = new Admin();

        Collection<UserLevel> userLevels = new ArrayList<>();
        userLevels.add(userLevelAdminNo1);
        userLevels.add(userLevelStaffNo1);
        userLevels.add(userLevelClientNo1);


        Optional<Account> account = accountMOKFacade.find(accountIdNo1);
        //Check if method find existing user
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
    public void findMethodReturnNullAccountTestPositive() {
        UUID uuid = UUID.fromString("b3b8c2ac-22ff-434b-b490-aa6d717447c0");
        Optional<Account> account = accountMOKFacade.find(uuid);

        Assertions.assertTrue(account.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findByLoginReturnExistingAccountPositiveTest() {
        Optional<Account> account = accountMOKFacade.findByLogin(accountLoginNo2);

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
    public void findByLoginReturnNullAccountTestPositive() {
        Optional<Account> account = accountMOKFacade.findByLogin(accountLoginNo1);

        Assertions.assertTrue(account.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAndRefreshReturnExistingAccountPositiveTest() {
        Optional<Account> account = accountMOKFacade.findAndRefresh(accountIdNo1);

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
    public void findAndRefreshReturnNullAccountTestPositive() {
        UUID uuid = UUID.fromString("b3b8c2ac-22ff-434b-b490-aa6d717447c0");
        Optional<Account> account = accountMOKFacade.find(uuid);

        Assertions.assertTrue(account.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllTestPositive() {
        List<Account> accounts = accountMOKFacade.findAll();

        Assertions.assertEquals(7, accounts.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllWithPaginationTestPositive() {
        List<Account> accountsNo1 = accountMOKFacade.findAllAccountsWithPagination(0, 4);
        List<Account> accountsNo2 = accountMOKFacade.findAllAccountsWithPagination(1, 3);
        List<Account> accountsNo3 = accountMOKFacade.findAllAccountsWithPagination(1, 4);

        Assertions.assertEquals(4, accountsNo1.size());
        Assertions.assertEquals(3, accountsNo2.size());
        Assertions.assertEquals(3, accountsNo3.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllActiveAccountsWithPaginationTestPositive() {
        List<Account> accountsNo1 = accountMOKFacade.findAllActiveAccountsWithPagination(0, 4);
        List<Account> accountsNo2 = accountMOKFacade.findAllActiveAccountsWithPagination(1, 3);
        List<Account> accountsNo3 = accountMOKFacade.findAllActiveAccountsWithPagination(1, 4);

        Assertions.assertEquals(4, accountsNo1.size());
        Assertions.assertEquals(3, accountsNo2.size());
        Assertions.assertEquals(3, accountsNo3.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findByEmailReturnExistingAccountPositiveTest() {
        Optional<Account> account = accountMOKFacade.findByEmail(accountEmailNo2);

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
    public void findByEmailReturnNullAccountTestPositive() {
        Optional<Account> account = accountMOKFacade.findByEmail(accountEmailNo1);

        Assertions.assertTrue(account.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findByLoginAndRefreshReturnExistingAccountTestPositive() {
        Optional<Account> account = accountMOKFacade.findByLoginAndRefresh(accountLoginNo2);

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
    public void findByLoginAndRefreshReturnNullTestPositive() {
        Optional<Account> account = accountMOKFacade.findByLoginAndRefresh(accountLoginNo1);

        Assertions.assertTrue(account.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void createAndRemoveMethodPositiveTest() {
        //Create account with user level
        Account account = new Account(accountLoginNo1, accountPasswordNo1, accountFirstNameNo1, accountLastNameNo1,
                accountEmailNo1, accountPhoneNumberNo1);
        UserLevel userLevelClientNo1 = new Client();
        userLevelClientNo1.setAccount(account);
        account.addUserLevel(userLevelClientNo1);
        account.setAccountLanguage(accountLanguageNo1);
        //Facade
        accountMOKFacade.create(account);
        Optional<Account> newAccount = accountMOKFacade.findByLoginAndRefresh(accountLoginNo1);
        Assertions.assertTrue(newAccount.isPresent());

        Assertions.assertEquals(accountLoginNo1, newAccount.get().getLogin());
        Assertions.assertEquals(accountPasswordNo1, newAccount.get().getPassword());
        Assertions.assertEquals(accountFirstNameNo1, newAccount.get().getName());
        Assertions.assertEquals(accountLastNameNo1, newAccount.get().getLastname());
        Assertions.assertEquals(accountEmailNo1, newAccount.get().getEmail());
        Assertions.assertEquals(accountPhoneNumberNo1, newAccount.get().getPhoneNumber());
        Assertions.assertEquals(accountLanguageNo1, newAccount.get().getAccountLanguage());

        accountMOKFacade.remove(newAccount.get());

        Optional<Account> deletedAccount = accountMOKFacade.findByLoginAndRefresh(accountLoginNo1);
        Assertions.assertTrue(deletedAccount.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllInactiveAccountsWithPaginationPositiveTest() {
        Account account = new Account(accountLoginNo3, accountPasswordNo1, accountFirstNameNo3, accountLastNameNo3,
                accountEmailNo3, accountPhoneNumberNo3);
        UserLevel userLevelClientNo1 = new Client();
        account.setActive(false);
        userLevelClientNo1.setAccount(account);
        account.addUserLevel(userLevelClientNo1);
        account.setAccountLanguage(accountLanguageNo1);

        accountMOKFacade.create(account);

        Account account2 = new Account(accountLoginNo4, accountPasswordNo1, accountFirstNameNo4, accountLastNameNo4,
                accountEmailNo4, accountPhoneNumberNo4);
        account2.setActive(false);
        userLevelClientNo1.setAccount(account2);
        account2.addUserLevel(userLevelClientNo1);
        account2.setAccountLanguage(accountLanguageNo1);

        accountMOKFacade.create(account2);


        Collection<Account> accounts1 = accountMOKFacade.findAllInactiveAccountsWithPagination(0, 4);
        Collection<Account> accounts2 = accountMOKFacade.findAllInactiveAccountsWithPagination(0, 1);
        Collection<Account> accounts3 = accountMOKFacade.findAllInactiveAccountsWithPagination(1, 1);
        Assertions.assertEquals(2, accounts1.size());
        Assertions.assertEquals(1, accounts2.size());
        Assertions.assertEquals(1, accounts3.size());

        Optional<Account> accountToDelete1 = accountMOKFacade.findByLoginAndRefresh(accountLoginNo4);
        Optional<Account> accountToDelete2 = accountMOKFacade.findByLoginAndRefresh(accountLoginNo3);

        accountMOKFacade.remove(accountToDelete1.get());
        accountMOKFacade.remove(accountToDelete2.get());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllActiveAccountsWithGivenUserLevelWithPaginationPositiveTest() {
        List<Account> accounts0 = accountMOKFacade.findAll();
        Assertions.assertEquals(7, accounts0.size());
        List<Account> accounts1 = accountMOKFacade.findAllActiveAccountsWithGivenUserLevelWithPagination(Client.class, 0, 20);
        List<Account> accounts2 = accountMOKFacade.findAllActiveAccountsWithGivenUserLevelWithPagination(Staff.class, 0, 2);
        List<Account> accounts3 = accountMOKFacade.findAllActiveAccountsWithGivenUserLevelWithPagination(Staff.class, 1, 1);
        List<Account> accounts4 = accountMOKFacade.findAllActiveAccountsWithGivenUserLevelWithPagination(Admin.class, 0, 3);
        Assertions.assertEquals(4, accounts1.size());
        Assertions.assertEquals(2, accounts2.size());
        Assertions.assertEquals(1, accounts3.size());
        Assertions.assertEquals(1, accounts4.size());

    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllAccountsMatchingLoginWithPagination() {
        Account account = new Account(accountLoginNo3, accountPasswordNo1, accountFirstNameNo3, accountLastNameNo3,
                accountEmailNo3, accountPhoneNumberNo3);
        UserLevel userLevelClientNo1 = new Client();
        account.setActive(false);
        userLevelClientNo1.setAccount(account);
        account.addUserLevel(userLevelClientNo1);
        account.setAccountLanguage(accountLanguageNo1);

        accountMOKFacade.create(account);

        Account account2 = new Account(accountLoginNo4, accountPasswordNo1, accountFirstNameNo4, accountLastNameNo4,
                accountEmailNo4, accountPhoneNumberNo4);
        account2.setActive(false);
        userLevelClientNo1.setAccount(account2);
        account2.addUserLevel(userLevelClientNo1);
        account2.setAccountLanguage(accountLanguageNo1);

        accountMOKFacade.create(account2);

        List<Account> accounts0 = accountMOKFacade.findAllAccountsMatchingLoginWithPagination("loginITN", false, 0, 2);
        List<Account> accounts1 = accountMOKFacade.findAllAccountsMatchingLoginWithPagination("jerzy", true, 0, 5);
        List<Account> accounts2 = accountMOKFacade.findAllAccountsMatchingLoginWithPagination("loginITN", true, 0, 4);
        Assertions.assertEquals(2, accounts0.size());
        Assertions.assertEquals(1, accounts1.size());
        Assertions.assertEquals(0, accounts2.size());

        Optional<Account> accountToDelete1 = accountMOKFacade.findByLoginAndRefresh(accountLoginNo4);
        Optional<Account> accountToDelete2 = accountMOKFacade.findByLoginAndRefresh(accountLoginNo3);

        accountMOKFacade.remove(accountToDelete1.get());
        accountMOKFacade.remove(accountToDelete2.get());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void editPositiveTest() {

        Account account = new Account(accountLoginNo3, accountPasswordNo1, accountFirstNameNo3, accountLastNameNo3,
                accountEmailNo3, accountPhoneNumberNo3);
        UserLevel userLevelClientNo1 = new Client();
        account.setActive(false);
        userLevelClientNo1.setAccount(account);
        account.addUserLevel(userLevelClientNo1);
        account.setAccountLanguage(accountLanguageNo1);

        accountMOKFacade.create(account);
        Optional<Account> accountFind = accountMOKFacade.findByLoginAndRefresh(accountLoginNo3);

        Assertions.assertEquals(accountEmailNo3, accountFind.get().getEmail());
        accountFind.get().setEmail(accountEmailNo4);

        accountMOKFacade.edit(accountFind.get());
        Optional<Account> accountEdited = accountMOKFacade.findByLoginAndRefresh(accountLoginNo3);
        Assertions.assertEquals(accountEmailNo4, accountFind.get().getEmail());

        accountMOKFacade.remove(accountEdited.get());

    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllAccountsMarkedForDeletionTestPositive() throws NoSuchFieldException, IllegalAccessException {
        /*Account account = new Account(accountLoginNo3, accountPasswordNo1, accountFirstNameNo3, accountLastNameNo3,
                accountEmailNo3, accountPhoneNumberNo3);
        UserLevel userLevelClientNo1 = new Client();
        userLevelClientNo1.setAccount(account);
        account.addUserLevel(userLevelClientNo1);
        account.setAccountLanguage(accountLanguageNo1);

        Account account2 = new Account(accountLoginNo4, accountPasswordNo1, accountFirstNameNo4, accountLastNameNo4,
                accountEmailNo4, accountPhoneNumberNo4);
        userLevelClientNo1.setAccount(account2);
        account2.addUserLevel(userLevelClientNo1);
        account2.setAccountLanguage(accountLanguageNo1);


        accountMOKFacade.create(account);
        accountMOKFacade.create(account2);
        Optional<Account> accountToEdit1 = accountMOKFacade.findByLoginAndRefresh(accountLoginNo4);
        Optional<Account> accountToEdit2 = accountMOKFacade.findByLoginAndRefresh(accountLoginNo3);
        Assertions.assertFalse(accountToEdit1.isEmpty());
        Assertions.assertFalse(accountToEdit2.isEmpty());

        //Two unverified accounts - now we use reflection to change their creation time
        Field creationDateField = Account.class.getDeclaredField("creationDate");
        creationDateField.setAccessible(true);
        //creationDateField.set(accountToEdit1.get(), LocalDateTime.now().minus(Long.parseLong("5"), TimeUnit.HOURS));
        //creationDateField.set(accountToEdit2.get(), LocalDateTime.now().minus(Long.parseLong("5"), TimeUnit.HOURS));

        List<Account> accounts0 = accountMOKFacade.findAllAccountsMarkedForDeletion(2, TimeUnit.HOURS);
        Assertions.assertEquals(0,accounts0.size());*/
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllAccountsByBlockedTestPositive() throws NoSuchFieldException, IllegalAccessException {
        Account account = new Account(accountLoginNo3, accountPasswordNo1, accountFirstNameNo3, accountLastNameNo3,
                accountEmailNo3, accountPhoneNumberNo3);
        UserLevel userLevelClientNo1 = new Client();
        userLevelClientNo1.setAccount(account);
        account.addUserLevel(userLevelClientNo1);

        account.setAccountLanguage(accountLanguageNo1);

        Account account2 = new Account(accountLoginNo4, accountPasswordNo1, accountFirstNameNo4, accountLastNameNo4,
                accountEmailNo4, accountPhoneNumberNo4);
        userLevelClientNo1.setAccount(account2);
        account2.addUserLevel(userLevelClientNo1);
        account2.setAccountLanguage(accountLanguageNo1);

        accountMOKFacade.create(account);
        accountMOKFacade.create(account2);

        Optional<Account> account1Find = accountMOKFacade.findByLoginAndRefresh(accountLoginNo3);
        Optional<Account> account2Find = accountMOKFacade.findByLoginAndRefresh(accountLoginNo4);

        Field blockedField = Account.class.getDeclaredField("blocked");
        blockedField.setAccessible(true);
        blockedField.set(account1Find.get(), true);
        blockedField.set(account2Find.get(), true);

        Assertions.assertTrue(account1Find.get().getBlocked());
        Assertions.assertTrue(account2Find.get().getBlocked());

        accountMOKFacade.edit(account1Find.get());
        accountMOKFacade.edit(account2Find.get());

        List<Account> accounts0 = accountMOKFacade.findAllAccountsByBlocked(true, 0, 2);
        List<Account> accounts1 = accountMOKFacade.findAllAccountsByBlocked(true, 0, 1);
        List<Account> accounts2 = accountMOKFacade.findAllAccountsByBlocked(true, 1, 1);

        List<Account> accounts3 = accountMOKFacade.findAllAccountsByBlocked(false, 0, 10);

        Assertions.assertEquals(2, accounts0.size());
        Assertions.assertEquals(1, accounts1.size());
        Assertions.assertEquals(1, accounts2.size());

        Assertions.assertEquals(7, accounts3.size());

        accountMOKFacade.remove(account1Find.get());
        accountMOKFacade.remove(account2Find.get());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllBlockedAccountsThatWereBlockedByAdminWithPagination() throws IllegalAccessException, NoSuchFieldException {
        Account account = new Account(accountLoginNo3, accountPasswordNo1, accountFirstNameNo3, accountLastNameNo3,
                accountEmailNo3, accountPhoneNumberNo3);
        UserLevel userLevelClientNo1 = new Client();
        userLevelClientNo1.setAccount(account);
        account.addUserLevel(userLevelClientNo1);

        account.setAccountLanguage(accountLanguageNo1);

        Account account2 = new Account(accountLoginNo4, accountPasswordNo1, accountFirstNameNo4, accountLastNameNo4,
                accountEmailNo4, accountPhoneNumberNo4);
        userLevelClientNo1.setAccount(account2);
        account2.addUserLevel(userLevelClientNo1);
        account2.setAccountLanguage(accountLanguageNo1);

        accountMOKFacade.create(account);
        accountMOKFacade.create(account2);

        Optional<Account> account1Find = accountMOKFacade.findByLoginAndRefresh(accountLoginNo3);
        Optional<Account> account2Find = accountMOKFacade.findByLoginAndRefresh(accountLoginNo4);

        Field blockedTimeField = Account.class.getDeclaredField("blockedTime");
        Field blockedField = Account.class.getDeclaredField("blocked");
        blockedTimeField.setAccessible(true);
        blockedField.setAccessible(true);
        blockedTimeField.set(account1Find.get(), null);
        blockedField.set(account1Find.get(), true);
        blockedTimeField.set(account2Find.get(), null);
        blockedField.set(account2Find.get(), true);

        Assertions.assertNull(account1Find.get().getBlockedTime());
        Assertions.assertNull(account2Find.get().getBlockedTime());

        accountMOKFacade.edit(account1Find.get());
        accountMOKFacade.edit(account2Find.get());

        List<Account> accounts0 = accountMOKFacade.findAllBlockedAccountsThatWereBlockedByAdminWithPagination(0,2);
        List<Account> accounts1 = accountMOKFacade.findAllBlockedAccountsThatWereBlockedByAdminWithPagination(0,1);
        List<Account> accounts2 = accountMOKFacade.findAllBlockedAccountsThatWereBlockedByAdminWithPagination(1,1);
        List<Account> accounts3 = accountMOKFacade.findAllBlockedAccountsThatWereBlockedByAdminWithPagination(0,10);

        Assertions.assertEquals(2, accounts0.size());
        Assertions.assertEquals(1, accounts1.size());
        Assertions.assertEquals(1, accounts2.size());
        Assertions.assertEquals(2, accounts3.size());

        accountMOKFacade.remove(account1Find.get());
        accountMOKFacade.remove(account2Find.get());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllActiveAccountsWithUnverifiedEmailWithPaginationTestPositive() {
        List<Account> accounts0 = accountMOKFacade.findAllActiveAccountsWithUnverifiedEmailWithPagination(0, 10);
        List<Account> accounts1 = accountMOKFacade.findAllActiveAccountsWithUnverifiedEmailWithPagination(0, 2);
        List<Account> accounts2 = accountMOKFacade.findAllActiveAccountsWithUnverifiedEmailWithPagination(0, 1);
        List<Account> accounts3 = accountMOKFacade.findAllActiveAccountsWithUnverifiedEmailWithPagination(1, 1);

        Assertions.assertEquals(2, accounts0.size());
        Assertions.assertEquals(2, accounts1.size());
        Assertions.assertEquals(1, accounts2.size());
        Assertions.assertEquals(1, accounts3.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllInactiveAccountsWithUnverifiedEmailWithPagination() {
        Account account0 = accountMOKFacade.findByLogin("tonyhalik").get();
        Account account1 = accountMOKFacade.findByLogin("adamn").get();

        account0.setActive(false);
        account1.setActive(false);

        accountMOKFacade.edit(account0);
        accountMOKFacade.edit(account1);

        List<Account> accounts0 = accountMOKFacade.findAllInactiveAccountsWithUnverifiedEmailWithPagination(0, 10);
        List<Account> accounts1 = accountMOKFacade.findAllInactiveAccountsWithUnverifiedEmailWithPagination(0, 2);
        List<Account> accounts2 = accountMOKFacade.findAllInactiveAccountsWithUnverifiedEmailWithPagination(0, 1);
        List<Account> accounts3 = accountMOKFacade.findAllInactiveAccountsWithUnverifiedEmailWithPagination(1, 1);

        Assertions.assertEquals(2, accounts0.size());
        Assertions.assertEquals(2, accounts1.size());
        Assertions.assertEquals(1, accounts2.size());
        Assertions.assertEquals(1, accounts3.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllAccountsByActiveAndLoginAndUserFirstNameAndUserLastNameWithPagination() {
        Account account = new Account(accountLoginNo3, accountPasswordNo1, accountFirstNameNo3, accountLastNameNo3,
                accountEmailNo3, accountPhoneNumberNo3);
        UserLevel userLevelClientNo1 = new Client();
        userLevelClientNo1.setAccount(account);
        account.addUserLevel(userLevelClientNo1);

        account.setAccountLanguage(accountLanguageNo1);

        Account account2 = new Account(accountLoginNo4, accountPasswordNo1, accountFirstNameNo4, accountLastNameNo4,
                accountEmailNo4, accountPhoneNumberNo4);
        userLevelClientNo1.setAccount(account2);
        account2.addUserLevel(userLevelClientNo1);
        account2.setAccountLanguage(accountLanguageNo1);
        account2.setActive(true);
        account.setActive(true);

        accountMOKFacade.create(account);
        accountMOKFacade.create(account2);

        List<Account> accounts0 = accountMOKFacade.findAllAccountsByActiveAndLoginAndUserFirstNameAndUserLastNameWithPagination("ginIT", "irstNa", "eITN", true,true, 0, 10);
        List<Account> accounts1 = accountMOKFacade.findAllAccountsByActiveAndLoginAndUserFirstNameAndUserLastNameWithPagination("ginIT", "irstNa", "eITN", true, false, 0, 10);
        List<Account> accounts2 = accountMOKFacade.findAllAccountsByActiveAndLoginAndUserFirstNameAndUserLastNameWithPagination("ginIT", "irstNa", "eITN", true, false, 0, 2);
        List<Account> accounts3 = accountMOKFacade.findAllAccountsByActiveAndLoginAndUserFirstNameAndUserLastNameWithPagination("ginIT", "irstNa", "eITN", true, false, 1, 1);

        Assertions.assertEquals(2, accounts0.size());
        Assertions.assertEquals(2, accounts1.size());
        Assertions.assertEquals(2, accounts2.size());
        Assertions.assertEquals(1, accounts3.size());

        accountMOKFacade.remove(account);
        accountMOKFacade.remove(account2);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllAccountsWithoutRecentActivityWithPagination() {
        Account account = new Account(accountLoginNo3, accountPasswordNo1, accountFirstNameNo3, accountLastNameNo3,
                accountEmailNo3, accountPhoneNumberNo3);
        UserLevel userLevelClientNo1 = new Client();
        userLevelClientNo1.setAccount(account);
        account.addUserLevel(userLevelClientNo1);

        account.setAccountLanguage(accountLanguageNo1);

        Account account2 = new Account(accountLoginNo4, accountPasswordNo1, accountFirstNameNo4, accountLastNameNo4,
                accountEmailNo4, accountPhoneNumberNo4);
        userLevelClientNo1.setAccount(account2);
        account2.addUserLevel(userLevelClientNo1);
        account2.setAccountLanguage(accountLanguageNo1);
        account2.setActive(true);
        account.setActive(true);

        accountMOKFacade.create(account);
        accountMOKFacade.create(account2);

        Account accountFind1 = accountMOKFacade.findByLoginAndRefresh(accountLoginNo3).get();
        Account accountFind2 = accountMOKFacade.findByLoginAndRefresh(accountLoginNo4).get();

        accountFind1.getActivityLog().setLastSuccessfulLoginTime(LocalDateTime.of(2017, 5, 8, 15, 30));
        accountFind2.getActivityLog().setLastSuccessfulLoginTime(LocalDateTime.of(2018, 5, 8, 15, 30));

        accountMOKFacade.edit(accountFind1);
        accountMOKFacade.edit(accountFind2);

        accountFind1 = accountMOKFacade.findByLoginAndRefresh(accountLoginNo3).get();
        accountFind2 = accountMOKFacade.findByLoginAndRefresh(accountLoginNo4).get();

        List<Account> accounts0 = accountMOKFacade.findAllAccountsWithoutRecentActivityWithPagination(LocalDateTime.of(2017, 6, 8, 15, 30), true, 0, 10);
        List<Account> accounts1 = accountMOKFacade.findAllAccountsWithoutRecentActivityWithPagination(LocalDateTime.of(2018, 6, 8, 15, 30), true, 0, 10);
        Assertions.assertEquals(1, accounts0.size());
        Assertions.assertEquals(2, accounts1.size());

        accountMOKFacade.remove(accountFind1);
        accountMOKFacade.remove(accountFind2);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void countAllAccountsWithoutRecentActivityWithPagination() {
        Account account = new Account(accountLoginNo3, accountPasswordNo1, accountFirstNameNo3, accountLastNameNo3,
                accountEmailNo3, accountPhoneNumberNo3);
        UserLevel userLevelClientNo1 = new Client();
        userLevelClientNo1.setAccount(account);
        account.addUserLevel(userLevelClientNo1);

        account.setAccountLanguage(accountLanguageNo1);

        Account account2 = new Account(accountLoginNo4, accountPasswordNo1, accountFirstNameNo4, accountLastNameNo4,
                accountEmailNo4, accountPhoneNumberNo4);
        userLevelClientNo1.setAccount(account2);
        account2.addUserLevel(userLevelClientNo1);
        account2.setAccountLanguage(accountLanguageNo1);
        account2.setActive(true);
        account.setActive(true);

        accountMOKFacade.create(account);
        accountMOKFacade.create(account2);

        Account accountFind1 = accountMOKFacade.findByLoginAndRefresh(accountLoginNo3).get();
        Account accountFind2 = accountMOKFacade.findByLoginAndRefresh(accountLoginNo4).get();

        accountFind1.getActivityLog().setLastSuccessfulLoginTime(LocalDateTime.of(2017, 5, 8, 15, 30));
        accountFind2.getActivityLog().setLastSuccessfulLoginTime(LocalDateTime.of(2018, 5, 8, 15, 30));

        accountMOKFacade.edit(accountFind1);
        accountMOKFacade.edit(accountFind2);

        accountFind1 = accountMOKFacade.findByLoginAndRefresh(accountLoginNo3).get();
        accountFind2 = accountMOKFacade.findByLoginAndRefresh(accountLoginNo4).get();

        Long count1 = accountMOKFacade.countAllAccountsWithoutRecentActivityWithPagination(LocalDateTime.of(2017, 6, 8, 15, 30), true, 0, 10).get();
        Long count2 = accountMOKFacade.countAllAccountsWithoutRecentActivityWithPagination(LocalDateTime.of(2018, 6, 8, 15, 30), true, 0, 10).get();
        Assertions.assertEquals(1, count1);
        Assertions.assertEquals(2, count2);

        accountMOKFacade.remove(accountFind1);
        accountMOKFacade.remove(accountFind2);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllBlockedAccountsThatWereBlockedByLoginIncorrectlyCertainAmountOfTimes() throws NoSuchFieldException, IllegalAccessException {
        Account account = new Account(accountLoginNo3, accountPasswordNo1, accountFirstNameNo3, accountLastNameNo3,
                accountEmailNo3, accountPhoneNumberNo3);
        UserLevel userLevelClientNo1 = new Client();
        userLevelClientNo1.setAccount(account);
        account.addUserLevel(userLevelClientNo1);

        account.setAccountLanguage(accountLanguageNo1);

        Account account2 = new Account(accountLoginNo4, accountPasswordNo1, accountFirstNameNo4, accountLastNameNo4,
                accountEmailNo4, accountPhoneNumberNo4);
        userLevelClientNo1.setAccount(account2);
        account2.addUserLevel(userLevelClientNo1);
        account2.setAccountLanguage(accountLanguageNo1);

        accountMOKFacade.create(account);
        accountMOKFacade.create(account2);

        Account accountFind1 = accountMOKFacade.findByLoginAndRefresh(accountLoginNo3).get();
        Account accountFind2 = accountMOKFacade.findByLoginAndRefresh(accountLoginNo4).get();

        Field blockedTimeField = Account.class.getDeclaredField("blockedTime");
        Field blockedField = Account.class.getDeclaredField("blocked");
        blockedTimeField.setAccessible(true);
        blockedField.setAccessible(true);
        blockedTimeField.set(accountFind1, LocalDateTime.of(2017, 5, 8, 15, 30));
        blockedField.set(accountFind1, true);
        blockedTimeField.set(accountFind2, LocalDateTime.of(2018, 5, 8, 15, 30));
        blockedField.set(accountFind2, true);


        //accountFind1.getActivityLog().setLastSuccessfulLoginTime(LocalDateTime.of(2017, 5, 8, 15, 30));
        //accountFind2.getActivityLog().setLastSuccessfulLoginTime(LocalDateTime.of(2018, 5, 8, 15, 30));

        accountMOKFacade.edit(accountFind1);
        accountMOKFacade.edit(accountFind2);

        accountFind1 = accountMOKFacade.findByLogin(accountLoginNo3).get();
        //Assertions.assertNull(accountFind1);

        List<Account> accounts0 = accountMOKFacade.findAllBlockedAccountsThatWereBlockedByLoginIncorrectlyCertainAmountOfTimes(2, TimeUnit.HOURS);
        Assertions.assertEquals(2, accounts0.size());

        accountMOKFacade.remove(accountFind1);
        accountMOKFacade.remove(accountFind2);
    }



}
