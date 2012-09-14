package net.sf.jcommon.ui.table;

/**
 * An interface that has to be implemented by a column model for the ability to hide columns
 * {@link net.sf.jcommon.ui.table.DisplayedColumnsMenu}.
 * If the column model doesn't implement this then all the columns are considered hideable.
 */
public interface DisplayedColumnsModel {

    /**
     * @param index the column index from the model, not the visual one
     * @return true if the column with the given index can be hidden
     */
    boolean isColumnHideable(int index);
}
