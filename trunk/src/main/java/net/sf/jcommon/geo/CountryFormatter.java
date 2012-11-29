package net.sf.jcommon.geo;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

public class CountryFormatter implements Formatter<Country> {

	private boolean allowEmpty = true;
	
	/**
	 * @return if this formatter accepts null or empty values. If true, it parses a null or empty string 
	 * and returns null in both cases.
	 */
	public boolean isAllowEmpty() {
		return allowEmpty;
	}

	public void setAllowEmpty(boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
	}

	@Override
	public String print(Country object, Locale locale) {
		return object.getISO();
	}

	@Override
	public Country parse(String text, Locale locale) throws ParseException {
		if (text == null) {
			if (!allowEmpty)
				throw new ParseException("Country code cannot be null", 0);
			return null;
		}
		text = text.trim();
		if (text.length() == 0) {
			if (!allowEmpty)
				throw new ParseException("Country code cannot be empty", 0);
			return null;
		}
		Country country = Country.getCountries().findByISO(text);
		if (country == null)
			throw new ParseException("Country code " + text + " is invalid.", 0);
		return country;
	}

}
