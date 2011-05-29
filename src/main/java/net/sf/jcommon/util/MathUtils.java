package net.sf.jcommon.util;

public class MathUtils {

	private MathUtils() {}
	
	public static int clip(int value, int min, int max) {
	    return Math.min(Math.max(value, min), max);
	}
	
	public static double clip(double value, double min, double max) {
	    return Math.min(Math.max(value, min), max);
	}
	
	public static long factorial(int n) {
		long f = 1;
		for (int i = 2; i <= n; i++) {
			f *= i;
		}
		return f;
	}
	
    public static long combinationsCount(int n, int k) {
        long a = 1;
        long b = 1;
        int start = Math.max(k, n-k);
        for (int i = start + 1; i <= n; i++) {
            a *= i;
            b *= i - start;
        }
        return a / b;
    }

}
