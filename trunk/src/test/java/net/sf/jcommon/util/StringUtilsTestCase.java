package net.sf.jcommon.util;

import static org.junit.Assert.*;

import org.junit.*;

/**
 */
public class StringUtilsTestCase {
    
    @Test public void testNl2p() {
    	assertEquals("Hello<br/>world!", StringUtils.nl2p("Hello\nworld!"));
    	assertEquals("<p>Hello</p><p>world!</p>", StringUtils.nl2p("Hello\n\nworld!"));
    }
}
