package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.conflict.ParkingAddressAlreadyTakenException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.read.ParkingNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.validation.ParkingConstraintViolationException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.read.SectorNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.status.SectorAlreadyActiveException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.status.SectorAlreadyInactiveException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Interface used for managing Parking and Sectors
 */
public interface ParkingServiceInterface {

    /***
     * Create a new parking object with specified address.
     *
     * @param city City, where the parking is located, part of the Address object.
     * @param zipCode Zip code, of the administrative area, where the parking is located, part of the Address object.
     * @param street Street, which the parking is located at, part of the Address object.
     * @param strategy Algorithm used in determining sector when entering parking without reservation.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    Parking createParking(String city, String zipCode, String street, Parking.SectorDeterminationStrategy strategy)
            throws ApplicationBaseException;

    /**
     * Create sector in the given parking.
     *
     * @param parkingId Identifier of parking to which the sector will be added.
     * @param name      The name of the sector.
     * @param type      The type of the sector.
     * @param maxPlaces The maximum number of parking spots in the sector.
     * @param weight    The weight of the sector.
     * @throws ApplicationBaseException            General superclass for all exceptions thrown by aspects intercepting this method.
     * @throws ParkingConstraintViolationException Throws when database constraints are not followed
     * @throws ParkingAddressAlreadyTakenException Throws when database constraint unique on (zip-code, city, street) is not followed
     */
    void createSector(UUID parkingId, String name, Sector.SectorType type, Integer maxPlaces, Integer weight, Boolean active) throws ApplicationBaseException;

    /**
     * Retrieves all parking in the system.
     *
     * @param pageNumber The page number of the results to return.
     * @param pageSize   The number of results to return per page.
     * @return A list of all parking in the system, with pagination applied.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    List<Parking> getAllParkingWithPagination(int pageNumber, int pageSize)
            throws ApplicationBaseException;

    /**
     * Retrieves from the database sector by id.
     *
     * @param id Sector's id.
     * @return If Sector with the given id was found returns Sector.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    Sector getSectorById(UUID id)
            throws ApplicationBaseException;

    /**
     * Retrieves from the database list of sectors by parking id.
     *
     * @param id         Parking's id.
     * @param active     Determines whether to return all sectors or only active ones.
     * @param pageNumber Number of the page.
     * @param pageSize   Size of the page with sector entries.
     * @return If Parking with the given id was found, returns list of its Sectors.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    List<Sector> getSectorsByParkingId(UUID id, boolean active, int pageNumber, int pageSize) throws ApplicationBaseException;

    /**
     * Retrieves parking from the database by id.
     *
     * @param id Parking's id.
     * @return If parking with the given id was found returns Parking.
     * @throws ParkingNotFoundException Thrown when parking with given id cannot be found in the database.
     */
    Parking getParkingById(UUID id)
            throws ApplicationBaseException;

    /**
     * Activates sector with given id, by setting active field to true.
     *
     * @param id Sector's id.
     * @throws SectorNotFoundException      Thrown when sector with given id cannot be found in the database.
     * @throws SectorAlreadyActiveException Thrown when trying to activate an active sector.
     */
    void activateSector(UUID id) throws ApplicationBaseException;

    /**
     * Deactivates sector with given id, by setting active field to false.
     *
     * @param id Sector's id.
<<<<<<< HEAD
     * @param deactivationTime Time of the sectors planned deactivation.
     * @throws SectorNotFoundException Thrown when sector with given id cannot be found in the database.
=======
     * @throws SectorNotFoundException        Thrown when sector with given id cannot be found in the database.
>>>>>>> 5c4dedbc97c2eef6f6e67fae5c6e202ff1f66c17
     * @throws SectorAlreadyInactiveException Thrown when trying to deactivate an inactive sector.
     */
    void deactivateSector(UUID id, LocalDateTime deactivationTime) throws ApplicationBaseException;

    /**
     * Removes parking from the database by its id.
     *
     * @param id Identifier of the parking to be removed.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    void removeParkingById(UUID id)
            throws ApplicationBaseException;

    /**
     * Registers new entry parking event for given reservation, identified by its id.
     * Moreover, this method also sends e-mail notification about beginning of the reservation.
     *
     * @param reservationId Identifier of the reservation, which the user uses.
     * @param userName      Login of the user, who perform the action.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    void enterParkingWithReservation(UUID reservationId, String userName) throws ApplicationBaseException;

    /**
     * Edits parking in the database by its id.
     *
     * @param modifiedParking Parking with potentially modified properties: city, zipCode, street.
     * @param parkingId       Identifier of the parking to be edited.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    Parking editParking(Parking modifiedParking, UUID parkingId) throws ApplicationBaseException;

    /**
     * Edits sector in the database by its id.
     *
     * @param modifiedSector Sector to be edited.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    Sector editSector(UUID id, Long version, Sector modifiedSector)
            throws ApplicationBaseException;

    /**
     * Removes sector from the database by its id.
     *
     * @param id Identifier of the sector to be removed.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    void removeSectorById(UUID id) throws ApplicationBaseException;

    /**
     * Retrieves all parking that are available (have at least one active sector) from the database.
     *
     * @param pageNumber Number of the page to retrieve.
     * @param pageSize   Number of results per page.
     * @return If there are available parking returns these parking.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    List<Parking> getAvailableParkingWithPagination(int pageNumber, int pageSize)
            throws ApplicationBaseException;

    /**
     * Uses parking's spot assignment algorithm to choose a parking spot for the requested entry. Then in creates
     * a new reservation , generates allocation code and registers entry parking event.
     *
     * @param parkingId ID of the parking that the user want to enter.
     * @param login Login of the user, who performs the action.
     * @param isAnonymous Determines whether the action is made by an anonymous user or not.
     * @return Data transfer object containing allocation code, used later for ending the allocation, and basic
     * information about the assigned sector.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New reservation"),
            @ApiResponse(responseCode = "400", description = "Entry was not possible due to reservation not being created."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    Reservation enterParkingWithoutReservation(UUID parkingId, String login, boolean isAnonymous) throws ApplicationBaseException;

    /**
     * Ends the parking allocation by registering the end of a parking event.
     * If the reservation ID is correct, the parking spot is freed and the reservation is marked as ended.
     *
     * @param reservationId Identifier of the reservation, which the user uses.
     * @param userLogin Login of the currently logged-in user, or anonymousUser string of characters if user is not
     *                  authenticated in the application.
     * @param endReservation Boolean flag indicating whether the reservation should be finished after the exit event.
     *                       By default, the endReservation flag is false, meaning that the reservation will not be finished
     *                       after exit event.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    void exitParking(UUID reservationId, String userLogin, boolean endReservation)
            throws ApplicationBaseException;
}
