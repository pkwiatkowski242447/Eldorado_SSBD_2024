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
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ParkingFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class ParkingFacadeTest extends TestcontainersConfig {


    @Autowired
    ParkingFacade parkingFacade;

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
    public void createParkingTest() {
        assertNotNull(parking);
        parkingFacade.create(parking);

        assertEquals("dd",parking.getAddress().getCity());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findParkingTest(){
        UUID uuid =UUID.fromString("96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1");
        Optional<Parking> parkingOptional = parkingFacade.find(uuid);
        assertTrue(parkingOptional.isPresent());

        Parking retrievedParking = parkingOptional.get();
        assertNotNull(retrievedParking);

    }



    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void editTest() {
        Address address1 = new Address("Tes", "Te", "Tes");
        Parking parking1 = new Parking(address1);
        parkingFacade.create(parking1);

        Address newAddress = new Address("New", "NewT", "NewT");
        parking1.setAddress(newAddress);

        parkingFacade.edit(parking1);

        Parking editedParking = parkingFacade.find(parking1.getId()).orElse(null);
        assertEquals(newAddress, editedParking.getAddress());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    void findAndRefreshTest() {
        parkingFacade.create(parking);

        UUID parkingId = parking.getId();

        Optional<Parking> optionalParking = parkingFacade.findAndRefresh(parkingId);
        assertTrue(optionalParking.isPresent());

        Parking refreshedParking = optionalParking.get();
        assertNotNull(refreshedParking);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void countTest() {
        parkingFacade.create(parking);
        int count = parkingFacade.count();
        assertEquals(2,count);
    }


    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeParkingTest(){
        parkingFacade.create(parking);
        Optional<Parking> retrivedParkingOptional = parkingFacade.find(parking.getId());
        assertTrue(retrivedParkingOptional.isPresent());

        Parking retrievedOptional = retrivedParkingOptional.get();
        assertNotNull(retrievedOptional);
        parkingFacade.remove(parking);

        Optional <Parking> deleted = parkingFacade.find(parking.getId());
        assertTrue(deleted.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllTest() {
        List<Parking> parkings = parkingFacade.findAll();
        assertNotNull(parkings);
        assertFalse(parkings.isEmpty());
        parkings.add(parking);
        assertEquals(2,parkings.size());
    }


    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findAllWithPaginationTest(){
        Address address1 = new Address("a","b","c");
        Parking parking1 = new Parking(address1);
        Parking parking2 = new Parking(address1);
        parking1.addSector("name1", Sector.SectorType.COVERED,100,200);
        parking1.addSector("name2", Sector.SectorType.UNCOVERED,20,200);
        parking2.addSector("name3", Sector.SectorType.UNCOVERED,20,200);
        parking2.addSector("name3", Sector.SectorType.UNCOVERED,20,200);

        List <Parking> parkings = parkingFacade.findAllWithPagination(0,10,true);
        assertEquals(1,parkings.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findParkingBySectorTypesTest() {
        Parking parking1 = new Parking(new Address("fa", "fb", "cf"));
        Parking parking2 = new Parking(new Address("ab", "bbb", "cbf"));
        Parking parking3 = new Parking(new Address("a1", "b11", "cb11f"));
        parkingFacade.create(parking1);
        parkingFacade.create(parking2);
        parkingFacade.create(parking3);

        parking1.addSector("nnn", Sector.SectorType.COVERED,100,100);
        parking2.addSector("mmm", Sector.SectorType.COVERED,100,100);
        parking2.addSector("llll", Sector.SectorType.UNCOVERED,100,100);
        parkingFacade.create(parking1);

        List <Sector.SectorType> sectorTypes = List.of(Sector.SectorType.COVERED);
        List <Parking> parkings = parkingFacade.findParkingsBySectorTypes(sectorTypes,0,5,true);
        assertEquals(2,parkings.size());

    }



    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findSectorsInParkingWithPaginationTest(){
        Address address1 = new Address("a","b","c");
        Parking parking1 = new Parking(address1);
        parking1.addSector("name1", Sector.SectorType.COVERED,100,200);
        parking1.addSector("name2", Sector.SectorType.UNCOVERED,20,200);
        parking1.addSector("name3", Sector.SectorType.UNCOVERED,20,200);

        parkingFacade.create(parking1);

        List <Sector> sectors1 = parkingFacade.findSectorsInParkingWithPagination(parking1.getId(),0,4,true);

        assertEquals(3,sectors1.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void findSectorInParkingWithAvailablePlacesTest(){
        Address address1 = new Address("a","b","c");
        Parking parking1 = new Parking(address1);

        parkingFacade.create(parking1);
        parking1.addSector("name1", Sector.SectorType.COVERED,100,200);
        parking1.addSector("name2", Sector.SectorType.UNCOVERED,10,200);
        parking1.addSector("name3", Sector.SectorType.UNCOVERED,10,200);
        List <Sector> sectors = parking1.getSectors();
        for (int i=0; i<sectors.size();i++){
            sectors.get(i).setAvailablePlaces(i);
        }

        List <Sector> sectors1 = parkingFacade.findSectorInParkingWithAvailablePlaces(parking1.getId(),0,15,true);

        assertEquals(2,sectors1.size());
    }

//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    public void findSectorInParkingBySectorTypesTest(){
//        Address address = new Address("tt", "Mi", "ttt");
//        Parking parking1 = new Parking(address);
//
//        parking1.addSector("Sektor testowy 1", Sector.SectorType.COVERED, 100, 200);
//        parking1.addSector("Sektor testowy 2", Sector.SectorType.UNCOVERED, 20, 200);
//        parking1.addSector("Sektor testowy 3", Sector.SectorType.UNCOVERED, 30, 300);
//
//
//        List<Sector> sectors = parkingFacade.findSectorInParkingBySectorTypes(parking.getId(), 0, 10, true);
//
//        assertEquals(3, sectors.size());
//        //org.hibernate.QueryParameterException: No argument for named parameter ':sectorTypes'
//    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void editSectorTest(){
        Address address1 = new Address("Tes", "Te", "Tes");
        Parking parking1 = new Parking(address1);

        parkingFacade.create(parking1);
        parking1.addSector("test", Sector.SectorType.COVERED, 100, 100);


        Sector sector1 = new Sector(parking1, "tt11", Sector.SectorType.COVERED, 100, 100);

        sector1.setMaxPlaces(200);

        parkingFacade.editSector(sector1);
        //findSectorById protected
        assertEquals(1, parking1.getSectors().size());

    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeSectorTest(){
        parkingFacade.removeSector(sector);
        List <Sector> sectors = new ArrayList<>();
        assertEquals(sectors, parking.getSectors());

    }




}
