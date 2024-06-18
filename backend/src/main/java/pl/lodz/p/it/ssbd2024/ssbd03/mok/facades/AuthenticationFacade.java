package pl.lodz.p.it.ssbd2024.ssbd03.mok.facades;

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
 * Repository used to manage Accounts Entities in the database
 * for authentication purposes.
 *
 * @see Account
 */
@Slf4j
@Repository
@LoggerInterceptor
@TxTracked
@Transactional(propagation = Propagation.MANDATORY)
public class AuthenticationFacade extends AbstractFacade<Account> {

    /**
     * Autowired entityManager used for managing entities.
     */
    @PersistenceContext(unitName = DatabaseConfigConstants.AUTH_PU)
    private EntityManager entityManager;

    /**
     * Constructs the facade.
     */
    public AuthenticationFacade() {
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

    /**
     * Retrieves an Account by the ID.
     *
     * @param id ID of the Account to be retrieved.
     * @return If Account with the given ID was found returns an Optional containing the Account, otherwise returns an empty Optional.
     */
    @Override
    @PermitAll
    @Transactional(propagation = Propagation.REQUIRED)
    public Optional<Account> find(UUID id) throws ApplicationBaseException {
        return super.findAndRefresh(id);
    }

    /**
     * Forces the modification of the entity in the database.
     *
     * @param entity Account to be modified.
     */
    @Override
    @RolesAllowed({Authorities.LOGIN})
    public void edit(Account entity) throws ApplicationBaseException {
        super.edit(entity);
    }

    /**
     * This method is used to find user account by username. As username needs to be unique, it returns a single result.
     *
     * @param login Login of the searched user account.
     * @return If there is user account with given username in the system, this method returns their account in a form of Optional.
     * Otherwise, empty optional is returned.
     */
    @RolesAllowed({Authorities.LOGIN, Authorities.REFRESH_SESSION})
    @Transactional(propagation = Propagation.REQUIRED)
    public Optional<Account> findByLogin(String login) throws ApplicationBaseException {
        try {
            TypedQuery<Account> tq = getEntityManager().createNamedQuery("Account.findByLogin", Account.class);
            tq.setParameter("login", login);
            Account foundAccount = tq.getSingleResult();
            entityManager.refresh(foundAccount);
            return Optional.of(foundAccount);
        } catch (PersistenceException pe) {
            return Optional.empty();
        }
    }
}
