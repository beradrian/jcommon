package net.sf.jcommon.geo;

import org.datanucleus.store.types.converters.TypeConverter;

public class CountryStringConverter implements TypeConverter<Country, String> {

	private static final long serialVersionUID = 1L;

	@Override
	public String toDatastoreType(Country memberValue) {
		return memberValue == null ? null : memberValue.getISO();
	}

	@Override
	public Country toMemberType(String datastoreValue) {
		return  datastoreValue == null ? null : Country.getCountries().findByISO(datastoreValue.trim());
	}

}
