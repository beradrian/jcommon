package net.sf.jcommon.ui.table;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;

/**
 * A class for customizing a table renderer. It works in association with
 * {@link StyledTableModel} and it acts as a decorator for another renderer.
 * @see javax.swing.JTable
 */
public class StyledRenderer implements TableCellRenderer {

    /**
     * The decorated cell renderer
     */
    private TableCellRenderer renderer;

    /**
     * Creates a table cell renderer with an order icon that wrapps a
     * DefaultTableCellRenderer.
     */
    public StyledRenderer() {
        this(new DefaultTableCellRenderer());
    }

    /**
     * Creates a table cell renderer with an order icon.
     * 
     * @param renderer the renderer to be wrapped
     */
    public StyledRenderer(TableCellRenderer renderer) {
        this.renderer = renderer;
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
        // the component returned by the decorated renderer
        Component c = renderer.getTableCellRendererComponent(table, value, isSelected,
            hasFocus, row, column);

        if (!isSelected) {
            TableModel model = table.getModel();
            if (model instanceof StyledTableModel) {
                Color bgColor = ((StyledTableModel)model).getBackgroundColorAt(row, column);
                if (bgColor != null) {
                    c.setBackground(bgColor);
                }
                Color fgColor = ((StyledTableModel)model).getForegroundColorAt(row, column);
                if (fgColor != null) {
                    c.setForeground(fgColor);
                }
                Font font = ((StyledTableModel)model).getFontAt(row, column, c.getFont());
                if (font != null) {
                    c.setFont(font);
                }
            }
        }

        return c;
    }

}


