package net.sf.jcommon.geo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Comparator;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class CountryTest {

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
	
	@Test
	public void testDisplayNameComparator() {
		Comparator<Country> comp = new Country.DisplayNameComparator("de");
        assertTrue(comp.compare(Country.getCountries().findByISO("ro"), Country.getCountries().findByISO("vn")) < 0);		
	}
	
	@Test
	public void testISOs() {
		for (Country c : Country.getCountries().getAllCountries()) {
			if (c.getISO() == null || c.getISO().length() == 0)
				continue;
			
			for (char ch : c.getISO2().toCharArray()) {
				assertTrue(c.getISO() + ": ISO2 not uppercase", Character.isUpperCase(ch));
			}
			for (char ch : c.getISO3().toCharArray()) {
				assertTrue(c.getISO() + ": ISO2 not uppercase", Character.isUpperCase(ch));
			}
		}
	}
	
	@Test
	public void testIBANs() {
		for (Country c : Country.getCountries().getAllCountries()) {
			assertTrue(c.getIBANCheckDigits() >= 0);
			assertTrue(c.getIBANCheckDigits() < 100);
			
			if (c.getIBAN() == null || c.getIBAN().length() == 0) {
				assertNull(c.getIBANPattern());
				assertTrue(c.getISO() + ": Null or empty IBAN, but BBAN =" + c.getBBAN(), c.getBBAN() == null || c.getBBAN().length() == 0);
				assertTrue(c.getIBANCheckDigits() <= 0);
				continue;
			}
			
			assertEquals(c.getISO2(), c.getIBAN().substring(0, 2));
			
			int s1 = 0, s2 = 0;
    		for (String group : c.getBBAN().split("\\s+")) {
    			s1 += Integer.parseInt(group.substring(0, group.length() - 1));
    		}
    		
    		for (char ch : c.getIBAN().toCharArray()) {
    			s2 += (Character.isWhitespace(ch) ? 0 : 1);
    		}
    		
    		s2 -=4;
    		
    		assertEquals("IBAN != BBAN for " + c.getISO(), s2, s1);
		}
	}
	
	@Test
	public void serializeJsonCountries() throws JsonGenerationException, JsonMappingException, IOException {
	}

}
