package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.AttributeName;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.AttributeValue;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AttributeNameUnitTest {

    private static final String ATTRIBUTE_NAME_NO1 = "Timezone";
    private static final String ATTRIBUTE_NAME_NO2 = "Theme";

    private static final String TIMEZONE_VALUE_NO1 = "GMT+0";
    private static final String TIMEZONE_VALUE_NO2 = "GMT+1";
    private static final String TIMEZONE_VALUE_NO3 = "GMT+2";
    private static final String TIMEZONE_VALUE_NO4 = "GMT+3";
    private static final String TIMEZONE_VALUE_NO5 = "GMT+4";
    private static final String TIMEZONE_VALUE_NO6 = "GMT+5";
    private static final String TIMEZONE_VALUE_NO7 = "GMT+6";
    private static final String TIMEZONE_VALUE_NO8 = "GMT+7";
    private static final String TIMEZONE_VALUE_NO9 = "GMT+8";
    private static final String TIMEZONE_VALUE_NO10 = "GMT+9";
    private static final String TIMEZONE_VALUE_NO11 = "GMT+10";
    private static final String TIMEZONE_VALUE_NO12 = "GMT+11";
    private static final String TIMEZONE_VALUE_NO13 = "GMT+12";

    private static final String THEME_VALUE_NO1 = "Dark";
    private static final String THEME_VALUE_NO2 = "Light";

    private static AttributeValue TIMEZONE_NO1;
    private static AttributeValue TIMEZONE_NO2;
    private static AttributeValue TIMEZONE_NO3;
    private static AttributeValue TIMEZONE_NO4;
    private static AttributeValue TIMEZONE_NO5;
    private static AttributeValue TIMEZONE_NO6;
    private static AttributeValue TIMEZONE_NO7;
    private static AttributeValue TIMEZONE_NO8;
    private static AttributeValue TIMEZONE_NO9;
    private static AttributeValue TIMEZONE_NO10;
    private static AttributeValue TIMEZONE_NO11;
    private static AttributeValue TIMEZONE_NO12;
    private static AttributeValue TIMEZONE_NO13;

    private static AttributeValue THEME_NO1;
    private static AttributeValue THEME_NO2;

    private static AttributeName exampleAttribute;
    private static AttributeName testAttribute;

    @BeforeAll
    public static void setUp() throws Exception {
        exampleAttribute = new AttributeName(ATTRIBUTE_NAME_NO1);

        TIMEZONE_NO1 = new AttributeValue(TIMEZONE_VALUE_NO1, exampleAttribute);
        TIMEZONE_NO2 = new AttributeValue(TIMEZONE_VALUE_NO2, exampleAttribute);
        TIMEZONE_NO3 = new AttributeValue(TIMEZONE_VALUE_NO3, exampleAttribute);
        TIMEZONE_NO4 = new AttributeValue(TIMEZONE_VALUE_NO4, exampleAttribute);
        TIMEZONE_NO5 = new AttributeValue(TIMEZONE_VALUE_NO5, exampleAttribute);
        TIMEZONE_NO6 = new AttributeValue(TIMEZONE_VALUE_NO6, exampleAttribute);
        TIMEZONE_NO7 = new AttributeValue(TIMEZONE_VALUE_NO7, exampleAttribute);
        TIMEZONE_NO8 = new AttributeValue(TIMEZONE_VALUE_NO8, exampleAttribute);
        TIMEZONE_NO9 = new AttributeValue(TIMEZONE_VALUE_NO9, exampleAttribute);
        TIMEZONE_NO10 = new AttributeValue(TIMEZONE_VALUE_NO10, exampleAttribute);
        TIMEZONE_NO11 = new AttributeValue(TIMEZONE_VALUE_NO11, exampleAttribute);
        TIMEZONE_NO12 = new AttributeValue(TIMEZONE_VALUE_NO12, exampleAttribute);
        TIMEZONE_NO13 = new AttributeValue(TIMEZONE_VALUE_NO13, exampleAttribute);

        testAttribute = new AttributeName(ATTRIBUTE_NAME_NO2);

        THEME_NO1 = new AttributeValue(THEME_VALUE_NO1, testAttribute);
        THEME_NO2 = new AttributeValue(THEME_VALUE_NO2, testAttribute);

        Field listOfAttributeValues = AttributeName.class.getDeclaredField("listOfAttributeValues");
        listOfAttributeValues.setAccessible(true);

        listOfAttributeValues.set(exampleAttribute, List.of(
                TIMEZONE_NO1,
                TIMEZONE_NO2,
                TIMEZONE_NO3,
                TIMEZONE_NO4,
                TIMEZONE_NO5,
                TIMEZONE_NO6,
                TIMEZONE_NO7,
                TIMEZONE_NO8,
                TIMEZONE_NO9,
                TIMEZONE_NO10,
                TIMEZONE_NO11,
                TIMEZONE_NO12,
                TIMEZONE_NO13
        ));

        listOfAttributeValues.set(testAttribute, List.of(
                THEME_NO1,
                THEME_NO2
        ));

        listOfAttributeValues.setAccessible(false);
    }

    @Test
    public void attributeNameNoArgsConstructorTestPositive() {
        AttributeName attributeName = new AttributeName();
        assertNotNull(attributeName);
    }

    @Test
    public void attributeNameAllArgsConstructorAndGettersTestPositive() {
        assertNotNull(exampleAttribute);
        assertFalse(exampleAttribute.getListOfAttributeValues().isEmpty());
        assertEquals(13, exampleAttribute.getListOfAttributeValues().size());
        assertTrue(exampleAttribute.getListOfAttributeValues().contains(TIMEZONE_NO1));
        assertTrue(exampleAttribute.getListOfAttributeValues().contains(TIMEZONE_NO2));
        assertTrue(exampleAttribute.getListOfAttributeValues().contains(TIMEZONE_NO3));
        assertTrue(exampleAttribute.getListOfAttributeValues().contains(TIMEZONE_NO4));
        assertTrue(exampleAttribute.getListOfAttributeValues().contains(TIMEZONE_NO5));
        assertTrue(exampleAttribute.getListOfAttributeValues().contains(TIMEZONE_NO6));
        assertTrue(exampleAttribute.getListOfAttributeValues().contains(TIMEZONE_NO7));
        assertTrue(exampleAttribute.getListOfAttributeValues().contains(TIMEZONE_NO8));
        assertTrue(exampleAttribute.getListOfAttributeValues().contains(TIMEZONE_NO9));
        assertTrue(exampleAttribute.getListOfAttributeValues().contains(TIMEZONE_NO10));
        assertTrue(exampleAttribute.getListOfAttributeValues().contains(TIMEZONE_NO11));
        assertTrue(exampleAttribute.getListOfAttributeValues().contains(TIMEZONE_NO12));
        assertTrue(exampleAttribute.getListOfAttributeValues().contains(TIMEZONE_NO13));
        assertEquals(exampleAttribute.getAttributeName(), ATTRIBUTE_NAME_NO1);
    }

    @Test
    public void attributeNameSetAttributeNameTestPositive() {
        String attributeNameBefore = testAttribute.getAttributeName();
        String newAttributeName = "OtherTheme";

        assertNotNull(attributeNameBefore);
        assertNotNull(newAttributeName);

        testAttribute.setAttributeName(newAttributeName);

        String attributeNameAfter = testAttribute.getAttributeName();

        assertNotNull(attributeNameAfter);

        assertNotEquals(attributeNameBefore, newAttributeName);
        assertNotEquals(attributeNameBefore, attributeNameAfter);
        assertEquals(attributeNameAfter, newAttributeName);
    }

    @Test
    public void attributeNameToStringTestPositive() {
        String result = exampleAttribute.toString();
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
        assertTrue(result.contains("AttributeName"));
    }
}
