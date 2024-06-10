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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @BeforeEach
    public void setup() {
        address = new Address("Strykow","90-000","Kosciuszki");
        parking = new Parking(address);
        sector = new Sector(parking,"AA-02", Sector.SectorType.COVERED,23,11, true);
        reservation = new Reservation(sector, LocalDateTime.now());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Authorities.RESERVE_PARKING_PLACE})
    public void reservationFacadeCreateReservationTest() throws ApplicationBaseException {
        assertNotNull(reservation);
        reservationFacade.create(reservation);

        assertEquals("AA-02", reservation.getSector().getName());
    }

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

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Authorities.GET_ALL_RESERVATIONS, Authorities.CANCEL_RESERVATION, Authorities.RESERVE_PARKING_PLACE})
    public void reservationFacadeFindAllReservationsWithPaginationTest() throws ApplicationBaseException {
        List<Reservation> reservations = reservationFacade.findAllWithPagination(0, 50);
        assertNotNull(reservations);
        assertEquals(15, reservations.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Authorities.RESERVE_PARKING_PLACE})
    public void countAllSectorReservationInTimeframeTest() throws ApplicationBaseException {
        long numOfReservations = reservationFacade.countAllSectorReservationInTimeframe(
                UUID.fromString("3e6a85db-d751-4549-bbb7-9705f0b2fa6b"),
                LocalDateTime.of(2024, 12, 12, 12, 0, 0),
                24,
                LocalDateTime.of(2024, 12, 12, 8, 0, 0)
        );

        assertEquals(8, numOfReservations);
    }
}
