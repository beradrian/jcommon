package net.sf.jcommon.util;

import java.util.Collections;
import java.util.Comparator;

public class Comparators {

	public static enum SortingDirection {
		ASCENDING, DESCENDING
	}
	
	/** Use only static methods. */
	private Comparators() {}
	
	private static class ChainComparator<T> implements Comparator<T> {

	    private Comparator<T>[] comparators;

	    protected ChainComparator(Comparator<T>... comparators) {
	        this.comparators = comparators;
	    }

	    public int compare(T o1, T o2) {
	        for (Comparator<T> c : comparators) {
	            int val = c.compare(o1, o2);
	            if (val != 0)
	                return val;
	        }
	        return 0;
	    }
	}
	
	/** Chains a list of comparators that will return the first non-zero value. */
	public static <T> Comparator<T> chain(Comparator<T>... comparators) {
		return new ChainComparator<T>(comparators);
	}
	
	private static final Comparator<String> IGNORE_CASE_COMPARATOR = new Comparator<String>() {
		public int compare(String o1, String o2) {
			return o1 == null ? (o2 == null ? 0 : Integer.MIN_VALUE)
					: (o2 == null ? Integer.MAX_VALUE : o1.compareToIgnoreCase(o2));
		}
	};
	
	/** @return a case insensitive string comparator */
	public static Comparator<String> ignoreCaseComparator() {
		return IGNORE_CASE_COMPARATOR;
	}
	
	/**
	 * @param comparator the original comparator that is considered to compare in ascending order
	 * @return a reversed comparator, if the direction is descending, the same comparator otherwise
	 */
	public static <T> Comparator<T> forDirection(Comparator<T> comparator, SortingDirection direction) {
		return direction == SortingDirection.ASCENDING ? comparator : Collections.reverseOrder(comparator);
	}
}
