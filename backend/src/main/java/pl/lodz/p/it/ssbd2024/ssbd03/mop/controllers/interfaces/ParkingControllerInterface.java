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
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO.SectorModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Interface used for managing Parking and Sectors
 */
public interface ParkingControllerInterface {

    /***
     * This method is used to create a new parking in the system.
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
    ResponseEntity<?> createSector(@PathVariable("id") String parkingId,@Valid @RequestBody SectorCreateDTO sectorCreateDTO) throws ApplicationBaseException;

    /**
     * This method is used to find all parking in the system, using pagination.
     *
     * @param pageNumber Number of the page, which parking will be retrieved from.
     * @param pageSize   Number of parking per page.
     * @return It returns HTTP response 200 OK with all parking list. Otherwise, if the list of parking is empty
     * then 204 NO CONTENT is returned. 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
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
                                                  @RequestParam("pageSize") int pageSize)
            throws ApplicationBaseException;

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
    @Operation(summary = "Get sector by id", description = "The endpoint is used retrieve sector in order to edit it later.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The sector was found and returned correctly."),
            @ApiResponse(responseCode = "400", description = "The format of the identifier of the sector is invalid."),
            @ApiResponse(responseCode = "404", description = "Sector with given id could not be found in the database."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> getSectorById(@PathVariable("id") String id) throws ApplicationBaseException;

    /**
     * This method is used to find all sectors from parking with a given id.
     * Both active and inactive sectors are returned.
     *
     * @param parkingId Identifier of parking containing the sectors to find.
     * @return It returns HTTP response 200 OK with information about sectors of a given parking. If parking with the
     * given uuid doesn't exist, returns 404. If the uuid has invalid format, returns 400.
     * 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/{id}/sectors", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get sectors", description = "The endpoint is used to get sectors from parking identified with the given identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of sectors in the given parking was found successfully"),
            @ApiResponse(responseCode = "204", description = "List of sectors returned from given page of given size is empty"),
            @ApiResponse(responseCode = "400", description = "Invalid format of parking uuid or parking with given identifier could not be found"),
            @ApiResponse(responseCode = "500", description = "Unexpected exception occurred.")
    })
    ResponseEntity<?> getSectorsByParkingId(@PathVariable("id") String parkingId,
                                            @RequestParam("pageNumber") int pageSize,
                                            @RequestParam("pageSize") int pageNumber)
            throws ApplicationBaseException;

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
     * return 204 NO CONTENT. When uuid is invalid returns 400.
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
     * @return It returns HTTP response 204 NO CONTENT when sector is successfully activated. If sector with given
     * id doesn't exist or when uuid is invalid returns 400. 500 INTERNAL SERVER ERROR is returned
     * when other unexpected exception is encountered while processing the request.
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
     * @return It returns HTTP response 204 NO_CONTENT when the sector is successfully deactivated.
     * When the sector with the provided id doesn't exist, the method returns 400. When the sector is already deactivated,
     * the method returns 400. 500 INTERNAL SERVER ERROR is returned when other unexpected exception is
     * encountered while processing the request.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from the facade and service layers below.
     */
    @PostMapping(value = "/sectors/{id}/deactivate", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Deactivate sector", description = "The endpoint is used to deactivate a sector with a given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The account has been deactivated correctly."),
            @ApiResponse(responseCode = "400", description = "The account has not been deactivated due to it being already inactive or because the sector is not in the database."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> deactivateSector(@PathVariable("id") String id) throws ApplicationBaseException;

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
    ResponseEntity<?> removeParkingById(@PathVariable("id") String id)
            throws ApplicationBaseException;

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
    @PostMapping(value = "/reservations/{id}/enter")
    @Operation(summary = "Enter parking with reservation", description = "The endpoint is used to generate entry event for user entering parking with reservation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entry parking event generated and place from sector allocated successfully."),
            @ApiResponse(responseCode = "400", description = "Reservation could not be found or is expired / not started. This response is returned when user is not the owner of the reservation or when there are no available places in the sector."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> enterParkingWithReservation(@PathVariable("id") String reservationId) throws ApplicationBaseException;

    /**
     * This method is used to edit parking.
     *
     * @param ifMatch          Value of If-Match header
     * @param parkingModifyDTO Parking properties with potentially changed values.
     * @return This method returns 204 NO CONTENT if the parking is edited successfully. Otherwise, if the parking
     * could not be found in the database then 400 BAD REQUEST is returned. In the situation where either id or version
     * fields change, then HTTP response with 409 CONFLICT is returned. 500 INTERNAL SERVER ERROR is returned
     * when other unexpected exception occurs while processing the request.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Edit parking", description = "The endpoint is used to edit parking identified with the given identifier and name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The parking was successfully edited"),
            @ApiResponse(responseCode = "400", description = "The parking could not be edited"),
            @ApiResponse(responseCode = "409", description = "The parking could not be edited, since object id or version changed"),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed")
    })
    ResponseEntity<?> editParking(@RequestHeader(HttpHeaders.IF_MATCH) String ifMatch,
                                  @Valid @RequestBody ParkingModifyDTO parkingModifyDTO)
            throws ApplicationBaseException;

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
    @PutMapping(value = "/sectors", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
            @ApiResponse(responseCode = "400", description = "The sector could not be removed."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    @DeleteMapping(value = "/sectors/{id}")
    ResponseEntity<?> removeSectorById(@PathVariable("id") String id)
            throws ApplicationBaseException;

    /**
     * This method is used to find all available parking, which is a parking where at least one
     * sector is still active.
     *
     * @param pageNumber Number of the page to retrieve.
     * @param pageSize   Number of results per page.
     * @return It returns HTTP response 200 OK with all available parking if these parking exist.
     * If there are no available parking returns 204. 500 INTERNAL SERVER ERROR is returned
     * when other unexpected exception is encountered while processing the request.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all active parking", description = "The endpoint is used to retrieve all parking with at least one active sector.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of active parking was found and returned successfully."),
            @ApiResponse(responseCode = "400", description = "There are no active parking in the database."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
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
    @Operation(summary = "Exit parking", description = "The endpoint is used to register exit event for reservation object, and possibly end the reservation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Parking exit event was registered successfully and reservation was finished (if flag was set to true)."),
            @ApiResponse(responseCode = "400", description = "There is no reservation with given id, or that the currently logged in user is the owner of. Parking exit event could not be registered."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> exitParking(@PathVariable("id") String reservationId) throws ApplicationBaseException;
}
