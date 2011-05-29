package net.sf.jcommon.util;

import java.util.Collection;

/**
 * An abstract class for a filter that is a collection of filters. 
 * The logic is to be decided in the defining subclasses. 
 */
public abstract class ComposedFilter<T> implements Filter<T> {

    private Filter<T>[] filters;

    @SuppressWarnings("unchecked")
	protected ComposedFilter(Collection<Filter<T>> filters) {
        this.filters = filters.toArray(new Filter[filters.size()]);
    }

    protected ComposedFilter(Filter<T>... filters) {
        this.filters = filters;
    }

    protected Filter<T>[] getFilters() {
        return filters;
    }
}
