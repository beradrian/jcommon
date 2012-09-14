package net.sf.jcommon.util;

import java.util.EventListener;

/** A listener that is triggered every time a change is made to an {@link ObservableCollection}. */
public interface CollectionListener<E> extends EventListener {
	
	/** This is called before the actual change. */
	void pre(CollectionEvent<E> event);
	
	/** This is called before the change. */
	void post(CollectionEvent<E> event);
	
}
