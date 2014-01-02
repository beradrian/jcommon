package net.sf.jcommon.geo;

import static org.junit.Assert.*;

import org.junit.Test;

public class IbanValidatorTest {

	@Test
	public void testSamples() {
		IbanValidator v = new IbanValidator();
		assertTrue(v.apply("GR16 0110 1250 0000 0001 2300 695"));
		assertTrue(v.apply("GB29 NWBK 6016 1331 9268 19"));
		assertTrue(v.apply("SA03 8000 0000 6080 1016 7519"));
		assertTrue(v.apply("CH93 0076 2011 6238 5295 7"));
		assertTrue(v.apply("IL62 0108 0000 0009 9999 999"));
		assertTrue(v.apply("DE13700100800737828809"));
		assertFalse(v.apply("DE13700100800737828808"));
		assertTrue(v.apply("DE13 7001 0080 0737 8288 09"));
	}
	
}
