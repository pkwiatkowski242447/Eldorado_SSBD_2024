package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.implementations;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.RollbackException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.util.RunAsSystem;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingEvent;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationDatabaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationOptimisticLockException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ParkingFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ReservationFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.UserLevelMOPFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces.ScheduleMOPServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;

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
    private final UserLevelMOPFacade userLevelFacade;
    private final ParkingFacade parkingFacade;
    private final MailProvider mailProvider;

    @Autowired
    public ScheduleMOPService(ReservationFacade reservationFacade,
                              UserLevelMOPFacade userLevelFacade,
                              ParkingFacade parkingFacade,
                              MailProvider mailProvider) {
        this.reservationFacade = reservationFacade;
        this.userLevelFacade = userLevelFacade;
        this.parkingFacade = parkingFacade;
        this.mailProvider = mailProvider;
    }

    @RunAsSystem
    @Override
    @RolesAllowed({Authorities.END_RESERVATION})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS, initialDelay = -1L)
    public void terminateReservation() {
        log.info("Method: endReservation(), used for terminating reservations which last more than scheduler.maximum_reservation_time value");
        List<Reservation> reservationsToBeFinished = new ArrayList<>();
        try {
            reservationsToBeFinished = reservationFacade.findAllReservationsMarkedForTermination(Long.parseLong(endTime), TimeUnit.HOURS);
        } catch (NumberFormatException | ApplicationBaseException exception) {
            log.error("Exception: {} occurred while searching for reservation to be terminated. Cause: {}.",
                    exception.getClass().getSimpleName(), exception.getMessage());
        }

        if (reservationsToBeFinished.isEmpty()) {
            log.info("No reservations to be terminated were found.");
            return;
        }

        log.info("List of identifiers of reservations to be terminated: {}", reservationsToBeFinished.stream().map(Reservation::getId).toList());

        for (Reservation reservation : reservationsToBeFinished) {
            try {
                long numberOfEntries = reservation.getParkingEvents().stream()
                        .filter(event -> event.getType() == ParkingEvent.EventType.ENTRY)
                        .count();

                long numberOfExits = reservation.getParkingEvents().stream()
                        .filter(event -> event.getType() == ParkingEvent.EventType.EXIT)
                        .count();

                if (numberOfExits != numberOfEntries) {
                    ParkingEvent exitEvent = new ParkingEvent(LocalDateTime.now(), ParkingEvent.EventType.EXIT);
                    reservation.addParkingEvent(exitEvent);
                    reservation.setStatus(Reservation.ReservationStatus.TERMINATED);
                    this.reservationFacade.edit(reservation);
                    Sector sector = reservation.getSector();
                    sector.setOccupiedPlaces(sanitizeInteger(sector.getOccupiedPlaces() - 1));
                    parkingFacade.editSector(sector);

                    if (reservation.getClient() != null) {
                        // Send mail notification
                        mailProvider.sendSystemEndReservationInfoEmail(
                                reservation.getClient().getAccount().getName(),
                                reservation.getClient().getAccount().getLastname(),
                                reservation.getClient().getAccount().getEmail(),
                                reservation.getClient().getAccount().getAccountLanguage(),
                                reservation.getId().toString()
                        );
                    }
                }
            } catch (Exception exception) {
                log.error("Exception: {} occurred while terminating reservation with id: {}. Cause: {}.",
                        exception.getClass().getSimpleName(), reservation.getId(), exception.getMessage());
            }
        }
    }

    @RunAsSystem
    @Override
    @RolesAllowed({Authorities.END_RESERVATION})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS, initialDelay = -1L)
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

        log.info("List of identifiers of reservations to be completed: {}", reservationsToEnd.stream().map(Reservation::getId).toList());

        for (Reservation reservation : reservationsToEnd) {
            try {
                reservation.setStatus(Reservation.ReservationStatus.COMPLETED_AUTOMATICALLY);
                reservationFacade.edit(reservation);

                Sector sector = reservation.getSector();
                sector.setOccupiedPlaces(sanitizeInteger(sector.getOccupiedPlaces() - 1));
                parkingFacade.editSector(sector);

                Client client = reservation.getClient();

                userLevelFacade.clientTypeChangeCheck(reservation);
                userLevelFacade.edit(client);
            } catch (Exception exception) {
                log.error("Exception: {} occurred while canceling reservation with id: {}. Cause: {}.",
                        exception.getClass().getSimpleName(), reservation.getId(), exception.getMessage());
            }
        }
    }

    private static int sanitizeInteger(int value) {
        return Math.max(value, 0);
    }
}
