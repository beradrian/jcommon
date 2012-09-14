/*
 * Copyright (c) 2005 GREEF Software. All Rights Reserved.
 */
package net.sf.jcommon.ui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * List model formed by grouping more other ComboBox models.
 */
@SuppressWarnings("serial")
public class ListModelGroup<E> extends AbstractListModel<E> {
    private List<ListModel<E>> models = new ArrayList<ListModel<E>>();

    protected ListModelGroup() {
    }

    public ListModelGroup(List<ListModel<E>> models) {
        setModels(models);
    }

    protected void setModels(List<ListModel<E>> models) {
        this.models = models;
    }

    public int getSize() {
        int size = 0;
        for (int i = models.size() - 1; i >= 0; i--) {
            size += models.get(i).getSize();
        }
        return size;
    }

    public E getElementAt(int index) {
        long position = localize(index);
        return getModel((int) (position >> 32)).getElementAt((int) position);
    }

    /**
     * Localizes the element at the specified index.
     *
     * @param index The index of the element.
     * @return A positive long having the model index in the first integer (most signifiant 4 bytes)
     *         and the index of the less signifiant 4 bytes.
     */
    private long localize(int index) {
        int noOfModels = models.size();
        if (noOfModels == 0) {
            throw new IllegalStateException("empty list of models");
        }

        // offset = the beginning of the current model where we search for the element.
        for (int i = 0, offset = 0; i < noOfModels; i++) {
            ListModel<E> model = getModel(i);
            int modelSize = model.getSize();

            //if index is between the offset and the end of the current model, the element is in
            // the current model. Return the localization of the element.
            if (index < offset + modelSize) {
                return (((long) i) << 32) + (index - offset);
            }

            //otherwise move the offset at the beginning of the next model.
            offset += modelSize;
        }

        throw new IllegalArgumentException("wrong index");
    }

    /**
     * Convenient method to get the model at a index. Only converts it to ListModel.
     *
     * @param modelIndex The index of the model.
     * @return The model at the specified index from the group.
     */
    private ListModel<E> getModel(int modelIndex) {
        return models.get(modelIndex);
    }
}
