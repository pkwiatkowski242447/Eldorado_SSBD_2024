package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository used to manage Account Entities in the database on behalf of MOP module.
 *
 * @see Account
 */
@Repository
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
    @Transactional
    @Override
    public Optional<Account> find(UUID id) {
        return super.find(id);
    }

    /**
     * Retrieves an Account by the ID and forces its refresh.
     *
     * @param id ID of the Account to be retrieved.
     * @return If Account with the given ID was found returns an Optional containing the Account, otherwise returns an empty Optional.
     */
    @Transactional
    @Override
    public Optional<Account> findAndRefresh(UUID id) {
        return super.findAndRefresh(id);
    }

    /**
     * Retrieves an Account by login.
     *
     * @param login Login of the Account to be retrieved.
     * @return If Account with the given login was found returns an Optional containing the Account, otherwise returns an empty Optional.
     */
    @Transactional
    public Optional<Account> findByLogin(String login) {
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
    @Transactional
    @Override
    public void edit(Account account) {
        super.edit(account);
    }
}
