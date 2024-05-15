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
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import java.util.concurrent.TimeUnit;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
@ExtendWith(SpringExtension.class)
public class AccountMOKFacadeIntegrationTest extends TestcontainersConfig {

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
    private AccountMOKFacade accountMOKFacade;

    private final UUID accountIdNo1 = UUID.fromString("b3b8c2ac-21ff-434b-b490-aa6d717447c0");

    //login
    private final String accountLoginNo1 = "loginITNo1";
    private final String accountLoginNo2 = "jerzybem";
    private final String accountLoginNo3 = "loginITNo2";
    private final String accountLoginNo4 = "loginITNo3";

    //password

    private final String accountPasswordNo1 = "$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa";

    //name
    private final String accountFirstNameNo1 = "firstNameITNo1";
    private final String accountFirstNameNo3 = "firstNameITNo2";
    private final String accountFirstNameNo4 = "firstNameITNo3";
    private final String accountFirstNameNo2 = "Jerzy";

    //last name
    private final String accountLastNameNo1 = "lastNameITNo1";
    private final String accountLastNameNo3 = "lastNameITNo2";
    private final String accountLastNameNo4 = "lastNameITNo3";
    private final String accountLastNameNo2 = "Bem";

    //email
    private final String accountEmailNo1 = "emailtest@it.com";
    private final String accountEmailNo3 = "emailtest2@it.com";
    private final String accountEmailNo4 = "emailtest3@it.com";
    private final String accountEmailNo2 = "jbem@example.com";

    //phone number
    private final String accountPhoneNumberNo1 = "123123123";
    private final String accountPhoneNumberNo2 = "111111111";
    private final String accountPhoneNumberNo3 = "111111112";
    private final String accountPhoneNumberNo4 = "111111115";

    //language
    private final String accountLanguageNo1 = "PL";

