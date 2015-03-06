package net.sf.jcommon.geo.format;

import java.text.ParseException;
import java.util.Locale;

import net.sf.jcommon.geo.Country;
import net.sf.jcommon.geo.CountryPredicates;

import org.springframework.format.Formatter;

import com.google.common.collect.Iterables;

public class CountryFormatter implements Formatter<Country> {

	@Override
	public String print(Country country, Locale inLocale) {
		return country.getDisplayName(inLocale);
	}

	@Override
	public Country parse(String text, Locale inLocale) throws ParseException {
		Country country = Iterables.find(Country.getCountries(), CountryPredicates.displayName(text, inLocale));
		if (country == null)
			throw new ParseException("Country name " + text + " is not a valid name in locale " + inLocale, 0);
		return country;
	}

}
