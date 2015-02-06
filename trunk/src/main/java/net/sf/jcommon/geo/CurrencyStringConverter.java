package net.sf.jcommon.geo;

import java.util.Currency;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class CurrencyStringConverter implements AttributeConverter<Currency, String> {

	@Override
	public String convertToDatabaseColumn(Currency attributeObject) {
		return attributeObject == null ? null : attributeObject.getCurrencyCode();
	}

	@Override
	public Currency convertToEntityAttribute(String datastoreValue) {
		return datastoreValue == null ? null : Currency.getInstance(datastoreValue.trim());
	}

}
