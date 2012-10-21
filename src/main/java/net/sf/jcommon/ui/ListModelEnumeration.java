package net.sf.jcommon.ui;

import javax.swing.*;
import java.util.Enumeration;

/**
 * An enumeration that goes through a ListModel.
 */
public class ListModelEnumeration<T> implements Enumeration<T> {

    private int index = 0;
    private ListModel<T> listModel;

    public ListModelEnumeration(ListModel<T> listModel) {
        this.listModel = listModel;
    }

    public boolean hasMoreElements() {
        return index < listModel.getSize();
    }

    public T nextElement() {
        return listModel.getElementAt(index++);
    }
}