package pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.interfaces;

import org.springframework.http.ResponseEntity;
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
    ResponseEntity<?> getSectorById(String id) throws ApplicationBaseException;
}
