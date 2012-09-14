package net.sf.jcommon.ui;

import com.jhlabs.awt.BasicGridLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.ButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.beans.PropertyVetoException;

/**
 * @author Adrian BER
 */
public class JGroupedPane extends JPanel {
    public static final int NORTH_PLACEMENT = 0;
    public static final int EAST_PLACEMENT = 1;
    public static final int SOUTH_PLACEMENT = 2;
    public static final int WEST_PLACEMENT = 3;

    private int groupPlacement = -1;
    private JPanel menuPanel;
    private Component lastMenuComponent;
    private Component firstMenuComponent;
    private JSimpleScrollPane menuScrollBar;
    private JPanel menuBar;
    private ButtonGroup buttonGroup;

    /**
     * The panel containing the pages.
     */
    private JPanel pagesPanel;

    /**
     * The pages model.
     */
    private List pages;

    /**
     * The list of items in the menu.
     */
    private List buttons;

    /**
     * The default selection model
     */
    protected SingleSelectionModel model;

    /**
     * The <code>changeListener</code> is the listener we add to the
     * model.
     */
    protected ChangeListener changeListener = null;

    /**
     * Only one <code>ChangeEvent</code> is needed per <code>TabPane</code>
     * instance since the
     * event's only (read-only) state is the source property.  The source
     * of events generated here is always "this".
     */
    protected transient ChangeEvent changeEvent = null;

    private int horizontalTextPosition = SwingConstants.CENTER;
    private int verticalTextPosition = SwingConstants.BOTTOM;
    private int horizontalAlignment = SwingConstants.CENTER;
    private int verticalAlignment = SwingConstants.CENTER;

    public JGroupedPane() {
        this(WEST_PLACEMENT);
    }

    public JGroupedPane(int groupPlacement) {
        init();
        setGroupPlacement(groupPlacement);
    }

    private void init() {
        setLayout(new BorderLayout());

        menuBar = new JPanel(new BasicGridLayout(0, 1, 0, 0));
        menuBar.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        menuBar.setBackground(buttonsBackground);

        menuScrollBar = new JSimpleScrollPane(menuBar);
        menuScrollBar.setBorder(null);

        menuPanel = new JPanel(new BorderLayout());
        menuPanel.add(menuScrollBar, BorderLayout.CENTER);

        buttonGroup = new ButtonGroup();
        setGroupPlacement(WEST_PLACEMENT);
        buttons = new ArrayList();

        pagesPanel = new JPanel(new BorderLayout());
        pages = new ArrayList();

        add(pagesPanel, BorderLayout.CENTER);

        setModel(new DefaultSingleSelectionModel());
    }

    public int getGroupPlacement() {
        return groupPlacement;
    }

    public void setGroupPlacement(int groupPlacement) {
        if (this.groupPlacement == groupPlacement)
            return;
        int oldValue = this.groupPlacement;
        this.groupPlacement = groupPlacement;
        BasicGridLayout buttonsLayout = (BasicGridLayout) menuBar.getLayout();
        buttonsLayout.setRows(1);
        buttonsLayout.setColumns(1);
        final int index = menuBar.getComponentCount();

        switch (groupPlacement) {
            case NORTH_PLACEMENT:
                add(menuPanel, BorderLayout.NORTH);
                buttonsLayout.setRows(index);
                if (firstMenuComponent != null)
                    menuPanel.add(firstMenuComponent, BorderLayout.WEST);
                if (lastMenuComponent != null)
                    menuPanel.add(lastMenuComponent, BorderLayout.EAST);
                break;
            case EAST_PLACEMENT:
                add(menuPanel, BorderLayout.EAST);
                buttonsLayout.setColumns(index);
                if (firstMenuComponent != null)
                    menuPanel.add(firstMenuComponent, BorderLayout.NORTH);
                if (lastMenuComponent != null)
                    menuPanel.add(lastMenuComponent, BorderLayout.SOUTH);
                break;
            case SOUTH_PLACEMENT:
                add(menuPanel, BorderLayout.SOUTH);
                buttonsLayout.setRows(index);
                if (firstMenuComponent != null)
                    menuPanel.add(firstMenuComponent, BorderLayout.WEST);
                if (lastMenuComponent != null)
                    menuPanel.add(lastMenuComponent, BorderLayout.EAST);
                break;
            case WEST_PLACEMENT:
                add(menuPanel, BorderLayout.WEST);
                buttonsLayout.setColumns(index);
                if (firstMenuComponent != null)
                    menuPanel.add(firstMenuComponent, BorderLayout.NORTH);
                if (lastMenuComponent != null)
                    menuPanel.add(lastMenuComponent, BorderLayout.SOUTH);
        }
        firePropertyChange("groupPlacement", oldValue, groupPlacement);
    }

