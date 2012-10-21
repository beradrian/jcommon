package net.sf.jcommon.geo;

import java.util.Collection;

public interface CountryDAO {

	public abstract Collection<Country> getISOCountries();

	/** Just an alias for {@link #getISOCountries}.
	 * @return same as {@link #getISOCountries}.
	 */
	public abstract Collection<Country> getCountries();

	/** @return the full country list, even the legacy ones. */
	public abstract Collection<Country> getAllCountries();

	public abstract Collection<Country> getCountriesForRegion(String region);

	/** Finds a country by its ISO alpha2 code.
	 * @param code the ISO alpha2 code
	 * @return the country or null if no country has the given ISO code assigned.
	 */
	public abstract Country findByISO(String code);

	/** Finds a country by its ISO alpha2 code.
	 * @param code the ISO alpha2 code
	 * @return the country or null if no country has the given ISO code assigned.
	 */
	public abstract Country findByISO2(String code);

	public abstract Country findByLanguage(String code);

	/**
	 * Finds a country by its English name
	 * @param name the country name in English
	 * @return the country or null if no country has the given name assigned
	 */
	public abstract Country findByName(String name);

	/** Finds a country by its ISO alpha3 code.
	 * @param code the ISO alpha3 code
	 * @return the country or null if no country has the given ISO code assigned.
	 */
	public abstract Country findByISO3(String code);
	
}