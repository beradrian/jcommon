package net.sf.jcommon.util;

import java.util.Comparator;

/**
 */
public class OrderComparator<T> implements Comparator<T> {

    public enum Order { DESCENDENT(-1), ASCENDENT(1);

        private int value;

        Order(int value) {
            this.value = value;
        }

        public int intValue() {
            return value;
        }
    }

    private Order order;
    private Comparator<T> comp;

    public OrderComparator(Comparator<T> comp, Order order) {
        this.order = order;
        this.comp = comp;
    }

    public OrderComparator(Comparator<T> comp) {
        this(comp, Order.ASCENDENT);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int compare(T o1, T o2) {
        return comp.compare(o1, o2) * order.intValue();
    }
}
