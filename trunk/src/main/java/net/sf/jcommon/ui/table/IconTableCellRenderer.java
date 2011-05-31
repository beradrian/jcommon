package net.sf.jcommon.ui.table;

import java.awt.Component;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Table cell renderer used to display an icon. The cell value must be an <code>{@link Icon}</code>
 */
public class IconTableCellRenderer extends DefaultTableCellRenderer {

    /**
     * Creates a new table cell renderer to render icons
     */
    public IconTableCellRenderer() {
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        this.setIcon((Icon) value);
        this.setText("");
        return this;
    }
}