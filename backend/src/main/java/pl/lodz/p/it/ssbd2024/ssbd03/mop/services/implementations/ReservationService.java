package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.implementations;

import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.reservationDTO.MakeReservationDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingEvent;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationClientAccountNonEnabledException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationClientLimitException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationClientUserLevelNotFound;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationNoAvailablePlaceException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationSectorNonActiveException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.read.SectorNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ParkingEventFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ParkingFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ReservationFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.UserLevelMOPFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces.ReservationServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

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
    private final ParkingEventFacade parkingEventFacade;
    private final UserLevelMOPFacade userLevelMOPFacade;
    private final ParkingFacade parkingFacade;

    @Value("${reservation.client_limit}")
    private Integer clientLimit;

    @Value("${reservation.max_hours}")
    private Integer reservationMaxHours;

    @Autowired
    public ReservationService(ReservationFacade reservationFacade,
                              ParkingEventFacade parkingEventFacade,
                              UserLevelMOPFacade userLevelMOPFacade,
                              ParkingFacade parkingFacade) {
        this.reservationFacade = reservationFacade;
        this.parkingEventFacade = parkingEventFacade;
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
        if (reservationFacade.countAllActiveUserReservationByLogin(clientLogin) + 1 > clientLimit) throw new ReservationClientLimitException();

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
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.GET_ALL_RESERVATIONS)
    public List<Reservation> getAllReservations(int pageNumber, int pageSize) throws ApplicationBaseException {
        return reservationFacade.findAllWithPagination(pageNumber, pageSize);
    }

}
