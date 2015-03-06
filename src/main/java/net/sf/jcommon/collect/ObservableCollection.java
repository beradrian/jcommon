package net.sf.jcommon.collect;

import java.util.*;

import com.google.common.collect.ForwardingCollection;
import com.google.common.collect.ForwardingIterator;

/** A collection that decorates another collection 
 * and triggers {@link CollectionListener}'s for every change.
 * @see CollectionEvent 
 */
public class ObservableCollection<E> extends ForwardingCollection<E> {

	/** An iterator that decorates another iterator and 
	 * triggers {@link CollectionListener}'s every time an element is removed. 
	 * The triggered listeners are the ones from the enclosing collection. 
	 */
	protected class ObservableIterator extends ForwardingIterator<E> {

		/** The underlying iterator. */
		private Iterator<E> decorated;
		/** The current element. */
		protected E current;

		public ObservableIterator(Iterator<E> decorated) {
			this.decorated = decorated;
		}
		
		@Override
		protected Iterator<E> delegate() {
			return decorated;
		}
		
		@Override
		public E next() {
			current = delegate().next();
			return current;
		}

		@Override
		public void remove() {
			CollectionEvent<E> evt = new CollectionEvent<E>(ObservableCollection.this, current, CollectionEvent.Operation.REMOVE);
			firePreEvent(evt);
			delegate().remove();
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
	
	@Override
	protected Collection<E> delegate() {
		return decorated;
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

	public boolean add(E e) {
		CollectionEvent<E> evt = new CollectionEvent<E>(this, e, CollectionEvent.Operation.ADD);
		firePreEvent(evt);
		if (delegate().add(e)) {
			firePostEvent(evt);
			return true;
		}
		return false;
	}

	public boolean addAll(Collection<? extends E> c) {
		CollectionEvent<E> evt = new CollectionEvent<E>(this, c, CollectionEvent.Operation.ADD);
		firePreEvent(evt);
		if (delegate().addAll(c)) {
			firePostEvent(evt);
			return true;
		}
		return false;	
	}

	public void clear() {
		CollectionEvent<E> evt = new CollectionEvent<E>(this, CollectionEvent.Operation.CLEAR);
		firePreEvent(evt);
		delegate().clear();
		firePostEvent(evt);
	}

	public Iterator<E> iterator() {
		return new ObservableIterator(delegate().iterator());
	}

	@SuppressWarnings("unchecked")
	public boolean remove(Object e) {
		CollectionEvent<E> evt = new CollectionEvent<E>(this, (E)e, CollectionEvent.Operation.REMOVE);
		firePreEvent(evt);
		boolean retval = delegate().remove(e);
		if (retval)
			firePostEvent(evt);
		return retval;
	}

	@SuppressWarnings("unchecked")
	public boolean removeAll(Collection<?> c) {
		CollectionEvent<E> evt = new CollectionEvent<E>(this, (Collection<E>)c, CollectionEvent.Operation.REMOVE);
		firePreEvent(evt);
		boolean retval = delegate().removeAll(c);
		if (retval)
			firePostEvent(evt);
		return retval;
	}

	public boolean retainAll(Collection<?> c) {
		Collection<E> toRemove = new HashSet<E>();
		for (E e : delegate()) {
			if (!c.contains(e)) {
				toRemove.add(e);
			}
		}
		CollectionEvent<E> evt = new CollectionEvent<E>(this, toRemove, CollectionEvent.Operation.REMOVE);
		firePreEvent(evt);
		boolean retval = delegate().retainAll(c);
		if (retval)
			firePostEvent(evt);
		return retval;
	}
	
}
