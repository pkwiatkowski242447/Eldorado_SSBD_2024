package pl.lodz.p.it.ssbd2024.ssbd03.integration;

import jakarta.servlet.ServletContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import pl.lodz.p.it.ssbd2024.ssbd03.TestcontainersConfig;
import pl.lodz.p.it.ssbd2024.ssbd03.config.webconfig.WebConfig;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ReservationFacade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class ReservationFacadeTest extends TestcontainersConfig {


    @Autowired
    ReservationFacade reservationFacade;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;

    private Address address;
    private Parking parking;
    private Sector sector;
    private Reservation reservation;
    private Reservation reservation1;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        address = new Address("dd","casc","wqc");
        parking = new Parking(address);
        sector = new Sector(parking,"dd", Sector.SectorType.COVERED,23,11);
        reservation = new Reservation(sector);
        reservation1 = new Reservation(sector);
    }

    @Test
    public void initializationContextTest() {
        ServletContext servletContext = webApplicationContext.getServletContext();
        assertNotNull(servletContext);
        assertInstanceOf(MockServletContext.class, servletContext);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void createReservationTest() {
        assertNotNull(reservation);
        reservationFacade.create(reservation);

        assertEquals("dd",reservation.getSector().getName());

    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findReservationTest(){
        reservationFacade.create(reservation);
        Optional<Reservation> retrievedReservationOptional = reservationFacade.find(reservation.getId());
        assertTrue(retrievedReservationOptional.isPresent());

        Reservation retrievedReservation = retrievedReservationOptional.get();
        assertNotNull(retrievedReservation);

    }
    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void editTest() {
        reservation.setBeginTime(LocalDateTime.now());
        reservationFacade.create(reservation);

        LocalDateTime newBeginTime = LocalDateTime.now().minusHours(1);
        reservation.setBeginTime(newBeginTime);

        reservationFacade.edit(reservation);

        Reservation editedReservation = reservationFacade.find(reservation.getId()).orElse(null);

        assertNotNull(editedReservation);
        assertEquals(newBeginTime, editedReservation.getBeginTime());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    void findAndRefreshTest() {
        reservationFacade.create(reservation);

        UUID reservationId = reservation.getId();

        Optional<Reservation> optionalReservation = reservationFacade.findAndRefresh(reservationId);
        assertTrue(optionalReservation.isPresent());

        Reservation refreshedReservation = optionalReservation.get();
        assertNotNull(refreshedReservation);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void countTest() {
        reservationFacade.create(reservation);
        int count = reservationFacade.count();
        assertEquals(4,count);
    }


    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeReservationTest(){
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
    public void findAllTest() {
        List<Reservation> reservations = reservationFacade.findAll();
        assertNotNull(reservations);
        assertFalse(reservations.isEmpty());
        reservations.add(reservation);
        assertEquals(4,reservations.size());
    }


    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllWithPaginationTest() {
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
    public void findHistoricalReservationsWithPagination(){
        UUID reservationId = UUID.fromString("1ec7d685-71ac-4418-834a-ed7b6fc68fc8");
        UUID clientId = UUID.fromString("9428fadf-191c-4dd7-8626-01c3e0ff603c");

        Optional <Reservation> reservation = reservationFacade.find(reservationId);

        Reservation reservation1 = reservation.get();

        reservation1.setBeginTime(LocalDateTime.now().withYear(2024).withMonth(2).withDayOfMonth(14).withHour(14).withMinute(30));
        reservation1.setEndTime(LocalDateTime.now().withYear(2024).withMonth(2).withDayOfMonth(14).withHour(19).withMinute(30));


        List <Reservation> reservations = reservationFacade.findHistoricalReservationsWithPagination(clientId, 0, 5);
        assertEquals(1, reservations.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findActiveReservationsWithPagination(){
        UUID reservationId = UUID.fromString("1ec7d685-71ac-4418-834a-ed7b6fc68fc8");
        UUID clientId = UUID.fromString("9428fadf-191c-4dd7-8626-01c3e0ff603c");

        UUID reservationId1 = UUID.fromString("a7709a4d-b7bc-40c4-8fd5-5c7cfcb0f146");
        UUID clientId1 = UUID.fromString("c51557aa-284d-44a6-b38d-b6ceb9c23725");

        Optional <Reservation> reservation = reservationFacade.find(reservationId);
        Optional <Reservation> reservationn = reservationFacade.find(reservationId1);

        Reservation reservation1 = reservation.get();
        Reservation reservation2 = reservationn.get();

        reservation1.setBeginTime(LocalDateTime.now());
        reservation1.setEndTime(LocalDateTime.now().plusHours(3));

        reservation2.setBeginTime(LocalDateTime.now().withYear(2024).withMonth(2).withDayOfMonth(14).withHour(14).withMinute(30));
        reservation2.setEndTime(LocalDateTime.now().withYear(2024).withMonth(2).withDayOfMonth(14).withHour(19).withMinute(30));


        List <Reservation> reservations = reservationFacade.findActiveReservationsWithPagination(clientId, 0, 5);
        List <Reservation> reservations1 = reservationFacade.findActiveReservationsWithPagination(clientId1, 0, 5);

        assertEquals(1, reservations.size());
        assertEquals(0, reservations1.size());

    }


}
