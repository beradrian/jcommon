package net.sf.jcommon.geo;

import java.util.Locale;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class CountryPredicates {

	private CountryPredicates() {}

	private static Predicate<Country> IS_ISO = new Predicate<Country>() {
		public boolean apply(Country c) {
			return c != null && c.getIso() != null;
	    }
	};

	public static Predicate<Country> isIso() {
		return IS_ISO;
	}
	
	public static Predicate<Country> iso(final String value) {
		if (value == null) {
			return  new Predicate<Country>() {
				@Override
				public boolean apply(Country c) {
					return c != null && c.getIso2() == null; 
				}
			};
		}
		try {
			return iso(Integer.parseInt(value));
		} catch (NumberFormatException exc) {}
		final String v = value.toUpperCase();
		switch (v.length()) {
			case 2:
				return new Predicate<Country>() {
					@Override
					public boolean apply(Country c) {
						return c != null && v.equals(c.getIso2()); 
					}
				};
			case 3:
				return new Predicate<Country>() {
					@Override
					public boolean apply(Country c) {
						return c != null && v.equals(c.getIso3()); 
					}
				};
		}
		return Predicates.alwaysFalse();
	}

	public static Predicate<Country> iso(final int value) {
		return new Predicate<Country>() {
			@Override
			public boolean apply(Country c) {
				return c != null && value == c.getIsoNumeric(); 
			}
		};
	}

	public static Predicate<Country> region(final String value) {
		if (value == null) {
			return  new Predicate<Country>() {
				@Override
				public boolean apply(Country c) {
					return c != null && c.getRegion() == null; 
				}
			};
		}
		return new Predicate<Country>() {
			@Override
			public boolean apply(Country c) {
				return c != null && value.equals(c.getRegion()); 
			}
		};
	}
	
	public static Predicate<Country> name(final String value) {
		if (value == null) {
			return Predicates.alwaysFalse();
		}
		return new Predicate<Country>() {
			@Override
			public boolean apply(Country c) {
				return c != null && value.equals(c.getName()); 
			}
		};
	}
	
	public static Predicate<Country> displayName(final String value, final Locale inLocale) {
		if (value == null) {
			return Predicates.alwaysFalse();
		}
		return new Predicate<Country>() {
			@Override
			public boolean apply(Country c) {
				return c != null && value.equals(c.getDisplayName(inLocale)); 
			}
		};
	}

	public static Predicate<Country> hasLocale(final Locale locale) {
		return new Predicate<Country>() {
			@Override
			public boolean apply(Country c) {
				String iso2 = c.getIso2(); 
				if (iso2 != null) {
					return iso2.equals(locale.getCountry()); 
				}
				Locale countryLocale = c.getLocale();
				return countryLocale.getDisplayCountry(countryLocale)
						.equals(locale.getDisplayCountry(countryLocale));
			}
		};
	}
	
	public static Predicate<Locale> localeForCountry(final Country country) {
		if (country.getIso2() != null) {
			final String iso2 = country.getIso2();
			return new Predicate<Locale>() {
				@Override
				public boolean apply(Locale locale) {
					return iso2.equals(locale.getCountry());
				}
			};
		}
		final String countryLocalName = country.getDisplayName(country.getLocale());
		return new Predicate<Locale>() {
			@Override
			public boolean apply(Locale locale) {
				return countryLocalName.equals(locale.getDisplayCountry(country.getLocale()));
			}
		};
		
	}
}
