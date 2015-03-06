package net.sf.jcommon.geo.persistence;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import net.sf.jcommon.geo.Country;

@Converter(autoApply = true)
public class CountryStringConverter implements AttributeConverter<Country, String> {

	@Override
	public String convertToDatabaseColumn(Country attributeObject) {
		return attributeObject == null ? null : attributeObject.getIso();
	}

	@Override
	public Country convertToEntityAttribute(String datastoreValue) {
		return datastoreValue == null ? null : Country.findByIso(datastoreValue.trim());
	}

}
