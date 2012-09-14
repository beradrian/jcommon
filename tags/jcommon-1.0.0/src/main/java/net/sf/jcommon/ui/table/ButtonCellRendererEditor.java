package net.sf.jcommon.ui.table;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * Class that implements TableCellRenderer and TableCellEditor. It can be used
 * to put a button in a table cell.
 * Simply create a ButtonCellRendererEditor instance and set it as the editor
 * and renderer for the column that you want to be a button.
 */
public class ButtonCellRendererEditor extends JButton implements TableCellRenderer, TableCellEditor {
    protected EventListenerList listenerList = new EventListenerList();
    transient protected ChangeEvent changeEvent = null;
    protected JButton button;
    private boolean alwaysEnabled = true;
    private boolean textHidden = true;

    /**
     * Creates a button with no set text or icon.
     */
    public ButtonCellRendererEditor() {
        this(null, null);
    }

    /**
     * Creates a button with an icon.
     *
     * @param icon the Icon image to display on the button
     */
    public ButtonCellRendererEditor(Icon icon) {
        this(null, icon);
    }

    /**
     * Creates a button with text.
     *
     * @param text the text of the button
     */
    public ButtonCellRendererEditor(String text) {
        this(text, null);
    }

    /**
     * Creates a button where properties are taken from the
     * <code>Action</code> supplied.
     *
     * @param a the <code>Action</code> used to specify the new button
     * @since 1.3
     */
    public ButtonCellRendererEditor(Action a) {
        this();
        setAction(a);
    }

    /**
     * Creates a button with initial text and an icon.
     *
     * @param text the text of the button
     * @param icon the Icon image to display on the button
     */
    public ButtonCellRendererEditor(String text, Icon icon) {
        button = this;
        button.setFont(new Font("monospaced", Font.BOLD, 18));
        button.setFocusable(false);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopCellEditing();
            }
        });

        // initialize
        init(text, icon);
    }

    public ButtonCellRendererEditor(JButton button) {
        this.button = button;
        button.setFont(new Font("monospaced", Font.BOLD, 18));
        button.setFocusable(false);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopCellEditing();
            }
        });
    }

    /**
     * Returns the value of this cell.
     *
     * @return the value of this cell
     */
    public Object getCellEditorValue() {
        return this;
    }

    /**
     * Sets the value of this cell.
     *
     * @param value the new value of this cell
     */
    public void setValue(Object value) {
        if (value instanceof Action) {
            button.setAction((Action) value);
            if (textHidden) {
                button.setText("");
            }
        } else if (value instanceof Icon) {
            button.setText("");
            button.setIcon((Icon) value);
        } else if (value instanceof String) {
            button.setText(value.toString());
            button.setIcon(null);
        }
        if (alwaysEnabled)
            button.setEnabled(true);
    }

    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent) anEvent).getClickCount() >= 1;
        }
        return false;
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        table.getSelectionModel().setSelectionInterval(row, row);
        value = table.getModel().getValueAt(row, column);
        return getTableCellRendererComponent(table, value, true, true, row, column);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setValue(value);
        return button;
    }

    public boolean isAlwaysEnabled() {
        return alwaysEnabled;
    }

    public void setAlwaysEnabled(boolean alwaysEnabled) {
        this.alwaysEnabled = alwaysEnabled;
    }

    public boolean isTextHidden() {
        return textHidden;
    }

    public void setTextHidden(boolean textHidden) {
        this.textHidden = textHidden;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return false;
    }

    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    /**
     * Adds a <code>CellEditorListener</code> to the listener list.
     *
     * @param l the new listener to be added
     */
    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    /**
     * Removes a <code>CellEditorListener</code> from the listener list.
     *
     * @param l the listener to be removed
     */
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is created lazily.
     *
     * @see javax.swing.event.EventListenerList
     */
    protected void fireEditingStopped() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is created lazily.
     *
     * @see javax.swing.event.EventListenerList
     */
    protected void fireEditingCanceled() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
            }
        }
    }

}
