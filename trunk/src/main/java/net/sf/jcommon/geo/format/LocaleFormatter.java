package net.sf.jcommon.geo.format;

import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.format.Formatter;

/**
 * Formats a locale based on a pattern.
 * <dl>
 * 	<dt>l</dt>
 * 	<dd>language localized name</dd>
 * 
 * 	<dt>ll</dt>
 * 	<dd>language ISO2 code</dd>
 * 
 * 	<dt>lll</dt>
 * 	<dd>language ISO3 code</dd>
 * 
 * 	<dt>c</dt>
 * 	<dd>country localized name</dd>
 * 
 * 	<dt>cc</dt>
 * 	<dd>country ISO2 code</dd>
 * 
 * 	<dt>ccc</dt>
 * 	<dd>country ISO3 code</dd>
 * </dl>
 */
public class LocaleFormatter implements Formatter<Locale> {

	private static Pattern L = Pattern.compile("(\\b)(l|L)(\\b)"),
			LL = Pattern.compile("(\\b)(l|L){2}(\\b)"),
			LLL = Pattern.compile("(\\b)(l|L){3}(\\b)"),
			C = Pattern.compile("(\\b)(c|C)(\\b)"),
			CC = Pattern.compile("(\\b)(c|C){2}(\\b)"),
			CCC = Pattern.compile("(\\b)(c|C){3}(\\b)");
	
	private String pattern;
	
	public LocaleFormatter(String pattern) {
		this.pattern = pattern;
	}
	
	public LocaleFormatter() {
		this("L-C");
	}
		
	@Override
	public String print(Locale locale, Locale inLocale) {
		String s = pattern;
		s = L.matcher(s).replaceAll("${1}" + locale.getDisplayLanguage(inLocale) + "${3}");
		s = LL.matcher(s).replaceAll("${1}" + locale.getLanguage() + "${3}");
		s = LLL.matcher(s).replaceAll("${1}" + locale.getISO3Language() + "${3}");
		s = C.matcher(s).replaceAll("${1}" + locale.getDisplayCountry(inLocale) + "${3}");
		s = CC.matcher(s).replaceAll("${1}" + locale.getCountry() + "${3}");
		s = CCC.matcher(s).replaceAll("${1}" + locale.getISO3Country() + "${3}");
		return s;
	}

	@Override
	public Locale parse(String text, Locale inLocale) throws ParseException {
		throw new UnsupportedOperationException("Please use this formatter only as a printer.CountryFormatter.java");
	}

}
