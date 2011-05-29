package net.sf.jcommon.util;

import java.util.*;

/** A collection that decorates another collection 
 * and triggers {@link CollectionListener}'s for every change.
 * @see CollectionEvent 
 */
public class ObservableCollection<E> implements Collection<E> {

	/** An iterator that decorates another iterator and 
	 * triggers {@link CollectionListener}'s every time an element is removed. 
	 * The triggered listeners are the ones from the enclosing collection. 
	 */
	protected class ObservableIterator implements Iterator<E> {

		/** The underlying iterator. */
		private Iterator<E> decorated;
		/** The current element. */
		protected E current;

		public ObservableIterator(Iterator<E> decorated) {
			this.decorated = decorated;
		}
		
		public boolean hasNext() {
			return decorated.hasNext();
		}

		public E next() {
			current = decorated.next();
			return current;
		}

		public void remove() {
			CollectionEvent<E> evt = new CollectionEvent<E>(ObservableCollection.this, current, CollectionEvent.Operation.REMOVE);
			firePreEvent(evt);
			decorated.remove();
			firePostEvent(evt);			
		}
		
	}
	
	/** The underlying collection. */
	private Collection<E> decorated;
	/** The listeners that needs to be triggered for every change. */
	private Collection<CollectionListener<E>> listeners = new ArrayList<CollectionListener<E>>(); 
	
	/** Creates a new collection by decorating the given collection. */
	public ObservableCollection(Collection<E> decorated) {
		this.decorated = decorated;
	}
	
	protected void firePreEvent(CollectionEvent<E> evt) {
		for (CollectionListener<E> listener : listeners) {
			listener.pre(evt);
		}
	}
	
	protected void firePostEvent(CollectionEvent<E> evt) {
		for (CollectionListener<E> listener : listeners) {
			listener.post(evt);
		}		
	}

	public void addCollectionListener(CollectionListener<E> listener) {
		listeners.add(listener);
	}
	
	public void removeCollectionListener(CollectionListener<E> listener) {
		listeners.remove(listener);
	}

	public boolean add(E elem) {
		CollectionEvent<E> evt = new CollectionEvent<E>(this, elem, CollectionEvent.Operation.ADD);
		firePreEvent(evt);
		boolean retval = decorated.add(elem);
		if (retval)
			firePostEvent(evt);
		return retval;
	}

	public boolean addAll(Collection<? extends E> elems) {
		CollectionEvent<E> evt = new CollectionEvent<E>(this, elems, CollectionEvent.Operation.ADD);
		firePreEvent(evt);
		boolean retval = decorated.addAll(elems);
		if (retval)
			firePostEvent(evt);
		return retval;
	}

	public void clear() {
		CollectionEvent<E> evt = new CollectionEvent<E>(this, CollectionEvent.Operation.CLEAR);
		firePreEvent(evt);
		decorated.clear();
		firePostEvent(evt);
	}

	public boolean contains(Object e) {
		return decorated.contains(e);
	}

	public boolean containsAll(Collection<?> c) {
		return decorated.containsAll(c);
	}

	public boolean isEmpty() {
		return decorated.isEmpty();
	}

	public Iterator<E> iterator() {
		return new ObservableIterator(decorated.iterator());
	}

	@SuppressWarnings("unchecked")
	public boolean remove(Object e) {
		CollectionEvent<E> evt = new CollectionEvent<E>(this, (E)e, CollectionEvent.Operation.REMOVE);
		firePreEvent(evt);
		boolean retval = decorated.remove(e);
		if (retval)
			firePostEvent(evt);
		return retval;
	}

	@SuppressWarnings("unchecked")
	public boolean removeAll(Collection<?> c) {
		CollectionEvent<E> evt = new CollectionEvent<E>(this, (Collection<E>)c, CollectionEvent.Operation.REMOVE);
		firePreEvent(evt);
		boolean retval = decorated.removeAll(c);
		if (retval)
			firePostEvent(evt);
		return retval;
	}

	public boolean retainAll(Collection<?> c) {
		Collection<E> toRemove = new HashSet<E>();
		for (E e : decorated) {
			if (!c.contains(e)) {
				toRemove.add(e);
			}
		}
		CollectionEvent<E> evt = new CollectionEvent<E>(this, toRemove, CollectionEvent.Operation.REMOVE);
		firePreEvent(evt);
		boolean retval = decorated.retainAll(c);
		if (retval)
			firePostEvent(evt);
		return retval;
	}

	public int size() {
		return decorated.size();
	}

	public Object[] toArray() {
		return decorated.toArray();
	}

	public <T> T[] toArray(T[] array) {
		return decorated.toArray(array);
	}

	
}
