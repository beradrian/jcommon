package net.sf.jcommon.util;

import static org.junit.Assert.*;

import org.junit.*;

/**
 */
public class ArrayUtilsTestCase {

    @Test public void testShortToBytes() {
        byte[] buff = new byte[3];
        ArrayUtils.shortToBytes((short)220, buff, 1);
        assertEquals("testShortToBytes1",   0, buff[0]);
        assertEquals("testShortToBytes1",   0, buff[1]);
        assertEquals("testShortToBytes1", -36, buff[2]);
        assertEquals("testBytesToShort1", 220, ArrayUtils.bytesToShort(buff, 1));
    }

    /** Test {@link ArrayUtils#reverseArray} with no elements. */
    @Test public void testReverseArray0() {
        int[] array = new int[0];
        ArrayUtils.reverse(array);
    }

    /** Test {@link ArrayUtils#reverseArray} with one element. */
    @Test public void testReverseArray1() {
        int[] array = new int[] {9};
        ArrayUtils.reverse(array);

        assertEquals(9, array[0]);
    }

    /** Test {@link ArrayUtils#reverseArray} with 2 elements. */
    @Test public void testReverseArray2() {
        int[] array = new int[] {3, 2};
        ArrayUtils.reverse(array);

        assertEquals(2, array[0]);
        assertEquals(3, array[1]);
    }

    /** Test {@link ArrayUtils#reverseArray} with odd number of elements. */
    @Test public void testReverseArrayOdd() {
        int[] array = new int[] {7, 3, 9, 4, 5};
        ArrayUtils.reverse(array);

        assertEquals(5, array[0]);
        assertEquals(4, array[1]);
        assertEquals(9, array[2]);
        assertEquals(3, array[3]);
        assertEquals(7, array[4]);
    }

    /** Test {@link ArrayUtils#reverseArray} with even number of elements. */
    @Test public void testReverseArrayEven() {
        int[] array = new int[] {3, 9, 4, 5};
        ArrayUtils.reverse(array);

        assertEquals(5, array[0]);
        assertEquals(4, array[1]);
        assertEquals(9, array[2]);
        assertEquals(3, array[3]);
    }

    @Test public void testReverseSubArrayEnd() {
        int[] array = new int[] {3, 9, 1, 4, 5};
        ArrayUtils.reverse(array, 3, 2);

        assertEquals(3, array[0]);
        assertEquals(9, array[1]);
        assertEquals(1, array[2]);
        assertEquals(5, array[3]);
        assertEquals(4, array[4]);
    }

    @Test public void testReverseSubArrayStart() {
        int[] array = new int[] {3, 9, 1, 4, 5};
        ArrayUtils.reverse(array, 0, 2);

        assertEquals(9, array[0]);
        assertEquals(3, array[1]);
        assertEquals(1, array[2]);
        assertEquals(4, array[3]);
        assertEquals(5, array[4]);
    }
    
    @Test public void testReverseSubArrayMiddle() {
        int[] array = new int[] {3, 9, 1, 4, 5};
        ArrayUtils.reverse(array, 1, 3);

        assertEquals(3, array[0]);
        assertEquals(4, array[1]);
        assertEquals(1, array[2]);
        assertEquals(9, array[3]);
        assertEquals(5, array[4]);
    }

    @Test public void testIndexOfString() {
        String[] array = new String[] {"3", "9", "4", "5"};
        assertEquals(1, ArrayUtils.indexOf(array, "9"));
        assertEquals(-1, ArrayUtils.indexOf(array, "11"));        
    }

    @Test public void testRotateBig() {
        int[] array = new int[] {3, 9, 1, 4, 5};
        ArrayUtils.rotate(array, 4);
    	
        assertEquals(9, array[0]);
        assertEquals(1, array[1]);
        assertEquals(4, array[2]);
        assertEquals(5, array[3]);
        assertEquals(3, array[4]);
    }

    @Test public void testRotateSmall() {
        int[] array = new int[] {3, 9, 1, 4, 5};
        ArrayUtils.rotate(array, 2);
    	
        assertEquals(4, array[0]);
        assertEquals(5, array[1]);
        assertEquals(3, array[2]);
        assertEquals(9, array[3]);
        assertEquals(1, array[4]);
    }

    @Test public void testRotateOne() {
        int[] array = new int[] {3};
        ArrayUtils.rotate(array, 2);
    	
        assertEquals(3, array[0]);
    }

    @Test public void testRotateTwo() {
        int[] array = new int[] {3, 4};
        ArrayUtils.rotate(array, 1);
    	
        assertEquals(4, array[0]);
        assertEquals(3, array[1]);
    }
}

