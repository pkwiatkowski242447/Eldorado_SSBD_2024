package pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.implementations;

import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.interfaces.ParkingControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;


/**
 * Controller used for manipulating parking in the system.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/parking")
public class ParkingController implements ParkingControllerInterface {

    @Override
    @RolesAllowed({Roles.ANONYMOUS, Roles.CLIENT, Roles.STAFF})
    public ResponseEntity<?> getSectorById(String id) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed({Roles.STAFF})
    public ResponseEntity<?> removeParkingById(String id) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed({Roles.CLIENT})
    public ResponseEntity<?> enterParkingWithReservation(String reservationId) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed({Roles.ANONYMOUS, Roles.CLIENT})
    public ResponseEntity<?> getAvailableParkingsWithPagination (int pageNumber, int pageSize) throws ApplicationBaseException {
         throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }
}
