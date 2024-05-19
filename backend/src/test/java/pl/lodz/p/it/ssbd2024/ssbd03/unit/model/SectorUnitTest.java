package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SectorUnitTest {

    //ADDRESS DATA
    public static String CITY;
    public static String ZIP_CODE;
    public static String STREET;

    //PARKING DATA
    public static Address ADDRESS;
    public static Parking PARKING;

    //SECTOR DATA
    public static String SECTOR_NAME_NO_1;
    public static String SECTOR_NAME_NO_2;
    public static Sector.SectorType SECTOR_TYPE_NO_1;
    public static Sector.SectorType SECTOR_TYPE_NO_2;
    public static Integer MAX_PLACES_NO_1;
    public static Integer MAX_PLACES_NO_2;
    public static Integer MAX_WEIGHT_NO_1;
    public static Integer MAX_WEIGHT_NO_2;
    public static Integer AVAILABLE_PLACES_NO_1;

    @BeforeAll
    public static void initializeVariables() {
        CITY = "cityNo1";
        ZIP_CODE = "96-314";
        STREET = "streetNo1";

        ADDRESS = new Address(CITY, ZIP_CODE, STREET);
        PARKING = new Parking(ADDRESS);

        SECTOR_NAME_NO_1 = "sectorNameNo1";
        SECTOR_NAME_NO_2 = "sectorNameNo2";
        SECTOR_TYPE_NO_1 = Sector.SectorType.COVERED;
        SECTOR_TYPE_NO_2 = Sector.SectorType.UNDERGROUND;
        MAX_PLACES_NO_1 = 20;
        MAX_PLACES_NO_2 = 10;
        MAX_WEIGHT_NO_1 = 40;
        MAX_WEIGHT_NO_2 = 5;
        AVAILABLE_PLACES_NO_1 = 19;
    }

    @Test
    void sectorConstructorNotNullObjectPositiveTest() {
        Sector sector = new Sector(PARKING, SECTOR_NAME_NO_1, SECTOR_TYPE_NO_1, MAX_PLACES_NO_1, MAX_WEIGHT_NO_1);
        assertNotNull(sector);
    }

    @Test
    void sectorGetParkingPositiveTest() {
        Sector sector = new Sector(PARKING, SECTOR_NAME_NO_1, SECTOR_TYPE_NO_1, MAX_PLACES_NO_1, MAX_WEIGHT_NO_1);
        Assertions.assertEquals(PARKING, sector.getParking());
    }

    @Test
    void sectorGetSectorNamePositiveTest() {
        Sector sector = new Sector(PARKING, SECTOR_NAME_NO_1, SECTOR_TYPE_NO_1, MAX_PLACES_NO_1, MAX_WEIGHT_NO_1);
        Assertions.assertEquals(SECTOR_NAME_NO_1, sector.getName());
    }

    @Test
    void sectorGetSectorTypePositiveTest() {
        Sector sector = new Sector(PARKING, SECTOR_NAME_NO_1, SECTOR_TYPE_NO_1, MAX_PLACES_NO_1, MAX_WEIGHT_NO_1);
        Assertions.assertEquals(SECTOR_TYPE_NO_1, sector.getType());
    }

    @Test
    void sectorGetSectorMaxPlacesPositiveTest() {
        Sector sector = new Sector(PARKING, SECTOR_NAME_NO_1, SECTOR_TYPE_NO_1, MAX_PLACES_NO_1, MAX_WEIGHT_NO_1);
        Assertions.assertEquals(MAX_PLACES_NO_1, sector.getMaxPlaces());
    }

    @Test
    void sectorGetSectorWeightPositiveTest() {
        Sector sector = new Sector(PARKING, SECTOR_NAME_NO_1, SECTOR_TYPE_NO_1, MAX_PLACES_NO_1, MAX_WEIGHT_NO_1);
        Assertions.assertEquals(MAX_WEIGHT_NO_1, sector.getWeight());
    }

    @Test
    void sectorGetAvailablePlacesPositiveTest() {
        Sector sector = new Sector(PARKING, SECTOR_NAME_NO_1, SECTOR_TYPE_NO_1, MAX_PLACES_NO_1, MAX_WEIGHT_NO_1);
        Assertions.assertEquals(MAX_PLACES_NO_1, sector.getAvailablePlaces());
    }

    @Test
    void sectorSetSectorNamePositiveTest() {
        Sector sector = new Sector(PARKING, SECTOR_NAME_NO_1, SECTOR_TYPE_NO_1, MAX_PLACES_NO_1, MAX_WEIGHT_NO_1);
        sector.setName(SECTOR_NAME_NO_2);

        Assertions.assertEquals(SECTOR_NAME_NO_2, sector.getName());
    }

    @Test
    void sectorSetSectorTypePositiveTest() {
        Sector sector = new Sector(PARKING, SECTOR_NAME_NO_1, SECTOR_TYPE_NO_1, MAX_PLACES_NO_1, MAX_WEIGHT_NO_1);
        sector.setType(SECTOR_TYPE_NO_2);

        Assertions.assertEquals(SECTOR_TYPE_NO_2, sector.getType());
    }

    @Test
    void sectorSetMaxPlacesPositiveTest() {
        Sector sector = new Sector(PARKING, SECTOR_NAME_NO_1, SECTOR_TYPE_NO_1, MAX_PLACES_NO_1, MAX_WEIGHT_NO_1);
        sector.setMaxPlaces(MAX_PLACES_NO_2);

        Assertions.assertEquals(MAX_PLACES_NO_2, sector.getMaxPlaces());
    }

    @Test
    void sectorSetAvailablePlacesPositiveTest() {
        Sector sector = new Sector(PARKING, SECTOR_NAME_NO_1, SECTOR_TYPE_NO_1, MAX_PLACES_NO_1, MAX_WEIGHT_NO_1);
        sector.setAvailablePlaces(AVAILABLE_PLACES_NO_1);

        Assertions.assertEquals(AVAILABLE_PLACES_NO_1, sector.getAvailablePlaces());
    }

    @Test
    void sectorSetSectorWeightPositiveTest() {
        Sector sector = new Sector(PARKING, SECTOR_NAME_NO_1, SECTOR_TYPE_NO_1, MAX_PLACES_NO_1, MAX_WEIGHT_NO_1);
        sector.setWeight(MAX_WEIGHT_NO_2);

        Assertions.assertEquals(MAX_WEIGHT_NO_2, sector.getWeight());
    }

    @Test
    void sectorToStringPositiveTest() {
        Sector sector = new Sector(PARKING, SECTOR_NAME_NO_1, SECTOR_TYPE_NO_1, MAX_PLACES_NO_1, MAX_WEIGHT_NO_1);
        String testString = sector.toString();
        assertNotNull(testString);
        assertFalse(testString.isEmpty());
        assertFalse(testString.isBlank());
    }
}
