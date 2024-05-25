package pl.lodz.p.it.ssbd2024.ssbd03.mok.facades;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

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
@Slf4j
@Repository
@TxTracked
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
    @RolesAllowed(Roles.ADMIN)
    public void create(UserLevel userLevel) throws ApplicationBaseException {
        super.create(userLevel);
    }

    /**
     * Forces modification of the UserLevel in the database.
     *
     * @param userLevel UserLevel to be modified.
     */
    @Override
    @RolesAllowed({Roles.ADMIN})
    public void edit(UserLevel userLevel) throws ApplicationBaseException {
        super.edit(userLevel);
    }

    /**
     * Removes a UserLevel from the database.
     *
     * @param userLevel UserLevel to be removed from the database.
     */
    @Override
    @RolesAllowed(Roles.ADMIN)
    public void remove(UserLevel userLevel) throws ApplicationBaseException {
        super.remove(userLevel);
    }

    /**
     * Retrieves a UserLevel by the ID.
     *
     * @param id ID of the UserLevel to be retrieved.
     * @return If UserLevel with the given ID was found returns an Optional containing the UserLevel, otherwise returns an empty Optional.
     */
    @Override
    @RolesAllowed({Roles.ADMIN})
    public Optional<UserLevel> find(UUID id) throws ApplicationBaseException {
        return super.find(id);
    }

    /**
     * Retrieves a UserLevel by the ID and forces its refresh.
     *
     * @param id ID of the UserLevel to be retrieved.
     * @return If UserLevel with the given ID was found returns an Optional containing the UserLevel, otherwise returns an empty Optional.
     */
    @Override
    @RolesAllowed({Roles.ADMIN})
    public Optional<UserLevel> findAndRefresh(UUID id) throws ApplicationBaseException {
        return super.findAndRefresh(id);
    }

    /**
     * Retrieves all UserLevels.
     *
     * @return List containing all UserLevels.
     */
    @Override
    @RolesAllowed({Roles.ADMIN})
    public List<UserLevel> findAll() throws ApplicationBaseException {
        return super.findAll();
    }

    /**
     * Counts the number of UserLevels in the database.
     *
     * @return Number of UserLevels in the database.
     */
    @Override
    @RolesAllowed({Roles.ADMIN})
    public int count() throws ApplicationBaseException {
        return super.count();
    }
}

