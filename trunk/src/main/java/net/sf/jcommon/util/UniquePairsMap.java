package net.sf.jcommon.util;

import java.util.*;

/**
 * An object that keeps an array of pairs values.
 *  Once assigned a right value to a left value, this value can not be reassigned.
 *  For instance, if you insert the pair ("value", "mappedValue"),
 *  any other try to insert a pair like ("value", 'anything') will overwrite the
 *  previous mapping(pair). This means that the value "value" should no more
 *  be used as a left value.
 */
public class UniquePairsMap<K, V> implements Map<K, V> {
    /** Array used to keep the left values of the pairs */
    private ArrayList<K> leftArray = new ArrayList<K>();
    /** Array used to keep the right values of the pairs */
    private ArrayList<V> rightArray = new ArrayList<V>();

    /**
     * Gets the left value of the pair having as the right value the specified parameter.
     * @param right The right value of the pair.
     * @return The left value corresponding to the specified right value. Null if not found.
     */
    public Object getLeft(Object right) {
        if (right == null) return null;
        int i = getIndex(rightArray, right);
        if (i == -1)
            return null;
        return leftArray.get(i);
    }

    /**
     * Gets the right value of the pair having as the left value the specified parameter.
     * @param left The left value of the pair.
     * @return The right value coresponding to the specified left value. Null if not found.
     */
    public V getRight(Object left) {
        if (left == null) return null;
        int i = getIndex(leftArray, left);
        if (i == -1)
            return null;
        return rightArray.get(i);
    }

    /**
     * Puts a pair into the map. If one of the left or right value was already mapped
     *  it overwrites its pair. Starts searching through the left array.
     * @param left
     * @param right
     */
    public void putPair(K left, V right) {
        if (left == null || right == null) return;
        int leftIndex = getIndex(leftArray, left);
        int rightIndex = getIndex(rightArray, right);

        if (leftIndex != -1 && rightIndex != -1) return;

        if (leftIndex != -1) {
            rightArray.add(leftIndex, right);
            return;
        }
        if (rightIndex != -1) {
            leftArray.add(rightIndex, left);
            return;
        }

        leftArray.add(left);
        rightArray.add(right);
    }

    /**
     * For internal use only. Returns the index of the value object in the list array.
     * @param list the list in which the specified value is searched.
     * @param value The value to be searched in the specified array.
     * @return The index of the value in the list. -1 if not found.
     */
    private int getIndex(ArrayList<?> list, Object value) {
        //list should not be null. No test is made since this method is only
        //for internal use. This conditions should be checked before calling this method.
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (value.equals(list.get(i)))
                return i;
        }
        return -1;
    }


    public int size() {
        return leftArray.size();
    }

    public boolean isEmpty() {
        return size() <= 0;
    }

    public boolean containsKey(Object key) {
        return leftArray.contains(key);
    }

    public boolean containsValue(Object value) {
        return rightArray.contains(value);
    }

    @Override
    public V put(K key, V value) {
        V prev = getRight(key);
        putPair(key, value);
        return prev;
    }

    public void putAll(Map<? extends K, ? extends V> t) {
        for (K key : t.keySet()) {
            putPair(key, t.get(key));
        }
    }

    public void clear() {
        leftArray.clear();
        rightArray.clear();
    }

    public Set<K> keySet() {
        return new HashSet<K>(leftArray);
    }

    public Collection<V> values() {
        return new HashSet<V>(rightArray);
    }

    public Set<Map.Entry<K, V>> entrySet() {
		// TODO implement this
        return null;
    }

	@Override
	public V get(Object key) {
        return getRight(key);
	}

	@Override
	public V remove(Object key) {
		// TODO implement this
		return null;
	}
}