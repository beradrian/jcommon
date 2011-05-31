package net.sf.jcommon.ui;

import javax.swing.*;
import java.util.Enumeration;

/**
 * An enumeration that goes through a ListModel.
 * @author Adrian BER (adrian.ber@greefsoftware.com)
 */
public class ListModelEnumeration implements Enumeration {

    private int index = 0;
    private ListModel listModel;

    public ListModelEnumeration(ListModel listModel) {
        this.listModel = listModel;
    }

    public boolean hasMoreElements() {
        return index < listModel.getSize();
    }

    public Object nextElement() {
        return listModel.getElementAt(index++);
    }
}