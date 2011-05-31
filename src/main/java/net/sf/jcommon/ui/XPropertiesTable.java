package net.sf.jcommon.ui;

import net.sf.jcommon.util.XProperties;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.Map;
import java.util.HashMap;

/**
 */
@SuppressWarnings("serial")
public class XPropertiesTable extends JTable {

    public static class XPropertiesTableModel extends AbstractTableModel {
        private static final int NAME_COLUMN = 0;
        private static final int VALUE_COLUMN = 1;

        private XProperties properties;

        public XPropertiesTableModel() {
            this(new XProperties());
        }

        public XPropertiesTableModel(XProperties properties) {
            this.properties = properties;
        }

        public XProperties getProperties() {
            return properties;
        }

        public void setProperties(XProperties properties) {
            this.properties = properties;
            fireTableDataChanged();
        }

        private String getPropertyAt(int index) {
            return properties.keySet().toArray()[index].toString();
        }

        public int getRowCount() {
            return properties.size();
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case NAME_COLUMN:
                    return getPropertyAt(rowIndex);
                case VALUE_COLUMN:
                    return properties.getProperty(getPropertyAt(rowIndex));
                default:
                    return null;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == VALUE_COLUMN;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case NAME_COLUMN:
                    return "Name";
                case VALUE_COLUMN:
                    return "Value";
                default:
                    return "";
            }
        }
    }

    private Map<String, TableCellRenderer> renderers = new HashMap<String, TableCellRenderer>(); 
    private Map<String, TableCellEditor> editors = new HashMap<String, TableCellEditor>();

    public XPropertiesTable() {
        super(new XPropertiesTableModel());
    }

    public void setPropertyRenderer(String propertyName, TableCellRenderer renderer) {
        renderers.put(propertyName, renderer);
    }

    public void setPropertyEditor(String propertyName, TableCellEditor editor) {
        editors.put(propertyName, editor);
    }

    public XPropertiesTableModel getPropertiesTableModel() {
        return (XPropertiesTableModel)getModel();
    }

    @Override
    public void setModel(TableModel dataModel) {
        if (!(dataModel instanceof XPropertiesTableModel))
            throw new IllegalArgumentException("Only XPropertiesTableModel is supported");
        super.setModel(dataModel);
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        TableCellRenderer renderer = null;
        if (column == XPropertiesTableModel.VALUE_COLUMN)
            renderer = renderers.get(getValueAt(row, XPropertiesTableModel.NAME_COLUMN).toString());
        return renderer != null ? renderer : super.getCellRenderer(row, column);
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        TableCellEditor editor = null;
        if (column == XPropertiesTableModel.VALUE_COLUMN)
            editor = editors.get(getValueAt(row, XPropertiesTableModel.NAME_COLUMN).toString());
        return editor != null ? editor : super.getCellEditor(row, column);
    }

}
