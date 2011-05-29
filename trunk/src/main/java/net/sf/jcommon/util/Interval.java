package net.sf.jcommon.util;

/**
 * A class used as an interval of two values.
 * This is an immutable object.
 */
public class Interval<T extends Comparable<T>> implements Comparable<Interval<T>> {

    /** The interval lower limit. */
    private T from = null;
    /** The interval upper limit. */
    private T to = null;

    /** Creates a new interval.
     * @param from the start value
     * @param to the end value
     */
    public Interval(T from, T to) {
        if (from != null && to != null && from.compareTo(to) > 0) {
            throw new IllegalArgumentException("From value must be smaller than to value");
        }
        this.from = from;
        this.to = to;
    }

    /** Creates a new interval that includes only one given value.
     * @param value the only value in the interval
     */
    public Interval(T value) {
        this.from = this.to = value;
    }

    /** @return the lower limit of the interval. */
    public T getFrom() {
        return from;
    }

    /** @return the upper limit of the interval. */
    public T getTo() {
        return to;
    }

    /** Test if the interval contains a given value.
     * @param value the value to be tested
     * @return true if the given value is in this interval.
     */
    public boolean contains(T value) {
        return ((from == null) || (from.compareTo(value) <= 0))
                && ((to == null) || (to.compareTo(value) >= 0));
    }

    /** Test if the interval contains another interval.
     * @param interval the value to be tested
     * @return true if the given interval is in this interval.
     */
    public boolean contains(Interval<T> interval) {
        return ((from == null) || (from.compareTo(interval.from) <= 0))
                && ((to == null) || (to.compareTo(interval.to) >= 0));
    }

    /**
     * Calculates the intersection of two intervals.
     * @param that the second interval
     * @return the intersection between this interval and the given one.
     * If the intersection is empty it returns null.
     */
    public Interval<T> intersect(Interval<T> that) {
        if (that == null)
            return null;
        if ((this.from != null && that.to != null && this.from.compareTo(that.to) > 0)
            || (this.to != null && that.from != null && this.to.compareTo(that.from) < 0)) {
            return null;
        }
        if (this.from == null || (that.from != null && this.from.compareTo(that.from) <= 0)) {
            if (this.to != null && (that.to == null || this.to.compareTo(that.to) <= 0)) {
                return new Interval<T>(that.from, this.to);
            } else {
                return that;
            }
        } else {
            if (this.to.compareTo(that.to) <= 0) {
                return this;
            } else {
                return new Interval<T>(this.from, that.to);
            }
        }
    }

    public String toString() {
        return from.toString() + " -> " + to.toString();
    }

    public int compareTo(Interval<T> i) {
        return this.from.compareTo(i.from);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Interval<?>)) return false;

        final Interval<?> interval = (Interval<?>) o;

        if (from != null ? !from.equals(interval.from) : interval.from != null) return false;
        if (to != null ? !to.equals(interval.to) : interval.to != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (from != null ? from.hashCode() : 0);
        result = 29 * result + (to != null ? to.hashCode() : 0);
        return result;
    }
}