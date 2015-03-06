package net.sf.jcommon.geo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CSVCountryLoader {

	private static final Logger LOG = LoggerFactory.getLogger(CSVCountryLoader.class);

	protected Collection<Country> loadCountries() throws IOException {
		String columnSeparator = ",";
		try {
			InputStream is = Country.class.getResourceAsStream("countries.csv");
			if (is == null) {
	            LOG.warn("The resource file countries.csv is not found. Trying to load countries.tsv ...");
				is = Country.class.getResourceAsStream("countries.tsv");
				if (is == null) {
		            LOG.warn("The resource file countries.tsv is not found. The library might be corrupted.");					
				}
				columnSeparator = "\t";
			}
			return loadCountries(new BufferedReader(new InputStreamReader(is, "UTF-8")), columnSeparator);
		} catch (UnsupportedEncodingException exc) {
            LOG.warn("The resource file containing the countries list is not correctly encoded. The library might be corrupted.", exc);
			throw new IOException("The resource file containing the countries list is not correctly encoded. The library might be corrupted.");
		} catch (NullPointerException exc) {
            LOG.warn("The resource file containing the countries list cannot be found. The library might be corrupted.", exc);
            throw new IOException("The resource file containing the countries list cannot be found. The library might be corrupted.");
        }
	}
	
	protected Collection<Country> loadCountries(BufferedReader in, String columnSeparator) throws IOException {
    	
    	// open the resource file to read country information
		
		Collection<Country> countries = new HashSet<Country>();
		String[] columnHeaders = in.readLine().split(columnSeparator);
		
		String line = in.readLine();
		while (line != null) {
			Map<String, String> properties = new HashMap<String, String>();
			
			String[] columns = line.split(columnSeparator);
			for (int i = 0, n = Math.min(columnHeaders.length, columns.length); i < n; i++) {
				properties.put(columnHeaders[i].toLowerCase(), columns[i].trim());
			}
			
			countries.add(toCountry(properties));
			line = in.readLine();
		}
		
		if (countries.size() <= 0) {
			LOG.warn("{} countries loaded from resource file", countries.size());			
		} else {
			LOG.info("{} countries loaded from resource file", countries.size());
		}
		
		return countries;
	}
	
	private Country toCountry(Map<String, String> properties) {
		if (properties.isEmpty()) {
			return null;
		}		
		return new Country(
				properties.get("name"),
				properties.get("iso2"),
				properties.get("iso3"),
				parseInt(properties.get("ison"), -1),
				properties.get("iana"),
				properties.get("itu"),
				properties.get("unv"),
				properties.get("ioc"),
				properties.get("fips"),
				properties.get("fifa"),
				properties.get("ds"),
				properties.get("wmo"),
				properties.get("marc"),
				properties.get("region"),
				properties.get("bban"),
				properties.get("iban"),
				parseInt(properties.get("ibancheckdigits"), 0));
	}
	
	private Integer parseInt(String value, Integer defaultValue) {
		Integer i;
		try {
			i = Integer.parseInt(value);
		} catch (NumberFormatException exc) {
			i = defaultValue;
		}
		return i;
	}
	
	public Collection<Country> loadAllCountries() {
		try {
			return loadCountries();
		} catch (IOException exc) {
			LOG.error("Error loading countries", exc);
			return null;
		}
	}
	
}
