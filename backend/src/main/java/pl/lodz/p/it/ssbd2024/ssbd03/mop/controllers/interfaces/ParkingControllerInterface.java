package pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.interfaces;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO.AccountModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.ParkingModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Interface used for managing Parking and Sectors
 */
public interface ParkingControllerInterface {

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
    @PostMapping(value = "/reservation/{id}/enter")
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
     * exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/parking")
    ResponseEntity<?> editParking(@RequestHeader(HttpHeaders.IF_MATCH) String ifMatch,
                                  @Valid @RequestBody ParkingModifyDTO parkingModifyDTO) throws ApplicationBaseException;


}

