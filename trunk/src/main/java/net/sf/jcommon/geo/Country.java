package net.sf.jcommon.geo;

import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;

import net.sf.jcommon.geo.persistence.CountryIsoJsonDeserializer;
import net.sf.jcommon.geo.persistence.CountryIsoJsonSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * An object representing a country and all its code and other relevant information associated.
 */
@JsonSerialize(using=CountryIsoJsonSerializer.class, as=String.class)
@JsonDeserialize(using=CountryIsoJsonDeserializer.class, as=String.class)
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
	
	public enum BbanLetterCodes {
		a("alpha", "[A-Z]"), n("numeric", "[0-9]"), c("character", "[A-Z0-9]");

		private String description;
		private String regex;

		private BbanLetterCodes(String description, String regex) {
			this.description = description;
			this.regex = regex;
		}

		public String getDescription() {
			return description;
		}

		public String getRegex() {
			return regex;
		}
	}
	
	private static final Collection<Country> COUNTRIES = new CSVCountryLoader().loadAllCountries();
	private static final Collection<Country> ISO_COUNTRIES = Sets.newHashSet(Iterables.filter(COUNTRIES, CountryPredicates.isIso()));
	
	public static Collection<Country> getCountries() {
		return COUNTRIES;
	}

	public static Collection<Country> getIsoCountries() {
		return ISO_COUNTRIES;
	}

	public static Country findByIso(String iso) {
		return Iterables.find(COUNTRIES, CountryPredicates.iso(iso), null);
	}
	
	public static class Comparators {
		private Comparators() {}
		
		public static Comparator<Country> NAME = new Comparator<Country>() {
			public int compare(Country c1, Country c2) {
	            return c1 == null ? (c2 == null ? 0 : -1) 
	            		: (c2 == null ? 1 : (c1.getName() == null ? (c2.getName() == null ? 0 : -1) 
	            			: c1.getName().compareTo(c2.getName())));
	            		
	        }
	    };
	
	    public static Comparator<Country> ISO2 = new Comparator<Country>() {
	        public int compare(Country c1, Country c2) {
	            return c1.getIso2().compareTo(c2.getIso2());
	        }
	    };
	    
		private static class DisplayNameComparator implements Comparator<Country> {
			private Locale inLocale;
			
			public DisplayNameComparator(Locale inLocale) {
				this.inLocale = inLocale;
			}
	
			public int compare(Country c1, Country c2) {
				if (c1 == null)
					return c2 == null ? 0 : -1;
				if (c2 == null)
					return 1;
				String name1 = c1.getDisplayName(inLocale);
				if (name1 == null)
					name1 = c1.getName();
				String name2 = c2.getDisplayName(inLocale);
				if (name2 == null)
					name2 = c2.getName();
				return compareNulls(name1, name2);
	        }
	
			private static <T> int compareNulls(Comparable<T> c1, T c2) {
				if (c1 == null)
					return c2 == null ? 0 : -1;
				if (c2 == null)
					return 1;
				return c1.compareTo(c2);
			}
	    }
		
		public static Comparator<Country> displayName(Locale inLocale) {
			return new DisplayNameComparator(inLocale);
		}
		
	}

    private String name;
    private String iso2;
    private String iso3;
    private int isoNumeric;
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
    private String region;
    private Locale locale;

	Country(String name, String ISO2, String ISO3, Integer ISOnumeric, String IANA, String ITU, 
    		String UNvehicle, String IOC, String FIPS, String FIFA, String DS, String WMO, String MARC,
    		String region, String BBAN, String IBAN, Integer IBANCheckDigits) {
        this.name = name;
        this.iso2 = ISO2;
        this.iso3 = ISO3;
        this.isoNumeric = ISOnumeric;
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
        
        this.locale = new Locale("", iso2);
    }
	
    public String getName() {
        return name;
    }

    public int getIsoNumeric() {
        return isoNumeric;
    }

    public String getIso() {
        return iso2;
    }

    public String getIso2() {
        return iso2;
    }

    public String getIso3() {
        return iso3;
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
    
    public String getRegion() {
    	return region;
    }
    
    public String getIBAN() {
		return IBAN;
	}
    
    public String getBBAN() {
		return BBAN;
	}
    
	public int getIBANCheckDigits() {
		return IBANCheckDigits;
	}

    public String getDisplayName() {
    	return locale.getDisplayName();
	}

    public String getDisplayName(Locale inLocale) {
    	return locale.getDisplayName(inLocale);
	}

	public Locale getLocale() {
		return locale;
	}

	public String toString() {
        return name;
    }
    
    @Override
	public int hashCode() {
		return iso2.hashCode();
	}

	@Override
	public boolean equals(Object that) {
		return that instanceof Country && iso2.equals(((Country)that).iso2);
	}

}
