package net.sf.jcommon.util;

import java.util.Collection;

/**
 * A composed filter that accepts the object if and only if all the contained filters are accepting it. 
 */
public class AndFilter<T> extends ComposedFilter<T> {

    public AndFilter(Collection<Filter<T>> filters) {
        super(filters);
    }

    public AndFilter(Filter<T>... filters) {
        super(filters);
    }

    public boolean accept(T t) {
        for (Filter<T> f : getFilters()) {
            if (!f.accept(t))
                return false;
        }
        return true;
    }
}