    public Component getLastMenuComponent() {
        return lastMenuComponent;
    }

    public void setLastMenuComponent(Component lastMenuComponent) {
        if (this.lastMenuComponent != null)
            menuPanel.remove(this.lastMenuComponent);
        this.lastMenuComponent = lastMenuComponent;
        if (this.lastMenuComponent != null)
            menuPanel.add(lastMenuComponent,
                    groupPlacement == NORTH_PLACEMENT || groupPlacement == SOUTH_PLACEMENT
                            ? BorderLayout.EAST : BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    public Component getFirstMenuComponent() {
        return firstMenuComponent;
    }

    public void setFirstMenuComponent(Component firstMenuComponent) {
        if (this.firstMenuComponent != null)
            menuPanel.remove(this.firstMenuComponent);
        this.firstMenuComponent = firstMenuComponent;
        if (this.firstMenuComponent != null)
            menuPanel.add(lastMenuComponent,
                    groupPlacement == NORTH_PLACEMENT || groupPlacement == SOUTH_PLACEMENT
                            ? BorderLayout.WEST : BorderLayout.NORTH);
        revalidate();
        repaint();
    }

    public int getHorizontalTextPosition() {
        return horizontalTextPosition;
    }

    public void setHorizontalTextPosition(int horizontalTextPosition) {
        int oldValue = this.horizontalTextPosition;
        if (horizontalTextPosition == oldValue)
            return;
        this.horizontalTextPosition = horizontalTextPosition;
        for (int i = 0, n = menuBar.getComponentCount(); i < n; i++) {
            Component c = menuBar.getComponent(i);
            if (c instanceof JLabel) {
                ((JLabel) c).setHorizontalTextPosition(horizontalTextPosition);
            } else if (c instanceof AbstractButton) {
                ((AbstractButton) c).setHorizontalTextPosition(horizontalTextPosition);
            }
        }
        firePropertyChange("horizontalTextPosition", oldValue, horizontalTextPosition);
    }

    public int getVerticalTextPosition() {
        return verticalTextPosition;
    }

    public void setVerticalTextPosition(int verticalTextPosition) {
        int oldValue = this.verticalTextPosition;
        if (verticalTextPosition == oldValue)
            return;
        this.verticalTextPosition = verticalTextPosition;
        for (int i = 0, n = menuBar.getComponentCount(); i < n; i++) {
            Component c = menuBar.getComponent(i);
            if (c instanceof JLabel) {
                ((JLabel) c).setVerticalTextPosition(verticalTextPosition);
            } else if (c instanceof AbstractButton) {
                ((AbstractButton) c).setVerticalTextPosition(verticalTextPosition);
            }
        }
        firePropertyChange("verticalTextPosition", oldValue, verticalTextPosition);
    }

    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(int horizontalAlignment) {
        int oldValue = this.horizontalAlignment;
        if (horizontalAlignment == oldValue)
            return;
        this.horizontalAlignment = horizontalAlignment;
        for (int i = 0, n = menuBar.getComponentCount(); i < n; i++) {
            Component c = menuBar.getComponent(i);
            if (c instanceof JLabel) {
                ((JLabel) c).setVerticalTextPosition(horizontalAlignment);
            } else if (c instanceof AbstractButton) {
                ((AbstractButton) c).setVerticalTextPosition(horizontalAlignment);
            }
        }
        firePropertyChange("horizontalAlignment", oldValue, horizontalAlignment);
    }

    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(int verticalAlignment) {
        int oldValue = this.verticalAlignment;
        if (verticalAlignment == oldValue)
            return;
        this.verticalAlignment = verticalAlignment;
        for (int i = 0, n = menuBar.getComponentCount(); i < n; i++) {
            Component c = menuBar.getComponent(i);
            if (c instanceof JLabel) {
                ((JLabel) c).setVerticalTextPosition(verticalAlignment);
            } else if (c instanceof AbstractButton) {
                ((AbstractButton) c).setVerticalTextPosition(verticalAlignment);
            }
        }
        firePropertyChange("verticalAlignment", oldValue, verticalAlignment);
    }

    private Component selectedComponent;

    private void showPage(int index) {
        Container contentPane = getContentPane();
        if (selectedComponent != null)
            contentPane.remove(selectedComponent);
        selectedComponent = getPageAt(index);
        if (selectedComponent != null) {
            contentPane.add(selectedComponent, BorderLayout.CENTER);
            selectedComponent.requestFocus();
        }
        contentPane.invalidate();
        contentPane.validate();
        contentPane.repaint();
    }

    private class TabButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int index = 0;
            for (Iterator iterator = buttons.iterator(); iterator.hasNext();) {
                JToggleButton button = (JToggleButton) iterator.next();
                if (button == e.getSource()) {
                    setSelectedIndex(index);
                    return;
                }
                index++;
            }
        }
    }

