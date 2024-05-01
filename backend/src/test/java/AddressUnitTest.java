import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address;

public class AddressUnitTest {

    public static String CITY;
    public static String CITY_NO_2;
    public static String ZIP_CODE;
    public static String ZIP_CODE_NO_2;
    public static String STREET;
    public static String STREET_NO_2;


    @BeforeAll
    public static void initializeVariables() {
        CITY = "cityNo1";
        CITY_NO_2 = "cityNo2";
        ZIP_CODE = "96-314";
        ZIP_CODE_NO_2 = "94-000";
        STREET = "streetNo1";
        STREET_NO_2 = "streetNo2";
    }

    //Constructor test

    @Test
    void addressConstructorCreatesNotNullObjectTestPositive() {
        Address address = new Address(CITY, ZIP_CODE, STREET);
        Assertions.assertNotNull(address);
    }

    //Getters test

    @Test
    void addressGetCityTestPositive() {
        Address address = new Address(CITY, ZIP_CODE, STREET);
        Assertions.assertEquals(CITY, address.getCity());
    }

    @Test
    void addressGetZipCodeTestPositive() {
        Address address = new Address(CITY, ZIP_CODE, STREET);
        Assertions.assertEquals(ZIP_CODE, address.getZipCode());
    }

    @Test
    void addressGetStreetTestPositive() {
        Address address = new Address(CITY, ZIP_CODE, STREET);
        Assertions.assertEquals(STREET, address.getStreet());
    }

    //Setter tests

    @Test
    void addressSetCityTestPositive() {
        Address address = new Address(CITY, ZIP_CODE, STREET);
        address.setCity(CITY_NO_2);
        Assertions.assertEquals(CITY_NO_2, address.getCity());
    }

    @Test
    void addressSetZipCodeTestPositive() {
        Address address = new Address(CITY, ZIP_CODE, STREET);
        address.setZipCode(ZIP_CODE_NO_2);
        Assertions.assertEquals(ZIP_CODE_NO_2, address.getZipCode());
    }

    @Test
    void addressSetStreetTestPositive() {
        Address address = new Address(CITY, ZIP_CODE, STREET);
        address.setStreet(STREET_NO_2);
        Assertions.assertEquals(STREET_NO_2, address.getStreet());
    }
}
