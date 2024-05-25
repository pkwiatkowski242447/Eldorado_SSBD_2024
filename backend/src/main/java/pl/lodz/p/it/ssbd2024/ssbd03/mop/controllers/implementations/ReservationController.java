package pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.implementations;

import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.MakeReservationDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.interfaces.ReservationControllerInterface;

/**
 * Controller used for manipulating reservations and parking events in the system.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController implements ReservationControllerInterface {

    @Override
    @RolesAllowed(Roles.CLIENT)
    public ResponseEntity<?> makeReservation(@RequestBody MakeReservationDTO makeReservationDTO) throws ApplicationBaseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
