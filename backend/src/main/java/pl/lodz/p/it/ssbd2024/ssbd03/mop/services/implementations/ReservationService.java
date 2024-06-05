package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.implementations;

import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingEvent;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ParkingEventFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ReservationFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces.ReservationServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

import java.util.List;
import java.util.UUID;

/**
 * Service managing Reservations and Parking Events.
 *
 * @see Reservation
 * @see ParkingEvent
 */
@Slf4j
@Service
@LoggerInterceptor
@TxTracked
@Transactional(propagation = Propagation.MANDATORY)
public class ReservationService implements ReservationServiceInterface {

    private final ReservationFacade reservationFacade;
    private final ParkingEventFacade parkingEventFacade;

    @Autowired
    public ReservationService(ReservationFacade reservationFacade, ParkingEventFacade parkingEventFacade) {
        this.reservationFacade = reservationFacade;
        this.parkingEventFacade = parkingEventFacade;
    }

    @Override
    @RolesAllowed(Authorities.GET_ACTIVE_RESERVATIONS)
    public List<Reservation> getAllActiveReservationsByUserIdWthPagination(UUID id, int pageNumber, int pageSize) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.GET_HISTORICAL_RESERVATIONS)
    public List<Reservation> getAllHistoricalReservationsByUserIdWthPagination(String login, int pageNumber, int pageSize) throws ApplicationBaseException {
        return reservationFacade.findAllHistoricalUserReservationByLoginWithPagination(login, pageNumber, pageSize);
    }

    @Override
    @RolesAllowed(Authorities.RESERVE_PARKING_PLACE)
    public void makeReservation(String clientLogin, UUID sectorId) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.CANCEL_RESERVATION)
    public void cancelReservation(UUID reservationId) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.GET_ALL_RESERVATIONS)
    public List<Reservation> getAllReservations(int pageNumber, int pageSize) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

}
