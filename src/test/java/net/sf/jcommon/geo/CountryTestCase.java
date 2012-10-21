package net.sf.jcommon.geo;

import org.junit.Test;
import static org.junit.Assert.*;

public class CountryTestCase {

	@Test
    public void testGetCountries() {
        assertTrue(Country.getCountries().getISOCountries().size() > 200);
    }
    
	@Test
    public void testFindByISO() {
    	assertEquals("Romania", Country.getCountries().findByISO("ro").getName());
    	assertEquals("RO", Country.getCountries().findByISO("RO").getISO());
    }
	
	@Test
	public void testGetByRegion() {
        assertTrue(Country.getCountries().getCountriesForRegion("emea").size() > 3);		
	}
}
