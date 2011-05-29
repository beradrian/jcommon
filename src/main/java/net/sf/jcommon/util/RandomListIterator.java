package net.sf.jcommon.util;

import java.util.ListIterator;
import java.util.List;
import java.util.ArrayList;

/**
 * A list iterator that can iterate through the elements in the list in a random order.
 * The random order mode can be switched on or off. When in random order this iterator
 * will iterate through elements infinitely.
 * @author Adrian BER
 */
public class RandomListIterator<T> implements ListIterator<T> {

    /** The associated list. */
    private List<T> list;
    /** The history - already walked through elements. */
    private List<Integer> history = new ArrayList<Integer>();
    /** The current element. */
    private int current = -1;
    /** If true the list elements are iterated in a random way, otherwise in sequential order. */
    private boolean random;

    public RandomListIterator(List<T> list, boolean random) {
        this.list = list;
        this.random = random;
    }

    public RandomListIterator(List<T> list) {
        this(list, true);
    }

    /** @return if the list elements are retrieved in random or sequential order. */
    public boolean isRandom() {
        return random;
    }

    /** Sets the random order.
     * @param random the list elements are provided randomly if true, sequentially otherwise
     */
    public void setRandom(boolean random) {
        this.random = random;
    }

    public boolean hasNext() {
        return random || (current < 0) || ((history.get(current) + 1) < list.size());
    }

    public T next() {
        if (!hasNext()) {
            return null;
        }
        current++;
        int i = nextIndex();
        history.add(current, i);
        return list.get(i);
    }

    public boolean hasPrevious() {
        return current > 0;
    }

    public T previous() {
        if (!hasPrevious()) {
            return null;
        }
        current--;
        return list.get(history.get(current));
    }

    public int nextIndex() {
        if (random) {
            return (int)(Math.random() * list.size());
        } else {
            return (current >= 0 ? history.get(current) + 1 : 0);
        }
    }

    public int previousIndex() {
        return (current > 0 ? history.get(current - 1) : -1);
    }

    public void registerToHistory(T t) {
        int index = list.indexOf(t);
        if (index >= 0) {
            current++;
            history.add(current, index);
        }
    }

    public void remove() {
        if (current >= 0) {
            int index = history.get(current);
            list.remove(index);
            updateIndices(index, -1);
        }
    }

    public void set(T t) {
        if (current >= 0) {
            int index = history.get(current);
            list.set(index, t);
        }
    }

    public void add(T t) {
        if (current >= 0) {
            int index = history.get(current);
            list.add(index, t);
            updateIndices(index, 1);
        }
    }

    private void updateIndices(int overIndex, int delta) {
        for (ListIterator<Integer> i = history.listIterator(); i.hasNext();) {
            int index = i.next();
            if (index >= overIndex)
                i.set(index + delta);
        }
    }
}
