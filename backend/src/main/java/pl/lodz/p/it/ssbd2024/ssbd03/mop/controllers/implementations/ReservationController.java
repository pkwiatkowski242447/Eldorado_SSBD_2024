package pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.implementations;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.RollbackException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.reservationDTO.ReservationParkingEventListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.reservationDTO.UserReservationOutputListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop.UserHistoricalReservationListMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.reservationDTO.MakeReservationDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.reservationDTO.ReservationOutputListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop.ReservationListMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop.UserActiveReservationListMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingEvent;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationDatabaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationClientLimitException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationNoAvailablePlaceException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.InvalidDataFormatException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.controllers.interfaces.ReservationControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces.ParkingServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces.ReservationServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

import java.util.List;
import java.util.UUID;

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

    private final ReservationServiceInterface reservationService;

    @Autowired
    public ReservationController(ReservationServiceInterface reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    @RolesAllowed(Authorities.GET_ACTIVE_RESERVATIONS)
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class})
    public ResponseEntity<?> getAllActiveReservationSelf(int pageNumber, int pageSize) throws ApplicationBaseException {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserReservationOutputListDTO> reservationList = reservationService.getAllActiveReservationsByUserLoginWthPagination(login, pageNumber, pageSize)
                .stream()
                .map(UserActiveReservationListMapper::toSectorListDTO)
                .toList();
        if (reservationList.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(reservationList);
    }

    @Override
    @RolesAllowed(Authorities.GET_HISTORICAL_RESERVATIONS)
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class})
    public ResponseEntity<?> getAllHistoricalReservationSelf(int pageNumber, int pageSize) throws ApplicationBaseException {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserReservationOutputListDTO> reservationList = reservationService.getAllHistoricalReservationsByUserIdWthPagination(login, pageNumber, pageSize)
                .stream()
                .map(UserHistoricalReservationListMapper::toSectorListDTO)
                .toList();
        if (reservationList.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(reservationList);
    }

    @Override
    @RolesAllowed({Authorities.RESERVE_PARKING_PLACE, Authorities.DELETE_PARKING})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class,
                    ReservationNoAvailablePlaceException.class, ReservationClientLimitException.class})
    public ResponseEntity<?> makeReservation(@Valid MakeReservationDTO makeReservationDTO) throws ApplicationBaseException {
        //TODO future test Retryable

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        reservationService.makeReservation(
                login,
                makeReservationDTO.getSectorId(),
                makeReservationDTO.getBeginTime(),
                makeReservationDTO.getEndTime()
        );

        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed(Authorities.CANCEL_RESERVATION)
    public ResponseEntity<?> cancelReservation(String id) throws ApplicationBaseException {
        try {
            reservationService.cancelReservation(UUID.fromString(id));
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed({Authorities.GET_ALL_RESERVATIONS})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class})
    public ResponseEntity<?> getAllReservations(int pageNumber, int pageSize) throws ApplicationBaseException {
        List<ReservationOutputListDTO> reservationList = reservationService.getAllReservations(pageNumber, pageSize)
                .stream()
                .map(ReservationListMapper::toReservationListDTO)
                .toList();
        if (reservationList.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(reservationList);
    }

    @Override
    @RolesAllowed({Authorities.GET_OWN_RESERVATION_DETAILS})
    public ResponseEntity<?> getOwnReservationDetails(String reservationId, int pageNumber, int pageSize) throws ApplicationBaseException {
        try {
            String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();
            Reservation reservation = this.reservationService.getReservationById(UUID.fromString(reservationId), userLogin);
            List<ParkingEvent> listOfParkingEvents = this.reservationService.getParkingEventsForGivenReservation(UUID.fromString(reservationId), pageNumber, pageSize);
            ReservationParkingEventListDTO reservationDetailsDTO = ReservationListMapper.toReservationParkingEventListDTO(reservation, listOfParkingEvents);
            return ResponseEntity.ok(reservationDetailsDTO);
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }
    }
}
