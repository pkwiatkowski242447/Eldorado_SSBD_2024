package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingEvent;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Interface used for managing Reservations and Parking Events
 */
public interface ReservationServiceInterface {

    /***
     * Get all active reservation for client
     *
     * @param pageNumber Number of the page, which reservations will be retrieved from.
     * @param pageSize   Number of reservations per page.
     * @return List of active reservations for client.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    List<Reservation> getAllActiveReservationsByUserLoginWthPagination(String login, int pageNumber, int pageSize) throws ApplicationBaseException;

    /***
     * Get all historical reservations for client
     *
     * @param pageNumber Number of the page, which reservations will be retrieved from.
     * @param pageSize   Number of reservations per page.
     * @return List of historical reservations for client.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    List<Reservation> getAllHistoricalReservationsByUserIdWthPagination(String login, int pageNumber, int pageSize) throws ApplicationBaseException;

    /**
     * Create new reservation linking client and sector.
     *
     * @param clientLogin Login of client for whom the reservation is being created.
     * @param sectorId Sector identifier.
     * @param beginTime Start time of the reservation.
     * @param endTime End time of the reservation.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    void makeReservation(String clientLogin, UUID sectorId, LocalDateTime beginTime, LocalDateTime endTime) throws ApplicationBaseException;


    /**
     * Cancel reservation by its identifier.
     * @param reservationId Identifier of the reservation to be cancelled.
     */
    void cancelReservation(UUID reservationId) throws ApplicationBaseException;

    /**
     * Retrieve all reservations for in the system.
     * @param pageNumber The page number of the results to return.
     * @param pageSize   The number of results to return per page.
     * @return A list of all reservations in the system, with pagination applied.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    List<Reservation> getAllReservations(int pageNumber, int pageSize) throws ApplicationBaseException;

    /**
     * Retrieve reservation from the database by its identifier.
     *
     * @param reservationId Identifier of the reservation to be retrieved.
     * @param userLogin Login of the user, which tries to retrieve reservation.
     * @return Reservation entity object retrieved from the database.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    Reservation getReservationById(UUID reservationId, String userLogin) throws ApplicationBaseException;

    /**
     * Retrieve parking events for given reservation, identified by its identifier, which is UUID.
     *
     * @param reservationId Identifier of the reservation, which the parking events should be retrieved for.
     * @param pageNumber Number of page with parking event entries.
     * @param pageSize Number of the parking event entries per page.
     * @return List of parking events for given reservation, taking into account pagination. If no events matching criteria \
     * (like page number and size) were found then empty list is returned.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    List<ParkingEvent> getParkingEventsForGivenReservation(UUID reservationId, int pageNumber, int pageSize) throws ApplicationBaseException;
}
