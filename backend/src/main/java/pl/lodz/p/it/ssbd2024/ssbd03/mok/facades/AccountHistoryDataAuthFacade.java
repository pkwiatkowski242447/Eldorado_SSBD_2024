package pl.lodz.p.it.ssbd2024.ssbd03.mok.facades;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
public class AccountHistoryDataAuthFacade extends AbstractFacade<AccountHistoryData> {

    /**
     * Autowired entityManager used for managing entities.
     */
    @PersistenceContext(unitName = DatabaseConfigConstants.AUTH_PU)
    private EntityManager entityManager;

    /**
     * Constructs the facade.
     */
    public AccountHistoryDataAuthFacade() {
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
    @RolesAllowed({Authorities.LOGIN})
    public void create(AccountHistoryData account) throws ApplicationBaseException {
        TypedQuery<Integer> findParkingByIdQuery = entityManager.createNamedQuery("AccountHistoryData.checkIfEntityExists", Integer.class);
        findParkingByIdQuery.setParameter("id", account.getId());
        findParkingByIdQuery.setParameter("version", account.getVersion());
        boolean exists = !findParkingByIdQuery.getResultList().isEmpty();

        if (!exists) super.create(account);
    }
}
