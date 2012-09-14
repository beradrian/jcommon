/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package net.sf.jcommon.ui.table;

import net.sf.jcommon.ui.UIUtils;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

/**
 * A search component for the table rows.
 */
public class TableSearch extends JPanel {
    /** The default search up icon. */
    private static final Icon DEFAULT_UPARROW_ICON;

    /** The default search down icon. */
    private static final Icon DEFAULT_DOWNARROW_ICON;


    static {
        URL url;
        url = TableSearch.class.getResource("searchup.gif");
        DEFAULT_UPARROW_ICON = (url != null ? new ImageIcon(url) : null);
        url = TableSearch.class.getResource("searchdown.gif");
        DEFAULT_DOWNARROW_ICON = (url != null ? new ImageIcon(url) : null);
    }

    /**
     * Constant for search direction.
     */
    public static final int UP = -1;
    /**
     * Constant for search direction.
     */
    public static final int DOWN = 1;

    /**
     * the associated table.
     */
    private JTable table;

    private JComboBox columnsComboBox;

    private JPanel editorPanel;
    private TableCellEditor editor;
    private JButton downSearchButton;
    private JButton upSearchButton;

    /**
     * Constructor.
     *
     * @param table the table on which the search will be run
     */
    public TableSearch(JTable table) {
        this.table = table;
        init();
    }

    /**
     * Initialiaser.
     */
    private void init() {
        columnsComboBox = new JComboBox(new ColumnComboBoxModel(table.getColumnModel()));
        columnsComboBox.setFocusable(false);
        columnsComboBox.setEditable(false);
        columnsComboBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        columnsComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeEditor(true);
            }
        });
        editorPanel = new JPanel(new BorderLayout());

        downSearchButton = new JButton();
        downSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search(DOWN);
            }
        });

        upSearchButton = new JButton();
        upSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search(UP);
            }
        });

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 3, 0));
        buttonsPanel.add(downSearchButton);
        buttonsPanel.add(upSearchButton);

        setLayout(new BorderLayout(3, 0));
        add(columnsComboBox, BorderLayout.WEST);
        add(editorPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.EAST);

        if (columnsComboBox.getItemCount() > 0)
            columnsComboBox.setSelectedIndex(0);

        table.getModel().addTableModelListener(new TableModelListener() {
            private boolean empty = table.getModel().getRowCount() <= 0;

            public void tableChanged(TableModelEvent e) {
                boolean x = table.getModel().getRowCount() <= 0;
                if (empty != x) {
                    empty = x;
                    changeEditor(false);
                }
            }
        });

        updateUI();
    }

    private void changeEditor(boolean focus) {
        int selectedIndex = columnsComboBox.getSelectedIndex();
        if (selectedIndex < 0 || table.getRowCount() <= 0) {
            editor = null;
            editorPanel.removeAll();
            editorPanel.repaint();
        } else {
            selectedIndex = ((ColumnComboBoxModel)columnsComboBox.getModel())
                    .indexToColumnModelIndex(selectedIndex);
            editor = table.getColumnModel().getColumn(selectedIndex).getCellEditor();
            if (editor == null)
                editor = table.getDefaultEditor(table.getColumnClass(selectedIndex));
            Component c = editor.getTableCellEditorComponent(
                    table, null, true, 0, selectedIndex);
            editorPanel.add(c);
            editorPanel.repaint();
            if (focus)
                // put the focus on the component
                c.requestFocus();
        }
    }

    public int search() {
        return search(DOWN);
    }

    public int search(int direction) {
        if (editor != null) {
            editor.stopCellEditing();
            return search(columnsComboBox.getSelectedIndex(), editor.getCellEditorValue(),
                    table.getSelectedRow() + direction, direction);
        }
        return -1;
    }

    public int search(int column, Object value, int from, int direction) {
        // test direction for non-valid values
        if (direction != UP && direction != DOWN) {
            throw new IllegalArgumentException("Direction can be only UP or DOWN");
        }

        int begin = direction == DOWN ? 0 : table.getRowCount() - 1;

        // if from is an invalid value start search from the beggining
        if (from < 0) {
            from = begin;
        }

        // end of search
        int to = direction == DOWN ? table.getRowCount() : 0;

        // index of the found item
        int index = -1;
        for (int i = from; direction == DOWN ? i < to : i >= to; i += direction) {
            Object x = table.getValueAt(i, column);
            if ((x == null && value == null)
                    || (x != null && x.equals(value))) {
                index = i;
                break;
            }
        }

        // if not found and search wasn't started from the beggining restart it from the beggining
        if (index < 0 && from != begin) {
            index = search(column, value, begin, direction);
        }

        // if found select the item
        if (index >= 0) {
            table.setRowSelectionInterval(index, index);
        }

        // return the found index
        return index;
    }

    private class ColumnComboBoxModel extends AbstractListModel implements ComboBoxModel {
        private TableColumnModel tableColumnModel;

        private List<TableColumn> columns = new ArrayList<TableColumn>();

        private Object selectedItem;

        public ColumnComboBoxModel(TableColumnModel tableColumnModel) {
            this.tableColumnModel = tableColumnModel;
            tableColumnModel.addColumnModelListener(new TableColumnModelListener() {
                public void columnMarginChanged(ChangeEvent e) {
                }

                public void columnSelectionChanged(ListSelectionEvent e) {
                }

                public void columnAdded(TableColumnModelEvent e) {
                    update();
                }

                public void columnMoved(TableColumnModelEvent e) {
                    update();
                }

                public void columnRemoved(TableColumnModelEvent e) {
                    update();
                }
            });
            update();
        }

        protected boolean isColumnSearchable(int index) {
            if (table.getColumnModel() instanceof SearchableColumnsModel) {
                return ((SearchableColumnsModel) table.getColumnModel()).isColumnSearchable(index);
            } else if (table.getModel() instanceof SearchableColumnsModel) {
                return ((SearchableColumnsModel) table.getModel()).isColumnSearchable(index);
            }
            return true;
        }

        private void update() {
            columns.clear();
            for(int i = 0, n = tableColumnModel.getColumnCount(); i < n; i++) {
                TableColumn column = tableColumnModel.getColumn(i);
                if (isColumnSearchable(i))
                    columns.add(column);
            }
            fireContentsChanged(ColumnComboBoxModel.this, 0, columns.size());
        }

        public int getSize() {
            return columns.size();
        }

        public int indexToColumnModelIndex(int index) {
            return columns.get(index).getModelIndex();
        }

        public Object getElementAt(int index) {
            return columns.get(index).getHeaderValue();
        }

        public Object getSelectedItem() {
            return selectedItem;
        }

        public void setSelectedItem(Object selectedItem) {
            this.selectedItem = selectedItem;
        }
    }

    public void updateUI() {
        super.updateUI();
        if (downSearchButton != null) {
            downSearchButton.setIcon(UIUtils.getUIIcon("TableSearch.searchDownIcon", DEFAULT_DOWNARROW_ICON));
            downSearchButton.setText(UIUtils.getUIString("TableSearch.searchDownText", ""));
            downSearchButton.setToolTipText(UIUtils.getUIString("TableSearch.searchDownToolTipText", "Search Down"));
            upSearchButton.setIcon(UIUtils.getUIIcon("TableSearch.searchUpIcon", DEFAULT_UPARROW_ICON));
            upSearchButton.setText(UIUtils.getUIString("TableSearch.searchUpText", ""));
            upSearchButton.setToolTipText(UIUtils.getUIString("TableSearch.searchUpToolTipText", "Search Up"));
        }
    }

}
