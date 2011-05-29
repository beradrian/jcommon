package net.sf.jcommon.util;

/**
 * A dummy filter that accepts any object.
 * There is no FalseFilter, just use <code>new {@link NotFilter}(new TrueFilter())</code>.
 */
public class TrueFilter<T> implements Filter<T> {

    public boolean accept(T t) {
        return true;
    }
}