    //user levels
    private final UserLevel accountUserLevelClientNo1 = new Client();
    private final UserLevel accountUserLevelStaffNo1 = new Staff();
    private final UserLevel accountUserLevelAdminNo1 = new Admin();

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findMethodReturnExistingAccountTestPositive() throws ApplicationBaseException {
        Optional<Account> account = accountMOKFacade.find(accountIdNo1);
        //Check if method find existing user
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
    public void findMethodReturnNullAccountTestPositive() throws ApplicationBaseException {
        UUID uuid = UUID.fromString("b3b8c2ac-22ff-434b-b490-aa6d717447c0");
        Optional<Account> account = accountMOKFacade.find(uuid);

        assertTrue(account.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = Roles.AUTHENTICATED)
    public void findByLoginReturnExistingAccountPositiveTest() {
        Optional<Account> account = accountMOKFacade.findByLogin(accountLoginNo2);

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
    @WithMockUser(roles = Roles.AUTHENTICATED)
    public void findByLoginReturnNullAccountTestPositive() {
        Optional<Account> account = accountMOKFacade.findByLogin(accountLoginNo1);

        assertTrue(account.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAndRefreshReturnExistingAccountPositiveTest() {
        Optional<Account> account = accountMOKFacade.findAndRefresh(accountIdNo1);

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
    public void findAndRefreshReturnNullAccountTestPositive() throws ApplicationBaseException {
        UUID uuid = UUID.fromString("b3b8c2ac-22ff-434b-b490-aa6d717447c0");
        Optional<Account> account = accountMOKFacade.find(uuid);

        assertTrue(account.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = Roles.ADMIN)
    public void findAllTestPositive() {
        List<Account> accounts = accountMOKFacade.findAll();

        assertEquals(7, accounts.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = Roles.ADMIN)
    public void findAllWithPaginationTestPositive() {
        List<Account> accountsNo1 = accountMOKFacade.findAllAccountsWithPagination(0, 4);
        List<Account> accountsNo2 = accountMOKFacade.findAllAccountsWithPagination(1, 3);
        List<Account> accountsNo3 = accountMOKFacade.findAllAccountsWithPagination(1, 4);

        assertEquals(4, accountsNo1.size());
        assertEquals(3, accountsNo2.size());
        assertEquals(3, accountsNo3.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = Roles.ADMIN)
    public void findAllActiveAccountsWithPaginationTestPositive() {
        List<Account> accountsNo1 = accountMOKFacade.findAllActiveAccountsWithPagination(0, 4);
        List<Account> accountsNo2 = accountMOKFacade.findAllActiveAccountsWithPagination(1, 3);
        List<Account> accountsNo3 = accountMOKFacade.findAllActiveAccountsWithPagination(1, 4);

        assertEquals(4, accountsNo1.size());
        assertEquals(3, accountsNo2.size());
        assertEquals(3, accountsNo3.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findByEmailReturnExistingAccountPositiveTest() {
        Optional<Account> account = accountMOKFacade.findByEmail(accountEmailNo2);

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
    public void findByEmailReturnNullAccountTestPositive() {
        Optional<Account> account = accountMOKFacade.findByEmail(accountEmailNo1);

        assertTrue(account.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = Roles.AUTHENTICATED)
    public void findByLoginAndRefreshReturnExistingAccountTestPositive() {
        Optional<Account> account = accountMOKFacade.findByLogin(accountLoginNo2);

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
    @WithMockUser(roles = Roles.AUTHENTICATED)
    public void findByLoginAndRefreshReturnNullTestPositive() {
        Optional<Account> account = accountMOKFacade.findByLogin(accountLoginNo1);

        assertTrue(account.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = Roles.AUTHENTICATED)
    public void createAndRemoveMethodPositiveTest() throws ApplicationBaseException {
        //Create account with user level
        Account account = new Account(accountLoginNo1, accountPasswordNo1, accountFirstNameNo1, accountLastNameNo1,
                accountEmailNo1, accountPhoneNumberNo1);
        UserLevel userLevelClientNo1 = new Client();
        userLevelClientNo1.setAccount(account);
        account.addUserLevel(userLevelClientNo1);
        account.setAccountLanguage(accountLanguageNo1);
        //Facade
        accountMOKFacade.create(account);
        Optional<Account> newAccount = accountMOKFacade.findByLogin(accountLoginNo1);
        assertTrue(newAccount.isPresent());

        assertEquals(accountLoginNo1, newAccount.get().getLogin());
        assertEquals(accountPasswordNo1, newAccount.get().getPassword());
        assertEquals(accountFirstNameNo1, newAccount.get().getName());
        assertEquals(accountLastNameNo1, newAccount.get().getLastname());
        assertEquals(accountEmailNo1, newAccount.get().getEmail());
        assertEquals(accountPhoneNumberNo1, newAccount.get().getPhoneNumber());
        assertEquals(accountLanguageNo1, newAccount.get().getAccountLanguage());

        accountMOKFacade.remove(newAccount.get());

        Optional<Account> deletedAccount = accountMOKFacade.findByLogin(accountLoginNo1);
        assertTrue(deletedAccount.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Roles.AUTHENTICATED, Roles.ADMIN})
    public void findAllInactiveAccountsWithPaginationPositiveTest() throws ApplicationBaseException {
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
        assertEquals(2, accounts1.size());
        assertEquals(1, accounts2.size());
        assertEquals(1, accounts3.size());

        Optional<Account> accountToDelete1 = accountMOKFacade.findByLogin(accountLoginNo4);
        Optional<Account> accountToDelete2 = accountMOKFacade.findByLogin(accountLoginNo3);

        accountMOKFacade.remove(accountToDelete1.orElseThrow(NoSuchElementException::new));
        accountMOKFacade.remove(accountToDelete2.orElseThrow(NoSuchElementException::new));
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = Roles.ADMIN)
    public void findAllActiveAccountsWithGivenUserLevelWithPaginationPositiveTest() {
        List<Account> accounts0 = accountMOKFacade.findAll();
        assertEquals(7, accounts0.size());
        List<Account> accounts1 = accountMOKFacade.findAllActiveAccountsWithGivenUserLevelWithPagination(Client.class, 0, 20);
        List<Account> accounts2 = accountMOKFacade.findAllActiveAccountsWithGivenUserLevelWithPagination(Staff.class, 0, 2);
        List<Account> accounts3 = accountMOKFacade.findAllActiveAccountsWithGivenUserLevelWithPagination(Staff.class, 1, 1);
        List<Account> accounts4 = accountMOKFacade.findAllActiveAccountsWithGivenUserLevelWithPagination(Admin.class, 0, 3);
        assertEquals(4, accounts1.size());
        assertEquals(2, accounts2.size());
        assertEquals(1, accounts3.size());
        assertEquals(1, accounts4.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Roles.AUTHENTICATED, Roles.ADMIN})
    public void findAllAccountsMatchingLoginWithPagination() throws ApplicationBaseException {
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
        assertEquals(2, accounts0.size());
        assertEquals(1, accounts1.size());
        assertEquals(0, accounts2.size());

        Optional<Account> accountToDelete1 = accountMOKFacade.findByLogin(accountLoginNo4);
        Optional<Account> accountToDelete2 = accountMOKFacade.findByLogin(accountLoginNo3);

        accountMOKFacade.remove(accountToDelete1.orElseThrow(NoSuchElementException::new));
        accountMOKFacade.remove(accountToDelete2.orElseThrow(NoSuchElementException::new));
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = Roles.AUTHENTICATED)
    public void editPositiveTest() throws ApplicationBaseException {

        Account account = new Account(accountLoginNo3, accountPasswordNo1, accountFirstNameNo3, accountLastNameNo3,
                accountEmailNo3, accountPhoneNumberNo3);
        UserLevel userLevelClientNo1 = new Client();
        account.setActive(false);
        userLevelClientNo1.setAccount(account);
        account.addUserLevel(userLevelClientNo1);
        account.setAccountLanguage(accountLanguageNo1);

        accountMOKFacade.create(account);
        Optional<Account> accountFind = accountMOKFacade.findByLogin(accountLoginNo3);

        assertEquals(accountEmailNo3, accountFind.orElseThrow(NoSuchElementException::new).getEmail());
        accountFind.get().setEmail(accountEmailNo4);

        accountMOKFacade.edit(accountFind.get());
        Optional<Account> accountEdited = accountMOKFacade.findByLogin(accountLoginNo3);
        assertEquals(accountEmailNo4, accountFind.get().getEmail());

        accountMOKFacade.remove(accountEdited.orElseThrow(NoSuchElementException::new));

    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = Roles.AUTHENTICATED)
    public void findAllAccountsMarkedForDeletionTestPositive() throws ApplicationBaseException, NoSuchFieldException {
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
        Optional<Account> accountToEdit1 = accountMOKFacade.findByLogin(accountLoginNo4);
        Optional<Account> accountToEdit2 = accountMOKFacade.findByLogin(accountLoginNo3);
        assertFalse(accountToEdit1.isEmpty());
        assertFalse(accountToEdit2.isEmpty());

        //Two unverified accounts - now we use reflection to change their creation time
        Field creationDateField = Account.class.getDeclaredField("creationDate");
        creationDateField.setAccessible(true);
        //creationDateField.set(accountToEdit1.get(), LocalDateTime.now().minus(Long.parseLong("5"), TimeUnit.HOURS));
        //creationDateField.set(accountToEdit2.get(), LocalDateTime.now().minus(Long.parseLong("5"), TimeUnit.HOURS));

        List<Account> accounts0 = accountMOKFacade.findAllAccountsMarkedForDeletion(2, TimeUnit.HOURS);
        assertEquals(0,accounts0.size());

    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Roles.AUTHENTICATED, Roles.ADMIN})
    public void findAllAccountsByBlockedTestPositive() throws ApplicationBaseException, NoSuchFieldException, IllegalAccessException {
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

        Optional<Account> account1Find = accountMOKFacade.findByLogin(accountLoginNo3);
        Optional<Account> account2Find = accountMOKFacade.findByLogin(accountLoginNo4);

        Field blockedField = Account.class.getDeclaredField("blocked");
        blockedField.setAccessible(true);
        blockedField.set(account1Find.orElseThrow(NoSuchElementException::new), true);
        blockedField.set(account2Find.orElseThrow(NoSuchElementException::new), true);

        assertTrue(account1Find.get().getBlocked());
        assertTrue(account2Find.get().getBlocked());

        accountMOKFacade.edit(account1Find.get());
        accountMOKFacade.edit(account2Find.get());

        List<Account> accounts0 = accountMOKFacade.findAllAccountsByBlocked(true, 0, 2);
        List<Account> accounts1 = accountMOKFacade.findAllAccountsByBlocked(true, 0, 1);
        List<Account> accounts2 = accountMOKFacade.findAllAccountsByBlocked(true, 1, 1);

        List<Account> accounts3 = accountMOKFacade.findAllAccountsByBlocked(false, 0, 10);

        assertEquals(2, accounts0.size());
        assertEquals(1, accounts1.size());
        assertEquals(1, accounts2.size());

        assertEquals(7, accounts3.size());

        accountMOKFacade.remove(account1Find.get());
        accountMOKFacade.remove(account2Find.get());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Roles.AUTHENTICATED, Roles.ADMIN})
    public void findAllBlockedAccountsThatWereBlockedByAdminWithPagination() throws ApplicationBaseException, IllegalAccessException, NoSuchFieldException {
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

        Optional<Account> account1Find = accountMOKFacade.findByLogin(accountLoginNo3);
        Optional<Account> account2Find = accountMOKFacade.findByLogin(accountLoginNo4);

        Field blockedTimeField = Account.class.getDeclaredField("blockedTime");
        Field blockedField = Account.class.getDeclaredField("blocked");
        blockedTimeField.setAccessible(true);
        blockedField.setAccessible(true);
        blockedTimeField.set(account1Find.orElseThrow(NoSuchElementException::new), null);
        blockedField.set(account1Find.get(), true);
        blockedTimeField.set(account2Find.orElseThrow(NoSuchElementException::new), null);
        blockedField.set(account2Find.get(), true);

        assertNull(account1Find.get().getBlockedTime());
        assertNull(account2Find.get().getBlockedTime());

        accountMOKFacade.edit(account1Find.get());
        accountMOKFacade.edit(account2Find.get());

        List<Account> accounts0 = accountMOKFacade.findAllBlockedAccountsThatWereBlockedByAdminWithPagination(0,2);
        List<Account> accounts1 = accountMOKFacade.findAllBlockedAccountsThatWereBlockedByAdminWithPagination(0,1);
        List<Account> accounts2 = accountMOKFacade.findAllBlockedAccountsThatWereBlockedByAdminWithPagination(1,1);
        List<Account> accounts3 = accountMOKFacade.findAllBlockedAccountsThatWereBlockedByAdminWithPagination(0,10);

        assertEquals(2, accounts0.size());
        assertEquals(1, accounts1.size());
        assertEquals(1, accounts2.size());
        assertEquals(2, accounts3.size());

        accountMOKFacade.remove(account1Find.get());
        accountMOKFacade.remove(account2Find.get());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Roles.AUTHENTICATED, Roles.ADMIN})
    public void findAllActiveAccountsWithUnverifiedEmailWithPaginationTestPositive() {
        List<Account> accounts0 = accountMOKFacade.findAllActiveAccountsWithUnverifiedEmailWithPagination(0, 10);
        List<Account> accounts1 = accountMOKFacade.findAllActiveAccountsWithUnverifiedEmailWithPagination(0, 2);
        List<Account> accounts2 = accountMOKFacade.findAllActiveAccountsWithUnverifiedEmailWithPagination(0, 1);
        List<Account> accounts3 = accountMOKFacade.findAllActiveAccountsWithUnverifiedEmailWithPagination(1, 1);

        assertEquals(2, accounts0.size());
        assertEquals(2, accounts1.size());
        assertEquals(1, accounts2.size());
        assertEquals(1, accounts3.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Roles.AUTHENTICATED, Roles.ADMIN})
    public void findAllInactiveAccountsWithUnverifiedEmailWithPagination() throws ApplicationBaseException {
        Account account0 = accountMOKFacade.findByLogin("tonyhalik").orElseThrow(NoSuchElementException::new);
        Account account1 = accountMOKFacade.findByLogin("adamn").orElseThrow(NoSuchElementException::new);

        account0.setActive(false);
        account1.setActive(false);

        accountMOKFacade.edit(account0);
        accountMOKFacade.edit(account1);

        List<Account> accounts0 = accountMOKFacade.findAllInactiveAccountsWithUnverifiedEmailWithPagination(0, 10);
        List<Account> accounts1 = accountMOKFacade.findAllInactiveAccountsWithUnverifiedEmailWithPagination(0, 2);
        List<Account> accounts2 = accountMOKFacade.findAllInactiveAccountsWithUnverifiedEmailWithPagination(0, 1);
        List<Account> accounts3 = accountMOKFacade.findAllInactiveAccountsWithUnverifiedEmailWithPagination(1, 1);

        assertEquals(2, accounts0.size());
        assertEquals(2, accounts1.size());
        assertEquals(1, accounts2.size());
        assertEquals(1, accounts3.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Roles.AUTHENTICATED, Roles.ADMIN})
    public void findAllAccountsByActiveAndLoginAndUserFirstNameAndUserLastNameWithPagination() throws ApplicationBaseException {
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

        assertEquals(2, accounts0.size());
        assertEquals(2, accounts1.size());
        assertEquals(2, accounts2.size());
        assertEquals(1, accounts3.size());

        accountMOKFacade.remove(account);
        accountMOKFacade.remove(account2);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Roles.AUTHENTICATED, Roles.ADMIN})
    public void findAllAccountsWithoutRecentActivityWithPagination() throws ApplicationBaseException {
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

        Account accountFind1 = accountMOKFacade.findByLogin(accountLoginNo3).orElseThrow(NoSuchElementException::new);
        Account accountFind2 = accountMOKFacade.findByLogin(accountLoginNo4).orElseThrow(NoSuchElementException::new);

        accountFind1.getActivityLog().setLastSuccessfulLoginTime(LocalDateTime.of(2017, 5, 8, 15, 30));
        accountFind2.getActivityLog().setLastSuccessfulLoginTime(LocalDateTime.of(2018, 5, 8, 15, 30));

        accountMOKFacade.edit(accountFind1);
        accountMOKFacade.edit(accountFind2);

        accountFind1 = accountMOKFacade.findByLogin(accountLoginNo3).orElseThrow(NoSuchElementException::new);
        accountFind2 = accountMOKFacade.findByLogin(accountLoginNo4).orElseThrow(NoSuchElementException::new);

        List<Account> accounts0 = accountMOKFacade.findAllAccountsWithoutRecentActivityWithPagination(LocalDateTime.of(2017, 6, 8, 15, 30), true, 0, 10);
        List<Account> accounts1 = accountMOKFacade.findAllAccountsWithoutRecentActivityWithPagination(LocalDateTime.of(2018, 6, 8, 15, 30), true, 0, 10);
        assertEquals(1, accounts0.size());
        assertEquals(2, accounts1.size());

        accountMOKFacade.remove(accountFind1);
        accountMOKFacade.remove(accountFind2);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Roles.AUTHENTICATED, Roles.ADMIN})
    public void countAllAccountsWithoutRecentActivityWithPagination() throws ApplicationBaseException {
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

        Account accountFind1 = accountMOKFacade.findByLogin(accountLoginNo3).orElseThrow(NoSuchElementException::new);
        Account accountFind2 = accountMOKFacade.findByLogin(accountLoginNo4).orElseThrow(NoSuchElementException::new);

        accountFind1.getActivityLog().setLastSuccessfulLoginTime(LocalDateTime.of(2017, 5, 8, 15, 30));
        accountFind2.getActivityLog().setLastSuccessfulLoginTime(LocalDateTime.of(2018, 5, 8, 15, 30));

        accountMOKFacade.edit(accountFind1);
        accountMOKFacade.edit(accountFind2);

        accountFind1 = accountMOKFacade.findByLogin(accountLoginNo3).orElseThrow(NoSuchElementException::new);
        accountFind2 = accountMOKFacade.findByLogin(accountLoginNo4).orElseThrow(NoSuchElementException::new);

        Long count1 = accountMOKFacade.countAllAccountsWithoutRecentActivityWithPagination(LocalDateTime.of(2017, 6, 8, 15, 30), true, 0, 10).orElseThrow(NoSuchElementException::new);
        Long count2 = accountMOKFacade.countAllAccountsWithoutRecentActivityWithPagination(LocalDateTime.of(2018, 6, 8, 15, 30), true, 0, 10).orElseThrow(NoSuchElementException::new);
        assertEquals(1, count1);
        assertEquals(2, count2);

        accountMOKFacade.remove(accountFind1);
        accountMOKFacade.remove(accountFind2);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = Roles.AUTHENTICATED)
    public void findAllBlockedAccountsThatWereBlockedByLoginIncorrectlyCertainAmountOfTimes() throws ApplicationBaseException, NoSuchFieldException, IllegalAccessException {
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

        Account accountFind1 = accountMOKFacade.findByLogin(accountLoginNo3).orElseThrow(NoSuchElementException::new);
        Account accountFind2 = accountMOKFacade.findByLogin(accountLoginNo4).orElseThrow(NoSuchElementException::new);

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

        accountFind1 = accountMOKFacade.findByLogin(accountLoginNo3).orElseThrow(NoSuchElementException::new);
        //assertNull(accountFind1);

        List<Account> accounts0 = accountMOKFacade.findAllBlockedAccountsThatWereBlockedByLoginIncorrectlyCertainAmountOfTimes(2, TimeUnit.HOURS);
        assertEquals(2, accounts0.size());

        accountMOKFacade.remove(accountFind1);
        accountMOKFacade.remove(accountFind2);
    }

}
