package net.sf.jcommon.geo;

public class CountryType extends GenericStringType<Country> {

	@Override
	public Class<Country> returnedClass() {
		return Country.class;
	}

	@Override
	protected String valueToString(Object value) {
		if (value instanceof Country) {
			return ((Country)value).getISO();
		} else {
			return null;
		}
	}
	
	@Override
	protected Country stringToValue(String valueAsString) {
		return Country.getCountries().findByISO(valueAsString);
	}
	
}
