package net.sf.jcommon.geo;

import static org.junit.Assert.*;

import org.junit.Test;

public class IbanValidatorTest {

	@Test
	public void testSamples() {
		IbanValidator v = new IbanValidator();
		// TODO check the below
//		assertTrue(v.isValid("GR16 0110 1250 0000 0001 2300 695", null));
//		assertTrue(v.isValid("GB29 NWBK 6016 1331 9268 19", null));
//		assertTrue(v.isValid("SA03 8000 0000 6080 1016 7519", null));
//		assertTrue(v.isValid("CH93 0076 2011 6238 5295 7", null));
//		assertTrue(v.isValid("IL62 0108 0000 0009 9999 999", null));
//		assertTrue(v.isValid("DE13700100800737828809", null));
		assertFalse(v.isValid("DE13700100800737828808", null));
//		assertTrue(v.isValid("DE13 7001 0080 0737 8288 09", null));
	}
	
}
