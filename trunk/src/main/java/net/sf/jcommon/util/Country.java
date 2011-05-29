package net.sf.jcommon.util;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An object representing a country and all its code and other relevant information associated.
 */
public final class Country {

    /** For logging internal errors. */
    private static Logger logger = Logger.getLogger(Country.class.getName());
    
    /** The countries list. */
    private static Collection<Country> allCountries = null;
    /** The countries list having an ISO code assigned. */
    private static Collection<Country> isoCountries = null;
    /** ISO 2 country. */
    private static Map<String, Country> iso2country = null;
    /** Name 2 country */
    private static Map<String, Country> name2country = null;
    /** Language 2 country */
    private static Map<String, Country> lang2country = null;
    /** Countries mapped by region */
    private static Map<String, Collection<Country>> region2Countries; 

    private static void initCountries() {
        allCountries = new TreeSet<Country>(new NameComparator());
        isoCountries = new TreeSet<Country>(new ISO2Comparator());
        iso2country = new HashMap<String, Country>();
        name2country = new HashMap<String, Country>();
        lang2country = new HashMap<String, Country>();
        region2Countries = new HashMap<String, Collection<Country>>();
    }
    
    private static void addCountry(Country country) {
        allCountries.add(country);
        if ((country.getISO() != null) && (country.getISO().length() > 0)) {
            isoCountries.add(country);
            iso2country.put(country.getISO(), country);
            name2country.put(country.getName(), country);
        }
        if (country.getDefaultForLanguages() != null && country.getDefaultForLanguages().length > 0) {
        	String[] lls = country.getDefaultForLanguages();
        	for (String ll : lls) {
        		lang2country.put(ll, country);
        	}
        }
        String r = country.getRegion();
        if (r != null && r.length() > 0) {
        	Collection<Country> cs = region2Countries.get(r);
        	if (cs == null) {
        		cs = new HashSet<Country>();
        		region2Countries.put(r, cs);
        	}
        	cs.add(country);
        }
    }
    
    private static void loadCountries() {
    	initCountries();
    	
    	char columnSeparator = ',';
    	// open the resource file to read country information
    	BufferedReader in;
		try {
			InputStream is = Country.class.getResourceAsStream("countries.csv");
			if (is == null) {
	            logger.warning("The resource file countries.csv is not found. Trying to load countries.tsv ...");
				is = Country.class.getResourceAsStream("countries.tsv");
				if (is == null) {
		            logger.warning("The resource file countries.tsv is not found. The library might be corrupted.");					
				}
				columnSeparator = '\t';
			}
			in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		} catch (UnsupportedEncodingException exc) {
            logger.warning("The resource file containing the countries list is not correctly encoded. The library might be corrupted.");
			return;
		} catch (NullPointerException exc) {
            logger.warning("The resource file containing the countries list cannot be found. The library might be corrupted.");
            return;
        }
		
		String[] columnNames;
		RowIterator rows = new RowIterator(in, columnSeparator);
		if (rows.hasNext()) {
			columnNames = ColumnIterator.getAllColumns(rows.next());
			while (rows.hasNext()) {
				Country country = null;
				ColumnIterator cols = rows.next();
				int i = -1;
				while (cols.hasNext()) {
					if (country == null)
						country = new Country();
					i++;
					if (i < columnNames.length)
						country.setProperty(columnNames[i], cols.next());
				}
				if (country != null)
					addCountry(country);
			}
		}
		
		logger.log(allCountries.size() > 0 ? Level.WARNING : Level.INFO, 
				allCountries.size() + " countries loaded from resource file");
    }

    /** @return the country list having an ISO code assigned. */
    public static Collection<Country> getISOCountries() {
        if (isoCountries == null) {
            loadCountries();
        }
        return isoCountries;
    }

    /** Just an alias for {@link #getISOCountries}.
     * @return same as {@link #getISOCountries}.
     */
    public static Collection<Country> getCountries() {
        return getISOCountries();
    }

    /** @return the full country list, even the legacy ones. */
    public static Collection<Country> getAllCountries() {
        if (allCountries == null) {
            loadCountries();
        }
        return allCountries;
    }
    
