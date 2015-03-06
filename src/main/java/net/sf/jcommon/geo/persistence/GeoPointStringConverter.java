package net.sf.jcommon.geo.persistence;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import net.sf.jcommon.geo.GeoPoint;

@Converter(autoApply = true)
public class GeoPointStringConverter implements AttributeConverter<GeoPoint, String> {

	@Override
	public String convertToDatabaseColumn(GeoPoint attributeObject) {
		return attributeObject == null ? null : attributeObject.toString();
	}

	@Override
	public GeoPoint convertToEntityAttribute(String datastoreValue) {
		if (datastoreValue == null) {
			return null;
		}
		String[] coordinateStrings = datastoreValue.split("\\s|:|,|;");
		double[] coordinates = new double[coordinateStrings.length];
		int i = 0;
		for (String s : coordinateStrings) {
			try {
				coordinates[i] = Double.parseDouble(s);
			} catch(NumberFormatException exc) {
				coordinates[i] = 0;
			}
			i++;
		}
		switch (coordinates.length) {
			case 0:
				return new GeoPoint();
			case 1:
				return new GeoPoint(coordinates[0], coordinates[0]);
			case 2:
				return new GeoPoint(coordinates[0], coordinates[1]);
			case 3:
			default:
				return new GeoPoint(coordinates[0], coordinates[1], coordinates[2]);
		}
	}

}
