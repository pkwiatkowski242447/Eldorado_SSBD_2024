package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * Implementation of AbstractFacade that provides CRUD operations
 * on the Reservation class using the injected EntityManager implementation.
 *
 * @see Reservation
 * @see jakarta.persistence.EntityManager
 */
@Repository
@LoggerInterceptor
@TxTracked
@Transactional(propagation = Propagation.MANDATORY)
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
     *
     * @return EntityManager implementation
     */
    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * This method is used to create in the database, that is to persist new
     * reservation entity object.
     *
     * @param entity Reservation entity
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @Override
    @RolesAllowed({Authorities.RESERVE_PARKING_PLACE, Authorities.ENTER_PARKING_WITHOUT_RESERVATION})
    public void create(Reservation entity) throws ApplicationBaseException {
        super.create(entity);
    }

    /**
     * This method is used to modify exiting reservation entity object in the database.
     *
     * @param entity Reservation entity to be modified in the database.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @Override
    @RolesAllowed({
            Authorities.ENTER_PARKING_WITH_RESERVATION, Authorities.ENTER_PARKING_WITHOUT_RESERVATION,
            Authorities.EXIT_PARKING, Authorities.END_RESERVATION, Authorities.DEACTIVATE_SECTOR,
            Authorities.CANCEL_RESERVATION
    })
    public void edit(Reservation entity) throws ApplicationBaseException {
        super.edit(entity);
    }

    /**
     * This method is used to remove a reservation entity object from the database.
     *
     * @param entity Reservation entity object to be removed from the database.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @Override
    @RolesAllowed({Authorities.END_RESERVATION})
    public void remove(Reservation entity) throws ApplicationBaseException {
        super.remove(entity);
    }

    /**
     * This method is used to retrieve reservation entity object by its identifier and
     * refresh it at the same time, to enforce read from the database.
     *
     * @param id The UUID type identifier of the entity being searched for.
     * @return Entity, wrapped in Optional class, with identifiers equals to id param.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @Override
    @RolesAllowed({
            Authorities.RESERVE_PARKING_PLACE, Authorities.ENTER_PARKING_WITH_RESERVATION,
            Authorities.EXIT_PARKING, Authorities.CANCEL_RESERVATION
    })
    public Optional<Reservation> findAndRefresh(UUID id) throws ApplicationBaseException {
        return super.findAndRefresh(id);
    }

    /**
     * Returns all Reservation entities, based on NamedQuery defined on Reservation class, with pagination.
     *
     * @param page     Number of the page with reservation entries.
     * @param pageSize Defines the maximum number of entities per page.
     * @return All Reservation entities from selected page
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({Authorities.GET_ALL_RESERVATIONS})
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
     * Returns all active reservations for user with specified login
     *
     * @param login      The user login.
     * @param pageNumber page number.
     * @param pageSize   defines the maximum number of entities per page.
     * @return All Reservation entities for selected Sector and page.
     * If a persistence exception is thrown, then empty list is returned.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({Authorities.GET_ACTIVE_RESERVATIONS})
    public List<Reservation> findAllActiveUserReservationByLoginWithPagination(String login, int pageNumber, int pageSize) throws ApplicationBaseException {
        try {
            var list = getEntityManager()
                    .createNamedQuery("Reservation.findActiveReservationsByLogin", Reservation.class)
                    .setParameter("clientLogin", login)
                    .setFirstResult(pageNumber * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
            refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * Returns all historical reservations for user with specified login
     *
     * @param login      The user login.
     * @param pageNumber Page number with reservation entries.
     * @param pageSize   Defines the maximum number of entities per page.
     * @return All Reservation entities for selected Sector and page.
     * If a persistence exception is thrown, then empty list is returned.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({Authorities.GET_HISTORICAL_RESERVATIONS})
    public List<Reservation> findAllHistoricalUserReservationByLoginWithPagination(String login, int pageNumber, int pageSize) throws ApplicationBaseException {
        try {
            var list = getEntityManager()
                    .createNamedQuery("Reservation.findHistoricalReservationsByLogin", Reservation.class)
                    .setParameter("clientLogin", login)
                    .setFirstResult(pageNumber * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
            refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * This method is used to find all reservations, that last more than 24 hours
     *
     * @param amount   Length of the specified time window, used to end reservations that last more than 24 hours.
     * @param timeUnit Time unit, indicating size of the reservation ending time window.
     * @return List of reservation that last more than 24 hours, If persistence exception is thrown returns empty list.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
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

    /**
     * Counts all active reservations for user with specified login
     *
     * @param login The user login.
     * @return Count of Reservation entities for selected client (collapsed with login).
     * If a persistence exception is thrown, then empty list is returned.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({Authorities.GET_ACTIVE_RESERVATIONS, Authorities.ENTER_PARKING_WITHOUT_RESERVATION})
    public long countAllActiveUserReservationByLogin(String login) throws ApplicationBaseException {
        TypedQuery<Long> countAllActiveUserReservationByLoginWithPaginationQuery =
                entityManager.createNamedQuery("Reservation.countAllActiveUserReservationByLogin", Long.class);
        countAllActiveUserReservationByLoginWithPaginationQuery.setParameter("clientLogin", login);
        return Objects.requireNonNullElse(countAllActiveUserReservationByLoginWithPaginationQuery.getSingleResult(), 0L);
    }

    /**
     * Counts all reservations for sector in the timeframe (beginTime - beginTime + maxReservationHours)
     *
     * @param sectorId            Sector identifier.
     * @param beginTime           Start time of the reservation.
     * @param maxReservationHours Maximum duration of the reservation, given in hours.
     * @param benchmark           Value indicating the point in time from the perspective of which the query is executed.
     * @return Count of Reservation entities for selected client (collapsed with login).
     * If a persistence exception is thrown, then empty list is returned.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({Authorities.RESERVE_PARKING_PLACE})
    public long countAllSectorReservationInTimeframe(UUID sectorId, LocalDateTime beginTime, int maxReservationHours, LocalDateTime benchmark) throws ApplicationBaseException {
        TypedQuery<Long> countAllSectorReservationInTimeframeQuery =
                entityManager.createNamedQuery("Reservation.countAllSectorReservationInTimeframe", Long.class);
        countAllSectorReservationInTimeframeQuery.setParameter("sectorId", sectorId);
        countAllSectorReservationInTimeframeQuery.setParameter("beginTime", beginTime);
        countAllSectorReservationInTimeframeQuery.setParameter("beginTimeMinusMaxReservationTime", beginTime.minusHours(maxReservationHours));
        countAllSectorReservationInTimeframeQuery.setParameter("beginTimePlusMaxReservationTime", beginTime.plusHours(maxReservationHours));
        countAllSectorReservationInTimeframeQuery.setParameter("current_timestamp", benchmark);
        return Objects.requireNonNullElse(countAllSectorReservationInTimeframeQuery.getSingleResult(), 0L);
    }

    /**
     * This method is used to retrieve all reservation that need to be cancelled because of the deactivation of the
     * sector for which they were made.
     *
     * @param sectorId Identifier of the sector to be deactivated.
     * @param cancellationTimeWindow Beginning of the time window before the deactivation.
     * @return List of all the reservations that need to be cancelled.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by the exception
     * handling aspects.
     */
    @RolesAllowed({Authorities.DEACTIVATE_SECTOR})
    public List<Reservation> getAllReservationsToCancelBeforeDeactivation(UUID sectorId, LocalDateTime cancellationTimeWindow)
            throws ApplicationBaseException {
        TypedQuery<Reservation> getAllReservationsForDeactivation = getEntityManager()
                .createNamedQuery("Reservation.findAllReservationsToCancelBeforeDeactivation", Reservation.class);
        getAllReservationsForDeactivation.setParameter("sectorId", sectorId);
        getAllReservationsForDeactivation.setParameter("timestamp", cancellationTimeWindow);
        List<Reservation> list = getAllReservationsForDeactivation.getResultList();
        super.refreshAll(list);
        return list;
    }

    /**
     * Retrieves reservation by the id and its owner login.
     *
     * @param reservationId Identifier of reservation searched for.
     * @param ownerLogin    Owner's login of reservation searched for.
     * @return If Reservation with the ID, belonging to the specified owner, was found returns an Optional containing
     * the Reservation, otherwise returns an empty Optional.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({Authorities.CANCEL_RESERVATION})
    public Optional<Reservation> findClientReservation(UUID reservationId, String ownerLogin) throws ApplicationBaseException {
        try {
            TypedQuery<Reservation> findClientReservationQuery = entityManager.createNamedQuery("Reservation.findClientReservation", Reservation.class);
            findClientReservationQuery.setParameter("reservationId", reservationId);
            findClientReservationQuery.setParameter("ownerLogin", ownerLogin);
            Reservation reservation = findClientReservationQuery.getSingleResult();
            entityManager.refresh(reservation);
            return Optional.of(reservation);
        } catch (NoResultException ignore) {
            return Optional.empty();
        }
    }
}
