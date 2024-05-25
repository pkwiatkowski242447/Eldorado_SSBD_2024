package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingEvent;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingEventUnitTest {

    private ParkingEvent parkingEvent;
    private Reservation reservation;
    private LocalDateTime date;

    @BeforeEach
    public void setUp() {
        reservation = new Reservation();
        date = LocalDateTime.of(2024, 5, 1, 10, 0);
        parkingEvent = new ParkingEvent(date, ParkingEvent.EventType.ENTRY);
        parkingEvent.setReservation(reservation);
    }

    @Test
    public void parkingEventConstructorTest() {
        assertEquals(reservation, parkingEvent.getReservation());
        assertEquals(date, parkingEvent.getDate());
        assertEquals(ParkingEvent.EventType.ENTRY, parkingEvent.getType());
    }

    @Test
    public void parkingEventConstructorNotNullObjectPositiveTest() {
        assertNotNull(parkingEvent);
    }

    @Test
    public void dateGetterTest() {
        assertEquals(date, parkingEvent.getDate());
    }

    @Test
    public void eventTypeGetterTest() {
        assertEquals(ParkingEvent.EventType.ENTRY, parkingEvent.getType());
    }

    @Test
    public void reservationGetterTest() {
        assertEquals(reservation, parkingEvent.getReservation());
    }

    @Test
    public void testToString() {
        String result = parkingEvent.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
    }
}
