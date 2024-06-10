package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingEvent;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository used to manage Parking Entities in the database.
 *
 * @see Account
 */
@Repository
@LoggerInterceptor
@TxTracked
@Transactional(propagation = Propagation.MANDATORY)
public class ParkingEventFacade extends AbstractFacade<ParkingEvent> {

    @PersistenceContext(unitName = DatabaseConfigConstants.MOP_PU)
    private EntityManager entityManager;

    /**
     * Constructs the facade.
     */
    public ParkingEventFacade() {
        super(ParkingEvent.class);
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
     * Persists a new parkingEvent entity object in the database.
     *
     * @param entity ParkingEvent to be persisted.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @Override
    @RolesAllowed({
            Authorities.ENTER_PARKING_WITH_RESERVATION, Authorities.ENTER_PARKING_WITHOUT_RESERVATION,
            Authorities.EXIT_PARKING
    })
    public void create(ParkingEvent entity) throws ApplicationBaseException {
        super.create(entity);
    }

    /**
     * Retrieves a parkingEvent by its identifier.
     *
     * @param id ID of the ParkingEvent to be retrieved.
     * @return If an ParkingEvent with the given ID was found returns an Optional containing the ParkingEvent, otherwise returns an empty Optional.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @Override
    @DenyAll
    public Optional<ParkingEvent> find(UUID id) throws ApplicationBaseException {
        return super.find(id);
    }

    /**
     * Retrieves a ParkingEvent by its identifier and forces its refresh.
     *
     * @param id ID of the ParkingEvent to be retrieved.
     * @return If a ParkingEvent with the given ID was found returns an Optional containing the ParkingEvent, otherwise returns an empty Optional.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @Override
    @DenyAll
    public Optional<ParkingEvent> findAndRefresh(UUID id) throws ApplicationBaseException {
        return super.findAndRefresh(id);
    }
}
