package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.AttributeName;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.AttributeValue;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AttributeValueUnitTest {

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
    private static AttributeValue TIMEZONE_N10;
    private static AttributeValue TIMEZONE_N11;
    private static AttributeValue TIMEZONE_N12;
    private static AttributeValue TIMEZONE_N13;

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
        TIMEZONE_N10 = new AttributeValue(TIMEZONE_VALUE_NO10, exampleAttribute);
        TIMEZONE_N11 = new AttributeValue(TIMEZONE_VALUE_NO11, exampleAttribute);
        TIMEZONE_N12 = new AttributeValue(TIMEZONE_VALUE_NO12, exampleAttribute);
        TIMEZONE_N13 = new AttributeValue(TIMEZONE_VALUE_NO13, exampleAttribute);

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
                TIMEZONE_N10,
                TIMEZONE_N11,
                TIMEZONE_N12,
                TIMEZONE_N13
        ));

        listOfAttributeValues.set(testAttribute, List.of(
                THEME_NO1,
                THEME_NO2
        ));

        listOfAttributeValues.setAccessible(false);
    }

    @Test
    public void attributeValueNoArgsConstructorTestPositive() {
        AttributeValue attributeValue = new AttributeValue();
        assertNotNull(attributeValue);
    }

    @Test
    public void attributeValueAllArgsConstructorAndGettersTestPositive() {
        assertNotNull(TIMEZONE_NO1);
        assertEquals(TIMEZONE_NO1.getAttributeValue(), TIMEZONE_VALUE_NO1);
        assertEquals(TIMEZONE_NO1.getAttributeNameId(), exampleAttribute);
    }

    @Test
    public void attributeValueSetAttributeValueTestPositive() {
        String attributeValueBefore = TIMEZONE_NO1.getAttributeValue();
        String newAttributeValue = "GMT-1";

        assertNotNull(attributeValueBefore);
        assertNotNull(newAttributeValue);

        TIMEZONE_NO1.setAttributeValue(newAttributeValue);

        String attributeValueAfter = TIMEZONE_NO1.getAttributeValue();

        assertNotNull(attributeValueAfter);

        assertNotEquals(attributeValueBefore, newAttributeValue);
        assertNotEquals(attributeValueBefore, attributeValueAfter);
        assertEquals(newAttributeValue, attributeValueAfter);
    }

    @Test
    public void attributeValueSetAttributeNameTestPositive() {
        AttributeName attributeNameBefore = TIMEZONE_NO1.getAttributeNameId();
        AttributeName newAttributeName = testAttribute;

        assertNotNull(attributeNameBefore);
        assertNotNull(newAttributeName);

        TIMEZONE_NO1.setAttributeNameId(newAttributeName);

        AttributeName attributeNameAfter = TIMEZONE_NO1.getAttributeNameId();

        assertNotNull(attributeNameAfter);

        assertNotEquals(attributeNameBefore, newAttributeName);
        assertNotEquals(attributeNameBefore, attributeNameAfter);
        assertEquals(newAttributeName, attributeNameAfter);
    }

    @Test
    public void attributeValueToStringTestPositive() {
        String result = TIMEZONE_NO1.toString();
        assertFalse(result.isBlank());
        assertFalse(result.isEmpty());
        assertTrue(result.contains("AttributeValue"));
    }
}
