/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package net.sf.jcommon.ui;

import javax.swing.*;
import java.util.List;

/**
 */
@SuppressWarnings("serial")
public class ComboBoxModelGroup<E> extends ListModelGroup<E> implements ComboBoxModel<E> {
    private E selectedObject;

    protected ComboBoxModelGroup() {
    }

    public ComboBoxModelGroup(List<ListModel<E>> models) {
        super(models);
    }

    public E getSelectedItem() {
        return selectedObject;
    }

    /**
     * Set the value of the selected item. The selected item may be null.
     * <p/>
     *
     * @param anObject The combo box value or null for no selection.
     */
    @Override
    public void setSelectedItem(Object anObject) {
        if ((selectedObject != null && !selectedObject.equals(anObject)) ||
                selectedObject == null && anObject != null) {
            selectedObject = (E)anObject;
            fireContentsChanged(this, -1, -1);
        }
    }
}
