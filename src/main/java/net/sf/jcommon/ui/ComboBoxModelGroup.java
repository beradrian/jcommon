/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package net.sf.jcommon.ui;

import javax.swing.*;
import java.util.List;

/**
 * //todo: add description
 *
 * @author Iulian Stefanica (iulian.stefanica@greefsoftware.com)
 */
public class ComboBoxModelGroup extends ListModelGroup implements ComboBoxModel {
    private Object selectedObject;

    protected ComboBoxModelGroup() {
    }

    public ComboBoxModelGroup(List models) {
        super(models);
    }

    public Object getSelectedItem() {
        return selectedObject;
    }

    /**
     * Set the value of the selected item. The selected item may be null.
     * <p/>
     *
     * @param anObject The combo box value or null for no selection.
     */
    public void setSelectedItem(Object anObject) {
        if ((selectedObject != null && !selectedObject.equals(anObject)) ||
                selectedObject == null && anObject != null) {
            selectedObject = anObject;
            fireContentsChanged(this, -1, -1);
        }
    }
}
