package pl.lodz.p.it.ssbd2024.ssbd03.mok.facades;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.AccountHistoryData;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository used to manage Accounts Entities in the database on behalf of MOK module.
 *
 * @see Account
 */
@Slf4j
@Repository
@TxTracked
@Transactional(propagation = Propagation.MANDATORY)
public class AccountHistoryDataFacade extends AbstractFacade<AccountHistoryData> {

    /**
     * Autowired entityManager used for managing entities.
     */
    @PersistenceContext(unitName = DatabaseConfigConstants.MOK_PU)
    private EntityManager entityManager;

    /**
     * Constructs the facade.
     */
    public AccountHistoryDataFacade() {
        super(AccountHistoryData.class);
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
    public void create(AccountHistoryData account) throws ApplicationBaseException {
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
    public Optional<AccountHistoryData> find(UUID id) throws ApplicationBaseException {
        Optional<AccountHistoryData> optionalAccount = super.find(id);
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
    public Optional<AccountHistoryData> findAndRefresh(UUID id) throws ApplicationBaseException {
        return super.findAndRefresh(id);
    }

    /**
     * Retrieves all Accounts.
     *
     * @return `List` containing all Accounts.
     */
    @Override
    @DenyAll
    public List<AccountHistoryData> findAll() throws ApplicationBaseException {
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
    @RolesAllowed({Roles.ADMIN})
    public List<AccountHistoryData> findAllAccountsWithPagination(int pageNumber, int pageSize)
            throws ApplicationBaseException {
        try {
            TypedQuery<AccountHistoryData> findAllAccounts = entityManager.createNamedQuery("AccountHistoryData.findAll", AccountHistoryData.class);
            findAllAccounts.setFirstResult(pageNumber * pageSize);
            findAllAccounts.setMaxResults(pageSize);
            List<AccountHistoryData> list = findAllAccounts.getResultList();
            super.refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * This method is used to find user accounts by id.
     *
     * @param id ID of the searched account.
     * @param pageNumber Number of the page.
     * @param pageSize Size of the page.
     * @return If there are historic entries for the requested account, this method returns part of the entries
     * specified by pageSize and pageNumber. Otherwise, empty list.
     */
    @RolesAllowed({Roles.AUTHENTICATED})
    public List<AccountHistoryData> findByAccountId(UUID id, int pageNumber, int pageSize) throws ApplicationBaseException {
        try {
            TypedQuery<AccountHistoryData> findAccountsByLogin = entityManager.createNamedQuery("AccountHistoryData.findByAccountId", AccountHistoryData.class);
            findAccountsByLogin.setParameter("id", id);
            findAccountsByLogin.setFirstResult(pageNumber * pageSize);
            findAccountsByLogin.setMaxResults(pageSize);
            List<AccountHistoryData> list = findAccountsByLogin.getResultList();
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
    @DenyAll
    public void edit(AccountHistoryData account) throws ApplicationBaseException {
        super.edit(account);
    }

    // D - delete methods

    /**
     * Removes an Account from the database.
     *
     * @param account Account to be removed from the database.
     */
    @Override
    @DenyAll
    public void remove(AccountHistoryData account) throws ApplicationBaseException {
        super.remove(account);
    }
}
