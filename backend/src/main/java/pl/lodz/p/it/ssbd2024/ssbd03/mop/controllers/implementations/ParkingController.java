package pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.implementations;

import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.interfaces.ParkingControllerInterface;

/**
 * Controller used for manipulating parking in the system.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/parking")
public class ParkingController implements ParkingControllerInterface {

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
    @RolesAllowed({Roles.ANONYMOUS, Roles.CLIENT, Roles.STAFF})
    @Override
    public ResponseEntity<?> getSectorById(@PathVariable("id") String id) throws ApplicationBaseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
