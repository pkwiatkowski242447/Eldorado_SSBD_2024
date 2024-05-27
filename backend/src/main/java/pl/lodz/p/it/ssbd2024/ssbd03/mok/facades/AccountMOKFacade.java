package pl.lodz.p.it.ssbd2024.ssbd03.mok.facades;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Repository used to manage Accounts Entities in the database on behalf of MOK module.
 *
 * @see Account
 */
@Slf4j
@Repository
@TxTracked
@Transactional(propagation = Propagation.MANDATORY)
public class AccountMOKFacade extends AbstractFacade<Account> {

    /**
     * Autowired entityManager used for managing entities.
     */
    @PersistenceContext(unitName = DatabaseConfigConstants.MOK_PU)
    private EntityManager entityManager;

    /**
     * Constructs the facade.
     */
    public AccountMOKFacade() {
        super(Account.class);
    }

    /**
     * Retrieves an entity manager.
     *
     * @return Entity manager associated with the facade.
     */
    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    // C - create methods

    /**
     * Persists a new Account in the database.
     *
     * @param account Entity to be persisted.
     */
    @Override
    @PermitAll
    public void create(Account account) throws ApplicationBaseException {
        super.create(account);
    }

    // R - read methods

    /**
     * Retrieves an Account by the ID.
     *
     * @param id ID of the Account to be retrieved.
     * @return If Account with the given ID was found returns an Optional containing the Account, otherwise returns an empty Optional.
     */
    @Override
    @PermitAll
    public Optional<Account> find(UUID id) throws ApplicationBaseException {
        Optional<Account> optionalAccount = super.find(id);
        optionalAccount.ifPresent(entity -> entityManager.refresh(entity));
        return optionalAccount;
    }

    /**
     * Retrieves an Account by the ID and forces its refresh.
     *
     * @param id ID of the Account to be retrieved.
     * @return If Account with the given ID was found returns an Optional containing the Account, otherwise returns an empty Optional.
     */
    @Override
    @PermitAll
    public Optional<Account> findAndRefresh(UUID id) throws ApplicationBaseException {
        return super.findAndRefresh(id);
    }

    /**
     * Retrieves all Accounts.
     *
     * @return `List` containing all Accounts.
     */
    @Override
    @RolesAllowed({Roles.ADMIN})
    public List<Account> findAll() throws ApplicationBaseException {
        return super.findAll();
    }

