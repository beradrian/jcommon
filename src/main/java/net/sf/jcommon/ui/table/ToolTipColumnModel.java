package net.sf.jcommon.ui.table;

/** An interface to be implemented by a column model to render tooltips for every column.
 * The table must have a {@link ToolTipHeader}.
 */
public interface ToolTipColumnModel {

    /**
     * @param index the column model index
     * @return the tooltip of the column
     */
    String getColumnTooltip(int index);
}
