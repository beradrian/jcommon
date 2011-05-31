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
import java.io.File;

/**
 * Class that implements TableCellRenderer and TableCellEditor. It can be used
 * to put a button in a table cell.
 * Simply create a ButtonCellRendererEditor instance and set it as the editor
 * and renderer for the column that you want to be a button.
 */
public class FileCellRendererEditor extends JPanel implements TableCellRenderer, TableCellEditor {
    private JTextField fileTextField;
    private JFileChooser fileChooser;

    private String dialogTitle = "Open";
    private int dialogType = JFileChooser.OPEN_DIALOG;

    protected EventListenerList listenerList = new EventListenerList();
    transient protected ChangeEvent changeEvent = null;

    /**
     * Creates a button with initial text and an icon.
     *
     */
    public FileCellRendererEditor() {
        init();
    }

    private void init() {
        fileTextField = new JTextField(30);
        JButton browseButton = new JButton("...");
        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopCellEditing();
                browse();
            }
        });

        setLayout(new BorderLayout());
        add(fileTextField);
        add(browseButton, BorderLayout.EAST);
    }

    private void browse() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setAcceptAllFileFilterUsed(true);
            fileChooser.setDialogTitle(dialogTitle);
            fileChooser.setDialogType(dialogType);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        }
        File selectedFile = new File(fileTextField.getText());
        if (selectedFile.exists()) {
            fileChooser.setCurrentDirectory(selectedFile.getParentFile());
            fileChooser.setSelectedFile(selectedFile);
        }

        int retval;
        if (dialogType == JFileChooser.OPEN_DIALOG) {
            retval = fileChooser.showOpenDialog(this);
        } else {
            retval = fileChooser.showSaveDialog(this);
        }
        if (retval == JFileChooser.APPROVE_OPTION) {
            fileTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    protected String getPath() {
        return fileTextField.getText();
    }

    protected void setPath(String path) {
        fileTextField.setText(path);
    }

    public void requestFocus() {
        fileTextField.requestFocus();
    }

    /**
     * @return the value of this cell
     */
    public Object getCellEditorValue() {
        return getPath();
    }

    /**
     * Sets the value of this cell.
     *
     * @param value the new value of this cell
     */
    public void setValue(Object value) {
        if (value instanceof String) {
            setPath(value.toString());
        } else if (value instanceof File) {
            setPath(((File)value).getAbsolutePath());
        }
    }

    public boolean isCellEditable(EventObject anEvent) {
        return anEvent instanceof MouseEvent && ((MouseEvent) anEvent).getClickCount() >= 1;
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
        return this;
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