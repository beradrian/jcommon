package net.sf.jcommon.util;

import java.util.*;

/**
 * Some utility methods to work with array and base64 encoded strings.
 */
public class ArrayUtils {

    /** Use only the static methods. */
    private ArrayUtils() {}

    /** Tests two arrays for equality.
     * @param x the first array to be compared
     * @param y the second array to be compared
     * @return true if the arrays are equal, false otherwise.
     */
    public static boolean equals(byte[] x, byte[] y) {
        if (x == null || y == null)
            return x == y;
        if (x.length == y.length) {
            for (int i = 0; i < x.length; i++)
                if (x[i] != y[i])
                    return false;
            return true;
        }
        return false;
    }

    /** Tests two arrays for equality.
     * @param x the first array to be compared
     * @param y the second array to be compared
     * @return true if the arrays are equal, false otherwise.
     */
    public static boolean equals(int[] x, int[] y) {
        if (x == null || y == null)
            return x == y;
        if (x.length == y.length) {
            for (int i = 0; i < x.length; i++)
                if (x[i] != y[i])
                    return false;
            return true;
        }
        return false;
    }

    /** Returns a subarray of the given array.
     * @param x the initial array
     * @param startIndex the index of the first element in the subarray
     * @param endIndex the index of the last element in the subarray
     * @return the subbarray
     */
    public static byte[] subArray(byte[] x, int startIndex, int endIndex) {
        if (startIndex >= x.length)
            return null;
        if (endIndex > x.length)
            endIndex = x.length - 1;
        if (startIndex >= endIndex)
            return null;
        byte[] r = new byte[endIndex - startIndex];
        System.arraycopy(x, startIndex, r, 0, endIndex - startIndex);
        return r;
    }

    public static void intToBytes(int value, byte[] buff, int offset) {
        buff[offset    ] = (byte)((value >> 24) & 0xff);
        buff[offset + 1] = (byte)((value >> 16) & 0xff);
        buff[offset + 2] = (byte)((value >>  8) & 0xff);
        buff[offset + 3] = (byte)((value      ) & 0xff);
    }

    public static int bytesToInt(byte[] buff, int offset) {
        int x = buff[offset];
        x = (x << 8) | (buff[offset + 1] & 0xff);
        x = (x << 8) | (buff[offset + 2] & 0xff);
        x = (x << 8) | (buff[offset + 3] & 0xff);
        return x;
    }

    public static void shortToBytes(short value, byte[] buff, int offset) {
        buff[offset    ] = (byte)((value >>  8) & 0xff);
        buff[offset + 1] = (byte)((value      ) & 0xff);
    }

    public static short bytesToShort(byte[] buff, int offset) {
        short x = buff[offset];
        x = (short)((x << 8) | (buff[offset + 1] & 0xff));
        return x;
    }

    /** Converts a byte array to an int array.
     * @param arr the initial array of bytes
     * @return the array of ints
     */
    public static int[] toIntArray(byte[] arr) {
        int[] iarr = new int[arr.length / 4 + (arr.length % 4 == 0 ? 0 : 1)];
        for (int i = 0; i < iarr.length; i++) {
            iarr[i] = bytesToInt(arr, i * 4);
        }
        return iarr;
    }

    /** Converts an array of integers to a byte array.
     * @param arr the initial array of integers
     * @return the array of bytes
     */
    public static byte[] getBytes(int[] arr) {
        byte[] barr = new byte[arr.length * 4];
        for (int i = 0; i < arr.length; i++) {
            intToBytes(arr[i], barr, i * 4);
        }
        return barr;
    }

    /**
     * Reverse the elements in the array.
     * @param array the array to be reversed.
     */
    public static void reverse(int[] array) {
    	reverse(array, 0, array.length);
    }

    /**
     * Reverse the {@code length} number elements in the array starting with offset.
     * @param array the array to be reversed.
     */
    public static void reverse(int[] array, int offset, int length) {
        for (int i = 0, half = length / 2; i < half; i++) {
            int aux = array[i + offset];
            array[i + offset] = array[length - 1 - i + offset];
            array[length - 1 - i + offset] = aux;
        }
    }

    public static int indexOf(int[] array, int element) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == element)
                return i;
        }
        return -1;
    }

    public static <T> int indexOf(T[] array, T element) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(element))
                return i;
        }
        return -1;
    }

    public static int[] range(int n) {
    	return range(0, n);
    }
    	
    /**
     * Creates an array of {@code n} integers from {@code offset}.
     * @param n the length array
     * @return the generated array
     */
    public static int[] range(int offset, int n) {
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

    public static String concatToString(int[] array, String separator) {
        if (array == null)
            return null;
        StringBuilder sb = new StringBuilder();
        if (array.length > 0)
            sb.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            if (separator != null)
                sb.append(separator);
            sb.append(array[i]);
        }
        return sb.toString();
    }

    public static void swap(int[] array, int offset1, int offset2) {
    	swap(array, offset1, offset2, 1);
    }
    	
    public static void swap(int[] array, int offset1, int offset2, int len) {
    	for (int i = 0; i < len; i++) {
	        int aux = array[offset1 + i];
	        array[offset1 + i] = array[offset2 + i];
	        array[offset2 + i] = aux;
    	}
    }
    
    public static void swap(float[] array, int offset1, int offset2) {
    	swap(array, offset1, offset2, 1);
    }
    	
    public static void swap(float[] array, int offset1, int offset2, int len) {
    	for (int i = 0; i < len; i++) {
	        float aux = array[offset1 + i];
	        array[offset1 + i] = array[offset2 + i];
	        array[offset2 + i] = aux;
    	}
    }
    
    public static int[] rotate(int[] array, int k) {
    	return rotate(array, 0, array.length, k);
    }
    	
    public static int[] rotate(int[] array, int offset, int n, int k) {
    	// non-recursive version
    	k %= n; 
		while (n > 1 && k > 0) {
			int n_k = n - k;
			
	    	if (k > n_k) {
	    		swap(array, offset, offset + n - n_k,  n_k);
	    		//rotate(array, offset, k, k - n_k);
	    		n = k;
	    		k -= n_k;
	    	} else if (k < n_k) {
	    		swap(array, offset, offset + n_k,  k);
	    		//rotate(array, offset + k, n_k, k);
	    		offset += k;
	    		n = n_k;
	    	} else {
	    		swap(array, offset, offset + n_k, k);
	    		break;
	    	}
		}
    	return array;
    }
}
