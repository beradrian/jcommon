package net.sf.jcommon.geo;

import java.util.Collection;
import java.util.HashSet;

public abstract class AbstractCountryDAO implements CountryDAO {

	@Override
	public Collection<Country> getISOCountries() {
		Collection<Country> countries = new HashSet<Country>(); 
        for (Country country : getAllCountries()) {
            if (country.getISO() != null && country.getISO().length() > 0) {
                countries.add(country);
            }
        }
        return countries;
    }

    @Override
	public Collection<Country> getCountries() {
        return getISOCountries();
    }

	@Override
	public Collection<Country> getCountriesForRegion(String region) {
		Collection<Country> countries = new HashSet<Country>(); 
        for (Country country : getAllCountries()) {
            if (region.equalsIgnoreCase(country.getRegion())) {
                countries.add(country);
            }
        }
        return countries;
	}

    @Override
	public Country findByISO(String code) {
        return findByISO2(code);
    }

	@Override
	public Country findByISO2(String code) {
        if (code == null || code.length() != 2)
            return null;
        for (Country country : getISOCountries()) {
            if (code.equalsIgnoreCase(country.getISO2())) {
                return country;
            }
        }
        return null;
	}

	@Override
	public Country findByLanguage(String code) {
        for (Country country : getAllCountries()) {
            if (code.equalsIgnoreCase(country.getLanguage())) {
                return country;
            }
        }
        return null;
	}

	@Override
	public Country findByName(String name) {
        for (Country country : getAllCountries()) {
            if (name.equalsIgnoreCase(country.getName())) {
                return country;
            }
        }
        return null;
	}
    
	@Override
	public Country findByLocalizedName(String name, String language) {
        for (Country country : getAllCountries()) {
            if (name.equalsIgnoreCase(country.getLocalizedName(language))) {
                return country;
            }
        }
        return null;
	}

    @Override
	public Country findByISO3(String code) {
        if (code == null || code.length() != 3)
            return null;
        for (Country country : getISOCountries()) {
            if (code.equalsIgnoreCase(country.getISO3())) {
                return country;
            }
        }
        return null;
    }

}
