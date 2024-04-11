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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AccountMOPFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = DatabaseConfigConstants.MOP_PU)
    private EntityManager entityManager;

    public AccountMOPFacade() {
        super(Account.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    // R - read methods

    @Transactional
    @Override
    public Optional<Account> find(UUID id) {
        return super.find(id);
    }

    @Transactional
    @Override
    public Optional<Account> findAndRefresh(UUID id) {
        return super.findAndRefresh(id);
    }

    @Transactional
    @Override
    public List<Account> findAll() {
        return super.findAll();
    }

    @Transactional
    public Optional<Account> findByLogin(String login) {
        try {
            TypedQuery<Account> findAccountByLogin = entityManager.createNamedQuery("Account.findAccountByLogin", Account.class);
            findAccountByLogin.setParameter("login", login);
            Account account = findAccountByLogin.getSingleResult();
            entityManager.refresh(account);
            return Optional.of(account);
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    // U - update methods

    @Transactional
    @Override
    public void edit(Account account) {
        super.edit(account);
    }
}
