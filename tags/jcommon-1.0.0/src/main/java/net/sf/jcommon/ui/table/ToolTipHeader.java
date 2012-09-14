package net.sf.jcommon.ui.table;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.*;

/**
 * A table header that displays a tooltip for every column.
 * The column model must implement {@link ToolTipColumnModel}.
 * If the column model doesn't implement {@link ToolTipColumnModel} then the column name is used as a tooltip.
 * @author Adrian BER
 */
public class ToolTipHeader extends JTableHeader {

    public ToolTipHeader() {
    }

    public ToolTipHeader(TableColumnModel tcm) {
        super(tcm);
    }

    public String getToolTipText(MouseEvent e) {
        // Return the pixel position of the mouse cursor hotspot.
        Point p = e.getPoint();

        // Convert the pixel position to the zero-based column index of
        // the table header column over which the mouse cursor hotspot is
        // located. The result is a view-based column index.
        int viewColumnIndex = columnAtPoint(p);

        // Retrieve a reference to the JTable object associated with the
        // table header.
        JTable jTable = getTable();

        // Convert the view-based column index to a model-based column index.
        int modelColumnIndex = jTable.convertColumnIndexToModel(viewColumnIndex);

        String tooltip = null;
        TableColumnModel columnModel = jTable.getTableHeader().getColumnModel();
        if (columnModel instanceof ToolTipColumnModel) {
            tooltip = ((ToolTipColumnModel)columnModel).getColumnTooltip(modelColumnIndex);
        } else {
            TableModel tableModel = jTable.getModel();
            if (tableModel instanceof ToolTipColumnModel) {
                tooltip = ((ToolTipColumnModel)tableModel).getColumnTooltip(modelColumnIndex);
            } else {
                tooltip = jTable.getColumnName(modelColumnIndex);
            }
        }

        return tooltip;
    }

}