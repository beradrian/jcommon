package net.sf.jcommon.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

public class CollectionOfEnumsToStringAttributeConverterTests {

	enum MyEnum {A, B, C}
	
	CollectionOfEnumsToStringAttributeConverter<MyEnum> converter = new CollectionOfEnumsToStringAttributeConverter<MyEnum>() {};
	
	@Test
	public void testConvertToDatabaseColumn() {
		assertNull(converter.convertToDatabaseColumn(null));
		
		Collection<MyEnum> values = new ArrayList<MyEnum>();
		assertEquals("", converter.convertToDatabaseColumn(values));
		
		values.add(MyEnum.A);
		values.add(MyEnum.B);
		assertEquals("A,B", converter.convertToDatabaseColumn(values));
	}
	
	@Test
	public void testConvertToEntityAttribute() {
		assertNull(converter.convertToEntityAttribute(null));
		
		Collection<MyEnum> values = converter.convertToEntityAttribute("");
		assertEquals(0, values.size());
		
		values = converter.convertToEntityAttribute("B,C");
		assertEquals(2, values.size());
		assertTrue(values.contains(MyEnum.B));
		assertTrue(values.contains(MyEnum.C));
	}
	
}
