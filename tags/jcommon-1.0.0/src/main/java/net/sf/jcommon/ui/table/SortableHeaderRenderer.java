//package net.sf.jcommon.ui.table;
//
//import net.sf.jcommon.ui.UIUtils;
//
//import javax.swing.*;
//import javax.swing.table.*;
//
//import java.awt.event.*;
//import java.awt.BorderLayout;
//import java.awt.Component;
//
//import java.net.URL;
//import java.util.List;
//
///**
// * The class for rendering (displaying) columns header after which are ordered
// * the rows in a <code>JTable</code>. The columns header will contain
// * a little icon if the rows are ordered after that column.
// * <p>After creating such a renderer must be installed on a <code>JTable</code>
// * with the {@@link #install(JTable)} method.
// * This header renderer is a decorator for another header renderer.
// * @@author Adrian Ber
// * @@see JTable
// * @@see #install
// */
//public class SortableHeaderRenderer implements TableCellRenderer {
//
//    /** The default ascendent icon. */
//    private static final Icon DEFAULT_ASCENDENT_ICON;
//
//    /** The default descendent icon. */
//    private static final Icon DEFAULT_DESCENDENT_ICON;
//
//
//    static {
//        URL url;
//        url = SortableHeaderRenderer.class.getResource("img/ascendent.png");
//        DEFAULT_ASCENDENT_ICON = (url != null ? new ImageIcon(url) : null);
//        url = SortableHeaderRenderer.class.getResource("img/descendent.png");
//        DEFAULT_DESCENDENT_ICON = (url != null ? new ImageIcon(url) : null);
//    }
//
//    /**
//     * the icon displayed if the rows are ordered ascendent
//     */
//    private Icon ascendentIcon = DEFAULT_ASCENDENT_ICON;
//
//    /**
//     * the icon displayed if the rows are ordered descendent
//     */
//    private Icon descendentIcon = DEFAULT_DESCENDENT_ICON;
//
//    /**
//     * the wrapped cell renderer
//     */
//    private TableCellRenderer renderer;
//
//    /**
//     * the panel
//     */
//    private JPanel contentPane;
//
//    /**
//     * the order label
//     */
//    private JLabel sortingLabel;
//
//    /**
//     * Creates a table cell renderer with an order icon that wrapps a
//     * DefaultTableCellRenderer.
//     */
//    public SortableHeaderRenderer() {
//        this(new DefaultTableCellRenderer());
//    }
//
//    /**
//     * Creates a table cell renderer with an order icon.
//     *
//     * @@param renderer the renderer to be wrapped
//     */
//    public SortableHeaderRenderer(TableCellRenderer renderer) {
//        this.renderer = renderer;
//        sortingLabel = new JLabel();
//        sortingLabel.setHorizontalAlignment(JLabel.CENTER);
//        contentPane = new JPanel();
//        contentPane.setLayout(new BorderLayout());
//        contentPane.add(sortingLabel, BorderLayout.WEST);
//    }
//
//    /**
//     * Sets the icon displayed if the rows are ordered acendent.
//     */
//    public void setAscendentIcon(Icon ico) {
//        ascendentIcon = ico;
//    }
//
//    /**
//     * Returns the icon displayed if the rows are ordered acendent.
//     */
//    public Icon getAscendentIcon() {
//        return ascendentIcon;
//    }
//
//    /**
//     * Sets the icon displayed if the rows are ordered descendent
//     */
//    public void setDescendentIcon(Icon ico) {
//        descendentIcon = ico;
//    }
//
//    /**
//     * Returns the icon displayed if the rows are ordered decendent.
//     */
//    public Icon getDescendentIcon() {
//        return descendentIcon;
//    }
//
//    /**
//     * Returns the table cell renderer.
//     *
//     * @@param table      the <code>JTable</code>
//     * @@param value      the value to assign to the cell at <code>[row, column]</code>
//     * @@param isSelected true if cell is selected
//     * @@param hasFocus   true if cell has focus
//     * @@param row        the row of the cell to render
//     * @@param column     the column of the cell to render
//     * @@return the default table cell renderer
//     */
//    public Component getTableCellRendererComponent(JTable table, Object value,
//                                                   boolean isSelected, boolean hasFocus, int row, int column) {
//        // the component returned by the wrapped renderer
//        Component c = renderer.getTableCellRendererComponent(table, value, isSelected,
//            hasFocus, row, column);
//        if (c instanceof JComponent) {
//            ((JComponent) c).setOpaque(false);
//        }
//        contentPane.setBorder(BorderFactory.createEtchedBorder());
//
//        RowSorter.SortKey sortKey;
//        // the ascendent/descendent icon for the column after which the table rows are sorted
//        if (table.getRowSorter() != null && table.getRowSorter().getSortKeys().size() > 0
//                 && (sortKey = table.getRowSorter().getSortKeys().get(0)).getColumn() == column) {
//            sortingLabel.setIcon(sortKey.getSortOrder() == SortOrder.ASCENDING
//                    ? ascendentIcon : descendentIcon);
//        } else {
//            sortingLabel.setIcon(null);
//            sortingLabel.setToolTipText(null);
//        }
//
//        // add the component returned by the wrapped renderer to this renderer
//        contentPane.add(c, BorderLayout.CENTER);
//
//        return contentPane;
//    }
//
//
//    /**
//     * Installs this renderer and the mouse events needed to enable user selection
//     * of the column after which are ordered the table rows.
//     */
//    public void install(final JTable table) {
//
//        UIUtils.installHeaderRenderer(table, SortableHeaderRenderer.this);
//
//        // add a mouse listener for the header
//        table.getTableHeader().addMouseListener(new SortableHeaderMouseListener());
//    }
//
//    /**
//     * Mouse listener to be used by the ordered header renderer.
//     */
//    private static class SortableHeaderMouseListener extends MouseAdapter {
//
//        public void mouseClicked(MouseEvent ev) {
//            JTable table;
//
//            // get the source table
//            table = ((JTableHeader) ev.getSource()).getTable();
//
//            // if the table has a sorter
//            if (table.getRowSorter() != null) {
//                // if left mouse click
//                if (SwingUtilities.isLeftMouseButton(ev)) {
//
//                    // get the column on which we clicked
//                    int col = table.getTableHeader().columnAtPoint(ev.getPoint());
//
//                    boolean mustRepaint = true;
//
//                    List<? extends RowSorter.SortKey> sortKeys = table.getRowSorter().getSortKeys();
//                    for (RowSorter.SortKey sortKey : sortKeys) {
//                        if ( )
//                    }
//                    // if we clicked on the column which is already ordered reverse the order
//                    // otherwise set the new column as the ordering column ascendently
//                    if (sortableModel.getSortingColumn() == col) {
//                        sortableModel.setSortingOrder(SortableTableModel.ASCENDING
//                                + SortableTableModel.DESCENDING
//                                - sortableModel.getSortingOrder());
//                    } else {
//                        if (sortableModel.isColumnSortable(col))
//                            mustRepaint = sortableModel.setSortingColumn(col);
//                    }
//
//                    if (mustRepaint) {
//                        // repaint the header
//                        table.getTableHeader().repaint();
//                        table.repaint();
//                    }
//                }
//
//            } else {
//                throw new IllegalArgumentException("SortableHeaderMouseListener: " +
//                    "Table should have a RowSorter");
//            }
//        }
//    }
//
//}
//
