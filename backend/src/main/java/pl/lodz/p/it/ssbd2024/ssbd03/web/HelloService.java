package pl.lodz.p.it.ssbd2024.ssbd03.web;

import jakarta.transaction.Transactional;
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

@Service
public class HelloService {

    private final ReservationFacade reservationFacade;

    @Autowired
    public HelloService(ReservationFacade reservationFacade) {
        this.reservationFacade = reservationFacade;
    }

    @Transactional
    public void getTest() {
        Account account = new Account("Adas13", "@#12d]x1", "Adam", "Tom", "adam@example.com", "100100100");
        account.setAccountLanguage("PL");

        var clientLevel = new Client();
        clientLevel.setAccount(account);
        account.addUserLevel(clientLevel);
        Parking parking = new Parking();
        parking.setAddress(new Address("Boat City", "00-000", "Sloneczna"));
        parking.addSector("S1", Sector.SectorType.COVERED, 20, 1);
        parking.addSector("S2", Sector.SectorType.UNCOVERED, 20, 2);
        parking.addSector("S3", Sector.SectorType.UNDERGROUND, 20, 3);

        System.out.println(parking.getSectors().size());

        Reservation reservation = new Reservation(clientLevel, parking.getSectors().getFirst());

        reservation.setBeginTime(LocalDateTime.now());
        reservation.setEndTime(LocalDateTime.now().plusHours(2));

        reservationFacade.create(reservation);
    }

    public void addTestEnt() {

    }
}

