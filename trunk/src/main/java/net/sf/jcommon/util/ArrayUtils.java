package net.sf.jcommon.util;


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

}
