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

/**
 * Repository used to manage UserLevel Entities in the database.
 *
 * @see UserLevel
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Admin
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Staff
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class UserLevelFacade extends AbstractFacade<UserLevel> {

    /**
     * Autowired entityManager used for managing entities.
     */
    @PersistenceContext(unitName = DatabaseConfigConstants.MOK_PU)
    private EntityManager entityManager;

    /**
     * Constructs the facade.
     */
    public UserLevelFacade() {
        super(UserLevel.class);
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
     * Persists a new UserLevel in the database.
     *
     * @param userLevel UserLevel to be persisted.
     */
    @Override
    public void create(UserLevel userLevel) {
        super.create(userLevel);
    }

    /**
     * Forces modification of the UserLevel in the database.
     *
     * @param userLevel UserLevel to be modified.
     */
    @Override
    public void edit(UserLevel userLevel) {
        super.edit(userLevel);
    }

    /**
     * Removes a UserLevel from the database.
     *
     * @param userLevel UserLevel to be removed from the database.
     */
    @Override
    public void remove(UserLevel userLevel) {
        super.remove(userLevel);
    }

    /**
     * Retrieves a UserLevel by the ID.
     *
     * @param id ID of the UserLevel to be retrieved.
     * @return If UserLevel with the given ID was found returns an Optional containing the UserLevel, otherwise returns an empty Optional.
     */
    @Override
    public Optional<UserLevel> find(UUID id) {
        return super.find(id);
    }

    /**
     * Retrieves a UserLevel by the ID and forces its refresh.
     *
     * @param id ID of the UserLevel to be retrieved.
     * @return If UserLevel with the given ID was found returns an Optional containing the UserLevel, otherwise returns an empty Optional.
     */
    @Override
    public Optional<UserLevel> findAndRefresh(UUID id) {
        return super.findAndRefresh(id);
    }

    /**
     * Retrieves all UserLevels.
     *
     * @return List containing all UserLevels.
     */
    @Override
    public List<UserLevel> findAll() {
        return super.findAll();
    }

    /**
     * Counts the number of UserLevels in the database.
     *
     * @return Number of UserLevels in the database.
     */
    @Override
    public int count() {
        return super.count();
    }
}

