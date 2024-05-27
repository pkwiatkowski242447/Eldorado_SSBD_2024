package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.AllocationCodeDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.AllocationCodeWithSectorDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.List;
import java.util.UUID;

/**
 * Interface used for managing Parking and Sectors
 */
public interface ParkingServiceInterface {

    /**
     * Retrieves from the database sector by id.
     *
     * @param id Sector's id.
     * @return If Sector with the given id was found returns Sector.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    Sector getSectorById(UUID id) throws ApplicationBaseException;

    /**
     * Retrieves from the database list of sectors by parking id.
     *
     * @param id Parking's id.
     * @return If Parking with the given id was found, returns list of it's Sectors.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    List<Sector> getSectorsByParkingId(UUID id) throws ApplicationBaseException;

    /**
     * Retrieves parking from the database by id.
     *
     * @param id Parking's id.
     * @return If parking with the given id was found returns Parking.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    Parking getParkingById(UUID id) throws ApplicationBaseException;

    /**
     * Activates sector with given id.
     *
     * @param id Sector's id.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    void activateSector(UUID id) throws ApplicationBaseException;

    /**
     * Removes parking from the database by its id.
     *
     * @param id Identifier of the parking to be removed.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    void removeParkingById(UUID id) throws ApplicationBaseException;

    /**
     * Generates allocation code (if it does not exist for a given reservation) and registers entry parking event.
     * Moreover, this method also sends e-mail notification about beginning of the allocation with the allocation code,
     * used to end the reservation later.
     *
     * @param reservationId Identifier of the reservation, which the user uses.
     * @param userName      Login of the user, who perform the action.
     * @return Data transfer object containing allocation code, used later for ending the allocation.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    AllocationCodeDTO enterParkingWithReservation(UUID reservationId, String userName) throws ApplicationBaseException;

    /**
     * Uses parking's spot assignment algorithm to choose a parking spot for the requested entry. Then in creates
     * a new reservation , generates allocation code and registers entry parking event. Moreover, if the entry is made
     * by a registered client this method also sends an e-mail notification about beginning of the allocation
     * with the allocation code, used to end the reservation later.
     *
     * @param userName Login of the user, who performs the action. May be null if the entry is made by an anonymous user
     * @return Data transfer object containing allocation code, used later for ending the allocation, and basic
     * information about the assigned sector.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    AllocationCodeWithSectorDTO enterParkingWithoutReservation(String userName) throws ApplicationBaseException;
}
