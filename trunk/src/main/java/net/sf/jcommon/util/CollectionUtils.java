package net.sf.jcommon.util;

import java.util.Collection;

/**
 */
public class CollectionUtils {

    /** Use only static methods. */
    private CollectionUtils() {}


    /** Returns if the first collection contains any of the elements in the second collection.
     * @param main the collection for which we checked the inclusion
     * @param items the possible included elements in the first collection
     * @return true if the first collection contains any of the elements in the second collection, false otherwise
     */
    public static <T> boolean containsAny(Collection<T> main, Collection<? extends T> items) {
        for (T t : items) {
            if (main.contains(t))
                return true;
        }
        return false;
    }
}
