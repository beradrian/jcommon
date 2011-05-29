package net.sf.jcommon.util;

import java.util.Comparator;
import java.util.List;

/**
 */
public abstract class ComposedComparator<T> implements Comparator<T> {

    private Comparator<T>[] comparators;

    @SuppressWarnings("unchecked")
	protected ComposedComparator(List<Comparator<T>> comparators) {
        this.comparators = comparators.toArray(new Comparator[comparators.size()]);
    }

    protected ComposedComparator(Comparator<T>... comparators) {
        this.comparators = comparators;
    }

    protected Comparator<T>[] getComparators() {
        return comparators;
    }

    public int compare(T o1, T o2) {
        for (Comparator<T> c : comparators) {
            int val = c.compare(o1, o2);
            if (val != 0)
                return val;
        }
        return 0;
    }
}