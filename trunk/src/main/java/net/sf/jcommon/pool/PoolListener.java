package net.sf.jcommon.pool;

public interface PoolListener<T> {
	void acquired(PoolEvent<T> event);
	void released(PoolEvent<T> event);
}
