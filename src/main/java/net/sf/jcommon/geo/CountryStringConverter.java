package net.sf.jcommon.geo;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class CountryStringConverter implements AttributeConverter<Country, String> {

	@Override
	public String convertToDatabaseColumn(Country attributeObject) {
		return attributeObject == null ? null : attributeObject.getISO();
	}

	@Override
	public Country convertToEntityAttribute(String datastoreValue) {
		return datastoreValue == null ? null : Country.getCountries().findByISO(datastoreValue.trim());
	}

}
