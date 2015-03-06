package net.sf.jcommon.geo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Comparator;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.Iterables;

import org.junit.Test;

public class CountryTest {

	@Test
    public void testGetCountries() {
        assertTrue(Country.getCountries().size() > 200);
    }
    
	@Test
    public void testFindByIso() {
    	assertEquals("Romania", Country.findByIso("ro").getName());
    	assertEquals("RO", Country.findByIso("RO").getIso());
    }
	
	@Test
	public void testGetByRegion() {
        assertTrue(Iterables.size(Iterables.filter(Country.getCountries(), CountryPredicates.region("emea"))) > 3);		
	}
	
	@Test
	public void testDisplayNameComparator() {
		Comparator<Country> comp = Country.Comparators.displayName(new Locale("de"));
        assertTrue(comp.compare(Country.findByIso("ro"), Country.findByIso("vn")) < 0);		
	}
	
	@Test
	public void testISOs() {
		for (Country c : Country.getCountries()) {
			if (c.getIso() == null || c.getIso().length() == 0)
				continue;
			
			for (char ch : c.getIso2().toCharArray()) {
				assertTrue(c.getIso() + ": ISO2 not uppercase", Character.isUpperCase(ch));
			}
			for (char ch : c.getIso3().toCharArray()) {
				assertTrue(c.getIso() + ": ISO2 not uppercase", Character.isUpperCase(ch));
			}
		}
	}
	
	@Test
	public void testIBANs() {
		for (Country c : Country.getCountries()) {
			assertTrue(c.getIBANCheckDigits() >= 0);
			assertTrue(c.getIBANCheckDigits() < 100);
			
			if (c.getIBAN() == null || c.getIBAN().length() == 0) {
				assertNull(IbanValidator.compile(c));
				assertTrue(c.getIso() + ": Null or empty IBAN, but BBAN =" + c.getBBAN(), c.getBBAN() == null || c.getBBAN().length() == 0);
				assertTrue(c.getIBANCheckDigits() <= 0);
				continue;
			}
			
			assertEquals(c.getIso2(), c.getIBAN().substring(0, 2));
			
			int s1 = 0, s2 = 0;
    		for (String group : c.getBBAN().split("\\s+")) {
    			s1 += Integer.parseInt(group.substring(0, group.length() - 1));
    		}
    		
    		for (char ch : c.getIBAN().toCharArray()) {
    			s2 += (Character.isWhitespace(ch) ? 0 : 1);
    		}
    		
    		s2 -=4;
    		
    		assertEquals("IBAN != BBAN for " + c.getIso(), s2, s1);
		}
	}
	
	@Test
	public void serializeJsonCountries() throws JsonGenerationException, JsonMappingException, IOException {
	}

}
