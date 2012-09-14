package net.sf.jcommon.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * An array list with a maximum size restriction.
 * If the collection reached the maximum size all calls to addition methods are
 * simply ignored. 
 */
@SuppressWarnings("serial")
public class MaxSizeArrayList<E> extends ArrayList<E> {

    /** The maximum size. */
    private long maxSize;

    /** @return the maximum size */
    public long getMaxSize() {
        return maxSize;
    }

    /** Sets the maximum size.
     * @param maxSize the new maximum size
     */
    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    public boolean add(E e) {
        return size() < maxSize && super.add(e);
    }

    public void add(int index, E element) {
        if (size() < maxSize)
            super.add(index, element);
    }

    public boolean addAll(Collection<? extends E> c) {
        return size() + c.size() <= maxSize && super.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        return size() + c.size() <= maxSize && super.addAll(index, c);
    }

}
