package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

public class ParkingUnitTest {

    //ADDRESS DATA
    public static String CITY;
    public static String CITY_NO_2;
    public static String ZIP_CODE;
    public static String ZIP_CODE_NO_2;
    public static String STREET;
    public static String STREET_NO_2;
    public static Address ADDRESS;
    public static Address ADDRESS_NO_2;

    //SECTOR DATA
    public static String SECTOR_NAME_NO_1;
    public static String SECTOR_NAME_NO_2;
    public static Sector.SectorType SECTOR_TYPE_NO_1;
    public static Sector.SectorType SECTOR_TYPE_NO_2;
    public static Integer MAX_PLACES_NO_1;
    public static Integer MAX_PLACES_NO_2;
    public static Integer MAX_WEIGHT_NO_1;
    public static Integer MAX_WEIGHT_NO_2;

    @BeforeAll
    public static void initializeVariables() {
        CITY = "cityNo1";
        CITY_NO_2 = "cityNo2";
        ZIP_CODE = "96-314";
        ZIP_CODE_NO_2 = "96-000";
        STREET = "streetNo1";
        STREET_NO_2 = "streetNo2";
        SECTOR_NAME_NO_1 = "sectorNameNo1";
        SECTOR_NAME_NO_2 = "sectorNameNo2";
        SECTOR_TYPE_NO_1 = Sector.SectorType.COVERED;
        SECTOR_TYPE_NO_2 = Sector.SectorType.UNDERGROUND;
        MAX_PLACES_NO_1 = 20;
        MAX_PLACES_NO_2 = 10;
        MAX_WEIGHT_NO_1 = 40;
        MAX_WEIGHT_NO_2 = 5;
        ADDRESS = new Address(CITY, ZIP_CODE, STREET);
        ADDRESS_NO_2 = new Address(CITY_NO_2, ZIP_CODE_NO_2, STREET_NO_2);
    }

    @Test
    void parkingConstructorNotNullObjectPositiveTest() {
        Parking parking = new Parking(ADDRESS);
        Assertions.assertNotNull(parking);
    }

    @Test
    void parkingGetAddressTestPositive() {
        Parking parking = new Parking(ADDRESS);
        Assertions.assertEquals(ADDRESS, parking.getAddress());
    }

    @Test
    void parkingGetSectorsTestPositive() {
        Parking parking = new Parking(ADDRESS);
        Assertions.assertNotNull(parking.getSectors());
        Assertions.assertEquals(0, parking.getSectors().size());
    }

    @Test
    void parkingSetAddressTestPositive() {
        Parking parking = new Parking(ADDRESS);
        parking.setAddress(ADDRESS_NO_2);
        Assertions.assertEquals(ADDRESS_NO_2, parking.getAddress());
    }

    @Test
    void parkingAddSectorPositiveTest() {
        Parking parking = new Parking(ADDRESS);
        parking.addSector(SECTOR_NAME_NO_1, SECTOR_TYPE_NO_1, MAX_PLACES_NO_1, MAX_WEIGHT_NO_1);
        Assertions.assertEquals(1, parking.getSectors().size());

        Sector sector = parking.getSectors().getFirst();
        Assertions.assertEquals(SECTOR_NAME_NO_1, sector.getName());
        Assertions.assertEquals(SECTOR_TYPE_NO_1, sector.getType());
        Assertions.assertEquals(MAX_PLACES_NO_1, sector.getMaxPlaces());
        Assertions.assertEquals(MAX_WEIGHT_NO_1, sector.getWeight());
    }

    @Test
    void parkingDeleteSectorPositiveText() {
        Parking parking = new Parking(ADDRESS);
        parking.addSector(SECTOR_NAME_NO_1, SECTOR_TYPE_NO_1, MAX_PLACES_NO_1, MAX_WEIGHT_NO_1);
        parking.addSector(SECTOR_NAME_NO_2, SECTOR_TYPE_NO_2, MAX_PLACES_NO_2, MAX_WEIGHT_NO_2);

        parking.deleteSector(SECTOR_NAME_NO_1);
        Sector sector = parking.getSectors().getFirst();

        Assertions.assertEquals(1, parking.getSectors().size());
        Assertions.assertEquals(SECTOR_NAME_NO_2, sector.getName());
        Assertions.assertEquals(SECTOR_TYPE_NO_2, sector.getType());
        Assertions.assertEquals(MAX_PLACES_NO_2, sector.getMaxPlaces());
        Assertions.assertEquals(MAX_WEIGHT_NO_2, sector.getWeight());
    }
}
