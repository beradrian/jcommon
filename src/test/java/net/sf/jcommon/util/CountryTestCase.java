package net.sf.jcommon.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class CountryTestCase {

	@Test
    public void testGetCountries() {
        assertTrue(Country.getISOCountries().size() > 200);
    }
    
	@Test
    public void testFindByISO() {
    	assertEquals("Romania", Country.findByISO("ro").getName());
    	assertEquals("RO", Country.findByISO("RO").getISO());
    }
	
	@Test
	public void testGetByRegion() {
        assertTrue(Country.getCountriesForRegion("emea").size() > 3);		
	}
}
