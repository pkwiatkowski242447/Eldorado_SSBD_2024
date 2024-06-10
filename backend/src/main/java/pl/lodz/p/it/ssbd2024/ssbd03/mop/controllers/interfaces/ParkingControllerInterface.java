package pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingCreateDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO.SectorCreateDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO.SectorDeactivationTimeDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO.SectorModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Interface used for managing Parking and Sectors
 */
public interface ParkingControllerInterface {

    /***
     * This method is used to create new system.
     *
     * @param parkingCreateDTO Data transfer object, containing parking data.
     * @return It returns HTTP response 201 CREATED when parking creation is successful,
     *         It returns HTTP response 400 BAD REQUEST when persistence exception is being thrown.
     *         It returns HTTP response 409 CONFLICT when parking with specific data exits.
     *         It returns HTTP response 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create parking", description = "Create new parking with specified address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New parking successfully created."),
            @ApiResponse(responseCode = "400", description = "Persistence exception was thrown."),
            @ApiResponse(responseCode = "409", description = "Parking with specified address already exist."),
            @ApiResponse(responseCode = "500", description = "Unexpected exception occurred.")
    })
    ResponseEntity<?> createParking(@RequestBody ParkingCreateDTO parkingCreateDTO) throws ApplicationBaseException;

    /**
     * This method is used to create new sector.
     *
     * @param sectorCreateDTO Data transfer object, containing sector data.
     * @return It returns HTTP response 204 NO CONTENT when sector creation is successful,
     * It returns HTTP response 400 BAD REQUEST when persistence exception is being thrown.
     * It returns HTTP response 404 NOT FOUND when parking with specified id does not exist.
     * It returns HTTP response 409 CONFLICT when sector with specific data exits.
     * It returns HTTP response 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/{id}/sectors")
    @Operation(summary = "Add sector", description = "The endpoint is used to add sector to parking identified with the given identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The sector was created successfully"),
            @ApiResponse(responseCode = "400", description = "Persistence exception was thrown."),
            @ApiResponse(responseCode = "404", description = "Parking with the given identifier does not exist"),
            @ApiResponse(responseCode = "409", description = "Parking with specified data already exists"),
            @ApiResponse(responseCode = "500", description = "Unexpected exception occurred.")
    })
    ResponseEntity<?> createSector(@PathVariable("id") String parkingId, @RequestBody SectorCreateDTO sectorCreateDTO) throws ApplicationBaseException;

    /**
     * This method is used to find all parking spaces in system, using pagination.
     *
     * @param pageNumber Number of the page, which parking spaces will be retrieved from.
     * @param pageSize   Number of parking spaces per page.
     * @return It returns HTTP response 200 OK with all parking list.
     * It returns HTTP response 204 NO CONTENT when list is empty.
     * It returns HTTP response 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all parking", description = "The endpoint is used retrieve list of parking from given page of given size.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of parking returned from given page of given size is not empty."),
            @ApiResponse(responseCode = "204", description = "List of parking returned from given page of given size is empty."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> getAllParkingWithPagination(@RequestParam("pageNumber") int pageNumber,
                                                  @RequestParam("pageSize") int pageSize) throws ApplicationBaseException;

    /**
     * This method is used to find sector by id.
     *
     * @param id Identifier of sector to find.
     * @return It returns HTTP response 200 OK with sector information if sector exists. If sector with id doesn't exist
     * returns 404. When uuid is invalid returns 400.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/sectors/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getSectorById(@PathVariable("id") String id) throws ApplicationBaseException;

    /**
     * This method is used to find all sectors from parking with a given id.
     * Both active and inactive sectors are returned.
     *
     * @param parkingId Identifier of parking containing the sectors to find.
     * @return It returns HTTP response 200 OK with information about sectors of a given parking. If parking with the
     * given uuid doesn't exist, returns 404. If the uuid has invalid format, returns 400.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/{id}/sectors", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get sectors", description = "The endpoint is used to get sectors from parking identified with the given identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of sectors in the given parking was found successfully"),
            @ApiResponse(responseCode = "204", description = "List of sectors returned from given page of given size is empty"),
            @ApiResponse(responseCode = "400", description = "Invalid format of parking uuid"),
            @ApiResponse(responseCode = "404", description = "Parking with the given uuid does not exist"),
            @ApiResponse(responseCode = "500", description = "Unexpected exception occurred.")
    })
    ResponseEntity<?> getSectorsByParkingId(@PathVariable("id") String parkingId, @RequestParam("pageNumber") int pageSize,
                                            @RequestParam("pageSize") int pageNumber) throws ApplicationBaseException;

    /**
     * This method is used to find parking by id.
     *
     * @param id Identifier of parking to find.
     * @return It returns HTTP response 200 OK with parking information if parking exists. If parking with id doesn't exist
     * returns 404. When uuid is invalid returns 400.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get parking", description = "The endpoint is used retrieve list of parking with given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parking info."),
            @ApiResponse(responseCode = "400", description = "Invalid UUID"),
            @ApiResponse(responseCode = "404", description = "Parking with given id doesn't exist"),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> getParkingById(@PathVariable("id") String id) throws ApplicationBaseException;

    /**
     * This method is used to find active sectors in parking.
     *
     * @param id Identifier of parking.
     * @return It returns HTTP response 200 OK with sectors information if any sector exists. When there's no active sectors
     * return 204 NO CONTENT. If parking with id doesn't exist returns 404. When uuid is invalid returns 400.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/client/sectors/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get parking's active sectors", description = "The endpoint is used retrieve list of parking with given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of active sectors."),
            @ApiResponse(responseCode = "204", description = "No active sectors."),
            @ApiResponse(responseCode = "400", description = "Invalid UUID"),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> getClientSectorByParkingId(@PathVariable("id") String id, @RequestParam("pageNumber") int pageNumber,
                                                 @RequestParam("pageSize") int pageSize) throws ApplicationBaseException;

    /**
     * This method is used to activate a sector with given id.
     *
     * @param id Identifier of sector to activate.
     * @return It returns HTTP response 204 NO_CONTENT when sector is successfully activated. If sector with id doesn't exist
     * returns 404. When uuid is invalid returns 400.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/sectors/{id}/activate")
    @Operation(summary = "Activate sector", description = "The endpoint is used to activate a sector with a given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The account has been activated correctly."),
            @ApiResponse(responseCode = "400", description = "The account has not been activated due to it being already " +
                    "activated or because the sector is not available in the database"),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> activateSector(@PathVariable("id") String id) throws ApplicationBaseException;

    /**
     * This method is used to deactivate a sector with given id.
     *
     * @param id Identifier of sector to deactivate.
     * @param deactivationTimeDTO Data transfer object containing deactivation time for the sector.
     * @return It returns HTTP response 204 NO_CONTENT when the sector is successfully deactivated.
     * When the sector with the provided id doesn't exist,
     * the method returns 400. When the sector still has active parking spots,
     * or it is already deactivated, the method returns 400.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from the facade and service layers below.
     */
    @PostMapping(value = "/sectors/{id}/deactivate", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> deactivateSector(@PathVariable("id") String id, @RequestBody SectorDeactivationTimeDTO deactivationTimeDTO)
            throws ApplicationBaseException;

    /**
     * This method is used to remove parking, that is identified with the given identifier.
     *
     * @param id Identifier of the parking to be removed.
     * @return This method returns 204 NO CONTENT if the parking is removed successfully. Otherwise, if the parking
     * could not be found in the database then 400 BAD REQUEST is returned (when the parking could not be found or
     * parking does have sectors attached). 500 INTERNAL SERVER ERROR is returned
     * when other unexpected exception is encountered while processing the request.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @Operation(summary = "Remove parking", description = "The endpoint is used to remove parking with a given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The parking has been removed successfully."),
            @ApiResponse(responseCode = "400", description = "The parking could not be removed due to existing sectors of a given parking or the parking not existing in the database."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    @DeleteMapping(value = "/{id}")
    ResponseEntity<?> removeParkingById(@PathVariable("id") String id) throws ApplicationBaseException;

    /**
     * This method is used to begin parking spot allocation. Basically, it generates a parking event for entry,
     * which marks the start of the allocation, and then generates the exit code, which will be needed to end the
     * allocation. User may enter chosen parking without previously making a reservation. The spot is then assigned
     * according to the parking's spot assignment algorithm. After choosing the spot inpromptu reservation is created.
     *
     * @param parkingId Identifier of the parking, which the client wants to enter.
     * @return 200 OK response is returned if the allocation is started successfully, body contains exit code and basic
     * info about the assigned sector. Otherwise, if there is no such parking, user account does not exist or reservation could not
     * be started, then 400 BAD REQUEST is returned. 500 INTERNAL SERVER ERROR is returned when other unexpected
     * exception occurs during processing of the request.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/{id}/enter")
    ResponseEntity<?> enterParkingWithoutReservation(@PathVariable("id") String parkingId) throws ApplicationBaseException;

    /**
     * This method is used to begin parking spot allocation. Basically, it generates a parking event for entry,
     * which marks the start of the allocation.
     *
     * @param reservationId Identifier of the reservation, which the client wants to use.
     * @return 204 NO CONTENT response is returned if the allocation is started successfully. Otherwise,
     * if there is no such reservation, user account does not exist or reservation could not
     * be started, then 400 BAD REQUEST is returned. 500 INTERNAL SERVER ERROR is returned when other unexpected
     * exception occurs during processing of the request.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @Operation(summary = "Enter parking with reservation", description = "The endpoint is used to generate entry event for user entering parking with reservation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entry parking event generated and place from sector allocated successfully."),
            @ApiResponse(responseCode = "400", description = "Reservation could not be found or is expired / not started. This response is returned when user is not the owner of the reservation or when there are no available places in the sector."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    @PostMapping(value = "/reservations/{id}/enter")
    ResponseEntity<?> enterParkingWithReservation(@PathVariable("id") String reservationId) throws ApplicationBaseException;

    /**
     * This method is used to edit parking, that is identified with the given identifier.
     *
     * @param ifMatch          Value of If-Match header
     * @param parkingModifyDTO Parking properties with potentially changed values.
     * @return This method returns 204 NO CONTENT if the parking is edited successfully. Otherwise, if the parking
     * could not be found in the database then 400 BAD REQUEST is returned. 500 INTERNAL SERVER ERROR is returned
     * when other unexpected exception is encountered while processing the request.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Edit parking", description = "The endpoint is used to edit parking identified with the given identifier and name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The parking was successfully edited"),
            @ApiResponse(responseCode = "400", description = "The parking could not be edited."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> editParking(@RequestHeader(HttpHeaders.IF_MATCH) String ifMatch,
                                  @Valid @RequestBody ParkingModifyDTO parkingModifyDTO) throws ApplicationBaseException;

    /**
     * This method is used to edit sector, that is identified with the given identifier and name.
     *
     * @param ifMatch         Value of If-Match header
     * @param sectorModifyDTO Sector properties with potentially changed values.
     * @return This method returns 204 NO CONTENT if the sector is edited successfully. Otherwise, if the sector
     * could not be found in the database then 400 BAD REQUEST is returned. 500 INTERNAL SERVER ERROR is returned
     * when other unexpected exception is encountered while processing the request.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PutMapping(value = "/sectors", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Edit sector", description = "The endpoint is used to edit sector identified with the given identifier and name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The sector was successfully edited"),
            @ApiResponse(responseCode = "400", description = "Persistence exception was thrown."),
            @ApiResponse(responseCode = "500", description = "Unexpected exception occurred.")
    })
    ResponseEntity<?> editSector(@RequestHeader(HttpHeaders.IF_MATCH) String ifMatch,
                                 @Valid @RequestBody SectorModifyDTO sectorModifyDTO) throws ApplicationBaseException;

    /**
     * This method is used to remove sector, that is identified with the given identifier.
     *
     * @param id Identifier of the sector to be removed.
     * @return This method returns 204 NO CONTENT if the sector is removed successfully. Otherwise, if the sector
     * could not be found in the database then 400 BAD REQUEST is returned. 500 INTERNAL SERVER ERROR is returned
     * when other unexpected exception is encountered while processing the request.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */

    @Operation(summary = "Remove sector", description = "The endpoint is used to remove sector with a given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The sector has been removed successfully."),
            @ApiResponse(responseCode = "400", description = "The sector could not be remove."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })

