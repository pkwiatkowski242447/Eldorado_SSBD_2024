package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

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
}
