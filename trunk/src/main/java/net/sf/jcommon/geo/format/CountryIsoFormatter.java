package net.sf.jcommon.geo.format;

import java.text.ParseException;
import java.util.Locale;

import net.sf.jcommon.geo.Country;

import org.springframework.format.Formatter;

public class CountryIsoFormatter implements Formatter<Country> {

	@Override
	public String print(Country country, Locale inLocale) {
		return country.getIso();
	}

	@Override
	public Country parse(String text, Locale inLocale) throws ParseException {
		Country country = Country.findByIso(text);
		if (country == null)
			throw new ParseException("Country code " + text + " is not valid.", 0);
		return country;
	}

}
