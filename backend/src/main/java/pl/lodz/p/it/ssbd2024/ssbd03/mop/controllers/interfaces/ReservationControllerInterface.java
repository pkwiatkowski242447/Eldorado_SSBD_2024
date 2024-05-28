package pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.interfaces;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.MakeReservationDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Interface used for managing Reservations and Parking Events
 */
public interface ReservationControllerInterface {

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
    @DeleteMapping(value = "/cancel-reservation/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> cancelReservation(@PathVariable("id") String id) throws ApplicationBaseException;

    /**
     * This endpoint allows retrieving all reservations for a user in the system. In order to avoid sending too much data at once, the results are paginated.
     *
     * @param pageNumber Number of the page, which reservations will be retrieved from.
     * @param pageSize   Number of reservations per page.
     * @return This method returns 200 OK as a response, where in response body a list of user's reservations is a JSON format.
     * If the list is empty (there are no reservations for the user in the system),
     * this method would return 204 NO CONTENT as the response.
     * 500 INTERNAL SERVER ERROR is returned when another unexpected exception occurs.
     * @throws ApplicationBaseException Superclass for any application exception
     * thrown by exception handling aspects in the layer of facade and service components in the application.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getAllReservations(@RequestParam("pageNumber") int pageNumber,
                                         @RequestParam("pageSize") int pageSize) throws ApplicationBaseException;
}
