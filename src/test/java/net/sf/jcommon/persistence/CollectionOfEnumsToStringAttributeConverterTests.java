package net.sf.jcommon.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CollectionOfEnumsToStringAttributeConverterTests {

	enum MyEnum {A, B, C}
	
	CollectionOfEnumsToStringAttributeConverter<MyEnum, List<MyEnum>> converter = new CollectionOfEnumsToStringAttributeConverter<MyEnum, List<MyEnum>>() {

		@Override
		protected List<MyEnum> createCollection() {
			return new ArrayList<MyEnum>();
		}
		
	};
	
	@Test
	public void testConvertToDatabaseColumn() {
		assertNull(converter.convertToDatabaseColumn(null));
		
		List<MyEnum> values = new ArrayList<MyEnum>();
		assertEquals("", converter.convertToDatabaseColumn(values));
		
		values.add(MyEnum.A);
		values.add(MyEnum.B);
		assertEquals("A,B", converter.convertToDatabaseColumn(values));
	}
	
	@Test
	public void testConvertToEntityAttribute() {
		assertNull(converter.convertToEntityAttribute(null));
		
		List<MyEnum> values = converter.convertToEntityAttribute("");
		assertEquals(0, values.size());
		
		values = converter.convertToEntityAttribute("B,C");
		assertEquals(2, values.size());
		assertTrue(values.contains(MyEnum.B));
		assertTrue(values.contains(MyEnum.C));
	}
	
}
