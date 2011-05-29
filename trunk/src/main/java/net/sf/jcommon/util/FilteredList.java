package net.sf.jcommon.util;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/** A list for which the elements are filtered. 
 * If elements are not accepted by the associated filter then, 
 * depending on the tolerance level, either an exception is thrown or the elements are silently ignored. 
 */
public class FilteredList<E> extends FilteredCollection<E> implements List<E> {

	private List<E> decorated;
	
	/** Creates a new collection by decorating the given collection. */
	public FilteredList(List<E> decorated, Filter<E> filter, ToleranceLevel level) {
		super(decorated, filter, level);
		this.decorated = decorated;
	}
	
	public FilteredList(List<E> decorated, Filter<E> filter) {
		this(decorated, filter, ToleranceLevel.SILENT);
	}

	@Override
	public void add(int index, E e) {
		if (!getFilter().accept(e)) {
			if (getLevel() == ToleranceLevel.EXCEPTION)
				throw new IllegalArgumentException("The object cannot be added to the collection");
		} else {
			decorated.add(index, e);
		}
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		Filter<E> filter = getFilter();
		if (getLevel() == ToleranceLevel.SILENT) {
			boolean retval = false;
			for (E e : c) {
				if (filter.accept(e)) {
					retval = true;
					decorated.add(index, e);
					index++;
				}
			}
			return retval;
		} else {
			for (E e : c) {
				if (!filter.accept(e))
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
		if (!getFilter().accept(e)) {
			if (getLevel() == ToleranceLevel.EXCEPTION)
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
