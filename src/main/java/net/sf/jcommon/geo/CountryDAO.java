package net.sf.jcommon.geo;

import java.util.Collection;

public interface CountryDAO {

	Collection<Country> getISOCountries();

	/** Just an alias for {@link #getISOCountries}.
	 * @return same as {@link #getISOCountries}.
	 */
	Collection<Country> getCountries();

	/** @return the full country list, even the legacy ones. */
	Collection<Country> getAllCountries();

	Collection<Country> getCountriesForRegion(String region);

	/** Finds a country by its ISO alpha2 code.
	 * @param code the ISO alpha2 code
	 * @return the country or null if no country has the given ISO code assigned.
	 */
	Country findByISO(String code);

	/** Finds a country by its ISO alpha2 code.
	 * @param code the ISO alpha2 code
	 * @return the country or null if no country has the given ISO code assigned.
	 */
	Country findByISO2(String code);

	Country findByLanguage(String code);

	/**
	 * Finds a country by its English name
	 * @param name the country name in English
	 * @return the country or null if no country has the given name assigned
	 */
	Country findByName(String name);
	
	Country findByLocalizedName(String name, String language);

	/** Finds a country by its ISO alpha3 code.
	 * @param code the ISO alpha3 code
	 * @return the country or null if no country has the given ISO code assigned.
	 */
	Country findByISO3(String code);
	
}