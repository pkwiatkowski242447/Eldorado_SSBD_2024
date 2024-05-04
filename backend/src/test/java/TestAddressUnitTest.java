import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address;

public class TestAddressUnitTest {

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

    @Test
    void addressToStringTestPositive() {
        Address addressNo1 = new Address(CITY, ZIP_CODE, STREET);
        Address addressNo2 = new Address(CITY_NO_2, ZIP_CODE_NO_2, STREET_NO_2);

        String addressStringNo1 = "Address(city=" + CITY + ", zipCode=" + ZIP_CODE + ", street=" + STREET + ")";
        String addressStringNo2 = "Address(city=" + CITY_NO_2 + ", zipCode=" + ZIP_CODE_NO_2 + ", street=" + STREET_NO_2 + ")";

        Assertions.assertEquals(addressStringNo1, addressNo1.toString());
        Assertions.assertEquals(addressStringNo2, addressNo2.toString());
    }

    @Test
    void addressHashCodeTestPositive() {
        Address addressNo1 = new Address(CITY, ZIP_CODE, STREET);
        Address addressNo2 = new Address(CITY, ZIP_CODE, STREET);

        Assertions.assertEquals(addressNo1.hashCode(), addressNo2.hashCode());
    }

    @Test
    void addressHashCodeTestNegative() {
        Address addressNo1 = new Address(CITY, ZIP_CODE, STREET);
        Address addressNo2 = new Address(CITY_NO_2, ZIP_CODE_NO_2, STREET_NO_2);
        Address addressNo3 = new Address();

        Assertions.assertNotEquals(addressNo1.hashCode(), addressNo2.hashCode());
        Assertions.assertNotEquals(addressNo1.hashCode(), addressNo3.hashCode());
    }

    @Test
    void addressEqualsTestPositive() {
        class AddressOther extends Address {
            public AddressOther(String city, String zipCode, String street) {
                super(city, zipCode, street);
            }
            @Override
            protected boolean canEqual(Object other) {
                return false;
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


        Assertions.assertTrue(addressNo1.equals(addressNo1)); // pierwsze if - o == this
        Assertions.assertFalse(addressNo1.equals(addressNo3)); // drugie false - nie jest instanceof
        Assertions.assertFalse(addressNo1.equals(addressNo2)); // zwraca false w pierwszej petli
        Assertions.assertFalse(addressNo7.equals(addressNo8));
        Assertions.assertFalse(addressNo8.equals(addressNo1));
        Assertions.assertFalse(addressNo1.equals(addressNo6));
        Assertions.assertFalse(addressNo6.equals(addressNo9));
        Assertions.assertFalse(addressNo6.equals(addressNo1));
        Assertions.assertFalse(addressNo1.equals(addressNo10));
        Assertions.assertTrue(addressNo10.equals(addressNo11));
        Assertions.assertTrue(addressNo1.equals(addressNo12));
        Assertions.assertFalse(addressNo1.equals(addressOther));
    }
}
