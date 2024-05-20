package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(CITY, address.getCity());
    }

    @Test
    void addressGetZipCodeTestPositive() {
        Address address = new Address(CITY, ZIP_CODE, STREET);
        assertEquals(ZIP_CODE, address.getZipCode());
    }

    @Test
    void addressGetStreetTestPositive() {
        Address address = new Address(CITY, ZIP_CODE, STREET);
        assertEquals(STREET, address.getStreet());
    }

    //Setter tests

    @Test
    void addressSetCityTestPositive() {
        Address address = new Address(CITY, ZIP_CODE, STREET);
        address.setCity(CITY_NO_2);
        assertEquals(CITY_NO_2, address.getCity());
    }

    @Test
    void addressSetZipCodeTestPositive() {
        Address address = new Address(CITY, ZIP_CODE, STREET);
        address.setZipCode(ZIP_CODE_NO_2);
        assertEquals(ZIP_CODE_NO_2, address.getZipCode());
    }

    @Test
    void addressSetStreetTestPositive() {
        Address address = new Address(CITY, ZIP_CODE, STREET);
        address.setStreet(STREET_NO_2);
        assertEquals(STREET_NO_2, address.getStreet());
    }

    @Test
    void addressToStringTestPositive() {
        Address addressNo1 = new Address(CITY, ZIP_CODE, STREET);
        Address addressNo2 = new Address(CITY_NO_2, ZIP_CODE_NO_2, STREET_NO_2);

        String addressStringNo1 = addressNo1.toString();
        String addressStringNo2 = addressNo2.toString();

        assertNotNull(addressStringNo1);
        assertNotNull(addressStringNo2);
        assertFalse(addressStringNo1.isEmpty());
        assertFalse(addressStringNo2.isEmpty());
        assertFalse(addressStringNo1.isBlank());
        assertFalse(addressStringNo2.isBlank());
    }

    @Test
    void addressHashCodeTestPositive() {
        Address addressNo1 = new Address(CITY, ZIP_CODE, STREET);
        Address addressNo2 = new Address(CITY, ZIP_CODE, STREET);

        assertEquals(addressNo1.hashCode(), addressNo2.hashCode());
    }

    @Test
    void addressHashCodeTestNegative() {
        Address addressNo1 = new Address(CITY, ZIP_CODE, STREET);
        Address addressNo2 = new Address(CITY_NO_2, ZIP_CODE_NO_2, STREET_NO_2);
        Address addressNo3 = new Address();

        assertNotEquals(addressNo1.hashCode(), addressNo2.hashCode());
        assertNotEquals(addressNo1.hashCode(), addressNo3.hashCode());
    }

    @Test
    void addressEqualsTestPositive() {
        class AddressOther extends Address {
            public AddressOther(String city, String zipCode, String street) {
                super(city, zipCode, street);
            }
        }

        Address addressNo1 = new Address(CITY, ZIP_CODE, STREET);
        Address addressNo12 = new Address(CITY, ZIP_CODE, STREET);
        Address addressNo2 = new Address(CITY_NO_2, ZIP_CODE_NO_2, STREET_NO_2);
        String addressNo3 = CITY + ", " + ZIP_CODE + ", " + STREET;
        Address addressNo6 = new Address(CITY, null, null);
        Address addressNo9 = new Address(CITY, null, STREET);
        Address addressNo7 = new Address(null, ZIP_CODE, null);
        Address addressNo8 = new Address(null, null, STREET);
        Address addressNo10 = new Address(CITY, ZIP_CODE, null);
        Address addressNo11 = new Address(CITY, ZIP_CODE, null);
        AddressOther addressOther = new AddressOther(CITY, ZIP_CODE, STREET);


        assertEquals(addressNo1, addressNo1);
        assertNotEquals(addressNo1, addressNo3);
        assertNotEquals(addressNo1, addressNo2);
        assertNotEquals(addressNo7, addressNo8);
        assertNotEquals(addressNo8, addressNo1);
        assertNotEquals(addressNo1, addressNo6);
        assertNotEquals(addressNo6, addressNo9);
        assertNotEquals(addressNo6, addressNo1);
        assertNotEquals(addressNo1, addressNo10);
        assertEquals(addressNo10, addressNo11);
        assertEquals(addressNo1, addressNo12);
    }
}
