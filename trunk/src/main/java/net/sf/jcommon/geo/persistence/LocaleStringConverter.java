package net.sf.jcommon.geo.persistence;

import java.util.Locale;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocaleStringConverter implements AttributeConverter<Locale, String> {

	private static final String SEPARATOR = "_";

	@Override
	public String convertToDatabaseColumn(Locale attributeObject) {
		return attributeObject == null ? null : attributeObject.getLanguage() + SEPARATOR 
				+ attributeObject.getCountry() + SEPARATOR + attributeObject.getVariant();
	}

	@Override
	public Locale convertToEntityAttribute(String datastoreValue) {
		if (datastoreValue == null) {
			return null;
		}
		String[] x = datastoreValue.trim().split(SEPARATOR);
		switch(x.length) {
			case 1:
				return new Locale(x[0]);
			case 2:
				return new Locale(x[0], x[1]);
			case 3:
				return new Locale(x[0], x[1], x[2]);
			case 0:
			default:
				return null;
		}
	}

}
