package net.sf.jcommon.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

@Converter
public class ListOfStringsToStringAttributeConverter implements AttributeConverter<List<String>, String> {

	private static final String DEFAULT_SEPARATOR = ",";

	public String getSeparator() {
		return DEFAULT_SEPARATOR;
	}	
	
	@Override
	public String convertToDatabaseColumn(List<String> attributeObject) {
		return attributeObject == null ? null : Joiner.on(getSeparator()).join(attributeObject);
	}

	@Override
	public List<String> convertToEntityAttribute(String datastoreValue) {
		if (datastoreValue == null)
			return null;
		List<String> values = new ArrayList<String>();
		if (datastoreValue.length() == 0)
			return values;
		Iterables.addAll(values, Splitter.on(getSeparator()).split(datastoreValue));
		return values;
	}

}
