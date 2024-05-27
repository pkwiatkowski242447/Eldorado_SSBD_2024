package pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.interfaces;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO.AccountRegisterDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingCreateDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Interface used for managing Parking and Sectors
 */
public interface ParkingControllerInterface {

    /***
     * This method is used to create new system.
     *
     * @param parkingCreateDTO Data transfer object, containing parking data.
     * @return It returns HTTP response 204 NO CONTENT when parking creation is successful,
     *         It returns HTTP response 400 BAD REQUEST when persistence exception is being thrown.
     *         It returns HTTP response 409 CONFLICT when parking with specific data exits.
     *         It returns HTTP response 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    @PostMapping
    ResponseEntity<?> createParking(@RequestBody ParkingCreateDTO parkingCreateDTO) throws ApplicationBaseException;

    /**
     * This method is used to find sector by id.
     *
     * @param id Identifier of sector to find.
     * @return It returns HTTP response 200 OK with sector information if sector exists. If sector with id doesn't exist
     * returns 404. When uuid is invalid returns 400.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/sectors/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getSectorById(@PathVariable("id") String id) throws ApplicationBaseException;

    /**
     * This method is used to find all sectors from parking with a given id.
     *
     * @param id Identifier of parking containing the sectors to find.
     * @return It returns HTTP response 200 OK with information about sectors of a given parking. If parking with the
     * given uuid doesn't exist, returns 404. If the uuid has invalid format, returns 400.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/parking/{id}/sectors", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getSectorsByParkingId(@PathVariable("id") String id) throws ApplicationBaseException;

    /**
     * This method is used to find parking by id.
     *
     * @param id Identifier of parking to find.
     * @return It returns HTTP response 200 OK with parking information if sector exists. If parking with id doesn't exist
     * returns 404. When uuid is invalid returns 400.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/parking/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getParkingById(@PathVariable("id") String id) throws ApplicationBaseException;

    /**
     * This method is used to activate a sector with given id.
     *
     * @param id Identifier of sector to activate.
     * @return It returns HTTP response 204 NO_CONTENT when sector is successfully activated. If sector with id doesn't exist
     * returns 404. When uuid is invalid returns 400.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/sectors/{id}/activate", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> activateSector(@PathVariable("id") String id) throws ApplicationBaseException;

    /**
     * This method is used to remove parking, that is identified with the given identifier.
     *
     * @param id Identifier of the parking to be removed.
     * @return This method returns 204 NO CONTENT if the parking is removed successfully. Otherwise, if the parking
     * could not be found in the database then 400 BAD REQUEST is returned. 500 INTERNAL SERVER ERROR is returned
     * when other unexpected exception is encountered while processing the request.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    @DeleteMapping(value = "/parking/{id}")
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
     * exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/{id}/enter")
    ResponseEntity<?> enterParkingWithoutReservation(@PathVariable("id") String parkingId) throws ApplicationBaseException;

    /**
     * This method is used to begin parking spot allocation. Basically, it generates a parking event for entry,
     * which marks the start of the allocation, and then generates the exit code, which will be needed to end the
     * allocation.
     *
     * @param reservationId Identifier of the reservation, which the client wants to use.
     * @return 200 OK response is returned if the allocation is started successfully, and the code if returned in the
     * response body. Otherwise, if there is no such reservation, user account does not exist or reservation could not
     * be started, then 400 BAD REQUEST is returned. 500 INTERNAL SERVER ERROR is returned when other unexpected
     * exception occurs during processing of the request.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/reservations/{id}/enter")
    ResponseEntity<?> enterParkingWithReservation(@PathVariable("id") String reservationId) throws ApplicationBaseException;
}