    public static Collection<Country> getCountriesForRegion(String region) {
    	return region2Countries.get(region.toLowerCase());
    }

    /** Finds a country by its ISO alpha2 code.
     * @param code the ISO alpha2 code
     * @return the country or null if no country has the given ISO code assigned.
     */
    public static Country findByISO(String code) {
        return findByISO2(code);
    }

    /** Finds a country by its ISO alpha2 code.
     * @param code the ISO alpha2 code
     * @return the country or null if no country has the given ISO code assigned.
     */
    public static Country findByISO2(String code) {
        if (code == null || code.length() != 2)
            return null;
        code = code.toUpperCase();
        getISOCountries();
        return iso2country.get(code);
    }
    
    
    public static Country findByLanguage(String code) {
        if (code == null || code.length() != 2)
            return null;
        code = code.toUpperCase();
        getISOCountries();
        return lang2country.get(code);
    }
    
    /**
     * Finds a country by its English name
     * @param name the country name in English
     * @return the country or null if no country has the given name assigned
     */
    public static Country findByName(String name) {
    	if (name == null) {
    		return null;
    	}
    	getISOCountries();
    	return name2country.get(name);
    }

    /** Finds a country by its ISO alpha3 code.
     * @param code the ISO alpha3 code
     * @return the country or null if no country has the given ISO code assigned.
     */
    public static Country findByISO3(String code) {
        if (code == null || code.length() != 3)
            return null;
        code = code.toUpperCase();
        for (Country country : getISOCountries()) {
            if (code.equals(country.getISO3())) {
                return country;
            }
        }
        return null;
    }

	@SuppressWarnings("serial")
	public static class NameComparator implements Comparator<Country>, Serializable {
		public int compare(Country c1, Country c2) {
            return c1 == null ? (c2 == null ? 0 : -1) 
            		: (c2 == null ? 1 : (c1.getName() == null ? (c2.getName() == null ? 0 : -1) 
            			: c1.getName().compareTo(c2.getName())));
            		
        }
    }

	@SuppressWarnings("serial")
	public static class DisplayNameComparator implements Comparator<Country>, Serializable {
		private String language;
		
		public DisplayNameComparator(String language) {
			this.language = language;
		}

		public int compare(Country c1, Country c2) {
            return c1 == null ? (c2 == null ? 0 : -1) 
            		: (c2 == null ? 1 : (c1.getDisplayName(language) == null 
            				? (c2.getDisplayName(language) == null ? 0 : -1) 
            			: c1.getDisplayName(language).compareTo(c2.getDisplayName(language))));
            		
        }
    }

    @SuppressWarnings("serial")
	public static class ISO2Comparator implements Comparator<Country>, Serializable {
        public int compare(Country c1, Country c2) {
            return c1.getISO2().compareTo(c2.getISO2());
        }
    }

    private String name;
    private String ISO2;
    private String ISO3;
    private int ISOnumeric;
    private String IANA;
    private String ITU;
    private String UNvehicle;
    private String IOC;
    private String FIPS;
    private String FIFA;
    private String DS;
    private String WMO;
    private String MARC;
    private String[] locales;
    private String[] defaultForLanguages;
    private String region;
    private Map<String, String> displayNames = new HashMap<String, String>();

    private Country() {}
    
    private Country(String name, String ISO2, String ISO3, Integer ISOnumeric, String IANA, String ITU, 
    		String UNvehicle, String IOC, String FIPS, String FIFA, String DS, String WMO, String MARC) {
        this.name = name;
        this.ISO2 = ISO2;
        this.ISO3 = ISO3;
        this.ISOnumeric = ISOnumeric;
        this.IANA = IANA;
        this.ITU = ITU;
        this.UNvehicle = UNvehicle;
        this.IOC = IOC;
        this.FIPS = FIPS;
        this.FIFA = FIFA;
        this.DS = DS;
        this.WMO = WMO;
        this.MARC = MARC;
    }

    public String getName() {
        return name;
    }

    public int getISOnumeric() {
        return ISOnumeric;
    }

    public String getISO() {
        return ISO2;
    }

    public String getISO2() {
        return ISO2;
    }

    public String getISO3() {
        return ISO3;
    }

    public String getIANA() {
        return IANA;
    }

