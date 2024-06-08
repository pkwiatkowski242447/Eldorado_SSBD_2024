package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.allocationCodeDTO.AllocationCodeDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.allocationCodeDTO.AllocationCodeWithSectorDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.conflict.ParkingAddressAlreadyTakenException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.read.ParkingNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.validation.ParkingConstraintViolationException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.status.SectorAlreadyActiveException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.read.SectorNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.status.SectorAlreadyInactiveException;

import java.util.List;
import java.util.UUID;

/**
 * Interface used for managing Parking and Sectors
 */
public interface ParkingServiceInterface {

    /***
     * Create parking with specified address.
     *
     * @param city Address - city.
     * @param zipCode Address - zip code.
     * @param street Address - street.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    Parking createParking(String city, String zipCode, String street) throws ApplicationBaseException;

    /**
     * Create sector in the given parking.
     *
     * @param parkingId Identifier of parking to which the sector will be added.
     * @param name      The name of the sector.
     * @param type      The type of the sector.
     * @param maxPlaces The maximum number of parking spots in the sector.
     * @param weight    The weight of the sector.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     * @throws ParkingConstraintViolationException Throws when database constraints are not followed
     * @throws ParkingAddressAlreadyTakenException Throws when database constraint unique on (zip-code, city, street) is not followed
     */
    void createSector(UUID parkingId, String name, Sector.SectorType type, Integer maxPlaces, Integer weight, Boolean active) throws ApplicationBaseException;

    /**
     * Retrieve all parking spaces in the system.
     *
     * @param pageNumber The page number of the results to return.
     * @param pageSize   The number of results to return per page.
     * @return A list of all parking in the system, with pagination applied.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    List<Parking> getAllParkingWithPagination(int pageNumber, int pageSize) throws ApplicationBaseException;

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
     * @return If Parking with the given id was found, returns list of its Sectors.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    List<Sector> getSectorsByParkingId(UUID id) throws ApplicationBaseException;

    /**
     * Retrieves parking from the database by id.
     *
     * @param id Parking's id.
     * @return If parking with the given id was found returns Parking.
     * @throws ParkingNotFoundException Thrown when parking with given id cannot be found in the database.
     */
    Parking getParkingById(UUID id) throws ApplicationBaseException;

    /**
     * Activates sector with given id, by setting active field to true.
     *
     * @param id Sector's id.
     * @throws SectorNotFoundException Thrown when sector with given id cannot be found in the database.
     * @throws SectorAlreadyActiveException Thrown when trying to activate an active sector.
     */
    void activateSector(UUID id) throws ApplicationBaseException;

    /**
     * Deactivates sector with given id, by setting active field to false.
     *
     * @param id Sector's id.
     * @throws SectorNotFoundException Thrown when sector with given id cannot be found in the database.
     * @throws SectorAlreadyInactiveException Thrown when trying to deactivate an inactive sector.
     */
    void deactivateSector(UUID id) throws ApplicationBaseException;

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
     * Edits parking in the database by its id.
     *
     * @param modifiedParking Parking with potentially modified properties: city, zipCode, street.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    Parking editParking (Parking modifiedParking) throws ApplicationBaseException;

    /**
     * Edits sector in the database by its id.
     *
     * @param modifiedSector Sector to be edited.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    Sector editSector(Sector modifiedSector) throws ApplicationBaseException;

    /**
     * Removes sector from the database by its id.
     *
     * @param id Identifier of the sector to be removed.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    void removeSectorById(UUID id) throws ApplicationBaseException;

    /**
     * Retrieves all parking that are available from the database.
     *
     * @param pageNumber          Number of the page to retrieve.
     * @param pageSize            Number of results per page.
     * @return If there are available parking returns these parking.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    List<Parking> getAvailableParkingWithPagination(int pageNumber, int pageSize) throws ApplicationBaseException;

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

    /**
     * Ends the parking allocation by registering the end of a parking event.
     * If the reservation ID and the exit code are correct, the parking spot
     * is freed and the reservation is marked as ended.
     *
     * @param reservationId Identifier of the reservation, which the user uses.
     * @param exitCode      Code used to end the reservation.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    void exitParking(UUID reservationId, String exitCode) throws ApplicationBaseException;
}
