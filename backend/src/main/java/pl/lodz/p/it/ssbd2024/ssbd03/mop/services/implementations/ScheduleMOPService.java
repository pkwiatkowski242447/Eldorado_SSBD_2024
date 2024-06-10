package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.implementations;

import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.util.RunAsSystem;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingEvent;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ParkingEventFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ParkingFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ReservationFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.UserLevelMOPFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces.ScheduleMOPServiceInterface;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Service managing execution of scheduled tasks.
 * Configuration concerning tasks is set in consts.properties.
 */
@Slf4j
@Service
@Profile("!test")
@LoggerInterceptor
@TxTracked
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ScheduleMOPService implements ScheduleMOPServiceInterface {

    /**
     * String value that specifies time after which ending will occur.
     * Ending time is specified by <code>scheduler.maximum_reservation_time</code> property.
     */
    @Value("${scheduler.maximum_reservation_time}")
    private String endTime;
    private final ReservationFacade reservationFacade;
    private final ParkingEventFacade parkingEventFacade;
    private final UserLevelMOPFacade userLevelFacade;
    private final ParkingFacade parkingFacade;

    @Autowired
    public ScheduleMOPService(ReservationFacade reservationFacade,
                              UserLevelMOPFacade userLevelFacade,
                              ParkingEventFacade parkingEventFacade,
                              ParkingFacade parkingFacade) {
        this.reservationFacade = reservationFacade;
        this.userLevelFacade = userLevelFacade;
        this.parkingEventFacade = parkingEventFacade;
        this.parkingFacade = parkingFacade;
    }

    @RunAsSystem
    @Override
    @RolesAllowed({Authorities.END_RESERVATION})
    @Scheduled(fixedRate = 5L, timeUnit = TimeUnit.MINUTES, initialDelay = 5L)
    public void terminateReservation() {
        log.info("Method: endReservation(), used for terminating reservations which last more than scheduler.maximum_reservation_time value");
        List<Reservation> reservationsWhichLastMoreThan24h = new ArrayList<>();
        try {
            reservationsWhichLastMoreThan24h = reservationFacade.findAllReservationsMarkedForTerminating(Long.parseLong(endTime), TimeUnit.HOURS);
        } catch (NumberFormatException | ApplicationBaseException exception) {
            log.error("Exception: {} occurred while searching for reservation to be terminated. Cause: {}.",
                    exception.getClass().getSimpleName(), exception.getMessage());
        }

        if (reservationsWhichLastMoreThan24h.isEmpty()) {
            log.info("No reservations to be ended were found.");
            return;
        }

        log.info("List of identifiers of reservations to be ended: {}", reservationsWhichLastMoreThan24h.stream().map(Reservation::getId).toList());

        for (Reservation reservation : reservationsWhichLastMoreThan24h) {
            try {
                ParkingEvent exitEvent = new ParkingEvent(LocalDateTime.now(), ParkingEvent.EventType.EXIT);
                reservation.addParkingEvent(exitEvent);
                reservation.setStatus(Reservation.ReservationStatus.TERMINATED);
                this.reservationFacade.edit(reservation);
                Sector sector = reservation.getSector();
                sector.setAvailablePlaces(sector.getAvailablePlaces() + 1);
                parkingFacade.editSector(sector);
            } catch (Exception exception) {
                log.error("Exception: {} occurred while terminating reservation with id: {}. Cause: {}.",
                        exception.getClass().getSimpleName(), reservation.getId(), exception.getMessage());
            }
        }
    }

    @RunAsSystem
    @Override
    @RolesAllowed({Authorities.END_RESERVATION})
    @Scheduled(fixedRate = 5L, timeUnit = TimeUnit.MINUTES, initialDelay = 5L)
    public void completeReservation() {
        log.info("Method: completeReservation(), used for completing reservations");
        List<Reservation> reservationsToEnd = new ArrayList<>();
        try {
            reservationsToEnd = reservationFacade.findAllReservationsMarkedForCompleting();
        } catch (ApplicationBaseException exception) {
            log.error("Exception: {} occurred while searching for reservation to be completed. Cause: {}.",
                    exception.getClass().getSimpleName(), exception.getMessage());
        }

        if (reservationsToEnd.isEmpty()) {
            log.info("No reservations to be completed were found.");
            return;
        }

        log.info("List of identifiers of reservations to be ended: {}", reservationsToEnd.stream().map(Reservation::getId).toList());

        for (Reservation reservation : reservationsToEnd) {
            try {
                reservation.setStatus(Reservation.ReservationStatus.COMPLETED_AUTOMATICALLY);
                this.reservationFacade.edit(reservation);
                Sector sector = reservation.getSector();
                sector.setAvailablePlaces(sector.getAvailablePlaces() + 1);
                parkingFacade.editSector(sector);
            } catch (Exception exception) {
                log.error("Exception: {} occurred while canceling reservation with id: {}. Cause: {}.",
                        exception.getClass().getSimpleName(), reservation.getId(), exception.getMessage());
            }
        }
    }
}
