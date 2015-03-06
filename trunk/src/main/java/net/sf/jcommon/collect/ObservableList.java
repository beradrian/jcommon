package net.sf.jcommon.collect;

import java.util.*;

public class ObservableList<E> extends ObservableCollection<E> implements List<E> {

	protected class ObservableListIterator extends ObservableIterator implements ListIterator<E> {

		private ListIterator<E> decorated;
		private E current;
		private int currentIndex = -1;

		public ObservableListIterator(ListIterator<E> decorated) {
			super(decorated);
			this.decorated = decorated;
		}

		public void add(E elem) {
			ListEvent<E> evt = new ListEvent<E>(ObservableList.this, current, currentIndex, CollectionEvent.Operation.ADD);
			firePreEvent(evt);
			decorated.add(elem);
			firePostEvent(evt);						
		}

		public boolean hasPrevious() {
			return decorated.hasPrevious();
		}

		public int nextIndex() {
			return decorated.nextIndex();
		}

		public E next() {
			currentIndex++;
			return super.next();
		}

		public E previous() {
			currentIndex--;
			current = decorated.previous();
			return current;
		}

		public int previousIndex() {
			return decorated.previousIndex();
		}

		public void set(E elem) {
			ListEvent<E> evt = new ListEvent<E>(ObservableList.this, current, currentIndex, CollectionEvent.Operation.UPDATE);
			firePreEvent(evt);
			decorated.add(elem);
			firePostEvent(evt);						
		}
		
		
	}

	private List<E> decorated;
	
	public ObservableList(List<E> decorated) {
		super(decorated);
		this.decorated = decorated;
	}

	public void add(int location, E elem) {
		ListEvent<E> evt = new ListEvent<E>(this, elem, location, CollectionEvent.Operation.ADD);
		firePreEvent(evt);
		decorated.add(elem);
		firePostEvent(evt);
	}

	public boolean addAll(int location, Collection<? extends E> c) {
		CollectionEvent<E> evt = new CollectionEvent<E>(this, c, CollectionEvent.Operation.ADD);
		firePreEvent(evt);
		boolean retval = decorated.addAll(location, c);
		if (retval)
			firePostEvent(evt);
		return retval;
	}

	public E get(int location) {
		return decorated.get(location);
	}

	public int indexOf(Object object) {
		return decorated.indexOf(object);
	}

	public int lastIndexOf(Object object) {
		return decorated.lastIndexOf(object);
	}

	public ListIterator<E> listIterator() {
		return new ObservableListIterator(decorated.listIterator());
	}

	public ListIterator<E> listIterator(int location) {
		return new ObservableListIterator(decorated.listIterator(location));
	}

	public E remove(int location) {
		E elem = get(location);
		ListEvent<E> evt = new ListEvent<E>(this, elem, location, CollectionEvent.Operation.REMOVE);
		firePreEvent(evt);
		E retval = decorated.remove(location);
		firePostEvent(evt);
		return retval;
	}

	public E set(int location, E elem) {
		ListEvent<E> evt = new ListEvent<E>(this, decorated.get(location), elem, location);
		firePreEvent(evt);
		E retval = decorated.set(location, elem);
		firePostEvent(evt);
		return retval;
	}

	public List<E> subList(int start, int end) {
		return decorated.subList(start, end);
	}

}
