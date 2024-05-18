package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingEvent;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository used to manage Parking Entities in the database.
 *
 * @see Account
 */
@Repository
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
     * Persists a new ParkingEvent to the database.
     *
     * @param entity ParkingEvent to be persisted.
     */
    @Override
    public void create(ParkingEvent entity) throws ApplicationBaseException {
        super.create(entity);
    }

    /**
     * Forces modification of the ParkingEvent in the database.
     *
     * @param entity ParkingEvent to be modified.
     */
    @Override
    public void edit(ParkingEvent entity) throws ApplicationBaseException {
        super.edit(entity);
    }

    /**
     * Removes a ParkingEvent from the database.
     *
     * @param entity ParkingEvent to be removed from the database.
     */
    @Override
    public void remove(ParkingEvent entity) {
        super.remove(entity);
    }

    /**
     * Retrieves a ParkingEvent by the ID.
     *
     * @param id ID of the ParkingEvent to be retrieved.
     * @return If an ParkingEvent with the given ID was found returns an Optional containing the ParkingEvent, otherwise returns an empty Optional.
     */
    @Override
    public Optional<ParkingEvent> find(UUID id) throws ApplicationBaseException {
        return super.find(id);
    }

    /**
     * Retrieves a ParkingEvent by the ID and forces its refresh.
     *
     * @param id ID of the ParkingEvent to be retrieved.
     * @return If a ParkingEvent with the given ID was found returns an Optional containing the ParkingEvent, otherwise returns an empty Optional.
     */
    @Override
    public Optional<ParkingEvent> findAndRefresh(UUID id) {
        return super.findAndRefresh(id);
    }

    /**
     * Retrieves all ParkingEvents.
     *
     * @return List containing all ParkingEvents.
     */
    @Override
    public List<ParkingEvent> findAll() {
        return super.findAll();
    }

    /**
     * Counts the number of the ParkingEvents in the database.
     *
     * @return Number of ParkingEvents in the database.
     */
    @Override
    public int count() {
        return super.count();
    }
}
