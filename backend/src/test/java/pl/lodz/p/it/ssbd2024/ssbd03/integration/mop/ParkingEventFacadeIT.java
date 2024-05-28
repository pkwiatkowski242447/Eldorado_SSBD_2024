package pl.lodz.p.it.ssbd2024.ssbd03.integration.mop;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import pl.lodz.p.it.ssbd2024.ssbd03.TestcontainersConfig;
import pl.lodz.p.it.ssbd2024.ssbd03.config.webconfig.WebConfig;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.*;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ParkingEventFacade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class ParkingEventFacadeIT extends TestcontainersConfig {

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
    ParkingEventFacade parkingEventFacade;

    private Address address;
    private Parking parking;
    private Sector sector;
    private Reservation reservation;
    private ParkingEvent parkingEvent;

    @BeforeEach
    public void setup() {
        address = new Address("dd", "casc", "wqc");
        parking = new Parking(address);
        sector = new Sector(parking, "dd", Sector.SectorType.COVERED, 23, 11);
        reservation = new Reservation(sector);
        parkingEvent = new ParkingEvent(LocalDateTime.now(), ParkingEvent.EventType.ENTRY);
        parkingEvent.setReservation(reservation);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingEventFacadeCountParkingEventsTest() throws ApplicationBaseException {
        parkingEventFacade.create(parkingEvent);
        parkingEventFacade.create(parkingEvent);

        int parkingEventCount = parkingEventFacade.count();
        assertEquals(5, parkingEventCount);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeCreateParkingEventTest() throws ApplicationBaseException {
        assertNotNull(parkingEvent);
        parkingEventFacade.create(parkingEvent);

        assertEquals("dd", parkingEvent.getReservation().getSector().getName());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeFindParkingEventTest() throws ApplicationBaseException {
        parkingEventFacade.create(parkingEvent);
        Optional<ParkingEvent> parkingEventOptional = parkingEventFacade.find(parkingEvent.getId());
        assertTrue(parkingEventOptional.isPresent());

        ParkingEvent parkingEvent = parkingEventOptional.get();
        assertNotNull(parkingEvent);
    }

    // TODO check if edit method is sensible for ParkingEvent
//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    public void parkingFacadeEditParkingEventTest() throws ApplicationBaseException {
//        parkingEvent.set(LocalDateTime.now());
//        parkingEventFacade.create(parkingEvent);
//
//        LocalDateTime newBeginTime = LocalDateTime.now().minusHours(1);
//        parkingEvent.setDate(newBeginTime);
//
//        parkingEventFacade.edit(parkingEvent);
//
//        ParkingEvent editedParkingEvent = parkingEventFacade.find(parkingEvent.getId()).orElse(null);
//
//        assertNotNull(editedParkingEvent);
//        assertEquals(newBeginTime, editedParkingEvent.getDate());
//    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeFindAndRefreshParkingEventTest() throws ApplicationBaseException {
        parkingEventFacade.create(parkingEvent);
        UUID parkingEventId = parkingEvent.getId();

        Optional<ParkingEvent> optionalParkingEvent = parkingEventFacade.findAndRefresh(parkingEventId);
        assertTrue(optionalParkingEvent.isPresent());

        ParkingEvent refreshedParkingEvent = optionalParkingEvent.get();
        assertNotNull(refreshedParkingEvent);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeRemoveReservationTest() throws ApplicationBaseException {
        parkingEventFacade.create(parkingEvent);
        Optional<ParkingEvent> retrievedParkingEventOptional = parkingEventFacade.find(parkingEvent.getId());
        assertTrue(retrievedParkingEventOptional.isPresent());

        ParkingEvent retrievedParkingEvent = retrievedParkingEventOptional.get();
        assertNotNull(retrievedParkingEvent);

        parkingEventFacade.remove(parkingEvent);
        Optional<ParkingEvent> deleted = parkingEventFacade.find(parkingEvent.getId());
        assertTrue(deleted.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeFindAllParkingEventsTest() throws Exception {
        List<ParkingEvent> listOfParkingEvents = parkingEventFacade.findAll();
        assertNotNull(listOfParkingEvents);
        assertFalse(listOfParkingEvents.isEmpty());
    }
}