    private TabButtonActionListener tabButtonActionListener = new TabButtonActionListener();

    public void addSeparator(String text, Icon icon) {
        JLabel sep = new JLabel(text, icon, horizontalAlignment);
        sep.setVerticalAlignment(verticalAlignment);
        sep.setVerticalTextPosition(verticalTextPosition);
        sep.setHorizontalTextPosition(horizontalTextPosition);
        Border separatorBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(sep.getForeground().brighter()),
                BorderFactory.createEmptyBorder(3, 3, 3, 3));
        sep.setBorder(separatorBorder);
        sep.setBackground(menuBar.getBackground().brighter());
        sep.setOpaque(true);
        addMenuComponent(sep);
        buttons.add(null);
        pages.add(null);
    }

    public void addTab(String text, Icon icon, Component component) {
        final JToggleButton b = new JToggleButton(text, icon);
        b.setHorizontalAlignment(horizontalAlignment);
        b.setVerticalAlignment(verticalAlignment);
        b.setHorizontalTextPosition(horizontalTextPosition);
        b.setVerticalTextPosition(verticalTextPosition);
        b.setContentAreaFilled(false);
        b.setFocusable(isFocusable());
        b.setUI(buttonUI);

        b.addActionListener(tabButtonActionListener);
        buttonGroup.add(b);
        buttons.add(b);

        addMenuComponent(b);

        pages.add(component);

        if (getSelectedIndex() < 0) {
            setSelectedIndex(pages.indexOf(component));
        }
    }

    protected void addMenuComponent(Component c) {
        int index = menuBar.getComponentCount();
        ((BasicGridLayout) menuBar.getLayout())
                .setRows(groupPlacement == NORTH_PLACEMENT
                || groupPlacement == SOUTH_PLACEMENT ? 1 : index + 1);
        ((BasicGridLayout) menuBar.getLayout())
                .setColumns(groupPlacement == NORTH_PLACEMENT
                || groupPlacement == SOUTH_PLACEMENT ? index + 1 : 1);
        menuBar.add(c, index);
    }

    public int getPageCount() {
        return pages.size();
    }

    public void setPageAt(int index, Component component) {
        pages.set(index, component);
    }

    public Component getPageAt(int index) {
        return (Component) pages.get(index);
    }

    protected Container getContentPane() {
        return pagesPanel;
    }

    private void checkIndex(int index) {
        if (index < -1 || index >= pages.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Tab count: " + menuBar.getComponentCount());
        }
    }

    public int getSelectedIndex() {
        return model.getSelectedIndex();
    }

    public void setSelectedIndex(int selectedIndex) {
        int oldValue = model.getSelectedIndex();
        if (selectedIndex == oldValue)
            return;
        try {
            fireVetoableChange("selectedIndex", new Integer(oldValue), new Integer(selectedIndex));
            model.setSelectedIndex(selectedIndex);
            firePropertyChange("selectedIndex", new Integer(oldValue), new Integer(selectedIndex));
        } catch (PropertyVetoException e) {
            // reverse the button selection to the previously selected if veto
            ((JToggleButton) buttons.get(oldValue)).setSelected(true);
        }
    }

    private static ButtonUI buttonUI = new HighlightButtonUI(
            UIManager.getColor("Tree.selectionBackground"),
            UIManager.getColor("Tree.selectionBackground").darker().darker(),
            UIManager.getColor("Tree.selectionBackground").brighter(),
            UIManager.getColor("Tree.selectionBackground").darker());
    private static Color buttonsBackground = new JFrame().getBackground().darker();

    /**
     * We pass <code>ModelChanged</code> events along to the listeners with
     * the tabbedpane (instead of the model itself) as the event source.
     */
    protected class ModelListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            int selectedIndex = getSelectedIndex();
            checkIndex(selectedIndex);
            showPage(selectedIndex);
            JToggleButton button = ((JToggleButton) buttons.get(selectedIndex));
            if (!button.isSelected()) {
                button.setSelected(true);
            }
            fireStateChanged();
        }
    }

    /**
     * Subclasses that want to handle <code>ChangeEvents</code> differently
     * can override this to return a subclass of <code>ModelListener</code> or
     * another <code>ChangeListener</code> implementation.
     *
     * @see #fireStateChanged
     */
    protected ChangeListener createChangeListener() {
        return new ModelListener();
    }

    /**
     * Returns the model associated with this tabbedpane.
     *
     * @see #setModel
     */
    public SingleSelectionModel getModel() {
        return model;
    }

    /**
     * Sets the model to be used with this groupedpane.
     *
     * @param model the model to be used
     * @see #getModel
     */
    public void setModel(SingleSelectionModel model) {
        SingleSelectionModel oldModel = getModel();

        if (oldModel != null) {
            oldModel.removeChangeListener(changeListener);
            changeListener = null;
        }

        this.model = model;

        if (model != null) {
            changeListener = createChangeListener();
            model.addChangeListener(changeListener);
        }

        firePropertyChange("model", oldModel, model);
        repaint();
    }

    /**
     * Adds a <code>ChangeListener</code> to this tabbedpane.
     *
     * @param l the <code>ChangeListener</code> to add
     * @see #fireStateChanged
     * @see #removeChangeListener
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /**
     * Removes a <code>ChangeListener</code> from this tabbedpane.
     *
     * @param l the <code>ChangeListener</code> to remove
     * @see #fireStateChanged
     * @see #addChangeListener
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    /**
     * Returns an array of all the <code>ChangeListener</code>s added
     * to this <code>JTabbedPane</code> with <code>addChangeListener</code>.
     *
     * @return all of the <code>ChangeListener</code>s added or an empty
     *         array if no listeners have been added
     * @since 1.4
     */
    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) listenerList.getListeners(
                ChangeListener.class);
    }

    /**
     * Sends a <code>ChangeEvent</code>, whose source is this tabbedpane,
     * to each listener.  This method method is called each time
     * a <code>ChangeEvent</code> is received from the model.
     *
     * @see #addChangeListener
     * @see javax.swing.event.EventListenerList
     */
    protected void fireStateChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
        for (Enumeration e = buttonGroup.getElements(); e.hasMoreElements();) {
            ((Container) e.nextElement()).setFocusable(focusable);
        }
    }

}
