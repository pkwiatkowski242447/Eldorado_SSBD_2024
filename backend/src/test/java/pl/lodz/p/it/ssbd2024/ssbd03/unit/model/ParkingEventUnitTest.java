package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingEvent;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParkingEventUnitTest {

    private ParkingEvent parkingEvent;
    private Reservation reservation;
    private LocalDateTime date;

    @BeforeEach
    public void setUp() {
        reservation = new Reservation();
        date = LocalDateTime.of(2024, 5, 1, 10, 0);
        parkingEvent = new ParkingEvent(reservation, date, ParkingEvent.EventType.ENTRY);
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
    public void dateSetterTest() {
        LocalDateTime newDate = LocalDateTime.of(2024, 5, 1, 12, 0);
        parkingEvent.setDate(newDate);
        assertEquals(newDate, parkingEvent.getDate());
    }

    @Test
    public void eventTypeGetterTest() {
        assertEquals(ParkingEvent.EventType.ENTRY, parkingEvent.getType());
    }

    @Test
    public void eventTypeSetterTest() {
        ParkingEvent.EventType newType = ParkingEvent.EventType.EXIT;
        parkingEvent.setType(newType);
        assertEquals(newType, parkingEvent.getType());
    }

    @Test
    public void reservationGetterTest() {
        assertEquals(reservation, parkingEvent.getReservation());
    }
}
