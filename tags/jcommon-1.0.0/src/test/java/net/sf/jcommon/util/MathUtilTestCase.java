package net.sf.jcommon.util;

import static org.junit.Assert.*;

import org.junit.*;

public class MathUtilTestCase {

	@Test public void testFactorial() {
		assertEquals("fact(0) != 1", 1, MathUtils.factorial(0));
		assertEquals("fact(1) != 1", 1, MathUtils.factorial(1));
		assertEquals("fact(2) != 2", 2, MathUtils.factorial(2));
		assertEquals("fact(3) != 6", 6, MathUtils.factorial(3));
		assertEquals("fact(4) != 24", 24, MathUtils.factorial(4));
		assertEquals("fact(7) != 5040", 5040, MathUtils.factorial(7));
	}
	
    @Test public void testCombinationsCount() {
        assertEquals("c(6,4)<>15", 15, MathUtils.combinationsCount(6, 4));
    }

}