    /**
     * This method is used to retrieve all user accounts, including pagination.
     *
     * @param pageNumber Number of the page with user accounts to be retrieved.
     * @param pageSize   Number of user accounts per page.
     * @return List of all user accounts from a specified page, of a given page size.
     * If a persistence exception is thrown, then empty list is returned.
     * @note. Accounts are be default ordered (in the returned list) by the login.
     */
    @RolesAllowed(Authorities.MOK17)
    public List<Account> findAllAccountsWithPagination(int pageNumber, int pageSize)
            throws ApplicationBaseException {
        try {
            TypedQuery<Account> findAllAccounts = entityManager.createNamedQuery("Account.findAllAccounts", Account.class);
            findAllAccounts.setFirstResult(pageNumber * pageSize);
            findAllAccounts.setMaxResults(pageSize);
            List<Account> list = findAllAccounts.getResultList();
            super.refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * This method is used to retrieve all user accounts, which are active, including pagination.
     *
     * @param pageNumber Number of the page with active user accounts to be retrieved.
     * @param pageSize   Number of active user accounts per page.
     * @return List of all active user accounts from a specified page, of a given page size.
     * If a persistence exception is thrown, then empty list is returned.
     * @note. Accounts are be default ordered (in the returned list) by the login.
     */
    @RolesAllowed({Roles.ADMIN})
    public List<Account> findAllActiveAccountsWithPagination(int pageNumber, int pageSize)
            throws ApplicationBaseException {
        try {
            TypedQuery<Account> findAllAccounts = entityManager.createNamedQuery("Account.findAllAccountsByActive", Account.class);
            findAllAccounts.setFirstResult(pageNumber * pageSize);
            findAllAccounts.setMaxResults(pageSize);
            findAllAccounts.setParameter("active", true);
            List<Account> list = findAllAccounts.getResultList();
            super.refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * This method is used to retrieve all user accounts, which are inactive, including pagination.
     *
     * @param pageNumber Number of the page with inactive user accounts to be retrieved.
     * @param pageSize   Number of inactive user accounts per page.
     * @return List of all inactive user accounts from a specified page, of a given page size.
     * If a persistence exception is thrown, then empty list is returned.
     * @note. counts are be default ordered (in the returned list) by the login.
     */
    @RolesAllowed({Roles.ADMIN})
    public List<Account> findAllInactiveAccountsWithPagination(int pageNumber, int pageSize)
            throws ApplicationBaseException {
        try {
            TypedQuery<Account> findAllAccounts = entityManager.createNamedQuery("Account.findAllAccountsByActive", Account.class);
            findAllAccounts.setFirstResult(pageNumber * pageSize);
            findAllAccounts.setMaxResults(pageSize);
            findAllAccounts.setParameter("active", false);
            List<Account> list = findAllAccounts.getResultList();
            super.refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * This method is used to find all user accounts with specified user level access.
     *
     * @param userLevel  Class reference to the user level, which is searched among users accounts.
     * @param pageNumber Number of the page with user accounts to be retrieved.
     * @param pageSize   Number of user accounts per page.
     * @return List containing user accounts with specified user level. If no accounts with given user level are found or
     * persistence exception is thrown, then empty list is returned.
     */
    @RolesAllowed({Roles.ADMIN})
    public List<Account> findAllActiveAccountsWithGivenUserLevelWithPagination(
            Class<? extends UserLevel> userLevel, int pageNumber, int pageSize) throws ApplicationBaseException {
        try {
            TypedQuery<Account> findAllActiveAccountsByUserLevelQuery = entityManager.createNamedQuery("Account.findAccountsByUserLevelAndActive", Account.class);
            findAllActiveAccountsByUserLevelQuery.setFirstResult(pageNumber * pageSize);
            findAllActiveAccountsByUserLevelQuery.setMaxResults(pageSize);
            findAllActiveAccountsByUserLevelQuery.setParameter("userLevel", userLevel);
            List<Account> list = findAllActiveAccountsByUserLevelQuery.getResultList();
            super.refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * This method is used to find user account by username. As username needs to be unique, it returns a single result.
     *
     * @param login Login of the searched user account.
     * @return If there is user account with given username in the system, this method returns their account in a form of Optional.
     * Otherwise, empty optional is returned.
     */
    @RolesAllowed({Roles.AUTHENTICATED})
    public Optional<Account> findByLogin(String login) throws ApplicationBaseException {
        try {
            TypedQuery<Account> findAccountByLogin = entityManager.createNamedQuery("Account.findByLogin", Account.class);
            findAccountByLogin.setParameter("login", login);
            Account foundAccount = findAccountByLogin.getSingleResult();
            entityManager.refresh(foundAccount);
            return Optional.of(foundAccount);
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    /**
     * This method is used to find user account by e-mail address. As e-mail address needs to be unique, it returns a single result.
     *
     * @param email E-mail address of the searched user account.
     * @return If there is user account with given e-mail address in the system, this method returns their account in a form of Optional.
     * Otherwise, empty optional is returned.
     */
    @PermitAll
    public Optional<Account> findByEmail(String email) throws ApplicationBaseException {
        try {
            TypedQuery<Account> findAccountByEmail = entityManager.createNamedQuery("Account.findAccountByEmail", Account.class);
            findAccountByEmail.setParameter("email", email);
            Account account = findAccountByEmail.getSingleResult();
            entityManager.refresh(account);
            return Optional.of(account);
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    /**
     * This method is used to retrieve all user accounts, where login matches (contains) given phrase.
     * It does not take size of the phrase characters into consideration.
     *
     * @param login      Phrase searched insider login of an account.
     * @param active     Boolean value indication whether account is active or not.
     * @param pageNumber Number of the page with user accounts to be retrieved.
     * @param pageSize   Number of user accounts per page.
     * @return List of accounts, which login matches given phrase. If there are no accounts, which login matches given
     * phrase or persistence exception is thrown, then empty list is returned.
     */
    @RolesAllowed({Roles.ADMIN})
    public List<Account> findAllAccountsMatchingLoginWithPagination(
            String login, boolean active, int pageNumber, int pageSize) throws ApplicationBaseException {
        try {
            TypedQuery<Account> findAllAccountsMatchingLogin = entityManager.createNamedQuery("Account.findAllAccountsMatchingGivenLogin", Account.class);
            findAllAccountsMatchingLogin.setFirstResult(pageNumber * pageSize);
            findAllAccountsMatchingLogin.setMaxResults(pageSize);
            findAllAccountsMatchingLogin.setParameter("login", login);
            findAllAccountsMatchingLogin.setParameter("active", active);
            List<Account> list = findAllAccountsMatchingLogin.getResultList();
            super.refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * This method is used to find all accounts, that were not activated within specified time window
     * since creating them.
     *
     * @param amount   Length of the specified time window, used to activate newly registered account.
     * @param timeUnit Time unit, indicating size of the account activation time window.
     * @return List of accounts that were not activated in time (and therefore could not be activated). In case of
     * persistence exception, empty list is returned.
     */
    @PermitAll
    public List<Account> findAllAccountsMarkedForDeletion(long amount, TimeUnit timeUnit) throws ApplicationBaseException {
        try {
            TypedQuery<Account> findAllAccountsMarkedForDeletion = entityManager.createNamedQuery("Account.findAllAccountsMarkedForDeletion", Account.class);
            findAllAccountsMarkedForDeletion.setParameter("timestamp", LocalDateTime.now().minus(amount, timeUnit.toChronoUnit()));
            List<Account> list = findAllAccountsMarkedForDeletion.getResultList();
            super.refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * This method is used to find a list of all users accounts by their blocked status.
     *
     * @param blocked    Blocked status of an account. True means that account is in fact blocked, and false means that account is not blocked.
     * @param pageNumber Number of the page with user accounts to be retrieved.
     * @param pageSize   Number of user accounts per page.
     * @return List of all accounts with the specified value of blocked status. In case of persistence exception
     * empty list will be returned.
     */
    @RolesAllowed({Roles.ADMIN})
    public List<Account> findAllAccountsByBlocked(boolean blocked, int pageNumber, int pageSize) throws ApplicationBaseException {
        try {
            TypedQuery<Account> findAllBlockedAccounts = entityManager.createNamedQuery("Account.findAllAccountsByBlockedInAscOrder", Account.class);
            findAllBlockedAccounts.setFirstResult(pageNumber * pageSize);
            findAllBlockedAccounts.setMaxResults(pageSize);
            findAllBlockedAccounts.setParameter("blocked", blocked);
            List<Account> list = findAllBlockedAccounts.getResultList();
            super.refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * This method is used to find all users accounts that were blocked by the admin (so basically status of the account was
     * changed to blocked and blocked time is not set).
     *
     * @param pageNumber Number of the page with user accounts to be retrieved.
     * @param pageSize   Number of user accounts per page.
     * @return List of all users accounts that were blocked by the admin. If persistence exception is thrown, then
     * empty list will be returned.
     */
    @RolesAllowed({Roles.ADMIN})
    public List<Account> findAllBlockedAccountsThatWereBlockedByAdminWithPagination(int pageNumber, int pageSize)
            throws ApplicationBaseException {
        try {
            TypedQuery<Account> findAllAccountsBlockedByAdminQuery = entityManager.createNamedQuery("Account.findAllBlockedAccountsThatWereBlockedByAdmin", Account.class);
            findAllAccountsBlockedByAdminQuery.setFirstResult(pageNumber * pageSize);
            findAllAccountsBlockedByAdminQuery.setMaxResults(pageSize);
            List<Account> list = findAllAccountsBlockedByAdminQuery.getResultList();
            super.refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * This method is used to find all users accounts that were blocked by logging incorrectly certain amount of times
     * (so basically status of the account was changed to blocked and blocked time is set).
     *
     * @param amount   Length of the specified time window, used to unblock accounts, blocked by logging incorrectly
     *                 certain amount of times.
     * @param timeUnit Time unit, indicating size of the account blockade time window.
     * @return List of all users accounts that were blocked by the logging incorrectly certain amount of time.
     * If persistence exception is thrown, then empty list will be returned.
     */
    @PermitAll
    public List<Account> findAllBlockedAccountsThatWereBlockedByLoginIncorrectlyCertainAmountOfTimes(
            long amount, TimeUnit timeUnit) throws ApplicationBaseException {
        try {
            TypedQuery<Account> findAllAccountsBlockedByLoginIncorrectlyCertainAmountOfTimesQuery = entityManager
                    .createNamedQuery("Account.findAllBlockedAccountsThatWereBlockedByLoginIncorrectlyCertainAmountOfTimes", Account.class);
            findAllAccountsBlockedByLoginIncorrectlyCertainAmountOfTimesQuery
                    .setParameter("timestamp", LocalDateTime.now().minus(amount, timeUnit.toChronoUnit()));
            List<Account> list = findAllAccountsBlockedByLoginIncorrectlyCertainAmountOfTimesQuery.getResultList();
            super.refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieve accounts that match the given parameters.
     *
     * @param login      Account's login. The phrase will be sought in logins.
     * @param firstName  Account's owner first name. The phrase will be sought in first names.
     * @param lastName   Account's owner last name. The phrase will be sought in last names.
     * @param order      Login sorting order. True for ascending order, false for descending.
     * @param pageSize   Number of results per page.
     * @param pageNumber Number of the page to retrieve.
     * @return List of accounts that match the parameters.
     */
    @RolesAllowed({Roles.ADMIN})
    public List<Account> findAllAccountsByActiveAndLoginAndUserFirstNameAndUserLastNameWithPagination(String login,
                                                                                                      String firstName,
                                                                                                      String lastName,
                                                                                                      boolean active,
                                                                                                      boolean order,
                                                                                                      int pageNumber,
                                                                                                      int pageSize) throws ApplicationBaseException {
        try {
            TypedQuery<Account> findAllAccountsMatchingCriteriaQuery;
            if (order) {
                findAllAccountsMatchingCriteriaQuery = entityManager.createNamedQuery("Account.findAccountsByActiveAndMatchingUserFirstNameOrUserLastNameAndLoginInAscendingOrder", Account.class);
            } else {
                findAllAccountsMatchingCriteriaQuery = entityManager.createNamedQuery("Account.findAccountsByActiveAndMatchingUserFirstNameOrUserLastNameAndLoginInDescendingOrder", Account.class);
            }
            findAllAccountsMatchingCriteriaQuery.setFirstResult(pageNumber * pageSize);
            findAllAccountsMatchingCriteriaQuery.setMaxResults(pageSize);
            findAllAccountsMatchingCriteriaQuery.setParameter("login", login);
            findAllAccountsMatchingCriteriaQuery.setParameter("firstName", firstName);
            findAllAccountsMatchingCriteriaQuery.setParameter("lastName", lastName);
            findAllAccountsMatchingCriteriaQuery.setParameter("active", active);
            List<Account> list = findAllAccountsMatchingCriteriaQuery.getResultList();
            super.refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * This method is used to find all user accounts without any recent activity, which is understood as logging
     * into the application.
     *
     * @param lastSuccessfulLogin Date and time, which account activity is checked from. If there are no successful
     *                            login attempts from that date and time, then account is considered without recent activity.
     * @return List of all user accounts without recent activity. In case of persistence exception, empty list is returned.
     */
    //TODO RolesAllowed
    public List<Account> findAllAccountsWithoutRecentActivity(
            LocalDateTime lastSuccessfulLogin) throws ApplicationBaseException {
        try {
            TypedQuery<Account> findAllAccountsWithoutRecentActivityQuery = entityManager.createNamedQuery("Account.findAccountsWithoutAnyActivityFrom", Account.class);
            findAllAccountsWithoutRecentActivityQuery.setParameter("timestamp", lastSuccessfulLogin);
            List<Account> list = findAllAccountsWithoutRecentActivityQuery.getResultList();
            super.refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * This method is used to count all users accounts without any recent activity.
     *
     * @param lastSuccessfulLogin Date and time, which account activity is checked from. If there are no successful
     *                            login attempts from that date and time, then account is considered without recent activity.
     * @return Optional containing a number of inactive users accounts in the system. In case of persistence exception
     * empty optional is returned.
     */
    @RolesAllowed({Roles.ADMIN})
    public Optional<Long> countAllAccountsWithoutRecentActivityWithPagination(
            LocalDateTime lastSuccessfulLogin, boolean active) throws ApplicationBaseException {
        try {
            TypedQuery<Long> countAllAccountsWithoutRecentActivityQuery = entityManager.createNamedQuery("Account.countAccountsWithoutAnyActivityFrom", Long.class);
            countAllAccountsWithoutRecentActivityQuery.setParameter("lastSuccessfulLoginTime", lastSuccessfulLogin);
            countAllAccountsWithoutRecentActivityQuery.setParameter("active", active);
            return Optional.of(countAllAccountsWithoutRecentActivityQuery.getSingleResult());
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    // U - update methods

    /**
     * Forces the modification of the entity in the database.
     *
     * @param account Account to be modified.
     */
    @Override
    @PermitAll
    public void edit(Account account) throws ApplicationBaseException {
        super.edit(account);
    }

    // D - delete methods

    /**
     * Removes an Account from the database.
     *
     * @param account Account to be removed from the database.
     */
    @Override
    @PermitAll
    public void remove(Account account) throws ApplicationBaseException {
        super.remove(account);
    }
}
