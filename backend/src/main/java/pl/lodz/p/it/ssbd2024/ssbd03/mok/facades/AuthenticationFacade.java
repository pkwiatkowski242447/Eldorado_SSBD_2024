package pl.lodz.p.it.ssbd2024.ssbd03.mok.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class AuthenticationFacade extends AbstractFacade<Account> {

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
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Retrieves an Account by the ID.
     *
     * @param id ID of the Account to be retrieved.
     * @return If Account with the given ID was found returns an Optional containing the Account, otherwise returns an empty Optional.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Optional<Account> find(UUID id) {
        return super.find(id);
    }

    /**
     * Forces the modification of the entity in the database.
     *
     * @param account Account to be modified.
     */
    @Override
    public void edit(Account entity) {
        super.edit(entity);
    }

    /**
     * Retrieves an Account by the ID and forces its refresh.
     *
     * @param id ID of the Account to be retrieved.
     * @return If Account with the given ID was found returns an Optional containing the Account, otherwise returns an empty Optional.
     */
    @Override
    public Optional<Account> findAndRefresh(UUID id) {
        return super.findAndRefresh(id);
    }

    /**
     * This method is used to find user account by username. As username needs to be unique, it returns a single result.
     *
     * @param login Login of the searched user account.
     *
     * @return If there is user account with given username in the system, this method returns their account in a form of Optional.
     * Otherwise, empty optional is returned.
     */
    public Optional<Account> findByLogin(String login) {
        try {
            TypedQuery<Account> tq = getEntityManager().createNamedQuery("Account.findByLogin", Account.class);
            tq.setParameter("login", login);
            return Optional.of(tq.getSingleResult());
        } catch (PersistenceException pe) {
            return Optional.empty();
        }
    }
}
