package net.sf.jcommon.util;

/**
 * A general filter for objects.
 */
public interface Filter<T> {

    boolean accept(T t);

}
