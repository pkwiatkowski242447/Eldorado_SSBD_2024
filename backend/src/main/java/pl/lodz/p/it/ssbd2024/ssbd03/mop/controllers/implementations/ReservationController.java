package pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.interfaces.ReservationControllerInterface;

/**
 * Controller used for manipulating reservations and parking events in the system.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController implements ReservationControllerInterface {
}
