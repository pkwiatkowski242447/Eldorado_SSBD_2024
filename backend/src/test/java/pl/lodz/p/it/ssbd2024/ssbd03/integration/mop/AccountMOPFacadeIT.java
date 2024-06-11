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
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingEvent;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.AccountMOPFacade;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class AccountMOPFacadeIT extends TestcontainersConfig {

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
    AccountMOPFacade accountMOPFacade;

    private Address address;
    private Parking parking;
    private Sector sector;
    private Reservation reservation;
    private ParkingEvent parkingEvent;
    private Account account;

    @BeforeEach
    public void setup() {
        address = new Address("Boat", "90-000", "Pomorska");
        parking = new Parking(address, Parking.SectorDeterminationStrategy.LEAST_OCCUPIED);
        sector = new Sector(parking, "AA-02", Sector.SectorType.COVERED, 23, 11, true);
        reservation = new Reservation(sector, LocalDateTime.now());
        parkingEvent = new ParkingEvent(LocalDateTime.now(), ParkingEvent.EventType.ENTRY);
        parkingEvent.setReservation(reservation);
        account = new Account("login", "haslo", "imie", "nazwisko", "email", "123456789");
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @WithMockUser(roles = {Authorities.GET_ACTIVE_RESERVATIONS, Authorities.GET_HISTORICAL_RESERVATIONS})
    public void accountMOPFacadeFindAccountByLoginTest() throws Exception {
        Account account = new Account("login", "haslo", "imie", "nazwisko", "email", "123456789");

        Optional<Account> optionalAccount = accountMOPFacade.findByLogin("login");
        assertNotNull(optionalAccount);
        assertEquals("imie", account.getName());
        assertEquals("nazwisko", account.getLastname());
    }

// TODO: Resolve issue when updating user level (specifically - Client) by MOP in client_data (user_level ?)

//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    public void accountMOPFacadeEditAccountTest() {
//        UUID uuid = UUID.fromString("b3b8c2ac-21ff-434b-b490-aa6d717447c0");
//
//        Optional<Account> accountOptional = accountMOPFacade.find(uuid);
//        assertTrue(accountOptional.isPresent());
//
//        Account foundAccount = accountOptional.get();
//        assertEquals("Jerzy", foundAccount.getName());
//        foundAccount.setName("Mietek");
//
//        accountMOPFacade.edit(foundAccount);
//
//        Optional<Account> editedOptional = accountMOPFacade.find(uuid);
//        assertTrue(editedOptional.isPresent());
//        assertEquals("Mietek", editedOptional.get().getName());
//    }

//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    public void accountMOPFacadeFindAccountByIdTest() throws ApplicationBaseException {
//        UUID uuid = UUID.fromString("b3b8c2ac-21ff-434b-b490-aa6d717447c0");
//        Optional<Account> accountOptional = accountMOPFacade.find(uuid);
//        assertTrue(accountOptional.isPresent());
//
//        Account accountNo1 = accountOptional.get();
//        assertNotNull(accountNo1);
//    }

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
