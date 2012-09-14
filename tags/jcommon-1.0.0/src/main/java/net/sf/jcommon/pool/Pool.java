package net.sf.jcommon.pool;

import java.util.*;

/** A pool of object from which you can acquire (and release after) available objects. */
public abstract class Pool<E> {
	
	/** The maximum allowed size of this pool. It is counting both available and acquired. */
	private int maximumSize;
	
	/** The available objects. */
	private Collection<E> available = new HashSet<E>();
	/** The already acquired objects. */
	private Collection<E> acquired = new HashSet<E>();
	
	private List<PoolListener<E>> listeners = new ArrayList<PoolListener<E>>();
	
	/** @return the maximum allowed size of this pool. It is counting both available and acquired. */
	public int getMaximumSize() {
		return maximumSize;
	}

	/** Sets the maximum allowed size of this pool. It is counting both available and acquired. 
	 * @param size the new maximum size */
	public void setMaximumSize(int size) {
		if (acquired.size() < size) {
			synchronized (this) {
				this.maximumSize = size;
				int i = size - acquired.size() - available.size();
				if (i < 0) {
					for (Iterator<E> it = available.iterator(); it.hasNext(); i++) {
						it.remove();
					}
				}
				this.notify();
			}
		} else {
			throw new IllegalArgumentException("You cannot decrease size below the number of acquired objects.");
		}
	}

	public E acquire(boolean blocking) {
		if (available.size() > 0) {
			Iterator<E> iterator = available.iterator();
			E e = iterator.next();
			iterator.remove();
			acquired.add(e);
			fireAcquired(e);
			return e;
		} else if (acquired.size() < maximumSize) {
			E e = create();
			acquired.add(e);
			fireAcquired(e);
			return e;
		} else {
			if (!blocking) {
				return null;
			} else {
				E e = null;
				synchronized (this) {
					try {
						while (e == null) {
							wait();
							e = acquire(false);
						}
					} catch (InterruptedException exc) {
						return null;
					}
				}
				return e;
			}
		}
	}
	
	public void release(E e) {
		synchronized (this) {
			if (acquired.remove(e)) {
				available.add(e);
				fireReleased(e);
				this.notify();
			}
		}
	}
	
	public abstract E create();

	public void addPoolListener(PoolListener<E> listener) {
		listeners.add(listener);
	}
	
	public void removePoolListener(PoolListener<E> listener) {
		listeners.remove(listener);
	}
	
	protected void fireAcquired(E x) {
		PoolEvent<E> evt = new PoolEvent<E>(this, x);
		for (PoolListener<E> l : listeners) {
			l.acquired(evt);
		}
	}

	protected void fireReleased(E x) {
		PoolEvent<E> evt = new PoolEvent<E>(this, x);
		for (PoolListener<E> l : listeners) {
			l.released(evt);
		}
	}
}
