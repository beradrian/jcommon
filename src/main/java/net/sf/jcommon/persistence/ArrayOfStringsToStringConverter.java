package net.sf.jcommon.persistence;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.datanucleus.store.types.converters.TypeConverter;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

public class ArrayOfStringsToStringConverter implements TypeConverter<String[], String> {

	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_SEPARATOR = ",";

	public String getSeparator() {
		return DEFAULT_SEPARATOR;
	}

	@Override
	public String toDatastoreType(String[] memberValue) {
		return memberValue == null ? null : Joiner.on(getSeparator()).join(memberValue);
	}

	@Override
	public String[] toMemberType(String datastoreValue) {
		if (datastoreValue == null)
			return null;
		if (datastoreValue.length() == 0)
			return new String[0];
		// if the first character is not an alphabetic one, maybe the object was serialized
		if (!Character.isUnicodeIdentifierStart(datastoreValue.codePointAt(0))) {
			try {
				ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(datastoreValue.getBytes()));
				Object value = in.readObject();
				in.close();
				return (String[]) value;
			} catch (IOException e) {
				// do nothing, use the splitter
			} catch (ClassNotFoundException e) {
				// do nothing, use the splitter
			}
		}
		// split the value in the datastore
		return Iterables.toArray(Splitter.on(getSeparator()).split(datastoreValue), String.class);
	}

}