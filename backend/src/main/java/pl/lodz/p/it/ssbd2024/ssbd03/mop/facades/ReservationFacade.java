package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Implementation of AbstractFacade that provides CRUD operations
 * on the Reservation class using the injected EntityManager implementation.
 * @see Reservation
 * @see jakarta.persistence.EntityManager
 */
@Repository
@LoggerInterceptor
@TxTracked
@Transactional(propagation = Propagation.MANDATORY)
@Slf4j
public class ReservationFacade extends AbstractFacade<Reservation> {

    @PersistenceContext(unitName = DatabaseConfigConstants.MOP_PU)
    private EntityManager entityManager;

    /**
     * Invokes superclass constructor with Class object passed.
     */
    public ReservationFacade() {
        super(Reservation.class);
    }

    /**
     * Returns injected EntityManager implementation.
     * @return EntityManager implementation
     */
    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Invokes superclass create method on passed Reservation entity
     * @param entity Reservation entity
     */
    @Override
    @RolesAllowed(Authorities.RESERVE_PARKING_PLACE)
    public void create(Reservation entity) throws ApplicationBaseException {
        super.create(entity);
    }

    /**
     * Invokes superclass edit method on passed Reservation entity
     * @param entity Reservation entity
     */
    @Override
    @RolesAllowed({Authorities.ENTER_PARKING_WITHOUT_RESERVATION, Authorities.EXIT_PARKING})
    public void edit(Reservation entity) throws ApplicationBaseException {
        super.edit(entity);
    }

    /**
     * Invokes superclass remove method on passed Reservation entity
     * @param entity Reservation entity
     */
    @Override
    @RolesAllowed(Authorities.END_RESERVATION)
    public void remove(Reservation entity) throws ApplicationBaseException {
        log.error("USUWAM REZERWACJE");
        super.remove(entity);
        log.error("USUNALEM REZERWACJE");
    }

    /**
     * Invokes superclass find method by passed entity id.
     * @param id The UUID type identifier of the entity being searched for
     * @return Entity, wrapped in Optional class, with identifiers equals to id param
     */
    @Override
    @DenyAll
    public Optional<Reservation> find(UUID id) throws ApplicationBaseException {
        return super.find(id);
    }

    /**
     * Invokes superclass find with refreshing method by passed entity id.
     * @param id The UUID type identifier of the entity being searched for
     * @return Entity, wrapped in Optional class, with identifiers equals to id param
     */
    @Override
    @RolesAllowed(Authorities.RESERVE_PARKING_PLACE)
    public Optional<Reservation> findAndRefresh(UUID id) throws ApplicationBaseException {
        return super.findAndRefresh(id);
    }

    /**
     * Invokes superclass find all method.
     * @return All Reservation entities
     */
    @Override
    @DenyAll
    public List<Reservation> findAll() throws ApplicationBaseException {
        return super.findAll();
    }

    /**
     * Returns all Reservation entities, based on NamedQuery defined on Reservation class, with pagination.
     * @param page page number
     * @param pageSize defines the maximum number of entities per page
     * @return All Reservation entities from selected page
     */
    @RolesAllowed({Authorities.GET_ALL_RESERVATIONS, Authorities.CANCEL_RESERVATION})
    public List<Reservation> findAllWithPagination(int page, int pageSize) throws ApplicationBaseException {
        var list = getEntityManager()
                .createNamedQuery("Reservation.findAll", Reservation.class)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAll(list);
        return list;
    }

    /**
     * Returns all active Reservation entities, based on NamedQuery defined on Reservation class, with pagination.
     * @param page page number
     * @param pageSize defines the maximum number of entities per page
     * @return All active Reservation entities from selected page
     */
    @RolesAllowed(Authorities.GET_ACTIVE_RESERVATIONS)
    public List<Reservation> findActiveReservationsWithPagination(UUID clientId, int page, int pageSize)
            throws ApplicationBaseException {
        var list = getEntityManager()
                .createNamedQuery("Reservation.findActiveReservations", Reservation.class)
                .setParameter("clientId", clientId)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAll(list);
        return list;
    }

    /**
     * Returns all historical Reservation entities, based on NamedQuery defined on Reservation class, with pagination.
     * @param page page number
     * @param pageSize defines the maximum number of entities per page
     * @return All historical Reservation entities from selected page
     */
    @RolesAllowed(Authorities.GET_HISTORICAL_RESERVATIONS)
    public List<Reservation> findHistoricalReservationsWithPagination(UUID clientId, int page, int pageSize)
            throws ApplicationBaseException {
        var list = getEntityManager()
                .createNamedQuery("Reservation.findHistoricalReservations", Reservation.class)
                .setParameter("clientId", clientId)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAll(list);
        return list;
    }

    /**
     * Returns all Reservation entities for the selected Sector, based on NamedQuery defined on Reservation class,
     * with pagination.
     * @param sectorId The UUID type identifier of the selected Sector
     * @param page page number
     * @param pageSize defines the maximum number of entities per page
     * @return All Reservation entities for selected Sector and page
     */
    @DenyAll
    public List<Reservation> findSectorReservationsWithPagination(UUID sectorId, int page, int pageSize)
            throws ApplicationBaseException {
        var list = getEntityManager()
                .createNamedQuery("Reservation.findSectorReservations", Reservation.class)
                .setParameter("sectorId", sectorId)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAll(list);
        return list;
    }

    /**
     * Invokes superclass count method.
     * @return Returns the total number of Reservation entities
     */
    @Override
    @DenyAll
    public int count() throws ApplicationBaseException {
        return super.count();
    }

    /***
     * This method is used to find all reservations, that last more than 24 hours
     *
     * @param amount Length of the specified time window, used to end reservations that last more than 24 hours.
     * @param timeUnit Time unit, indicating size of the reservation ending time window.
     * @return List of reservation that last more than 24 hours, If persistence exception is thrown returns empty list.
     * @throws ApplicationBaseException thrown when other unexpected problems occurred.
     */
    @RolesAllowed({Authorities.END_RESERVATION})
    public List<Reservation> findAllReservationsMarkedForEnding(long amount, TimeUnit timeUnit) throws ApplicationBaseException {
        try {
            TypedQuery<Reservation> findAllReservationMarkedForEnding = entityManager.createNamedQuery("Reservation.findAllReservationsMarkedForEnding", Reservation.class);
            findAllReservationMarkedForEnding.setParameter("timestamp", LocalDateTime.now().minus(amount, timeUnit.toChronoUnit()));
            List<Reservation> list = findAllReservationMarkedForEnding.getResultList();
            super.refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }
}
