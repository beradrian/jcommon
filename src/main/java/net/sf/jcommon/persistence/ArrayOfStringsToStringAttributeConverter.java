package net.sf.jcommon.persistence;

import javax.persistence.jpa21.AttributeConverter;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

public class ArrayOfStringsToStringAttributeConverter implements AttributeConverter<String[], String> {

	private static final String DEFAULT_SEPARATOR = ",";

	public String getSeparator() {
		return DEFAULT_SEPARATOR;
	}	
	
	@Override
	public String convertToDatabaseColumn(String[] attributeObject) {
		return attributeObject == null ? null : Joiner.on(getSeparator()).join(attributeObject);
	}

	@Override
	public String[] convertToEntityAttribute(String datastoreValue) {
		if (datastoreValue == null)
			return null;
		if (datastoreValue.length() == 0)
			return new String[0];
		// split the value in the datastore
		return Iterables.toArray(Splitter.on(getSeparator()).split(datastoreValue), String.class);
	}

}
