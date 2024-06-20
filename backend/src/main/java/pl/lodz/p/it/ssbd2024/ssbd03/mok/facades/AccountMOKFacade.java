package pl.lodz.p.it.ssbd2024.ssbd03.mok.facades;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
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
@LoggerInterceptor
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
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    // C - create methods

    /**
     * Persists a new Account in the database.
     *
     * @param account Entity to be persisted.
     */
    @Override
    @RolesAllowed({Authorities.REGISTER_CLIENT, Authorities.REGISTER_USER})
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
    @RolesAllowed({
            Authorities.CONFIRM_ACCOUNT_CREATION, Authorities.CONFIRM_EMAIL_CHANGE, Authorities.CHANGE_OWN_MAIL,
            Authorities.ADD_USER_LEVEL, Authorities.REMOVE_USER_LEVEL, Authorities.RESTORE_ACCOUNT_ACCESS, Authorities.CHANGE_USER_MAIL
    })
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
    @RolesAllowed({
            Authorities.CHANGE_USER_PASSWORD, Authorities.BLOCK_ACCOUNT, Authorities.UNBLOCK_ACCOUNT,
            Authorities.GET_USER_ACCOUNT, Authorities.CHANGE_USER_PASSWORD, Authorities.BLOCK_ACCOUNT,
            Authorities.RESET_PASSWORD, Authorities.CHANGE_OWN_PASSWORD_AFTER_ADMINISTRATIVE_CHANGE
    })
    public Optional<Account> findAndRefresh(UUID id) throws ApplicationBaseException {
        return super.findAndRefresh(id);
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
    @RolesAllowed({Authorities.GET_ALL_USER_ACCOUNTS})
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
     * This method is used to find user account by username. As username needs to be unique, it returns a single result.
     *
     * @param login Login of the searched user account.
     * @return If there is user account with given username in the system, this method returns their account in a form of Optional.
     * Otherwise, empty optional is returned.
     */
    @RolesAllowed({
            Authorities.REGISTER_CLIENT, Authorities.REGISTER_USER, Authorities.CHANGE_USER_PASSWORD,
            Authorities.CHANGE_OWN_PASSWORD, Authorities.BLOCK_ACCOUNT, Authorities.UNBLOCK_ACCOUNT,
            Authorities.MODIFY_OWN_ACCOUNT, Authorities.MODIFY_USER_ACCOUNT, Authorities.CONFIRM_ACCOUNT_CREATION,
            Authorities.CONFIRM_EMAIL_CHANGE, Authorities.RESEND_EMAIL_CONFIRMATION_MAIL, Authorities.GET_OWN_HISTORICAL_DATA,
            Authorities.GET_OWN_ACCOUNT, Authorities.CHANGE_OWN_MAIL, Authorities.REMOVE_USER_LEVEL,
            Authorities.RESTORE_ACCOUNT_ACCESS, Authorities.GET_ADMIN_PASSWORD_RESET_STATUS, Authorities.RESET_PASSWORD,
            Authorities.CHANGE_OWN_PASSWORD_AFTER_ADMINISTRATIVE_CHANGE
    })
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
    @RolesAllowed({
            Authorities.RESET_PASSWORD, Authorities.CHANGE_OWN_MAIL,
            Authorities.RESTORE_ACCOUNT_ACCESS, Authorities.CHANGE_USER_PASSWORD, Authorities.CHANGE_USER_MAIL
    })
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
     * This method is used to find all accounts, that were not activated within specified time window
     * since creating them.
     *
     * @param amount   Length of the specified time window, used to activate newly registered account.
     * @param timeUnit Time unit, indicating size of the account activation time window.
     * @return List of accounts that were not activated in time (and therefore could not be activated). In case of
     * persistence exception, empty list is returned.
     */
    @RolesAllowed({Authorities.REMOVE_ACCOUNT})
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
     * This method is used to find all users accounts that were blocked by logging incorrectly certain amount of times
     * (so basically status of the account was changed to blocked and blocked time is set).
     *
     * @param amount   Length of the specified time window, used to unblock accounts, blocked by logging incorrectly
     *                 certain amount of times.
     * @param timeUnit Time unit, indicating size of the account blockade time window.
     * @return List of all users accounts that were blocked by the logging incorrectly certain amount of time.
     * If persistence exception is thrown, then empty list will be returned.
     */
    @RolesAllowed({Authorities.UNBLOCK_ACCOUNT})
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
     * @param phrase     Account's login. The phrase will be sought in first and last name.
     * @param orderBy    Order by which the list be ordered, either "login" or "level", if set to anything else defaults to "login".
     * @param order      Sorting order. True for ascending order, false for descending.
     * @param pageSize   Number of results per page.
     * @param pageNumber Number of the page to retrieve.
     * @return List of accounts that match the parameters.
     */
    @RolesAllowed({Authorities.GET_ALL_USER_ACCOUNTS})
    public List<Account> findAccountsMatchingPhraseInNameOrLastnameWithPagination(String phrase,
                                                                                  String orderBy,
                                                                                  boolean order,
                                                                                  int pageNumber,
                                                                                  int pageSize) throws ApplicationBaseException {
        try {
            TypedQuery<Account> findAllAccountsMatchingCriteriaQuery;
            if (orderBy.equals("level")) {
                if (order) {
                    findAllAccountsMatchingCriteriaQuery = entityManager.createNamedQuery("Account.findAccountsMatchingPhraseInNameOrLastnameWithUserLevelInAscendingOrder", Account.class);
                } else {
                    findAllAccountsMatchingCriteriaQuery = entityManager.createNamedQuery("Account.findAccountsMatchingPhraseInNameOrLastnameWithUserLevelInDescendingOrder", Account.class);
                }
            } else {
                if (order) {
                    findAllAccountsMatchingCriteriaQuery = entityManager.createNamedQuery("Account.findAccountsMatchingPhraseInNameOrLastnameWithLoginAscendingOrder", Account.class);
                } else {
                    findAllAccountsMatchingCriteriaQuery = entityManager.createNamedQuery("Account.findAccountsMatchingPhraseInNameOrLastnameWithLoginInDescendingOrder", Account.class);
                }
            }
            findAllAccountsMatchingCriteriaQuery.setFirstResult(pageNumber * pageSize);
            findAllAccountsMatchingCriteriaQuery.setMaxResults(pageSize);
            findAllAccountsMatchingCriteriaQuery.setParameter("phrase", phrase);
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
    @RolesAllowed({Authorities.BLOCK_ACCOUNT})
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

    // U - update methods

    /**
     * Forces the modification of the entity in the database.
     *
     * @param account Account to be modified.
     */
    @Override
    @RolesAllowed({
            Authorities.CHANGE_USER_PASSWORD, Authorities.CHANGE_OWN_PASSWORD, Authorities.BLOCK_ACCOUNT,
            Authorities.UNBLOCK_ACCOUNT, Authorities.MODIFY_OWN_ACCOUNT, Authorities.MODIFY_USER_ACCOUNT,
            Authorities.CONFIRM_ACCOUNT_CREATION, Authorities.CONFIRM_ACCOUNT_CREATION, Authorities.CONFIRM_EMAIL_CHANGE,
            Authorities.ADD_USER_LEVEL, Authorities.REMOVE_USER_LEVEL, Authorities.RESTORE_ACCOUNT_ACCESS,
            Authorities.RESET_PASSWORD, Authorities.CHANGE_OWN_PASSWORD_AFTER_ADMINISTRATIVE_CHANGE
    })
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
    @RolesAllowed({Authorities.REMOVE_ACCOUNT})
    public void remove(Account account) throws ApplicationBaseException {
        super.remove(account);
    }
}
