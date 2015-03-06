package net.sf.jcommon.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;

import org.junit.Test;

public class ListOfToStringAttributeConverterTests {

	ListOfToStringAttributeConverter<Integer> converter = new ListOfToStringAttributeConverter<Integer>(
			new AttributeConverter<Integer, String>() {

				@Override
				public String convertToDatabaseColumn(Integer attribute) {
					return String.valueOf(attribute + 1);
				}

				@Override
				public Integer convertToEntityAttribute(String dbData) {
					return Integer.parseInt(dbData) - 1;
				}

			}, ",") {
	};

	@Test
	public void testConvertToDatabaseColumn() {
		assertNull(converter.convertToDatabaseColumn(null));

		ArrayList<Integer> values = new ArrayList<Integer>();
		assertEquals("", converter.convertToDatabaseColumn(values));

		values.add(1);
		values.add(2);
		values.add(3);
		assertEquals("2,3,4", converter.convertToDatabaseColumn(values));
	}

	@Test
	public void testConvertToEntityAttribute() {
		assertNull(converter.convertToEntityAttribute(null));

		List<Integer> values = converter.convertToEntityAttribute("");
		assertEquals(0, values.size());

		values = converter.convertToEntityAttribute("9,8,7");
		assertEquals(3, values.size());
		assertEquals((Integer)8, values.get(0));
		assertEquals((Integer)7, values.get(1));
		assertEquals((Integer)6, values.get(2));
	}

}
