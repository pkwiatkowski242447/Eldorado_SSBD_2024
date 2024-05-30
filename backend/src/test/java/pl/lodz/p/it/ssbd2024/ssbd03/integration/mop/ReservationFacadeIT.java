package pl.lodz.p.it.ssbd2024.ssbd03.integration.mop;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import pl.lodz.p.it.ssbd2024.ssbd03.TestcontainersConfig;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.config.webconfig.WebConfig;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
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
public class ReservationFacadeIT extends TestcontainersConfig {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("jdbc.ssbd03.url", () -> String.format("jdbc:postgresql://localhost:%s/ssbd03", postgres.getFirstMappedPort()));
    }

    @AfterEach
    void teardown() {
        ((AtomikosDataSourceBean) webApplicationContext.getBean("dataSourceAdmin")).close();
        ((AtomikosDataSourceBean) webApplicationContext.getBean("dataSourceAuth")).close();
        ((AtomikosDataSourceBean) webApplicationContext.getBean("dataSourceMOP")).close();
        ((AtomikosDataSourceBean) webApplicationContext.getBean("dataSourceMOK")).close();
    }

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
    @WithMockUser(roles = {Authorities.RESERVE_PARKING_PLACE})
    public void reservationFacadeCreateReservationTest() throws ApplicationBaseException {
        assertNotNull(reservation);
        reservationFacade.create(reservation);

        assertEquals("dd",reservation.getSector().getName());
    }

//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    @WithMockUser(roles = {Authorities.RESERVE_PARKING_PLACE})
//    public void reservationFacadeFindReservationTest() throws ApplicationBaseException {
//        reservationFacade.create(reservation);
//        Optional<Reservation> retrievedReservationOptional = reservationFacade.find(reservation.getId());
//        assertTrue(retrievedReservationOptional.isPresent());
//
//        Reservation retrievedReservation = retrievedReservationOptional.get();
//        assertNotNull(retrievedReservation);
//    }

//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    public void reservationFacadeEditReservationTest() throws ApplicationBaseException {
//        reservation.setBeginTime(LocalDateTime.now());
//        reservationFacade.create(reservation);
//
//        LocalDateTime newBeginTime = LocalDateTime.now().minusHours(1);
//        reservation.setBeginTime(newBeginTime);
//
//        reservationFacade.edit(reservation);
//
//        Reservation editedReservation = reservationFacade.find(reservation.getId()).orElseThrow(NoSuchElementException::new);
//
//        assertNotNull(editedReservation);
//        assertEquals(newBeginTime, editedReservation.getBeginTime());
//    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Authorities.RESERVE_PARKING_PLACE, Authorities.RESERVE_PARKING_PLACE})
    void reservationFacadeFindAndRefreshTest() throws ApplicationBaseException {
        reservationFacade.create(reservation);

        UUID reservationId = reservation.getId();

        Optional<Reservation> optionalReservation = reservationFacade.findAndRefresh(reservationId);
        assertTrue(optionalReservation.isPresent());

        Reservation refreshedReservation = optionalReservation.get();
        assertNotNull(refreshedReservation);
    }

//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    public void reservationFacadeCountTest() throws ApplicationBaseException {
//        reservationFacade.create(reservation);
//        int count = reservationFacade.count();
//        assertEquals(4,count);
//    }

//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    public void reservationFacadeRemoveReservationTest() throws ApplicationBaseException {
//        reservationFacade.create(reservation);
//        Optional<Reservation> retrievedReservationOptional = reservationFacade.find(reservation.getId());
//        assertTrue(retrievedReservationOptional.isPresent());
//
//        Reservation retrievedReservation = retrievedReservationOptional.get();
//        assertNotNull(retrievedReservation);
//
//        reservationFacade.remove(reservation);
//        Optional <Reservation> deleted = reservationFacade.find(reservation.getId());
//        assertTrue(deleted.isEmpty());
//    }

//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    public void reservationFacadeFindAllReservationsTest() throws Exception {
//        List<Reservation> reservations = reservationFacade.findAll();
//
//        assertNotNull(reservations);
//        assertFalse(reservations.isEmpty());
//
//        reservations.add(reservation);
//
//        assertEquals(4,reservations.size());
//    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Authorities.GET_ALL_RESERVATIONS, Authorities.CANCEL_RESERVATION, Authorities.RESERVE_PARKING_PLACE})
    public void reservationFacadeFindAllReservationsWithPaginationTest() throws ApplicationBaseException {
        reservationFacade.create(reservation);

        List<Reservation> reservations = reservationFacade.findAllWithPagination(0,8);
        assertNotNull(reservations);
        assertEquals(4, reservations.size());
    }

//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    public void reservationFacadeFindHistoricalReservationsWithPagination() throws ApplicationBaseException {
//        UUID reservationId = UUID.fromString("1ec7d685-71ac-4418-834a-ed7b6fc68fc8");
//        UUID clientId = UUID.fromString("9428fadf-191c-4dd7-8626-01c3e0ff603c");
//
//        Reservation reservation = reservationFacade.find(reservationId).orElseThrow(NoSuchElementException::new);
//
//        reservation.setBeginTime(LocalDateTime.now().withYear(2024).withMonth(2).withDayOfMonth(14).withHour(14).withMinute(30));
//        reservation.setEndTime(LocalDateTime.now().withYear(2024).withMonth(2).withDayOfMonth(14).withHour(19).withMinute(30));
//
//        List<Reservation> reservations = reservationFacade.findHistoricalReservationsWithPagination(clientId, 0, 5);
//        assertEquals(1, reservations.size());
//    }

//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    public void reservationFacadeFindActiveReservationsWithPagination() throws ApplicationBaseException {
//        UUID reservationIdNo1 = UUID.fromString("1ec7d685-71ac-4418-834a-ed7b6fc68fc8");
//        UUID clientIdNo1 = UUID.fromString("9428fadf-191c-4dd7-8626-01c3e0ff603c");
//
//        UUID reservationIdNo2 = UUID.fromString("a7709a4d-b7bc-40c4-8fd5-5c7cfcb0f146");
//        UUID clientIdNo2 = UUID.fromString("c51557aa-284d-44a6-b38d-b6ceb9c23725");
//
//        Reservation reservationNo1 = reservationFacade.find(reservationIdNo1).orElseThrow(NoSuchElementException::new);
//        Reservation reservationNo2 = reservationFacade.find(reservationIdNo2).orElseThrow(NoSuchElementException::new);
//
//        reservationNo1.setBeginTime(LocalDateTime.now());
//        reservationNo1.setEndTime(LocalDateTime.now().plusHours(3));
//
//        reservationNo2.setBeginTime(LocalDateTime.now().withYear(2024).withMonth(2).withDayOfMonth(14).withHour(14).withMinute(30));
//        reservationNo2.setEndTime(LocalDateTime.now().withYear(2024).withMonth(2).withDayOfMonth(14).withHour(19).withMinute(30));
//
//        List<Reservation> listOfReservationsNo1 = reservationFacade.findActiveReservationsWithPagination(clientIdNo1, 0, 5);
//        List<Reservation> listOfReservationsNo2 = reservationFacade.findActiveReservationsWithPagination(clientIdNo2, 0, 5);
//
//        assertEquals(1, listOfReservationsNo1.size());
//        assertEquals(0, listOfReservationsNo2.size());
//    }
}
