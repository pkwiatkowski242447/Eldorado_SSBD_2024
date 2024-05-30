package pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.implementations;

import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.MakeReservationDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.interfaces.ReservationControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Controller used for manipulating reservations and parking events in the system.
 */
@Slf4j
@RestController
@LoggerInterceptor
@RequestMapping("/api/v1/reservations")
@TxTracked
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationBaseException.class)
public class ReservationController implements ReservationControllerInterface {

    @Override
    @RolesAllowed(Authorities.GET_ACTIVE_RESERVATIONS)
    public ResponseEntity<?> getAllActiveReservationSelf(int pageNumber, int pageSize) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.GET_HISTORICAL_RESERVATIONS)
    public ResponseEntity<?> getAllHistoricalReservationSelf(int pageNumber, int pageSize) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.RESERVE_PARKING_PLACE)
    public ResponseEntity<?> makeReservation(MakeReservationDTO makeReservationDTO) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.CANCEL_RESERVATION)
    public ResponseEntity<?> cancelReservation(String id) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.GET_ALL_RESERVATIONS)
    public ResponseEntity<?> getAllReservations(int pageNumber, int pageSize) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }
}
