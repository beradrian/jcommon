package net.sf.jcommon.util;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.*;

public class PermutationTestCase {
	
	@Test public void testKPerm() {
		assertEquals("abc", Permutation.getPermutation(3, 0).permute("abc"));
		assertEquals("acb", Permutation.getPermutation(3, 1).permute("abc"));
		assertEquals("bac", Permutation.getPermutation(3, 2).permute("abc"));
		assertEquals("cab", Permutation.getPermutation(3, 3).permute("abc"));
		assertEquals("bca", Permutation.getPermutation(3, 4).permute("abc"));
		assertEquals("cba", Permutation.getPermutation(3, 5).permute("abc"));
		assertEquals("abcd", Permutation.getPermutation(4, 0).permute("abcd"));
		assertEquals("dcba", Permutation.getPermutation(4, 23).permute("abcd"));
	}
	
	@Test public void testPermIndices() {
		assertEquals("abc", new Permutation(new int[] {0, 1, 2}).permute("abc"));
		assertEquals("acb", new Permutation(new int[] {0, 2, 1}).permute("abc"));
		assertEquals("bdac", new Permutation(new int[] {1, 3, 0, 2}).permute("abcd"));		
	}

	@Test public void testPermIterator() {
		Iterator<Permutation> it = Permutation.iterator(3);
		assertTrue(it.hasNext());
		assertEquals("abc", it.next().permute("abc"));
		assertNotSame("abc", it.next().permute("abc"));
		assertTrue(it.hasNext());
		it.next();
		it.next();
		it.next();
		it.next();
		assertNull(it.next());
		assertFalse(it.hasNext());
		assertNull(it.next());
	}
	
	@Test public void testPermIterator1() {
		Iterator<Permutation> it = Permutation.iterator(1);
		assertTrue(it.hasNext());
		assertEquals("a", it.next().permute("a"));
		assertFalse(it.hasNext());
		assertNull(it.next());
		assertFalse(it.hasNext());
	}
}
