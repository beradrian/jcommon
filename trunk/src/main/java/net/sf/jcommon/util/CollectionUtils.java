package net.sf.jcommon.util;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ForwardingList;

/**
 */
public class CollectionUtils {

	/** Use only static methods. */
	private CollectionUtils() {
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
