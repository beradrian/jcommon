package net.sf.jcommon.util;

import static org.junit.Assert.*;

import org.junit.*;

/**
 */
public class StringUtilsTestCase {

    @Test public void testToCamelCase() {
        assertEquals("toCamelCase1", "test", StringUtils.toCamelCase("test"));
        assertEquals("toCamelCase2", "Test", StringUtils.toCamelCase("Test"));
        assertEquals("toCamelCase3", "Testing", StringUtils.toCamelCase("TEStinG"));
        assertEquals("toCamelCase4", "testToCamelCase", StringUtils.toCamelCase("test_to_camel_case"));
        assertEquals("toCamelCase5", "TestToCamelCase", StringUtils.toCamelCase("TEST_TO_CAMEL_CASE"));
        assertEquals("toCamelCase6", "TestToCamelCase", StringUtils.toCamelCase("TEST_To_cAmEl_CaSE"));
        assertEquals("toCamelCase7", "camelCase", StringUtils.toCamelCase("camel_case"));
    }
}
