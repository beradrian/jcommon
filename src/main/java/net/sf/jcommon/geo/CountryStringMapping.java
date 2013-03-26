package net.sf.jcommon.geo;

import org.datanucleus.store.mapped.mapping.ObjectAsStringMapping;

public class CountryStringMapping extends ObjectAsStringMapping {

	@Override
	public Class<Country> getJavaType() {
		return Country.class;
	}

	@Override
	public int getDefaultLength(int index) {
		return 3;
	}

	@Override
	protected String objectToString(Object object) {
		return ((Country)object).getISO();
	}

	@Override
	protected Object stringToObject(String datastoreValue) {
		return Country.getCountries().findByISO(datastoreValue);
	}

}
