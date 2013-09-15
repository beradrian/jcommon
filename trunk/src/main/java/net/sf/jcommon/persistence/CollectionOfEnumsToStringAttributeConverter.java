package net.sf.jcommon.persistence;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.AttributeConverter;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public abstract class CollectionOfEnumsToStringAttributeConverter<T extends Enum<T>> implements AttributeConverter<Collection<T>, String> {

	private static final String DEFAULT_SEPARATOR = ",";

	private Class<T> enumClass;
	
	@SuppressWarnings("unchecked")
	public CollectionOfEnumsToStringAttributeConverter() {
    	// determine the model class by finding the parametrized type of this actual controller class
    	Type superClass = this.getClass().getGenericSuperclass();
    	// the model class is the class of the first generic argument
    	enumClass = (Class<T>)((ParameterizedType)superClass).getActualTypeArguments()[0];
	}
	
	public String getSeparator() {
		return DEFAULT_SEPARATOR;
	}	
	
	@Override
	public String convertToDatabaseColumn(Collection<T> attributeObject) {
		return attributeObject == null ? null : Joiner.on(getSeparator()).join(attributeObject);
	}

	@Override
	public Collection<T> convertToEntityAttribute(String datastoreValue) {
		if (datastoreValue == null)
			return null;
		List<T> values = new ArrayList<T>();
		if (datastoreValue.length() == 0)
			return values;
		for (String s : Splitter.on(getSeparator()).split(datastoreValue)) {
			T e = Enum.valueOf(enumClass, s);
			if (e != null) {
				values.add(e);
			}
		}
		return values;
	}

}
