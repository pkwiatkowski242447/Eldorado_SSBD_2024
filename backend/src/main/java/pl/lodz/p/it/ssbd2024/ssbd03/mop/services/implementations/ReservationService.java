package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.implementations;

import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingEvent;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.read.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationCancellationLateAttempt;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationClientAccountNonEnabledException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationClientLimitException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationClientUserLevelNotFound;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationNoAvailablePlaceException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationSectorNonActiveException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.read.ReservationNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.status.ReservationAlreadyCancelledException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.read.SectorNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.*;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces.ReservationServiceInterface;

import java.time.LocalDateTime;
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
    private final AccountMOPFacade accountFacade;
    private final UserLevelMOPFacade userLevelMOPFacade;
    private final ParkingFacade parkingFacade;

    @Value("${reservation.client_limit}")
    private Integer clientLimit;

    @Value("${reservation.max_hours}")
    private Integer reservationMaxHours;

    @Value("${reservation.cancellation.max_hours}")
    private Long cancellationMaxHoursBeforeReservation;

    @Autowired
    public ReservationService(ReservationFacade reservationFacade,
                              AccountMOPFacade accountFacade,
                              UserLevelMOPFacade userLevelMOPFacade,
                              ParkingFacade parkingFacade) {
        this.reservationFacade = reservationFacade;
        this.accountFacade = accountFacade;
        this.userLevelMOPFacade = userLevelMOPFacade;
        this.parkingFacade = parkingFacade;
    }

    @Override
    @RolesAllowed(Authorities.GET_ACTIVE_RESERVATIONS)
    public List<Reservation> getAllActiveReservationsByUserLoginWthPagination(String login, int pageNumber, int pageSize) throws ApplicationBaseException {
        return reservationFacade.findAllActiveUserReservationByLoginWithPagination(login, pageNumber, pageSize);
    }

    @Override
    @RolesAllowed(Authorities.GET_HISTORICAL_RESERVATIONS)
    public List<Reservation> getAllHistoricalReservationsByUserIdWthPagination(String login, int pageNumber, int pageSize) throws ApplicationBaseException {
        return reservationFacade.findAllHistoricalUserReservationByLoginWithPagination(login, pageNumber, pageSize);
    }

    @Override
    @RolesAllowed({Authorities.RESERVE_PARKING_PLACE, Authorities.DELETE_PARKING})
    public void makeReservation(String clientLogin, UUID sectorId, LocalDateTime beginTime, LocalDateTime endTime) throws ApplicationBaseException {
        // Obtaining Client
        Client client = userLevelMOPFacade.findGivenUserLevelForGivenAccount(clientLogin).orElseThrow(ReservationClientUserLevelNotFound::new);

        // Check client availability
        if (!client.getAccount().isEnabled()) throw new ReservationClientAccountNonEnabledException();

        // Check client reservations limit
        if (reservationFacade.countAllActiveUserReservationByLogin(clientLogin) + 1 > clientLimit)
            throw new ReservationClientLimitException();

        // Obtaining Sector
        Sector sector = parkingFacade.findAndRefreshSectorById(sectorId).orElseThrow(SectorNotFoundException::new);

        // Check sector availability
        if (!sector.getActive()) throw new ReservationSectorNonActiveException();

        // Check sector place availability
        long numOfPlacesTaken = reservationFacade.countAllSectorReservationInTimeframe(
                sectorId,
                beginTime,
                reservationMaxHours,
                LocalDateTime.now()
        );
        if (sector.getMaxPlaces() < numOfPlacesTaken + 1) throw new ReservationNoAvailablePlaceException();

        // Create reservation
        Reservation newReservation = new Reservation(client, sector, beginTime);
        newReservation.setEndTime(endTime);

        //TODO future check
        parkingFacade.forceVersionUpdate(sector);

        reservationFacade.create(newReservation);
    }

    @Override
    @RolesAllowed(Authorities.CANCEL_RESERVATION)
    public void cancelReservation(UUID reservationId) throws ApplicationBaseException {
        Reservation reservation = reservationFacade.findClientReservation(
                reservationId,
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()
        ).orElseThrow(ReservationNotFoundException::new);

        if (reservation.getStatus() == Reservation.ReservationStatus.CANCELLED) throw new ReservationAlreadyCancelledException();

        if (reservation.getBeginTime().minusHours(cancellationMaxHoursBeforeReservation).isBefore(LocalDateTime.now())) {
            throw new ReservationCancellationLateAttempt();
        }

        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        reservationFacade.edit(reservation);
    }

    @Override
    @RolesAllowed({Authorities.GET_ALL_RESERVATIONS})
    public List<Reservation> getAllReservations(int pageNumber, int pageSize) throws ApplicationBaseException {
        return reservationFacade.findAllWithPagination(pageNumber, pageSize);
    }

    @Override
    @RolesAllowed({Authorities.GET_OWN_RESERVATION_DETAILS})
    public Reservation getOwnReservationById(UUID reservationId, String userLogin) throws ApplicationBaseException {
        Reservation reservation = this.reservationFacade.findAndRefresh(reservationId).orElseThrow(ReservationNotFoundException::new);
        Account account = this.accountFacade.findByLogin(userLogin).orElseThrow(AccountNotFoundException::new);
        if(!account.getUserLevels().contains(reservation.getClient())) throw new ReservationNotFoundException();
        return reservation;
    }

    @Override
    @RolesAllowed({Authorities.GET_ANY_RESERVATION_DETAILS})
    public Reservation getAnyReservationById(UUID reservationId) throws ApplicationBaseException {
        return this.reservationFacade.findAndRefresh(reservationId).orElseThrow(ReservationNotFoundException::new);
    }

    @Override
    @RolesAllowed({Authorities.GET_OWN_RESERVATION_DETAILS, Authorities.GET_ANY_RESERVATION_DETAILS})
    public List<ParkingEvent> getParkingEventsForGivenReservation(UUID reservationId, int pageNumber, int pageSize) throws ApplicationBaseException {
        return this.reservationFacade.findParkingEventsForGivenReservationWithPagination(reservationId, pageNumber, pageSize);
    }
}
