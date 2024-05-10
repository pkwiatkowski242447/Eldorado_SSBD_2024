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
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.*;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ParkingEventFacade;

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
public class ParkingEventFacadeTest extends TestcontainersConfig {


    @Autowired
    ParkingEventFacade parkingEventFacade;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;

    private Address address;
    private Parking parking;
    private Sector sector;
    private Reservation reservation;
    private ParkingEvent parkingEvent;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        address = new Address("dd","casc","wqc");
        parking = new Parking(address);
        sector = new Sector(parking,"dd", Sector.SectorType.COVERED,23,11);
        reservation = new Reservation(sector);
        parkingEvent = new ParkingEvent(reservation,LocalDateTime.now(),ParkingEvent.EventType.ENTRY);

    }

    @Test
    public void initializationContextTest() {
        ServletContext servletContext = webApplicationContext.getServletContext();
        assertNotNull(servletContext);
        assertInstanceOf(MockServletContext.class, servletContext);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void testCount() {


        parkingEventFacade.create(parkingEvent);
        parkingEventFacade.create(parkingEvent);

        int count = parkingEventFacade.count();
        assertEquals(5, count);
    }
    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void createParkingEventTest() {
        assertNotNull(parkingEvent);
        parkingEventFacade.create(parkingEvent);


        assertEquals("dd",parkingEvent.getReservation().getSector().getName());

    }



    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findParkingEventTest(){
        parkingEventFacade.create(parkingEvent);
        Optional<ParkingEvent> parkingEventOptional = parkingEventFacade.find(parkingEvent.getId());
        assertTrue(parkingEventOptional.isPresent());

        ParkingEvent parkingEvent1 = parkingEventOptional.get();
        assertNotNull(parkingEvent1);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void editTest() {
        parkingEvent.setDate(LocalDateTime.now());
        parkingEventFacade.create(parkingEvent);

        LocalDateTime newBeginTime = LocalDateTime.now().minusHours(1);
        parkingEvent.setDate(newBeginTime);

        parkingEventFacade.edit(parkingEvent);

        ParkingEvent editedParkingEvent = parkingEventFacade.find(parkingEvent.getId()).orElse(null);

        assertNotNull(editedParkingEvent);
        assertEquals(newBeginTime, editedParkingEvent.getDate());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    void findAndRefreshTest() {
        parkingEventFacade.create(parkingEvent);
        UUID parkingEventId = parkingEvent.getId();

        Optional<ParkingEvent> optionalParkingEvent = parkingEventFacade.findAndRefresh(parkingEventId);
        assertTrue(optionalParkingEvent.isPresent());

        ParkingEvent refreshedParkingEvent = optionalParkingEvent.get();
        assertNotNull(refreshedParkingEvent);
    }


    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeReservationTest(){
        parkingEventFacade.create(parkingEvent);
        Optional<ParkingEvent> retrivedParkingEventOptional = parkingEventFacade.find(parkingEvent.getId());
        assertTrue(retrivedParkingEventOptional.isPresent());

        ParkingEvent retrievedParkingEvent = retrivedParkingEventOptional.get();
        assertNotNull(retrievedParkingEvent);
        parkingEventFacade.remove(parkingEvent);

        Optional <ParkingEvent> deleted = parkingEventFacade.find(parkingEvent.getId());
        assertTrue(deleted.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void testFindAll() {
        List<ParkingEvent> parkingEvents = parkingEventFacade.findAll();
        assertNotNull(parkingEvents);
        assertFalse(parkingEvents.isEmpty());
    }

}
