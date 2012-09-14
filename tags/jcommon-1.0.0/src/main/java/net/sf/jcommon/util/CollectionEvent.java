package net.sf.jcommon.util;

import java.util.Collection;
import java.util.EventObject;
import java.util.HashSet;

@SuppressWarnings("serial")
public class CollectionEvent<E> extends EventObject {

	public static enum Operation {ADD, UPDATE, REMOVE, CLEAR};
	private Collection<E> collection;
	private E oldElement;
	private E element;
	private Collection<E> elements = new HashSet<E>();
	private Operation operation;
	
	public CollectionEvent(Collection<E> source, Operation operation) {
		super(source);
		this.operation = operation;
	}

	public CollectionEvent(Collection<E> source, E element, Operation operation) {
		super(source);
		this.operation = operation;
		this.element = element;
		this.elements.add(element);
	}

	public CollectionEvent(Collection<E> source, Collection<? extends E> elements, Operation operation) {
		super(source);
		this.operation = operation;
		this.elements.addAll(elements);
		if (this.elements.size() > 0)
			this.element = this.elements.iterator().next();
	}

	public CollectionEvent(Collection<E> source, E oldElement, E newElement) {
		super(source);
		this.operation = Operation.UPDATE;
		this.element = newElement;
		this.elements.add(element);
		this.oldElement = oldElement;
	}

	public Collection<E> getCollection() {
		return collection;
	}

	public E getElement() {
		return element;
	}
	
	public E getOldElement() {
		return oldElement;
	}
	
	public Collection<? extends E> getElements() {
		return elements;
	}
	
	public Operation getOperation() {
		return operation;
	}
}
