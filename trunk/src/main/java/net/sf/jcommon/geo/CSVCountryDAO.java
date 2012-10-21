package net.sf.jcommon.geo;

import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

public class CSVCountryDAO extends AbstractCountryDAO {

	private static final Logger LOG = LoggerFactory.getLogger(CSVCountryDAO.class);

	protected Collection<Country> loadCountries() throws IOException {
    	char columnSeparator = ',';
    	// open the resource file to read country information
    	BufferedReader in;
		try {
			InputStream is = Country.class.getResourceAsStream("countries.csv");
			if (is == null) {
	            LOG.warn("The resource file countries.csv is not found. Trying to load countries.tsv ...");
				is = Country.class.getResourceAsStream("countries.tsv");
				if (is == null) {
		            LOG.warn("The resource file countries.tsv is not found. The library might be corrupted.");					
				}
				columnSeparator = '\t';
			}
			in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		} catch (UnsupportedEncodingException exc) {
            LOG.warn("The resource file containing the countries list is not correctly encoded. The library might be corrupted.");
			throw new IOException("The resource file containing the countries list is not correctly encoded. The library might be corrupted.", exc);
		} catch (NullPointerException exc) {
            LOG.warn("The resource file containing the countries list cannot be found. The library might be corrupted.");
            throw new IOException("The resource file containing the countries list cannot be found. The library might be corrupted.", exc);
        }
		
		Collection<Country> countries = new HashSet<Country>();
		Map<String, String> countryColumns = new HashMap<String, String>();
		Map<String, String> countryDisplayNames = new HashMap<String, String>();
		String[] columnNames;
		RowIterator rows = new RowIterator(in, columnSeparator);
		if (rows.hasNext()) {
			columnNames = Iterators.toArray(rows.next(), String.class);
			while (rows.hasNext()) {
				countryColumns.clear();
				countryDisplayNames.clear();
				ColumnIterator cols = rows.next();
				int i = -1;
				while (cols.hasNext()) {
					i++;
					if (i < columnNames.length) {
						String columnName = columnNames[i].toLowerCase();
						String columnValue = cols.next();
						if (columnName.startsWith("name_")) {
							countryDisplayNames.put(columnName.substring(5), columnValue);
						} else {
							countryColumns.put(columnName, columnValue);
						}
					}
				}
				if (!countryColumns.isEmpty()) {
					String localesAsStr = countryColumns.get("locales");					
					String[] locales = localesAsStr == null ? null 
							: Iterables.toArray(Splitter.on(',').split(localesAsStr), String.class);
					
					String defaultForLangAsStr = countryColumns.get("default-for-lang");					
					String[] defaultForLang = defaultForLangAsStr == null ? null 
							: Iterables.toArray(Splitter.on(',').split(defaultForLangAsStr), String.class);
					
					int ison;
					try {
						ison = Integer.parseInt(countryColumns.get("ison"));
					} catch (NumberFormatException exc) {
						ison = -1;
					}
					
					countries.add(new Country(
							countryColumns.get("name"),
							countryColumns.get("iso2"),
							countryColumns.get("iso3"),
							ison,
							countryColumns.get("iana"),
							countryColumns.get("itu"),
							countryColumns.get("unv"),
							countryColumns.get("ioc"),
							countryColumns.get("fips"),
							countryColumns.get("fifa"),
							countryColumns.get("ds"),
							countryColumns.get("wmo"),
							countryColumns.get("marc"),
							countryColumns.get("region"),
							locales, defaultForLang, countryDisplayNames));
				}
		    }
		}
		
		if (countries.size() <= 0) {
			LOG.warn("{} countries loaded from resource file", countries.size());			
		} else {
			LOG.info("{} countries loaded from resource file", countries.size());
		}
		
		return countries;
	}

    private static class RowIterator implements Iterator<ColumnIterator> {

    	private BufferedReader in;
    	private char columnSeparator;
    	private ColumnIterator currentColumn;
    	private String lastLine;

		public RowIterator(BufferedReader in, char columnSeparator) {
			this.in = in;
			this.columnSeparator = columnSeparator;
		}
		
		private String getNextLine() {
			try {
				String line = in.readLine();
				if (line == null)
					return null;
				// skip the BOM character
				if (line.charAt(0) == 0xfffe || line.charAt(0) == 0xfeff)
					line = line.substring(1);
				// skip empty lines
				if (line.length() == 0)
					return getNextLine();
				return line;
			} catch (IOException exc) {
				LOG.warn("Error reading ", exc);
				return null;
			}
		}

		@Override
		public boolean hasNext() {
			return (lastLine!= null) || ((lastLine = getNextLine()) != null);
		}

		@Override
		public ColumnIterator next() {
			if (lastLine == null) {
				lastLine = getNextLine();
			}
			if (lastLine != null) {
				currentColumn = new ColumnIterator(lastLine, columnSeparator);
				lastLine = null;
				return currentColumn;
			}
			return null;
		}

		@Override
		public void remove() {
			throw new IllegalStateException("This iterator does not support remove");
		}
    }
    
    private static class ColumnIterator implements Iterator<String> {
    	private String source;
    	private char separator;
    	private int begin, end, current;

		public ColumnIterator(String source, char separator) {
			this.source = source;
			this.separator = separator;
		}

		@Override
		public boolean hasNext() {
			return current < source.length();
		}

		@Override
		public String next() {
			if (current >= source.length())
				return null;
			begin = current;
			end = -1;
			if (current < source.length() && source.charAt(current) == '"') {
				begin = current + 1;
				current++;
				while (current < source.length() && source.charAt(current) != '"') {
					current++;
				}
				end = current;
			} else if (current < source.length() && source.charAt(current) == '\'') {
				begin = current + 1;
				current++;
				while (current < source.length() && source.charAt(current) != '\'') {
					current++;
				}
				end = current;
			}
			while (current < source.length() && source.charAt(current) != separator) {
				current++;
			}
			if (end < 0)
				end = current;
			current++;
			return source.substring(begin, end);
		}

		@Override
		public void remove() {
			throw new IllegalStateException("This iterator does not support remove");
		}
    }

	@Override
	public Collection<Country> getAllCountries() {
		try {
			return loadCountries();
		} catch (IOException exc) {
			LOG.error("Error loading countries", exc);
			return null;
		}
	}

}
