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
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.*;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.AccountMOPFacade;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class AccountMOPFacadeTest extends TestcontainersConfig {


    @Autowired
    AccountMOPFacade accountMOPFacade;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;

    private Address address;
    private Parking parking;
    private Sector sector;
    private Reservation reservation;
    private ParkingEvent parkingEvent;
    private Account account;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        address = new Address("dd", "casc", "wqc");
        parking = new Parking(address);
        sector = new Sector(parking, "dd", Sector.SectorType.COVERED, 23, 11);
        reservation = new Reservation(sector);
        parkingEvent = new ParkingEvent(reservation, LocalDateTime.now(), ParkingEvent.EventType.ENTRY);
        account = new Account("login", "haslo", "imie", "nazwisko", "email", "123456789");

    }

    @Test
    public void initializationContextTest() {
        ServletContext servletContext = webApplicationContext.getServletContext();
        assertNotNull(servletContext);
        assertInstanceOf(MockServletContext.class, servletContext);
    }


    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findByLoginTest() {
        Account account = new Account("login", "haslo", "imie", "nazwisko", "email", "123456789");


        Optional<Account> optionalAccount = accountMOPFacade.findByLogin("login");
        assertNotNull(optionalAccount);
        assertEquals("imie", account.getName());
        assertEquals("nazwisko", account.getLastname());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void editTest() {
        UUID uuid = UUID.fromString("b3b8c2ac-21ff-434b-b490-aa6d717447c0");
        Optional<Account> accountOptional = accountMOPFacade.find(uuid);
        assertEquals("Jerzy", accountOptional.get().getName());
        accountOptional.get().setName("Mietek");

        Optional<Account> editedOptional = accountMOPFacade.find(uuid);
        assertEquals("Mietek", accountOptional.get().getName());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findTest() {
        UUID uuid = UUID.fromString("b3b8c2ac-21ff-434b-b490-aa6d717447c0");
        Optional<Account> accountOptional = accountMOPFacade.find(uuid);
        assertTrue(accountOptional.isPresent());

        Account account1 = accountOptional.get();
        assertNotNull(account1);

    }

//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    void findAndRefreshTest() {
//        UUID uuid = UUID.fromString("b3b8c2ac-21ff-434b-b490-aa6d717447c0");
//        Optional<Account> optionalAccount = accountMOPFacade.findAndRefresh(uuid);
//        assertTrue(optionalAccount.isPresent());
//
//        Account refreshedAccount = optionalAccount.get();
//        assertNotNull(refreshedAccount);
//        // ERROR
//        //Caused by: org.postgresql.util.PSQLException: ERROR: permission denied for table staff_data
//    }




}
