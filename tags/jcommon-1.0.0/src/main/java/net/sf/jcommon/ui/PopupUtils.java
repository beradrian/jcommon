package net.sf.jcommon.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;

/**
 * Miscellanous functions for popup menus.
 * @author Adrian Ber
 */
public class PopupUtils {

    /** Use only static functions of this class, so the constructor is private */
    private PopupUtils() {
    }

    /** Initializes a popup menu from an XML element and a set of actions
     * @param popup the menu to be initialized
     * @param actions the set of actions
     * @param elemConfig the XML element used for configuration
     */
//    public static void init(JPopupMenu popup, Actions actions, Element elemConfig) {
//        //check arguments
//        if ((popup == null) || (actions == null) || (elemConfig == null)) {
//            return;
//        }
//
//        popup.removeAll();
//
//        for (Iterator it = elemConfig.getChildren().iterator(); it.hasNext();) {
//            Element elem = ((Element) it.next());
//
//            if (elem.getName().equals("separator")) {
//                popup.addSeparator();
//            } else if (elem.getName().equals("item")) {
//                String actionName = elem.getAttribute("action").getValue();
//                if (actionName != null) {
//                    Action action = actions.getAction(actionName);
//                    if (action != null) {
//                        popup.add(action);
//                    }
//                }
//            }
//
//        }
//    }

    /** Install a popup menu for a component.
     * @param popup the popup menu
     * @param component the component for which we associate the popup menu
     */
    public static void install(JPopupMenu popup, Component component) {
        if (component instanceof JTable)
            install(popup, (JTable)component);
        else if (component instanceof JTree)
            install(popup, (JTree)component);
        else
            component.addMouseListener(new ComponentPopupListener(popup));
    }


    /** Install a popup menu for a table.
     * @param popup the popup menu
     * @param table the table for which we associate the popup menu
     */
    public static void install(JPopupMenu popup, JTable table) {
        table.addMouseListener(new TablePopupMouseListener(popup, table));
    }


    /** Install a popup menu for a tree.
     * @param popup the popup menu
     * @param tree the tree for which we associate the popup menu
     */
    public static void install(JPopupMenu popup, JTree tree) {
        tree.addMouseListener(new TreePopupMouseListener(popup, tree));
    }

    /**
     * A mouse listener for showing a popup for a component.
     * @author Adrian BER
     */
    public static class ComponentPopupListener extends MouseAdapter {
        private JPopupMenu popup;

        public ComponentPopupListener(JPopupMenu popup) {
            this.popup = popup;
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    /** A mouse listener used to associate a popup menu with a table */
    private static class TablePopupMouseListener extends MouseAdapter {
        private JPopupMenu popup;
        private JTable table;

        public TablePopupMouseListener(JPopupMenu ppopup, JTable ptable) {
            popup = ppopup;
            table = ptable;
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent ev) {
            if (!SwingUtilities.isRightMouseButton(ev) && !ev.isPopupTrigger()) {
                return;
            }

            int prow = table.rowAtPoint(ev.getPoint());
            if (!((prow < 0) || (prow >= table.getRowCount()))) {
                if (!table.isRowSelected(prow) && table.getRowSelectionAllowed()) {
                    table.setRowSelectionInterval(prow, prow);
                }
            }
            else {
                return;
            }

            int pcol = table.columnAtPoint(ev.getPoint());
            if (!((pcol < 0) || (pcol >= table.getColumnCount()))) {
                if (!table.isColumnSelected(pcol) && table.getColumnSelectionAllowed()) {
                    table.setColumnSelectionInterval(pcol, pcol);
                }
            }
            else {
                return;
            }

            int x = ev.getX();
            int y = ev.getY();
            popup.pack();
            // Establish the position where the right-click menu is
            // going to be displayed
            Point pt = table.getLocationOnScreen();
            Dimension screenDimension = java.awt.Toolkit
                    .getDefaultToolkit().getScreenSize();
            Dimension dim = popup.getPreferredSize();
            if ((x + pt.getX() + dim.getWidth()) >
                    screenDimension.getWidth())
                x = x - (int) dim.getWidth();
            if ((y + pt.getY() + 2 * dim.getHeight()) >
                    screenDimension.getHeight())
                y = y - (int) dim.getHeight();

            //display the menu
            popup.show(table, x, y);

        }
    }

    /** A mouse listener used to associate a popup menu with a tree */
    private static class TreePopupMouseListener extends MouseAdapter {
        private JPopupMenu popup;
        private JTree tree;

        public TreePopupMouseListener(JPopupMenu popup, JTree tree) {
            this.popup = popup;
            this.tree = tree;
        }

        public void mouseReleased(MouseEvent ev) {
            if (!SwingUtilities.isRightMouseButton(ev) && !ev.isPopupTrigger())
                return;
            int x = ev.getX();
            int y = ev.getY();

            JPopupMenu nodePopup = popup;
            if (nodePopup == null)
                return;

            TreePath path = tree.getPathForLocation(ev.getX(), ev.getY());
            if (path != null) {
                TreePath[] selPaths = tree.getSelectionPaths();
                boolean sel = false;
                if (selPaths != null) {
                    for (int i = 0; i < selPaths.length; i++) {
                        if (path.equals(selPaths[i])) {
                            sel = true;
                            break;
                        }
                    }
                }
                if (!sel)
                    tree.setSelectionPath(path);
            }
//            if (path.getLastPathComponent() instanceof TreeNodeWithPopup) {
//                TreeNodeWithPopup node = (TreeNodeWithPopup) path.getLastPathComponent();
//                nodePopup = node.getPopup();
//            }

            // Establish the position where the right-click menu is
            // going to be displayed
            Point pt = tree.getLocationOnScreen();
            Dimension screenDimension = java.awt.Toolkit
                    .getDefaultToolkit().getScreenSize();
            Dimension dim = popup.getPreferredSize();
            if ((x + pt.getX() + dim.getWidth()) >
                    screenDimension.getWidth())
                x = x - (int) dim.getWidth();
            if ((y + pt.getY() + 2 * dim.getHeight()) >
                    screenDimension.getHeight())
                y = y - (int) dim.getHeight();

            nodePopup.pack();
            //display the menu
            nodePopup.show(tree, x, y);

        }
    }
}