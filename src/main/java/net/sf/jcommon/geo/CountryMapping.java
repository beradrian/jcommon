package net.sf.jcommon.geo;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.store.mapped.mapping.ObjectAsStringMapping;

public class CountryMapping extends ObjectAsStringMapping {
	
	private static final Country SAMPLE = Country.getCountries().findByISO("ro");

	public Object getSampleValue(ClassLoaderResolver clr) {
		return SAMPLE;
	}

	/**
	 * Method to return the Java type being represented
	 * 
	 * @return The Java type we represent
	 */
	public Class<Country> getJavaType() {
		return Country.class;
	}

	/**
	 * Method to return the default length of this type in the datastore.
	 * 
	 * @return The default length
	 */
	public int getDefaultLength(int index) {
		return 2;
	}

	/**
	 * Method to set the datastore string value based on the object value.
	 * 
	 * @param object The object
	 * @return The string value to pass to the datastore
	 */
	protected String objectToString(Object object) {
		if (object instanceof Country) {
			return ((Country) object).getISO();
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
		return Country.getCountries().findByISO(datastoreValue.trim());
	}
}
