package net.sf.jcommon.util;

import static org.junit.Assert.*;

import org.junit.Test;

class TestClazz<A, B> {
	
}

class TestSuper<C> extends TestClazz<String, C> {
	
}

class TestSuperSuper extends TestSuper<Long> {

}

class TestSuperSuperSuper extends TestSuperSuper {

}

public class ReflectUtilsTestCase {

	@Test public void testGetTypeIndex() {
		assertEquals(0, ReflectUtils.getTypeIndex(TestClazz.class, "A"));
		assertEquals(1, ReflectUtils.getTypeIndex(TestClazz.class, "B"));
		assertEquals(-1, ReflectUtils.getTypeIndex(TestClazz.class, "C"));
		assertEquals(-1, ReflectUtils.getTypeIndex(TestClazz.class, null));
	}
	
	@Test public void testGetClosestClass() {
		assertEquals(TestSuper.class, ReflectUtils.getClosestClass(TestSuperSuper.class, TestClazz.class));
		assertEquals(TestSuperSuper.class, ReflectUtils.getClosestClass(TestSuperSuperSuper.class, TestSuper.class));
		assertEquals(TestSuper.class, ReflectUtils.getClosestClass(TestSuperSuperSuper.class, TestClazz.class));
	}
	
	@Test public void testGetActualType() {
		assertEquals(String.class, ReflectUtils.getActualType(TestSuper.class, TestClazz.class, "A"));
		assertEquals(String.class, ReflectUtils.getActualType(TestSuperSuper.class, TestClazz.class, "A"));
		assertEquals(Long.class, ReflectUtils.getActualType(TestSuperSuper.class, TestClazz.class, "B"));

		assertEquals(String.class, ReflectUtils.getActualType(TestSuperSuperSuper.class, TestClazz.class, "A"));
		assertEquals(String.class, ReflectUtils.getActualType(TestSuperSuperSuper.class, TestClazz.class, "A"));
		assertEquals(Long.class, ReflectUtils.getActualType(TestSuperSuperSuper.class, TestClazz.class, "B"));
		
		assertEquals(Long.class, ReflectUtils.getActualType(TestSuperSuperSuper.class, TestSuper.class, "C"));
		
		assertNull(ReflectUtils.getActualType(TestSuper.class, TestClazz.class, "C"));
		assertNull(ReflectUtils.getActualType(TestSuperSuper.class, TestClazz.class, "C"));
		assertNull(ReflectUtils.getActualType(TestSuperSuperSuper.class, TestClazz.class, "C"));

		assertNull(ReflectUtils.getActualType(TestSuperSuper.class, TestClazz.class, "D"));
	}
}
