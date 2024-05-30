package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
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

import java.util.Optional;
import java.util.UUID;

/**
 * Repository used to manage Account Entities in the database on behalf of MOP module.
 *
 * @see Account
 */
@Repository
@LoggerInterceptor
@TxTracked
@Transactional(propagation = Propagation.MANDATORY)
public class AccountMOPFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = DatabaseConfigConstants.MOP_PU)
    private EntityManager entityManager;

    /**
     * Constructs the facade.
     */
    public AccountMOPFacade() {
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

    // R - read methods

    /**
     * Retrieves an Account by the ID.
     *
     * @param id ID of the Account to be retrieved.
     * @return If Account with the given ID was found returns an Optional containing the Account, otherwise returns an empty Optional.
     */
    @Override
    @DenyAll
    public Optional<Account> find(UUID id) throws ApplicationBaseException {
        return super.find(id);
    }

    /**
     * Retrieves an Account by the ID and forces its refresh.
     *
     * @param id ID of the Account to be retrieved.
     * @return If Account with the given ID was found returns an Optional containing the Account, otherwise returns an empty Optional.
     */
    @Override
    @DenyAll
    public Optional<Account> findAndRefresh(UUID id) throws ApplicationBaseException {
        return super.findAndRefresh(id);
    }

    /**
     * Retrieves an Account by login.
     *
     * @param login Login of the Account to be retrieved.
     * @return If Account with the given login was found returns an Optional containing the Account, otherwise returns an empty Optional.
     */
    @RolesAllowed({Authorities.RESERVE_PARKING_PLACE, Authorities.CANCEL_RESERVATION,
            Authorities.GET_ACTIVE_RESERVATIONS, Authorities.GET_HISTORICAL_RESERVATIONS}
    )
    public Optional<Account> findByLogin(String login) throws ApplicationBaseException {
        try {
            TypedQuery<Account> findAccountByLogin = entityManager.createNamedQuery("Account.findByLogin", Account.class);
            findAccountByLogin.setParameter("login", login);
            Account account = findAccountByLogin.getSingleResult();
            entityManager.refresh(account);
            return Optional.of(account);
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
    @DenyAll
    public void edit(Account account) throws ApplicationBaseException{
        super.edit(account);
    }
}
