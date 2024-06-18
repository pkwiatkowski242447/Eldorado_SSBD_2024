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
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.AccountHistoryData;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @RolesAllowed({
            Authorities.REGISTER_CLIENT, Authorities.REGISTER_USER, Authorities.CHANGE_USER_PASSWORD,
            Authorities.CHANGE_OWN_PASSWORD, Authorities.BLOCK_ACCOUNT, Authorities.UNBLOCK_ACCOUNT,
            Authorities.MODIFY_OWN_ACCOUNT, Authorities.MODIFY_USER_ACCOUNT, Authorities.CONFIRM_ACCOUNT_CREATION,
            Authorities.CONFIRM_EMAIL_CHANGE, Authorities.RESTORE_ACCOUNT_ACCESS, Authorities.RESET_PASSWORD
    })
    public void create(AccountHistoryData account) throws ApplicationBaseException {
        TypedQuery<Integer> findParkingByIdQuery = entityManager.createNamedQuery("AccountHistoryData.checkIfEntityExists", Integer.class);
        findParkingByIdQuery.setParameter("id", account.getId());
        findParkingByIdQuery.setParameter("version", account.getVersion());
        boolean exists = !findParkingByIdQuery.getResultList().isEmpty();

        if (!exists) super.create(account);
    }

    // R - read methods

    /**
     * This method is used to find user accounts by id.
     *
     * @param id ID of the searched account.
     * @param pageNumber Number of the page.
     * @param pageSize Size of the page.
     * @return If there are historic entries for the requested account, this method returns part of the entries
     * specified by pageSize and pageNumber. Otherwise, empty list.
     */
    @RolesAllowed({Authorities.GET_OWN_HISTORICAL_DATA, Authorities.GET_ACCOUNT_HISTORICAL_DATA})
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
}
