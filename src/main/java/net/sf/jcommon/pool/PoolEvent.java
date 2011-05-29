package net.sf.jcommon.pool;

import java.util.EventObject;

@SuppressWarnings("serial")
public class PoolEvent<T> extends EventObject {

	private T object;
	
	public PoolEvent(Pool<T> pool, T object) {
		super(pool);
		this.object = object;
	}

	public T getObject() {
		return object;
	}
}
