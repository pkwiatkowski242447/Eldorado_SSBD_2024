package pl.lodz.p.it.ssbd2024.ssbd03.mok.facades;

import jakarta.annotation.security.DenyAll;
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
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingHistoryData;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

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

    // R - read methods

    /**
     * Retrieves an Account by the ID.
     *
     * @param id ID of the Account to be retrieved.
     * @return If Account with the given ID was found returns an Optional containing the Account, otherwise returns an empty Optional.
     */
    @Override
    @DenyAll
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
    @DenyAll
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
