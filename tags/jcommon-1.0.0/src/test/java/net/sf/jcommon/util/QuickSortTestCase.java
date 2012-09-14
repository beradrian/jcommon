package net.sf.jcommon.util;

import static org.junit.Assert.*;

import org.junit.*;

/**
 */
public class QuickSortTestCase {

    private int[] a, a2;

    @Before
    public void setUp() throws Exception {
        a = new int[] {7, 3, 9, 4, 5};
        a2 = new int[] {3, 2};
    }

    @Test public void testOrder2() {
        QuickSort.quicksort(a2, null);

        assertEquals(2, a2[0]);
        assertEquals(3, a2[1]);

        QuickSort.quicksort(a2, null);

        assertEquals(2, a2[0]);
        assertEquals(3, a2[1]);
    }

    @Test public void testOrder() {
        QuickSort.quicksort(a, null);

        assertEquals(3, a[0]);
        assertEquals(4, a[1]);
        assertEquals(5, a[2]);
        assertEquals(7, a[3]);
        assertEquals(9, a[4]);
    }
}

