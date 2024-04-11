package pl.lodz.p.it.ssbd2024.ssbd03.mok.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserLevelFacade extends AbstractFacade<UserLevel> {

    @PersistenceContext(unitName = DatabaseConfigConstants.MOK_PU)
    private EntityManager entityManager;

    public UserLevelFacade() {
        super(UserLevel.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    @Transactional
    public void create(UserLevel entity) {
        super.create(entity);
    }

    @Override
    @Transactional
    public void edit(UserLevel entity) {
        super.edit(entity);
    }

    @Override
    @Transactional
    public void remove(UserLevel entity) {
        super.remove(entity);
    }

    @Override
    @Transactional
    public Optional<UserLevel> find(UUID id) {
        return super.find(id);
    }

    @Override
    @Transactional
    public Optional<UserLevel> findAndRefresh(UUID id) {
        return super.findAndRefresh(id);
    }

    @Override
    @Transactional
    public List<UserLevel> findAll() {
        return super.findAll();
    }

    @Override
    @Transactional
    public int count() {
        return super.count();
    }
}

