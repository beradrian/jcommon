package net.sf.jcommon.util;

import java.util.Collection;

/**
 * A composed filter that accepts the object if and only if at least one of the contained filters are accepting it. 
 */
public class OrFilter<T> extends ComposedFilter<T> {

    public OrFilter(Collection<Filter<T>> filters) {
        super(filters);
    }

    public OrFilter(Filter<T>... filters) {
        super(filters);
    }

    public boolean accept(T t) {
        for (Filter<T> f : getFilters()) {
            if (f.accept(t))
                return true;
        }
        return false;
    }
}