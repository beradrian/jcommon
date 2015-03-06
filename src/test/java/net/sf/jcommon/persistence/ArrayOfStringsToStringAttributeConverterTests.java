package net.sf.jcommon.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ArrayOfStringsToStringAttributeConverterTests {

	ArrayOfStringsToStringAttributeConverter converter = new ArrayOfStringsToStringAttributeConverter();
	
	@Test
	public void testConvertToDatabaseColumn() {
		assertNull(converter.convertToDatabaseColumn(null));
		
		assertEquals("", converter.convertToDatabaseColumn(new String[]{}));
		
		assertEquals("a,bb,ccc", converter.convertToDatabaseColumn(new String[]{"a", "bb", "ccc"}));
	}
	
	@Test
	public void testConvertToEntityAttribute() {
		assertNull(converter.convertToEntityAttribute(null));
		
		assertEquals(0,converter.convertToEntityAttribute("").length);
		
		String[] values = converter.convertToEntityAttribute("x,y,123");
		assertEquals(3, values.length);
		assertEquals("x", values[0]);
		assertEquals("y", values[1]);
		assertEquals("123", values[2]);
	}
	
}
