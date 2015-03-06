package net.sf.jcommon.collect;

import java.util.Collection;
import java.util.TreeSet;

@SuppressWarnings("serial")
public class ListEvent<E> extends CollectionEvent<E> {

	private int index;
	private Collection<Integer> indices = new TreeSet<Integer>();
	
	public ListEvent(Collection<E> source, Operation operation) {
		super(source, operation);
		this.index = -1;
	}

	public ListEvent(Collection<E> source, E element, int index, Operation operation) {
		super(source, element, operation);
		this.index = index;
		this.indices.add(index);
	}

	public ListEvent(Collection<E> source, Collection<? extends E> elements, 
			Collection<Integer> indices, Operation operation) {
		super(source, elements, operation);
		this.indices.addAll(indices);
		if (this.indices.size() > 0)
			this.index = this.indices.iterator().next();
	}

	public ListEvent(ObservableList<E> source, E oldElement, E newElement, int location) {
		super(source, oldElement, newElement);
		this.index = location;
		this.indices.add(index);
	}

	public int getIndex() {
		return index;
	}
	
	public Collection<Integer> getIndices() {
		return indices;
	}
	
}
