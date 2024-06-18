package pl.lodz.p.it.ssbd2024.ssbd03.mok.facades;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Admin;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Staff;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Repository used to manage UserLevel Entities in the database.
 *
 * @see UserLevel
 * @see Admin
 * @see Client
 * @see Staff
 */
@Slf4j
@Repository
@LoggerInterceptor
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
    @RolesAllowed(Authorities.ADD_USER_LEVEL)
    public void create(UserLevel userLevel) throws ApplicationBaseException {
        super.create(userLevel);
    }

    /**
     * Forces modification of the UserLevel in the database.
     *
     * @param userLevel UserLevel to be modified.
     */
    @Override
    @RolesAllowed({Authorities.CHANGE_CLIENT_TYPE})
    public void edit(UserLevel userLevel) throws ApplicationBaseException {
        super.edit(userLevel);
    }

    /**
     * Removes a UserLevel from the database.
     *
     * @param userLevel UserLevel to be removed from the database.
     */
    @Override
    @RolesAllowed(Authorities.REMOVE_USER_LEVEL)
    public void remove(UserLevel userLevel) throws ApplicationBaseException {
        super.remove(userLevel);
    }
}
