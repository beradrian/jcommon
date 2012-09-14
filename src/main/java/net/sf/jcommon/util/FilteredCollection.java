package net.sf.jcommon.util;

import java.util.Collection;
import java.util.Iterator;

import com.google.common.base.Predicate;

/** A collection for which the elements are filtered. 
 * If elements are not accepted by the associated filter then, 
 * depending on the tolerance level, either an exception is thrown or the elements are silently ignored. 
 */
public class FilteredCollection<E> implements Collection<E> {

	public enum UnaaceptanceAction {SILENT, EXCEPTION};
	
	/** The underlying collection. */
	private Collection<E> decorated;
	/** The listeners that needs to be triggered for every change. */
	private Predicate<? super E> predicate;
	private UnaaceptanceAction level;
	
	
	/** Creates a new collection by decorating the given collection. */
	public FilteredCollection(Collection<E> decorated, Predicate<? super E> predicate, UnaaceptanceAction level) {
		this.decorated = decorated;
		this.predicate = predicate;
		this.level = level;
	}
	
	public FilteredCollection(Collection<E> decorated, Predicate<? super E> predicate) {
		this(decorated, predicate, UnaaceptanceAction.SILENT);
	}
	
	public Predicate<? super E> getPredicate() {
		return predicate;
	}

	public UnaaceptanceAction getLevel() {
		return level;
	}

	public void setLevel(UnaaceptanceAction level) {
		this.level = level;
	}

	@Override
	public boolean add(E e) {
		if (!predicate.apply(e)) {
			if (level == UnaaceptanceAction.EXCEPTION)
				throw new IllegalArgumentException("The object cannot be added to the collection");
			return false;
		} else {
			return decorated.add(e);
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		if (level == UnaaceptanceAction.SILENT) {
			boolean retval = false;
			for (E e : c) {
				if (predicate.apply(e)) {
					retval = true;
					decorated.add(e);
				}
			}
			return retval;
		} else {
			for (E e : c) {
				if (!predicate.apply(e))
					throw new IllegalArgumentException("One of the objects cannot be added to the collection");
			}
			return decorated.addAll(c);
		}
	}

	@Override
	public void clear() {
		decorated.clear();
	}

	@Override
	public boolean contains(Object o) {
		return decorated.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return decorated.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return decorated.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return decorated.iterator();
	}

	@Override
	public boolean remove(Object o) {
		return decorated.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return decorated.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return decorated.retainAll(c);
	}

	@Override
	public int size() {
		return decorated.size();
	}

	@Override
	public Object[] toArray() {
		return decorated.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return decorated.toArray(a);
	}

}
