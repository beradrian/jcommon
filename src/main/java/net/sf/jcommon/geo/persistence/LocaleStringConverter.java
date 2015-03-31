package net.sf.jcommon.geo.persistence;

import java.util.Locale;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocaleStringConverter implements AttributeConverter<Locale, String> {

	@Override
	public String convertToDatabaseColumn(Locale attributeObject) {
	    if (attributeObject == null) {
	        return null;
	    }
		return attributeObject.toLanguageTag();
	}

	@Override
	public Locale convertToEntityAttribute(String datastoreValue) {
		if (datastoreValue == null) {
			return null;
		}
		return Locale.forLanguageTag(datastoreValue.replace('_', '-'));
	}

}