    public String getITU() {
        return ITU;
    }

    public String getUNvehicle() {
        return UNvehicle;
    }

    public String getIOC() {
        return IOC;
    }

    public String getFIPS() {
        return FIPS;
    }

    public String getFIFA() {
        return FIFA;
    }

    public String getDS() {
        return DS;
    }

    public String getWMO() {
        return WMO;
    }

    public String getMARC() {
        return MARC;
    }
    
    public String[] getLocales() {
    	return locales;
    }
    
    public String getRegion() {
    	return region;
    }
    
    public String[] getDefaultForLanguages() {
    	return defaultForLanguages;
    }
 
    /**
     * @return the language for this country or the first/default if there are multiple language 
     */
    public String getLanguage() {
    	if (locales.length > 0) {
    		String l = locales[0];
        	int i = l.indexOf("-");
        	return (i > 0) ? l.substring(0, i) : l;
    	} else {
    		return null;
    	}
    }
    
    /**
     * Checks if the given language is an official or spoken language for this country.
     * @param ll the language code
     * @return true if the given language is an official or spoken language, false otherwise
     */
    public boolean hasLanguage(String ll) {
    	// check to see if ll conforms to the standard
    	if (ll == null || ll.length() != 2) return false;
    	for (String locale : locales) {
    		if (locale.startsWith(ll)) return true;
    	}
    	return false;
    }

    public String toString() {
        return name;
    }
    
    @Override
	public int hashCode() {
		return ISO2.hashCode();
	}

	@Override
	public boolean equals(Object that) {
		return that instanceof Country && ISO2.equals(((Country)that).ISO2);
	}

	public String getDisplayName(String language) {
    	String displayName = displayNames.get(language.toLowerCase());
    	return (displayName == null ? name : displayName);
    }
    
    public static String getDisplayName(Country country, String language) {
    	return country.getDisplayName(language);
    }
    
    private void setProperty(String propertyName, String propertyValue) {
		if ("name".equalsIgnoreCase(propertyName))
			name = propertyValue;
		else if ("ISO2".equalsIgnoreCase(propertyName))
			ISO2 = propertyValue;
		else if ("ISO3".equalsIgnoreCase(propertyName))
			ISO3 = propertyValue;
		else if ("ISOn".equalsIgnoreCase(propertyName)) {
			try {
				ISOnumeric = Integer.parseInt(propertyValue);
			} catch (NumberFormatException exc) {}
		} else if ("IANA".equalsIgnoreCase(propertyName))
			IANA = propertyValue;
		else if ("ITU".equalsIgnoreCase(propertyName))
			ITU = propertyValue;
		else if ("UNv".equalsIgnoreCase(propertyName))
			UNvehicle = propertyValue;
		else if ("IOC".equalsIgnoreCase(propertyName))
			IOC = propertyValue;
		else if ("FIPS".equalsIgnoreCase(propertyName))
			FIPS = propertyValue;
		else if ("FIFA".equalsIgnoreCase(propertyName))
			FIFA = propertyValue;
		else if ("DS".equalsIgnoreCase(propertyName))
			DS = propertyValue;
		else if ("WMO".equalsIgnoreCase(propertyName))
			WMO = propertyValue;
		else if ("MARC".equalsIgnoreCase(propertyName))
			MARC = propertyValue;
		else if ("locales".equalsIgnoreCase(propertyName))
			locales = ColumnIterator.getAllColumns(new ColumnIterator(propertyValue, ','));
		else if ("region".equalsIgnoreCase(propertyName))
			region = propertyValue;
		else if ("default-for-lang".equalsIgnoreCase(propertyName))
			defaultForLanguages = ColumnIterator.getAllColumns(new ColumnIterator(propertyValue, ','));
		else if (propertyName.startsWith("name_"))
			displayNames.put(propertyName.substring(5).toLowerCase(), propertyValue);
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
				logger.log(Level.WARNING, "Error reading ", exc);
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
		
		public static String[] getAllColumns(ColumnIterator it) {
	    	ArrayList<String> result = new ArrayList<String>();
	    	while (it.hasNext()) {
	    		result.add(it.next());
	    	}
	    	return result.toArray(new String[result.size()]);
		}
    }
}
