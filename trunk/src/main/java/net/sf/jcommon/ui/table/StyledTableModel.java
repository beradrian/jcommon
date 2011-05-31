package net.sf.jcommon.ui.table;

import java.awt.*;

/**
 * A model for customizing the appearence of a table.
 */
public interface StyledTableModel {

    /** Returns the background color for the cell at (rowIndex,columnIndex).
     * @param rowIndex the row index
     * @param columnIndex the column index
     * @return the background color, or null if not specified.
     */
    public Color getBackgroundColorAt(int rowIndex, int columnIndex);

    /** Returns the foreground color for the cell at (rowIndex,columnIndex).
     * @param rowIndex the row index
     * @param columnIndex the column index
     * @return the foreground color, or null if not specified.
     */
    public Color getForegroundColorAt(int rowIndex, int columnIndex);

    /** Returns the font for the cell at (rowIndex,columnIndex).
     * @param rowIndex the row index
     * @param columnIndex the column index
     * @param font the default font
     * @return the font, or null if not specified.
     */
    public Font getFontAt(int rowIndex, int columnIndex, Font font);

}
