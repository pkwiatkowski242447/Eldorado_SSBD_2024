package pl.lodz.p.it.ssbd2024.ssbd03.web;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.ReservationFacade;

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
        System.out.println("TST_ACT_FIRST: " + reservationFacade.findActiveReservationsWithPagination(UUID.fromString("69507c7f-4c03-4087-85e6-3ae3b6fc2201"), 0, 200));
        System.out.println("TST_HIST_FIRST: " + reservationFacade.findHistoricalReservationsWithPagination(UUID.fromString("69507c7f-4c03-4087-85e6-3ae3b6fc2201"), 0, 200));

        System.out.println("TST_ACT_SECOND: " + reservationFacade.findActiveReservationsWithPagination(UUID.fromString("9428fadf-191c-4dd7-8626-01c3e0ff603c"), 0, 200));
        System.out.println("TST_HIST_SECOND: " + reservationFacade.findHistoricalReservationsWithPagination(UUID.fromString("9428fadf-191c-4dd7-8626-01c3e0ff603c"), 0, 200));

        System.out.println("TST_SECTOR" + reservationFacade.findSectorReservationsWithPagination(UUID.fromString("3e6a85db-d751-4549-bbb7-9705f0b2fa6b"), 0, 200));
    }

    public void addTestEnt() {

    }
}

