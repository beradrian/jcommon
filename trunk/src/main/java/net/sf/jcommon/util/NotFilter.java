package net.sf.jcommon.util;

/**
 * A filter that decorates another filter and accepts the object 
 * if and only if the decorated filter does not accept it.
 */
public class NotFilter<T> implements Filter<T> {

	/** The decorated filter. */
    private Filter<T> filter;

    public NotFilter(Filter<T> filter) {
        this.filter = filter;
    }

    public boolean accept(T t) {
        return !filter.accept(t);
    }
}
