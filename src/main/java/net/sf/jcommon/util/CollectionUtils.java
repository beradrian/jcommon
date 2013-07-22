package net.sf.jcommon.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ForwardingList;

/**
 */
public class CollectionUtils {

	/** Use only static methods. */
	private CollectionUtils() {
	}

	/**
	 * Returns if the first collection contains any of the elements in the
	 * second collection.
	 * 
	 * @param main the collection for which we checked the inclusion
	 * @param items the possible included elements in the first collection
	 * @return true if the first collection contains any of the elements in the second collection, false otherwise
	 */
	public static <T> boolean containsAny(Collection<T> main, Collection<? extends T> items) {
		for (T t : items) {
			if (main.contains(t))
				return true;
		}
		return false;
	}
	
	public static <T> ArrayList<T> copyAsArrayList(Collection<T> coll) {
		return new ArrayList<T>(coll);
	}

    public static <T extends Comparable<? super T>> List<T> sorted(Collection<T> c) {
    	List<T> d = copyAsArrayList(c);
    	Collections.sort(d);
    	return d;
    }

	/**
	 * Returns a decorated list with a limited amount of elements.
	 * @param decorated the decorated list to be limited
	 * @param maxSize the maximum size of the list
	 * @param silent if true, when an element is added, and the maximum size has been reached, 
	 * it silently ignores the operation, otherwise it throws an exception
	 * @return the newly decorated list
	 * @throws IllegalArgumentException if the decorated list has already more elements than maxSize
	 */
	public static <T> List<T> maxSizeLimit(final List<T> decorated, final int maxSize, final boolean silent) {
		if (decorated.size() > maxSize) {
			throw new IllegalArgumentException("The list contains already more elements than " + maxSize);
		}
		return new ForwardingList<T>() {
			@Override
			public boolean addAll(Collection<? extends T> collection) {
				return standardAddAll(collection);
			}

			@Override
			public boolean addAll(int index, Collection<? extends T> elements) {
				return standardAddAll(index, elements);
			}

			public boolean add(T e) {
				if (!checkMaxSize())
					return false;
				return delegate().add(e);
			}

			@Override
			public void add(final int index, final T e) {
				if (!checkMaxSize())
					return;
				delegate().add(index, e);
			}

			private boolean checkMaxSize() {
				if (size() >= maxSize) {
					if (silent) {
						return false;
					}
					throw new UnsupportedOperationException("Maximum Size " + maxSize + " reached");
				}
				return true;
			}

			@Override
			protected List<T> delegate() {
				return decorated;
			}
		};
	}

}
