package net.sf.jcommon.ui;

import javax.swing.*;
import java.io.Serializable;
import java.util.*;

/**
 * A combo-box model that uses a Collection instead of a Vector to store the elements.
 */
@SuppressWarnings("serial")
public class CollectionComboBoxModel<E> extends AbstractListModel<E> implements MutableComboBoxModel<E>, Serializable {

    /** The collection kept in sync with the list. */
    private Collection<E> originalObjects;

    /** The list used to store the elements. */
    private List<E> objects;
    private Comparator<E> comparator;

    /** The selected object. */
    private Object selectedObject;

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * a vector.
     *
     * @param collection  a List object ...
     */
    public CollectionComboBoxModel(Collection<E> collection) {
        this(collection, null);
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * a vector.
     *
     * @param collection  a List object ...
     */
    public CollectionComboBoxModel(Collection<E> collection, Comparator<E> comparator) {
        originalObjects = collection;
        objects = new ArrayList<E>(collection);
        this.comparator = comparator;
        if (this.comparator != null)
            Collections.sort(objects, comparator);

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

    public E getElementAt(int index) {
        if (index >= 0 && index < objects.size())
            return objects.get(index);
        else
            return null;
    }

    /**
     * Returns the index-position of the specified object in the list.
     * @param anObject
     * @return an integer representing the index position, where 0 is the first position
     */
    public int getIndexOf(E anObject) {
        return objects.indexOf(anObject);
    }

    public void addElement(E anObject) {
        objects.add(anObject);
        originalObjects.add(anObject);
        fireIntervalAdded(this, objects.size() - 1, objects.size() - 1);
        if (objects.size() == 1 && selectedObject == null && anObject != null) {
            setSelectedItem( anObject );
        }
    }

    public void insertElementAt(E anObject,int index) {
        objects.add(index, anObject);
        originalObjects.add(anObject);
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

        Object removedObject = objects.remove(index);
        originalObjects.remove(removedObject);

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
            originalObjects.clear();
            selectedObject = null;
            fireIntervalRemoved(this, firstIndex, lastIndex);
        } else {
            selectedObject = null;
        }
    }
}
