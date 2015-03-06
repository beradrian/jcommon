package net.sf.jcommon.persistence;

import java.util.ArrayList;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

@Converter
public abstract class ListOfToStringAttributeConverter<T> implements AttributeConverter<ArrayList<T>, String> {

	private AttributeConverter<T, String> itemConverter;
	private String separator;
	
	private Function<T, String> convertItemToDatabaseColumn = new Function<T, String>() {
		@Override
		public String apply(T input) {
			return itemConverter.convertToDatabaseColumn(input);
		}
	};
	
	private Function<String, T> convertToItem = new Function<String, T>() {
		@Override
		public T apply(String input) {
			return itemConverter.convertToEntityAttribute(input);
		}
	};	
		
	public ListOfToStringAttributeConverter(AttributeConverter<T, String> itemConverter, String separator) {
		this.itemConverter = itemConverter;
		this.separator = separator;
	}

	public String getSeparator() {
		return separator;
	}	
	
	@Override
	public String convertToDatabaseColumn(ArrayList<T> attributeObject) {
		return attributeObject == null ? null : Joiner.on(getSeparator())
				.join(Iterables.transform(attributeObject, convertItemToDatabaseColumn));
	}

	@Override
	public ArrayList<T> convertToEntityAttribute(String datastoreValue) {
		if (datastoreValue == null)
			return null;
		ArrayList<T> values = new ArrayList<T>();
		if (datastoreValue.length() == 0)
			return values;
		Iterables.addAll(values, Iterables.transform(Splitter.on(getSeparator()).split(datastoreValue), convertToItem));
		return values;
	}

}
