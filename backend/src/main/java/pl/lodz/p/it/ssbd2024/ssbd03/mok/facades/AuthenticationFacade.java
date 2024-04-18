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
public class AuthenticationFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = DatabaseConfigConstants.AUTH_PU)
    private EntityManager entityManager;

    public AuthenticationFacade() {
        super(Account.class);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public Optional<Account> find(UUID id) {
        return super.find(id);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public void edit(Account entity) {
        super.edit(entity);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public Optional<Account> findAndRefresh(UUID id) {
        return super.findAndRefresh(id);
    }

    @Transactional(propagation = Propagation.MANDATORY)
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
