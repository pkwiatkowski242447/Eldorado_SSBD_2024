package pl.lodz.p.it.ssbd2024.ssbd03.mok.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
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
    public void create(UserLevel userLevel) {
        super.create(userLevel);
    }

    @Override
    public void edit(UserLevel userLevel) {
        super.edit(userLevel);
    }

    @Override
    public void remove(UserLevel userLevel) {
        super.remove(userLevel);
    }

    @Override
    public Optional<UserLevel> find(UUID id) {
        return super.find(id);
    }

    @Override
    public Optional<UserLevel> findAndRefresh(UUID id) {
        return super.findAndRefresh(id);
    }

    @Override
    public List<UserLevel> findAll() {
        return super.findAll();
    }

    @Override
    public int count() {
        return super.count();
    }
}

