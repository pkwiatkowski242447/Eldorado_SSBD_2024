package pl.lodz.p.it.ssbd2024.ssbd03.web;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.ReservationFacade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class HelloService {

    private final ReservationFacade reservationFacade;

    @Autowired
    public HelloService(ReservationFacade reservationFacade) {
        this.reservationFacade = reservationFacade;
    }

    @Transactional
    public void getTest() {
        System.out.println("TST_ACT_FIRST: " + reservationFacade.findActiveReservations(UUID.fromString("69507c7f-4c03-4087-85e6-3ae3b6fc2201")));
        System.out.println("TST_HIST_FIRST: " + reservationFacade.findHistoricalReservations(UUID.fromString("69507c7f-4c03-4087-85e6-3ae3b6fc2201")));

        System.out.println("TST_ACT_SECOND: " + reservationFacade.findActiveReservations(UUID.fromString("9428fadf-191c-4dd7-8626-01c3e0ff603c")));
        System.out.println("TST_HIST_SECOND: " + reservationFacade.findHistoricalReservations(UUID.fromString("9428fadf-191c-4dd7-8626-01c3e0ff603c")));

        //Pagination
        System.out.println("TST_HIST_PAG11: " + reservationFacade.findHistoricalReservationsWithPagination(UUID.fromString("9428fadf-191c-4dd7-8626-01c3e0ff603c"), 1, 1));
        System.out.println("TST_HIST_PAG21: " + reservationFacade.findHistoricalReservationsWithPagination(UUID.fromString("9428fadf-191c-4dd7-8626-01c3e0ff603c"), 2, 1));
        System.out.println("TST_HIST_PAG12: " + reservationFacade.findHistoricalReservationsWithPagination(UUID.fromString("9428fadf-191c-4dd7-8626-01c3e0ff603c"), 1, 2));
        System.out.println("TST_HIST_PAG01: " + reservationFacade.findHistoricalReservationsWithPagination(UUID.fromString("9428fadf-191c-4dd7-8626-01c3e0ff603c"), 0, 1));
        System.out.println("TST_HIST_PAG02: " + reservationFacade.findHistoricalReservationsWithPagination(UUID.fromString("9428fadf-191c-4dd7-8626-01c3e0ff603c"), 0, 2));
    }

    public void addTestEnt() {

    }
}