    @DeleteMapping(value = "/sectors/{id}")
    ResponseEntity<?> removeSectorById(@PathVariable("id") String id) throws ApplicationBaseException;

    /**
     * This method is used to find all available parking.
     *
     * @param pageNumber Number of the page to retrieve.
     * @param pageSize   Number of results per page.
     * @return It returns HTTP response 200 OK with all available parking if these parking exist.
     * If there are no available parking returns 204.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getAvailableParkingWithPagination(@RequestParam("pageNumber") int pageNumber,
                                                        @RequestParam("pageSize") int pageSize) throws ApplicationBaseException;

    /**
     * This method is used to end the parking spot allocation. Basically, it generates a parking event for exit
     *
     * @param reservationId Identifier of the reservation, which the client wants to use.
     * @return 204 NO CONTENT responses are returned if the allocation ended successfully
     * Otherwise, if there is no such reservation, a user account does not exist,
     * the provided exit code is incorrect or the reservation could not be ended; then 400 BAD REQUEST is returned.
     * 500 INTERNAL SERVER ERROR is returned when another unexpected
     * exception occurs during processing of the request.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from the facade and service layers below.
     */
    @PostMapping(value = "/reservations/{id}/exit")
    ResponseEntity<?> exitParking(@PathVariable("id") String reservationId) throws ApplicationBaseException;
}

