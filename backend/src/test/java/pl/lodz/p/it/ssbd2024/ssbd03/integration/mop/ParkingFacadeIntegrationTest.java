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
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ParkingFacade;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class ParkingFacadeIntegrationTest extends TestcontainersConfig {

    @Autowired
    ParkingFacade parkingFacade;

    private Address address;
    private Parking parking;
    private Sector sector;
    private Reservation reservation;


    @BeforeEach
    public void setup() {
        address = new Address("dd","casc","wqc");
        parking = new Parking(address);
        sector = new Sector(parking,"dd", Sector.SectorType.COVERED,23,11);
        reservation = new Reservation(sector);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeCreateParkingTest() {
        assertNotNull(parking);
        parkingFacade.create(parking);

        assertEquals("dd", parking.getAddress().getCity());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeFindParkingTest(){
        UUID uuid = UUID.fromString("96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1");
        Optional<Parking> retrievedParkingOptional = parkingFacade.find(uuid);
        assertTrue(retrievedParkingOptional.isPresent());

        Parking retrievedParking = retrievedParkingOptional.get();
        assertNotNull(retrievedParking);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeEditParkingTest() {
        Address addressNo1 = new Address("Tes", "Te", "Tes");
        Parking parkingNo1 = new Parking(addressNo1);
        parkingFacade.create(parkingNo1);

        Address newAddress = new Address("New", "NewT", "NewT");
        parkingNo1.setAddress(newAddress);

        parkingFacade.edit(parkingNo1);

        Parking editedParking = parkingFacade.find(parkingNo1.getId()).orElseThrow(NoSuchElementException::new);
        assertEquals(newAddress, editedParking.getAddress());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    void parkingFacadeFindAndRefreshParkingTest() {
        parkingFacade.create(parking);

        UUID parkingId = parking.getId();

        Optional<Parking> optionalParking = parkingFacade.findAndRefresh(parkingId);
        assertTrue(optionalParking.isPresent());

        Parking refreshedParking = optionalParking.get();
        assertNotNull(refreshedParking);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeCountParkingLotsTest() {
        parkingFacade.create(parking);
        int parkingCount = parkingFacade.count();
        assertEquals(2,parkingCount);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeRemoveParkingTest(){
        parkingFacade.create(parking);
        Optional<Parking> retrivedParkingOptional = parkingFacade.find(parking.getId());
        assertTrue(retrivedParkingOptional.isPresent());

        Parking retrievedParking = retrivedParkingOptional.get();
        assertNotNull(retrievedParking);

        parkingFacade.remove(parking);
        Optional<Parking> deletedParkingOptional = parkingFacade.find(parking.getId());
        assertTrue(deletedParkingOptional.isEmpty());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeFindAllParkingLotsTest() {
        List<Parking> listOfParkingLots = parkingFacade.findAll();
        assertNotNull(listOfParkingLots);
        assertFalse(listOfParkingLots.isEmpty());

        listOfParkingLots.add(parking);

        assertEquals(2,listOfParkingLots.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeFindAllParkingLotsWithPaginationTest(){
        Address addressNo1 = new Address("avf","mjh","cdd");
        Address addressNo2 = new Address("ass","mfr","cpd");
        Address addressNo3 = new Address("ap","mja","cdl");

        Parking parkingNo1 = new Parking(addressNo1);
        Parking parkingNo2 = new Parking(addressNo2);
        Parking parkingNo3 = new Parking(addressNo3);

        parkingFacade.create(parkingNo1);
        parkingFacade.create(parkingNo2);
        parkingFacade.create(parkingNo3);

        parkingNo1.addSector("name1", Sector.SectorType.COVERED,100,200);
        parkingNo1.addSector("name2", Sector.SectorType.COVERED,20,200);
        parkingNo2.addSector("name3", Sector.SectorType.UNCOVERED,20,200);
        parkingNo3.addSector("name4", Sector.SectorType.UNCOVERED,20,200);

        List<Parking> listOfParkingLots = parkingFacade.findAllWithPagination(0,10,true);
        assertEquals(4, listOfParkingLots.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeFindParkingBySectorTypesTest() {
        Parking parkingNo1 = new Parking(new Address("fa", "fb", "cf"));
        Parking parkingNo2 = new Parking(new Address("ab", "bbb", "cbf"));
        Parking parkingNo3 = new Parking(new Address("a1", "b11", "cb11f"));
        parkingFacade.create(parkingNo1);
        parkingFacade.create(parkingNo2);
        parkingFacade.create(parkingNo3);

        parkingNo1.addSector("nnn", Sector.SectorType.COVERED,100,100);
        parkingNo2.addSector("mmm", Sector.SectorType.COVERED,100,100);
        parkingNo2.addSector("llll", Sector.SectorType.UNCOVERED,100,100);

        parkingFacade.create(parkingNo1);

        List<Sector.SectorType> sectorTypes = List.of(Sector.SectorType.COVERED);
        List<Parking> listOfParkingLots = parkingFacade.findParkingsBySectorTypes(sectorTypes,0,5,true);
        assertEquals(2, listOfParkingLots.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeFindSectorsInParkingWithPaginationTest(){
        Address addressNo1 = new Address("a","b","c");
        Parking parkingNo1 = new Parking(addressNo1);
        parkingNo1.addSector("name1", Sector.SectorType.COVERED,100,200);
        parkingNo1.addSector("name2", Sector.SectorType.UNCOVERED,20,200);
        parkingNo1.addSector("name3", Sector.SectorType.UNCOVERED,20,200);

        parkingFacade.create(parkingNo1);

        List<Sector> listOfSectors = parkingFacade.findSectorsInParkingWithPagination(parkingNo1.getId(),0,4,true);

        assertEquals(3,listOfSectors.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeFindSectorInParkingWithAvailablePlacesTest(){
        Address addressNo1 = new Address("a","b","c");
        Parking parkingNo1 = new Parking(addressNo1);

        parkingFacade.create(parkingNo1);
        parkingNo1.addSector("name1", Sector.SectorType.COVERED,100,200);
        parkingNo1.addSector("name2", Sector.SectorType.UNCOVERED,10,200);
        parkingNo1.addSector("name3", Sector.SectorType.UNCOVERED,10,200);

        List<Sector> listOfSectorsNo1 = parkingNo1.getSectors();
        for (int i = 0; i < listOfSectorsNo1.size(); i++){
            listOfSectorsNo1.get(i).setAvailablePlaces(i);
        }

        List<Sector> listOfSectorsNo2 = parkingFacade.findSectorInParkingWithAvailablePlaces(parkingNo1.getId(),0,15,true);

        assertEquals(2,listOfSectorsNo2.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeFindParkingWithAvailablePlacesTest(){
        Address addressNo1 = new Address("a","b","c");
        Address addressNo2 = new Address("ar","br","rc");
        Parking parkingNo1 = new Parking(addressNo1);
        Parking parkingNo2 = new Parking(addressNo2);

        parkingFacade.create(parkingNo1);
        parkingNo1.addSector("name1", Sector.SectorType.COVERED,100,200);
        parkingNo1.addSector("name2", Sector.SectorType.UNCOVERED,10,200);
        parkingNo1.addSector("name3", Sector.SectorType.UNCOVERED,10,200);
        parkingNo2.addSector("name4", Sector.SectorType.UNCOVERED,10,200);

        List<Sector> listOfSectors = parkingNo1.getSectors();
        for (int i = 0; i < listOfSectors.size(); i++){
            listOfSectors.get(i).setAvailablePlaces(i);
        }
        List<Parking> listOfParkingLots = parkingFacade.findParkingWithAvailablePlaces(0,5,true);
        assertEquals(2, listOfParkingLots.size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeEditSectorTest(){
        Address addressNo1 = new Address("Tes", "Te", "Tes");
        Parking parkingNo1 = new Parking(addressNo1);

        parkingFacade.create(parkingNo1);
        parkingNo1.addSector("test", Sector.SectorType.COVERED, 100, 100);
        Sector sectorNo1 = new Sector(parkingNo1, "tt11", Sector.SectorType.COVERED, 100, 100);

        sectorNo1.setMaxPlaces(200);

        parkingFacade.editSector(sectorNo1);

        assertEquals(1, parkingNo1.getSectors().size());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeRemoveSectorTest(){
        parkingFacade.removeSector(sector);
        List<Sector> listOfSectors = new ArrayList<>();
        assertEquals(listOfSectors, parking.getSectors());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void parkingFacadeFindSectorInParkingBySectorTypesTest(){
        Address addressNo1 = new Address("tt", "Mi", "ttt");
        Address addressNo2 = new Address("dtt", "Mei", "ttet");
        Parking parkingNo1 = new Parking(addressNo1);
        Parking parkingNo2 = new Parking(addressNo2);

        parkingNo1.addSector("Se2", Sector.SectorType.UNCOVERED, 20, 200);
        parkingNo1.addSector("St3", Sector.SectorType.UNCOVERED, 30, 300);
        parkingNo1.addSector("St4", Sector.SectorType.UNCOVERED, 30, 300);
        parkingNo2.addSector("St42", Sector.SectorType.UNCOVERED, 30, 300);

        List<Sector.SectorType> sectorTypes = List.of(Sector.SectorType.COVERED);
        List<Sector> listOfSectorsNo1 = parkingFacade.findSectorInParkingBySectorTypes(parkingNo1.getId(), sectorTypes, 0, 10, true);
        List<Sector> listOfSectorsNo2 = parkingFacade.findSectorInParkingBySectorTypes(parkingNo1.getId(), sectorTypes, 0, 10, true);

        assertEquals(0, listOfSectorsNo1.size());
        assertEquals(0, listOfSectorsNo2.size());
    }
}
