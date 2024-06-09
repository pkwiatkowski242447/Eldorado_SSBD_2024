package pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.reservationDTO.MakeReservationDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Interface used for managing Reservations and Parking Events
 */
public interface ReservationControllerInterface {


    /**
     * This method is used to find all active reservations in system, using pagination.
     *
     * @param pageNumber Number of the page, which reservations will be retrieved from.
     * @param pageSize   Number of reservations per page.
     * @return It returns HTTP response 200 OK with all active reservation list.
     *         It returns HTTP response 204 NO CONTENT when list is empty.
     *         It returns HTTP response 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/active/self", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get your own active reservation", description = "The endpoint is used to get user's all active reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of reservations returned from given page of given size is not empty."),
            @ApiResponse(responseCode = "204", description = "List of reservations returned from given page of given size is empty."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> getAllActiveReservationSelf(@RequestParam("pageNumber") int pageNumber,
                                    @RequestParam("pageSize") int pageSize) throws ApplicationBaseException;

    /**
     * This method is used to find all historical reservations in system, using pagination.
     *
     * @param pageNumber Number of the page, which reservations will be retrieved from.
     * @param pageSize   Number of reservations per page.
     * @return It returns HTTP response 200 OK with all historical reservation list.
     *         It returns HTTP response 204 NO CONTENT when list is empty.
     *         It returns HTTP response 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/historical/self", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get your own historical reservation", description = "The endpoint is used to get user's all historical reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of reservations returned from given page of given size is not empty."),
            @ApiResponse(responseCode = "204", description = "List of reservations returned from given page of given size is empty."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> getAllHistoricalReservationSelf(@RequestParam("pageNumber") int pageNumber,
                                    @RequestParam("pageSize") int pageSize) throws ApplicationBaseException;

    /**
     * This endpoint allows the client to reserve a place in the chosen sector.
     *
     * @param makeReservationDTO Data transfer object, containing the chosen sector id, the start time and
     *                           the end time of the reservation.
     * @return If reservation making is successful, then 204 NO CONTENT is returned as a response.
     * If invalid or erroneous data is passed, 400 BAD REQUEST is returned. Unexpected errors result in 500 INTERNAL SERVER ERROR.
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     *                                  layer of facade and service components in the application.
     */
    @PostMapping(value = "/make-reservation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Reserve a parking place", description = "The endpoint is used to reserve a place in selected Sector.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The reservation has been created successfully."),
            @ApiResponse(responseCode = "400", description = "The reservation has not been created due to the correctness of the request " +
                    "or sector blockage, including insufficient number of sector's places"),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> makeReservation(@RequestBody MakeReservationDTO makeReservationDTO) throws ApplicationBaseException;

    /**
     * This endpoint allows to cancel active parking place reservation by identifier.
     *
     * @return If reservation is cancelled successful, then 204 NO CONTENT is returned as a response.
     * If invalid UUID is passed 400 BAD REQUEST is returned. If reservation with id doesn't exist returns 404.
     * Unexpected errors result in 500 INTERNAL SERVER ERROR.
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     *                                  layer of facade and service components in the application.
     */
    @Operation(summary = "Cancel an active reservation", description = "The endpoint is used to cancel an active reservation by its owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The reservation has been cancelled successfully."),
            @ApiResponse(responseCode = "400", description = "The reservation has not been cancelled due to the correctness of the request"),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    @DeleteMapping(value = "/cancel-reservation/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> cancelReservation(@PathVariable("id") String id) throws ApplicationBaseException;

    /**
     * This endpoint allows retrieving all reservations in the system.
     * To avoid sending too much data at once, the results are paginated.
     *
     * @param pageNumber Number of the page, which reservations will be retrieved from.
     * @param pageSize   Number of reservations per page.
     * @return This method returns 200 OK as a response, where in response body a list of reservations is a JSON format.
     * If the list is empty (there are no reservations for in the system),
     * this method would return 204 NO CONTENT as the response.
     * 500 INTERNAL SERVER ERROR is returned when another unexpected exception occurs.
     * @throws ApplicationBaseException Superclass for any application exception
     * thrown by exception handling aspects in the layer of facade and service components in the application.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getAllReservations(@RequestParam("pageNumber") int pageNumber,
                                         @RequestParam("pageSize") int pageSize) throws ApplicationBaseException;
}
