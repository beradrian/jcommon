package net.sf.jcommon.util;

import static org.junit.Assert.*;

import org.junit.*;

import java.util.Properties;

/**
 */
public class PropertyUtilsTestCase {

    private Properties properties;

    @Before
    public void setUp() throws Exception {
        properties = new Properties();
        properties.put("property1", "value1");
        properties.put("property2", "value2");
        properties.put("property3", "${property1}-${property2}");
    }

    @Test public void testExpandPropertiesForValue() {
        assertEquals("Expanding properties in one string doesn't work.",
            "XXX value1 XXX",
            PropertyUtils.expandProperties("XXX ${property1} XXX", properties));
    }

    @Test public void testExpandAllProperties() {
        PropertyUtils.expandProperties(properties);
        assertEquals("Expanding all properties in one string doesn't work.",
            "value1-value2", properties.get("property3"));
    }

}
