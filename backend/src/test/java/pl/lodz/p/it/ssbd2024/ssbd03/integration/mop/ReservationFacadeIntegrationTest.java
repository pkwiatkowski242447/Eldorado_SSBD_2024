package pl.lodz.p.it.ssbd2024.ssbd03.integration.mop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.TestcontainersConfig;
import pl.lodz.p.it.ssbd2024.ssbd03.config.webconfig.WebConfig;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ReservationFacade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class ReservationFacadeIntegrationTest extends TestcontainersConfig {

    @Autowired
    ReservationFacade reservationFacade;

    private Address address;
    private Parking parking;
    private Sector sector;
    private Reservation reservation;
    private Reservation reservation1;

    @BeforeEach
    public void setup() {
        address = new Address("dd","casc","wqc");
        parking = new Parking(address);
        sector = new Sector(parking,"dd", Sector.SectorType.COVERED,23,11);
        reservation = new Reservation(sector);
        reservation1 = new Reservation(sector);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void reservationFacadeCreateReservationTest() {
        assertNotNull(reservation);
        reservationFacade.create(reservation);

        assertEquals("dd",reservation.getSector().getName());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void reservationFacadeFindReservationTest(){
        reservationFacade.create(reservation);
        Optional<Reservation> retrievedReservationOptional = reservationFacade.find(reservation.getId());
        assertTrue(retrievedReservationOptional.isPresent());

        Reservation retrievedReservation = retrievedReservationOptional.get();
        assertNotNull(retrievedReservation);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void reservationFacadeEditReservationTest() {
        reservation.setBeginTime(LocalDateTime.now());
        reservationFacade.create(reservation);

        LocalDateTime newBeginTime = LocalDateTime.now().minusHours(1);
        reservation.setBeginTime(newBeginTime);

        reservationFacade.edit(reservation);

        Reservation editedReservation = reservationFacade.find(reservation.getId()).orElseThrow(NoSuchElementException::new);

        assertNotNull(editedReservation);
        assertEquals(newBeginTime, editedReservation.getBeginTime());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    void reservationFacadeFindAndRefreshTest() {
        reservationFacade.create(reservation);

        UUID reservationId = reservation.getId();

        Optional<Reservation> optionalReservation = reservationFacade.findAndRefresh(reservationId);
        assertTrue(optionalReservation.isPresent());

        Reservation refreshedReservation = optionalReservation.get();
        assertNotNull(refreshedReservation);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void reservationFacadeCountTest() {
        reservationFacade.create(reservation);
        int count = reservationFacade.count();
        assertEquals(4,count);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void reservationFacadeRemoveReservationTest(){
        reservationFacade.create(reservation);
        Optional<Reservation> retrievedReservationOptional = reservationFacade.find(reservation.getId());
        assertTrue(retrievedReservationOptional.isPresent());

        Reservation retrievedReservation = retrievedReservationOptional.get();
        assertNotNull(retrievedReservation);

        reservationFacade.remove(reservation);
        Optional <Reservation> deleted = reservationFacade.find(reservation.getId());
        assertTrue(deleted.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void reservationFacadeFindAllReservationsTest() {
        List<Reservation> reservations = reservationFacade.findAll();

        assertNotNull(reservations);
        assertFalse(reservations.isEmpty());

        reservations.add(reservation);

        assertEquals(4,reservations.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void reservationFacadeFindAllReservationsWithPaginationTest() {
        reservationFacade.create(reservation);

        List<Reservation> reservations = reservationFacade.findAllWithPagination(0,8);
        assertNotNull(reservations);
        assertEquals(4, reservations.size());
//
//        page = 1;
//        List<Reservation> reservationsPage2 = reservationFacade.findAllWithPagination(page, pageSize);
//        assertNotNull(reservationsPage2);
//        assertEquals(totalReservations - pageSize, reservationsPage2.size());
//
//        assertNotEquals(reservationsPage1, reservationsPage2);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void reservationFacadeFindHistoricalReservationsWithPagination(){
        UUID reservationId = UUID.fromString("1ec7d685-71ac-4418-834a-ed7b6fc68fc8");
        UUID clientId = UUID.fromString("9428fadf-191c-4dd7-8626-01c3e0ff603c");

        Reservation reservation = reservationFacade.find(reservationId).orElseThrow(NoSuchElementException::new);

        reservation.setBeginTime(LocalDateTime.now().withYear(2024).withMonth(2).withDayOfMonth(14).withHour(14).withMinute(30));
        reservation.setEndTime(LocalDateTime.now().withYear(2024).withMonth(2).withDayOfMonth(14).withHour(19).withMinute(30));

        List<Reservation> reservations = reservationFacade.findHistoricalReservationsWithPagination(clientId, 0, 5);
        assertEquals(1, reservations.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void reservationFacadeFindActiveReservationsWithPagination(){
        UUID reservationIdNo1 = UUID.fromString("1ec7d685-71ac-4418-834a-ed7b6fc68fc8");
        UUID clientIdNo1 = UUID.fromString("9428fadf-191c-4dd7-8626-01c3e0ff603c");

        UUID reservationIdNo2 = UUID.fromString("a7709a4d-b7bc-40c4-8fd5-5c7cfcb0f146");
        UUID clientIdNo2 = UUID.fromString("c51557aa-284d-44a6-b38d-b6ceb9c23725");

        Reservation reservationNo1 = reservationFacade.find(reservationIdNo1).orElseThrow(NoSuchElementException::new);
        Reservation reservationNo2 = reservationFacade.find(reservationIdNo2).orElseThrow(NoSuchElementException::new);

        reservationNo1.setBeginTime(LocalDateTime.now());
        reservationNo1.setEndTime(LocalDateTime.now().plusHours(3));

        reservationNo2.setBeginTime(LocalDateTime.now().withYear(2024).withMonth(2).withDayOfMonth(14).withHour(14).withMinute(30));
        reservationNo2.setEndTime(LocalDateTime.now().withYear(2024).withMonth(2).withDayOfMonth(14).withHour(19).withMinute(30));

        List<Reservation> listOfReservationsNo1 = reservationFacade.findActiveReservationsWithPagination(clientIdNo1, 0, 5);
        List<Reservation> listOfReservationsNo2 = reservationFacade.findActiveReservationsWithPagination(clientIdNo2, 0, 5);

        assertEquals(1, listOfReservationsNo1.size());
        assertEquals(0, listOfReservationsNo2.size());
    }
}
