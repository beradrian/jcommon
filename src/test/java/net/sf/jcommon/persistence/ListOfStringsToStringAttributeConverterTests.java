package net.sf.jcommon.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ListOfStringsToStringAttributeConverterTests {

	ListOfStringsToStringAttributeConverter converter = new ListOfStringsToStringAttributeConverter();
	
	@Test
	public void testConvertToDatabaseColumn() {
		assertNull(converter.convertToDatabaseColumn(null));
		
		List<String> values = new ArrayList<String>();
		assertEquals("", converter.convertToDatabaseColumn(values));
		
		values.add("a");
		values.add("bb");
		values.add("ccc");
		assertEquals("a,bb,ccc", converter.convertToDatabaseColumn(values));
	}
	
	@Test
	public void testConvertToEntityAttribute() {
		assertNull(converter.convertToEntityAttribute(null));
		
		List<String> values = converter.convertToEntityAttribute("");
		assertEquals(0, values.size());
		
		values = converter.convertToEntityAttribute("x,y,123");
		assertEquals(3, values.size());
		assertEquals("x", values.get(0));
		assertEquals("y", values.get(1));
		assertEquals("123", values.get(2));
	}
	
}
