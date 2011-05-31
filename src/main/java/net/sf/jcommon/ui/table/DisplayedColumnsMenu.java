package net.sf.jcommon.ui.table;

import net.sf.jcommon.ui.PopupUtils;

import javax.swing.*;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.JTableHeader;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;

/**
 * A popup menu installed on a table header from where you can choose the columns to be displayed.
 * It can be simply installed on a table using <code>new DisplayedColumnsMenu(table)</code>
 */
public class DisplayedColumnsMenu extends JPopupMenu {

    /** the associated table. */
    private JTable table;

    /** Listener for table header. */
    private TableColumnModelListener columnModelListener;

    /** Mouse listener for table header to show the popup. */
    private MouseListener popupListener;

    /** Constructor.
     * @param table the table on which header this popup will be installed on
     */
    public DisplayedColumnsMenu(JTable table) {
        this.table = table;
        init();
    }

    /** Initialiaser. */
    private void init() {
        // create the menu items from columns
        addMenuItemsForColumns(0, table.getColumnCount() - 1);

        // column model listener for adding/removing menu items corresponding to columns
        columnModelListener = new TableColumnModelListener() {
            public void columnMarginChanged(ChangeEvent e) {
            }

            public void columnSelectionChanged(ListSelectionEvent e) {
            }

            public void columnAdded(TableColumnModelEvent e) {
                addMenuItemsForColumns(e.getToIndex(), e.getToIndex());
            }

            public void columnMoved(TableColumnModelEvent e) {
                moveMenuItems(e.getFromIndex(), e.getToIndex());
            }

            public void columnRemoved(TableColumnModelEvent e) {
                removeMenuItemsForColumns(e.getFromIndex(), e.getFromIndex());
            }
        };
        table.getColumnModel().addColumnModelListener(columnModelListener);
        popupListener = new PopupUtils.ComponentPopupListener(this);
        table.getTableHeader().addMouseListener(popupListener);
        // listener to notify when table header is changed
        table.addPropertyChangeListener("tableHeader", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                ((JTableHeader)evt.getOldValue()).removeMouseListener(popupListener);
                ((JTableHeader)evt.getNewValue()).addMouseListener(popupListener);
            }
        });
    }

    /** Adds menu items for the columns with the index in the specified range.
     * @param fromIndex the starting column index
     * @param toIndex the end column index
     */
    private void addMenuItemsForColumns(int fromIndex, int toIndex) {
        // create the menu items from columns
        for (int i = fromIndex; i <= toIndex; i++) {
            add(new JColumnMenuItem(table.getColumnModel().getColumn(i)), i);
        }
    }

    /** Removes menu items for the columns with the index in the specified range.
     * @param fromIndex the starting column index
     * @param toIndex the end column index
     */
    private void removeMenuItemsForColumns(int fromIndex, int toIndex) {
        for (int i = toIndex; i >= fromIndex; i++) {
            ((JColumnMenuItem)getComponent(i)).uninstall();
            remove(i);
        }
    }

    /** Swith two menu items between.
     * @param from the starting menu component index
     * @param to the end menu component index
     */
    private void moveMenuItems(int from, int to) {
        if (from != to) {
            JMenuItem x = (JMenuItem)getComponent(from);
            JMenuItem y = (JMenuItem)getComponent(to);
            remove(from);
            remove(to);
            add(x, to);
            add(y, from);
        }
    }

    protected boolean isColumnHideable(int index) {
        if (table.getColumnModel() instanceof DisplayedColumnsModel) {
            return ((DisplayedColumnsModel)table.getColumnModel()).isColumnHideable(index);
        } else if (table.getModel() instanceof DisplayedColumnsModel) {
            return ((DisplayedColumnsModel)table.getModel()).isColumnHideable(index);
        }
        return true;
    }

    /** Uninstall the popup. It will not be shown on the table header anymore. */
    public void uninstall() {
        removeMenuItemsForColumns(0, getComponentCount() - 1);
        table.getColumnModel().removeColumnModelListener(columnModelListener);
        table.getTableHeader().removeMouseListener(popupListener);
    }

    /** A menu item corresponding to a table column. */
    private class JColumnMenuItem extends JCheckBoxMenuItem {

        /** The associated table column. */
        private TableColumn column;
        private boolean visible = true;

        /** Previous value before hiding for minWidth. */
        private int minWidth;
        /** Previous value before hiding for maxWidth. */
        private int maxWidth;
        /** Previous value before hiding for preferredWidth. */
        private int preferredWidth;
        /** Previous value before hiding for resizable flag. */
        private boolean resizable;
        /** The listener notified when a column name is changed. */
        private PropertyChangeListener columnListener;

        /** Constructor
         * @param column the corresponding table column
         */
        public JColumnMenuItem(TableColumn column) {
            this.column = column;
            updateTextFromColumnHeaderValue();
            setSelected(true);
            // action listener
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boolean visible = isSelected();
                    boolean columnHiddeable = isColumnHideable(JColumnMenuItem.this.column.getModelIndex());
                    if (visible || columnHiddeable)
                        setColumnVisible(visible);
                    setEnabled(columnHiddeable);
                }
            });
            // property listener for the column header value
            // the menu item text will be displayed according to the header value
            columnListener = new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    if (TableColumn.HEADER_VALUE_PROPERTY.equals(evt.getPropertyName())) {
                        updateTextFromColumnHeaderValue();
                    }
                }
            };
            column.addPropertyChangeListener(columnListener);

            setEnabled(isColumnHideable(column.getModelIndex()));
        }

        /** Sets the text menu item according to the column header value. */
        private void updateTextFromColumnHeaderValue() {
            Object value = JColumnMenuItem.this.column.getHeaderValue();
            String text = null;
            if (value != null) {
                text = value.toString();
            }
            if (text == null || text.length() <= 0) {
                text = "Column " + (table.convertColumnIndexToView(JColumnMenuItem.this.column.getModelIndex()) + 1);
            }

            // tooltip
            String tooltip = null;
            if (table.getColumnModel() instanceof ToolTipColumnModel) {
                int index = table.convertColumnIndexToView(JColumnMenuItem.this.column.getModelIndex());
                tooltip = ((ToolTipColumnModel)table.getColumnModel()).getColumnTooltip(index);
            } else if (table.getModel() instanceof ToolTipColumnModel) {
                int index = table.convertColumnIndexToView(JColumnMenuItem.this.column.getModelIndex());
                tooltip = ((ToolTipColumnModel)table.getModel()).getColumnTooltip(index);
            }
            if (tooltip != null) {
                text += " [" + tooltip + "]";
            }

            setText(text);
        }

        /** Sets a column visibility.
         * @param visible if true the column will become visible, otherwise hidden
         */
        private void setColumnVisible(boolean visible) {
            if (this.visible == visible)
                return;
            this.visible = visible;
            if (visible) {
                column.setMinWidth(minWidth);
                column.setMaxWidth(maxWidth);
                column.setPreferredWidth(preferredWidth);
                column.setResizable(resizable);
            } else {
                minWidth = column.getMinWidth();
                maxWidth = column.getMaxWidth();
                preferredWidth = column.getPreferredWidth();
                resizable = column.getResizable();
                column.setMinWidth(0);
                column.setMaxWidth(0);
                column.setPreferredWidth(0);
                column.setResizable(false);
            }
        }

        private boolean isColumnVisible() {
            return visible;
        }

        private void uninstall() {
            column.removePropertyChangeListener(columnListener);
        }
    }

    public void setColumnVisible(int index, boolean visible) {
        ((JColumnMenuItem)getComponent(index)).setColumnVisible(visible);
    }

    public boolean isColumnVisible(int index) {
        return ((JColumnMenuItem)getComponent(index)).isColumnVisible();
    }
}
