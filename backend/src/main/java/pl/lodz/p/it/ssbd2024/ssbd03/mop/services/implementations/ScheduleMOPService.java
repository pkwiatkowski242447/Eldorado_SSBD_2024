package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.implementations;

import ch.qos.logback.core.util.TimeUtil;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.util.RunAsSystem;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
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
    public ScheduleMOPService(ReservationFacade reservationFacade, UserLevelMOPFacade userLevelFacade, ParkingEventFacade parkingEventFacade, ParkingFacade parkingFacade) {
        this.reservationFacade = reservationFacade;
        this.userLevelFacade = userLevelFacade;
        this.parkingEventFacade = parkingEventFacade;
        this.parkingFacade = parkingFacade;
    }

    @RunAsSystem
    @Override
    @RolesAllowed({Authorities.END_RESERVATION})
    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS, initialDelay = -1L)
    public void endReservation() {
        log.info("Method: endReservation(), used for removing reservations which last more than 24 hours");
        List<Reservation> reservationsWhichLastMoreThan24h = new ArrayList<>();
        try {
            reservationsWhichLastMoreThan24h = reservationFacade.findAllReservationsMarkedForEnding(Long.parseLong(endTime), TimeUnit.HOURS);
        } catch (NumberFormatException | ApplicationBaseException exception) {
            log.error("Exception: {} occurred while searching for reservation to be ended. Cause: {}.",
                    exception.getClass().getSimpleName(), exception.getMessage());
        }

        if (reservationsWhichLastMoreThan24h.isEmpty()) {
            log.info("No reservations to be ended were found.");
            return;
        }

        log.info("List of identifiers of reservations to be ended: {}", reservationsWhichLastMoreThan24h.stream().map(Reservation::getId).toList());

        for (Reservation reservation : reservationsWhichLastMoreThan24h) {
            try {
                int numberOfEntries = 0;
                int numberOfExits = 0;
                for (ParkingEvent parkingEvent : reservation.getParkingEvents()) {
                    if (parkingEvent.getType() == ParkingEvent.EventType.ENTRY) {
                        numberOfEntries += 1;
                    }
                    else {
                        numberOfExits += 1;
                    }
                }
                if (numberOfExits != numberOfEntries) {
                    ParkingEvent exitEvent = new ParkingEvent(LocalDateTime.now(), ParkingEvent.EventType.EXIT);
                    reservation.addParkingEvent(exitEvent);
                    this.reservationFacade.edit(reservation);
                    Sector sector = reservation.getSector();
                    sector.setAvailablePlaces(sector.getAvailablePlaces() + 1);
                    parkingFacade.editSector(sector);
                }
            } catch (Exception exception) {
                log.error("Exception: {} occurred while removing reservation with id: {}. Cause: {}.",
                        exception.getClass().getSimpleName(), reservation.getId(), exception.getMessage());
            }
        }
    }
}
