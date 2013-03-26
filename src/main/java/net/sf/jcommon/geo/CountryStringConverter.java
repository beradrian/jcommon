package net.sf.jcommon.geo;

import javax.persistence.jpa21.AttributeConverter;

import org.datanucleus.store.types.converters.TypeConverter;

public class CountryStringConverter implements TypeConverter<Country, String>, AttributeConverter<Country, String> {

	private static final long serialVersionUID = 1L;

	@Override
	public String toDatastoreType(Country memberValue) {
		return memberValue == null ? null : memberValue.getISO();
	}

	@Override
	public Country toMemberType(String datastoreValue) {
		return  datastoreValue == null ? null : Country.getCountries().findByISO(datastoreValue.trim());
	}

	@Override
	public String convertToDatabaseColumn(Country attributeObject) {
		return toDatastoreType(attributeObject);
	}

	@Override
	public Country convertToEntityAttribute(String dbData) {
		return toMemberType(dbData);
	}

}
