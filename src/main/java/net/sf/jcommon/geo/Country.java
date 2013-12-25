package net.sf.jcommon.geo;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * An object representing a country and all its code and other relevant information associated.
 */
@JsonSerialize(using=CountryISOJsonSerializer.class, as=String.class)
@JsonDeserialize(using=CountryISOJsonDeserializer.class, as=String.class)
public final class Country {
	
	public enum IbanLetterCodes {
			b("National bank code"), 
			s("Branch code"), 
			x("National check digit"), 
			c("Account number"),
			t("Account type (Cheque account, Savings account etc)"), 
			n("Owner account number (1, 2 etc)"),
			g("Branch code (fr:code guichet)"),
			k("IBAN check digits");
			
		private String description;

		private IbanLetterCodes(String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}
	}
	
	private static final CountryDAO DEFAULT = new CachedCountryDAO(new CSVCountryDAO());
	
	public static CountryDAO getCountries() {
		return DEFAULT;
	}

	private static <T> int compareNulls(Comparable<T> c1, T c2) {
		if (c1 == null)
			return c2 == null ? 0 : -1;
		if (c2 == null)
			return 1;
		return c1.compareTo(c2);
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
			if (c1 == null)
				return c2 == null ? 0 : -1;
			if (c2 == null)
				return 1;
			String name1 = c1.getLocalizedName(language);
			if (name1 == null)
				name1 = c1.getName();
			String name2 = c2.getLocalizedName(language);
			if (name2 == null)
				name2 = c2.getName();
			return compareNulls(name1, name2);
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
    private String IBAN;
    private String BBAN;
    private int IBANCheckDigits;
    private Pattern IBANPattern;
    private String[] locales;
    private String[] defaultForLanguages;
    private String region;
    private Map<String, String> localizedNames = new HashMap<String, String>();

    Country() {}
    
    Country(String name, String ISO2, String ISO3, Integer ISOnumeric, String IANA, String ITU, 
    		String UNvehicle, String IOC, String FIPS, String FIFA, String DS, String WMO, String MARC,
    		String region, String BBAN, String IBAN, Integer IBANCheckDigits,
    		String[] locales, String[] defaultForLanguages,
    		Map<String, String> localizedNames) {
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
        
        this.region = region;
        
        this.BBAN = BBAN;
        this.IBAN = IBAN;
        this.IBANCheckDigits = IBANCheckDigits;
                
        this.locales = locales;
        this.defaultForLanguages = defaultForLanguages;
        
        this.localizedNames = localizedNames;
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
    
    public String getIBAN() {
		return IBAN;
	}
    
    public String getBBAN() {
		return BBAN;
	}
    
    /**
     * 
     * @return a regular expression to validate the IBAN.
     */
    public Pattern getIBANPattern() {
    	if (IBANPattern == null) {
    		if (IBAN == null || IBAN.length() == 0) {
    			return null;
    		}
    		
    		int i = 0;
    		StringBuilder sb = new StringBuilder();
    		for (String group : BBAN.split("\\s+")) {
    			String g = null;
    			switch (group.charAt(group.length() - 1)) {
    				case 'a':
    					g = "[A-Z]";
    					break;
    				case 'n':
    					g = "[0-9]";
    					break;
    				case 'c':
    					g = "[A-Z0-9]";
    			}
    			if (g == null) {
    				continue;
    			}
    			
    			group = group.substring(0, group.length() - 1);
    			for (int x = Integer.parseInt(group); x > 0; x--) {
    				sb.append(g);
    				i++;
    				if (i % 4 == 0) {
    					sb.append("\\s?");
    				}
    			}
    		}
    		if (i % 4 == 0) {
    			sb.delete(sb.length() - 3, sb.length());
    		}
    		IBANPattern = Pattern.compile(IBAN.substring(0, 2) 
    				+ (IBANCheckDigits <= 0 ? "..\\s?" : (IBANCheckDigits < 10 ? "0" : "") + IBANCheckDigits) 
    				+ sb.toString());
    	}
    	return IBANPattern;
    }

	public int getIBANCheckDigits() {
		return IBANCheckDigits;
	}

	public String[] getDefaultForLanguages() {
    	return defaultForLanguages;
    }
 
    /**
     * @return the language for this country or the first/default if there are multiple language 
     */
    public String getLanguage() {
    	if (locales == null || locales.length <= 0) {
    		return null;
    	}
		String l = locales[0];
    	int i = l.indexOf("-");
    	return (i > 0) ? l.substring(0, i) : l;
    }
    
    /**
     * Checks if the given language is an official or spoken language for this country.
     * @param ll the language code
     * @return true if the given language is an official or spoken language, false otherwise
     */
    public boolean hasLanguage(String ll) {
    	if (locales == null || locales.length <= 0) {
    		return false;
    	}
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

	public String getLocalizedName(String language) {
    	String displayName = localizedNames.get(language.toLowerCase());
    	return (displayName == null ? name : displayName);
    }
    
	/** To be used in JSTL as custom function. */
    public static String getLocalizedName(Country country, String language) {
    	return country.getLocalizedName(language);
    }
}
