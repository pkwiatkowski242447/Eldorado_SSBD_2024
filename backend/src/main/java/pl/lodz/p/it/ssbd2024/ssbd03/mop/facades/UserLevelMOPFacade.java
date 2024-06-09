package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
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
     * Forces modification of the UserLevel in the database.
     *
     * @param userLevel UserLevel to be modified.
     */
    @Override
    @RolesAllowed(Authorities.CHANGE_CLIENT_TYPE)
    public void edit(UserLevel userLevel) throws ApplicationBaseException {
        super.edit(userLevel);
    }

    /**
     * Retrieves a UserLevel by the ID.
     *
     * @param id ID of the UserLevel to be retrieved.
     * @return If UserLevel with the given ID was found returns an Optional containing the UserLevel, otherwise returns an empty Optional.
     */
    @Override
    @DenyAll
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
    @DenyAll
    public Optional<UserLevel> findAndRefresh(UUID id) throws ApplicationBaseException {
        return super.findAndRefresh(id);
    }

    /**
     * Retrieves all user levels by the account ID and forces its refresh.
     *
     * @param login Identifier of the account, which the user levels are searched for.
     * @return All user levels associated with given account.
     * @throws ApplicationBaseException General superclass of all possible exceptions throw in the persistence layer.
     */
    @DenyAll
    public List<UserLevel> findUserLevelsForGivenAccount(String login) throws ApplicationBaseException {
        TypedQuery<UserLevel> findUserLevelsForGivenAccount = entityManager.createNamedQuery("UserLevel.findAllUserLevelsForGivenAccount", UserLevel.class);
        findUserLevelsForGivenAccount.setParameter("login", login);
        List<UserLevel> listOfUserLevels = findUserLevelsForGivenAccount.getResultList();
        super.refreshAll(listOfUserLevels);
        return listOfUserLevels;
    }

    /**
     * Retrieves particular user level (of class searchedUserLevel) associated with given account.
     *
     * @param login Text identifier of the account, which the user level is searched for.
     * @param searchedUserLevel User level class, of user level connected to given account, that is searched for.
     * @return Certain user level, of given class, connected to the user account.
     * @throws ApplicationBaseException General superclass of all possible exceptions throw in the persistence layer.
     */
    @RolesAllowed(Authorities.RESERVE_PARKING_PLACE)
    public Optional<Client> findGivenUserLevelForGivenAccount(String login, Class<? extends UserLevel> searchedUserLevel) throws ApplicationBaseException {
        TypedQuery<Client> findGivenUserLevelForGivenAccount = entityManager.createNamedQuery("UserLevel.findGivenUserLevelsForGivenAccount", Client.class);
        findGivenUserLevelForGivenAccount.setParameter("login", login);
        findGivenUserLevelForGivenAccount.setParameter("userLevel", Client.class);
        Client userLevel = findGivenUserLevelForGivenAccount.getSingleResult();
        entityManager.refresh(userLevel);
        return Optional.of(userLevel);
    }
}