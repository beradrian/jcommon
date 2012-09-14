package net.sf.jcommon.util;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import com.google.common.base.Predicate;

/** A list for which the elements are filtered. 
 * If elements are not accepted by the associated filter then, 
 * depending on the tolerance level, either an exception is thrown or the elements are silently ignored. 
 */
public class FilteredList<E> extends FilteredCollection<E> implements List<E> {

	private List<E> decorated;
	
	/** Creates a new collection by decorating the given collection. */
	public FilteredList(List<E> decorated, Predicate<? super E> predicate, UnaaceptanceAction level) {
		super(decorated, predicate, level);
		this.decorated = decorated;
	}
	
	public FilteredList(List<E> decorated, Predicate<? super E> predicate) {
		this(decorated, predicate, UnaaceptanceAction.SILENT);
	}

	@Override
	public void add(int index, E e) {
		if (!getPredicate().apply(e)) {
			if (getLevel() == UnaaceptanceAction.EXCEPTION)
				throw new IllegalArgumentException("The object cannot be added to the collection");
		} else {
			decorated.add(index, e);
		}
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		Predicate<? super E> predicate = getPredicate();
		if (getLevel() == UnaaceptanceAction.SILENT) {
			boolean retval = false;
			for (E e : c) {
				if (predicate.apply(e)) {
					retval = true;
					decorated.add(index, e);
					index++;
				}
			}
			return retval;
		} else {
			for (E e : c) {
				if (!predicate.apply(e))
					throw new IllegalArgumentException("One of the objects cannot be added to the collection");
			}
			return decorated.addAll(index, c);
		}
	}

	@Override
	public E get(int index) {
		return decorated.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return decorated.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return decorated.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return decorated.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return decorated.listIterator(index);
	}

	@Override
	public E remove(int index) {
		return decorated.remove(index);
	}

	@Override
	public E set(int index, E e) {
		if (!getPredicate().apply(e)) {
			if (getLevel() == UnaaceptanceAction.EXCEPTION)
				throw new IllegalArgumentException("The object cannot be added to the collection");
			else
				return null;
		} else {
			return decorated.set(index, e);
		}
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return decorated.subList(fromIndex, toIndex);
	}
	

}
