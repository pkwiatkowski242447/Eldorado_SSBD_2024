package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.List;
import java.util.UUID;

/**
 * Interface used for managing Reservations and Parking Events
 */
public interface ReservationServiceInterface {

    /**
     * Create new reservation linking client and sector.
     *
     * @param clientLogin Login of client for whom the reservation is being created.
     * @param sectorId Identifier of sector in which a place is being reserved.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    void makeReservation(String clientLogin, UUID sectorId) throws ApplicationBaseException;

    /**
     * Cancel reservation by its identifier.
     * @param reservationId Identifier of the reservation to be cancelled.
     */
    void cancelReservation(UUID reservationId) throws ApplicationBaseException;

    /**
     * Retrieve all reservations for a user in the system.
     * @param pageNumber The page number of the results to return.
     * @param pageSize   The number of results to return per page.
     * @return A list of all accounts in the system, ordered by account login, with pagination applied.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    List<Reservation> getAllReservations(int pageNumber, int pageSize) throws ApplicationBaseException;
}
