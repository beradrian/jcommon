package net.sf.jcommon.geo.format;

import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

import org.springframework.format.Formatter;

public class CurrencyFormatter implements Formatter<Currency> {

	@Override
	public String print(Currency currency, Locale inLocale) {
		return currency.getDisplayName(inLocale);
	}

	@Override
	public Currency parse(String text, Locale inLocale) throws ParseException {
		throw new UnsupportedOperationException("Please use this formatter only as a printer.CountryFormatter.java");
	}

}
