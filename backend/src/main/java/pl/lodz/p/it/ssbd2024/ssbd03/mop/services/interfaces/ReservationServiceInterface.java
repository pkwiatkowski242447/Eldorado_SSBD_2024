package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.reservationDTO.MakeReservationDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

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
     * @param makeReservationDTO Object containing data collapsed with creating new reservation.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    void makeReservation(String clientLogin, MakeReservationDTO makeReservationDTO) throws ApplicationBaseException;

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
}
