package net.sf.jcommon.ui;

import javax.swing.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * A combobox model that uses a List instead of a Vector to store the elements.
 * @author Adrian BER
 */
public class ListComboBoxModel extends AbstractListModel implements MutableComboBoxModel, Serializable {

    /** The list used to store the elements. */
    List objects;

    /** The selected object. */
    Object selectedObject;

    /**
     * Constructs an empty DefaultComboBoxModel object.
     */
    public ListComboBoxModel() {
        objects = new ArrayList();
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * an array of objects.
     *
     * @param items  an array of Object objects
     */
    public ListComboBoxModel(final Object items[]) {
        objects = new ArrayList();
        ((ArrayList)objects).ensureCapacity(items.length);

        int i,c;
        for (i=0, c=items.length; i < c; i++ )
            objects.add(items[i]);

        if (getSize() > 0) {
            selectedObject = getElementAt(0);
        }
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * a vector.
     *
     * @param v  a List object ...
     */
    public ListComboBoxModel(List v) {
        objects = v;

        if (getSize() > 0) {
            selectedObject = getElementAt(0);
        }
    }

    // implements javax.swing.ComboBoxModel
    /**
     * Set the value of the selected item. The selected item may be null.
     * <p>
     * @param anObject The combo box value or null for no selection.
     */
    public void setSelectedItem(Object anObject) {
        if ((selectedObject != null && !selectedObject.equals( anObject )) ||
	    selectedObject == null && anObject != null) {
	    selectedObject = anObject;
	    fireContentsChanged(this, -1, -1);
        }
    }

    public Object getSelectedItem() {
        return selectedObject;
    }

    public int getSize() {
        return objects.size();
    }

    public Object getElementAt(int index) {
        if (index >= 0 && index < objects.size())
            return objects.get(index);
        else
            return null;
    }

    /**
     * Returns the index-position of the specified object in the list.
     * @param anObject
     * @return an int representing the index position, where 0 is the first position
     */
    public int getIndexOf(Object anObject) {
        return objects.indexOf(anObject);
    }

    public void addElement(Object anObject) {
        objects.add(anObject);
        fireIntervalAdded(this, objects.size() - 1, objects.size() - 1);
        if (objects.size() == 1 && selectedObject == null && anObject != null) {
            setSelectedItem( anObject );
        }
    }

    public void insertElementAt(Object anObject,int index) {
        objects.add(index, anObject);
        fireIntervalAdded(this, index, index);
    }

    public void removeElementAt(int index) {
        if (getElementAt(index) == selectedObject) {
            if (index == 0) {
                setSelectedItem(getSize() == 1 ? null : getElementAt( index + 1 ));
            }
            else {
                setSelectedItem(getElementAt(index - 1));
            }
        }

        objects.remove(index);

        fireIntervalRemoved(this, index, index);
    }

    public void removeElement(Object anObject) {
        int index = objects.indexOf(anObject);
        if (index != -1) {
            removeElementAt(index);
        }
    }

    /**
     * Empties the list.
     */
    public void removeAllElements() {
        if (objects.size() > 0) {
            int firstIndex = 0;
            int lastIndex = objects.size() - 1;
            objects.clear();
    	    selectedObject = null;
            fireIntervalRemoved(this, firstIndex, lastIndex);
        } else {
            selectedObject = null;
        }
    }
}
