package net.sf.jcommon.persistence;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.JoinMetaData;
import org.datanucleus.store.mapped.mapping.ObjectAsStringMapping;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

public class StringsMapping extends ObjectAsStringMapping {

	private static final String DEFAULT_SEPARATOR = ",";
	
	/**
	 * Method to return the Java type being represented
	 * 
	 * @return The Java type we represent
	 */
	public Class<String[]> getJavaType() {
		return String[].class;
	}

	private String getSeparator() {
		AbstractMemberMetaData memberMetaData = getMemberMetaData();
		if (memberMetaData == null) 
			return DEFAULT_SEPARATOR;
		JoinMetaData joinMetaData = memberMetaData.getJoinMetaData();
		if (joinMetaData == null)
			return DEFAULT_SEPARATOR;
		String joinColumnName = joinMetaData.getColumnName();
		if (joinColumnName == null || joinColumnName.trim().length() == 0)
			return DEFAULT_SEPARATOR;
		return joinColumnName;
	}
	
	/**
	 * Method to set the datastore string value based on the object value.
	 * 
	 * @param object The object
	 * @return The string value to pass to the datastore
	 */
	protected String objectToString(Object object) {
		if (object == null)
			return null;
		if (object instanceof String[]) {
			return Joiner.on(getSeparator()).join((String[])object);
		} else {
			return object.toString();
		}
	}

	/**
	 * Method to extract the objects value from the datastore string value.
	 * 
	 * @param datastoreValue Value obtained from the datastore
	 * @return The value of this object (derived from the datastore string value)
	 */
	protected Object stringToObject(String datastoreValue) {
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
				return value;
			} catch (IOException e) {
				// do nothing, try to use the splitter
			} catch (ClassNotFoundException e) {
				// do nothing, try to use the splitter
			}
		}
		// split the value in the datastore
		return Iterables.toArray(Splitter.on(getSeparator()).split(datastoreValue), String.class);
	}
}
