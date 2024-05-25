package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.implementations;

import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingEvent;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces.ReservationServiceInterface;

import java.util.UUID;

/**
 * Service managing Reservations and Parking Events.
 *
 * @see Reservation
 * @see ParkingEvent
 */
@Slf4j
@Service
@TxTracked
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ReservationService implements ReservationServiceInterface {

    @Override
    @RolesAllowed(Roles.CLIENT)
    public void makeReservation(String clientLogin, UUID sectorId) throws ApplicationBaseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
