package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Staff;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Admin;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.Optional;

/**
 * Repository used to manage UserLevel Entities in the database.
 *
 * @see UserLevel
 * @see Admin
 * @see Client
 * @see Staff
 */
@Repository
@LoggerInterceptor
@TxTracked
@Transactional(propagation = Propagation.MANDATORY)
public class UserLevelMOPFacade extends AbstractFacade<UserLevel> {

    /**
     * Autowired entityManager used for managing entities.
     */
    @PersistenceContext(unitName = DatabaseConfigConstants.MOP_PU)
    private EntityManager entityManager;

    /**
     * Constructs the facade.
     */
    public UserLevelMOPFacade() {
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
     * Forces modification of the UserLevel entity object in the database.
     *
     * @param userLevel UserLevel to be modified.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @Override
    @RolesAllowed({Authorities.CHANGE_CLIENT_TYPE})
    public void edit(UserLevel userLevel) throws ApplicationBaseException {
        super.edit(userLevel);
    }

    /**
     * Retrieves particular user level (of class searchedUserLevel) associated with given account.
     *
     * @param login Text identifier of the account, which the user level is searched for.
     * @return Certain user level, of given class, connected to the user account.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({Authorities.RESERVE_PARKING_PLACE, Authorities.DELETE_PARKING, Authorities.ENTER_PARKING_WITHOUT_RESERVATION})
    public Optional<Client> findGivenUserLevelForGivenAccount(String login) throws ApplicationBaseException {
        Client userLevel = null;

        try {
            TypedQuery<Client> findGivenUserLevelForGivenAccount = entityManager.createNamedQuery("UserLevel.findGivenUserLevelsForGivenAccount", Client.class);
            findGivenUserLevelForGivenAccount.setParameter("login", login);
            findGivenUserLevelForGivenAccount.setParameter("userLevel", Client.class);
            userLevel = findGivenUserLevelForGivenAccount.getSingleResult();
            entityManager.refresh(userLevel);
        } catch (NoResultException ignore) {
        }
        return Optional.ofNullable(userLevel);
    }
}