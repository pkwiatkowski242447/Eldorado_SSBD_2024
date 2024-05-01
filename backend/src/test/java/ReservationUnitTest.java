import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingEvent;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationUnitTest {

    private Reservation reservation;
    private Client client;
    private Sector sector;


    @BeforeEach
    public void setUp() {
        client = new Client();
        sector = new Sector();
        reservation = new Reservation(client, sector);

    }

    @Test
    public void reservationConstructorWithClientTest() {
        assertEquals(client, reservation.getClient());
        assertEquals(sector, reservation.getSector());
        assertEquals(0, reservation.getParkingEvents().size());
    }

    @Test
    public void reservationConstructorWithoutClientTest() {
        Reservation reservationWithoutClient = new Reservation(sector);
        assertNull(reservationWithoutClient.getClient());
        assertEquals(sector, reservationWithoutClient.getSector());
        assertEquals(0, reservationWithoutClient.getParkingEvents().size());
    }

    @Test
    public void reservationConstructorNotNullObjectPositiveTest() {
        assertNotNull(reservation);
    }

    @Test
    public void beginTimeGetterAndSetterTest() {
        LocalDateTime newBeginTime = LocalDateTime.of(2024, 5, 1, 10, 0);
        reservation.setBeginTime(newBeginTime);
        assertEquals(newBeginTime, reservation.getBeginTime());
    }

    @Test
    public void endTimeGetterAndSetterTest() {
        LocalDateTime newEndTime = LocalDateTime.of(2024, 5, 1, 12, 0);
        reservation.setEndTime(newEndTime);
        assertEquals(newEndTime, reservation.getEndTime());
    }

    @Test
    public void clientGetterTest() {
        assertEquals(client, reservation.getClient());
    }

    @Test
    public void sectorGetterTest() {
        assertEquals(sector, reservation.getSector());
    }

    @Test
    public void parkingEventsGetterTest() {
        ParkingEvent parkingEvent = new ParkingEvent();
        reservation.getParkingEvents().add(parkingEvent);

        assertEquals(1, reservation.getParkingEvents().size());
        assertEquals(parkingEvent, reservation.getParkingEvents().getFirst());
    }

}
