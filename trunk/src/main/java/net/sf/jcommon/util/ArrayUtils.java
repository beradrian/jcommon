package net.sf.jcommon.util;

import java.util.*;

/**
 * Some utility methods to work with array and base64 encoded strings.
 */
public class ArrayUtils {

    /** Use only the static methods. */
    private ArrayUtils() {}

    public static int[] fillRange(int n) {
    	return fillRange(0, n);
    }
    	
    /**
     * Creates an array of {@code n} integers from {@code offset}.
     * @param n the length array
     * @return the generated array
     */
    public static int[] fillRange(int offset, int n) {
        int[] r = new int[n];
        for (int i = 0; i < r.length; i++) {
            r[i] = offset + i;
        }
        return r;
    }
    
    private static final Random randomGenerator = new Random();
    
    public static int[] random(int n) {
    	return random(0, n, n);
    }
    
    public static int[] random(int start, int end, int n) {
        int[] r = new int[n];
        for (int i = 0; i < r.length; i++) {
            r[i] = randomGenerator.nextInt(end - start) + start;
        }
        return r;    	
    }

}
