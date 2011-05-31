package net.sf.jcommon.ui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Adrian BER
 */
public class JSimpleScrollPane extends JComponent {

    public static final int ALWAYS = 1;
    public static final int AS_NEEDED = 2;
    public static final int NEVER = 3;

    private int horizontalButtonPolicy;
    private int verticalButtonPolicy;
    private int unitScrollAmount = 5;
    private int blockScrollAmount = 20;
    /** The delay in milliseconds to determine the scrolling speed when pressing the direction buttons.
     * Bigger values means less speed.
     */
    private int scrollSpeedThrottle = 60;
    /** A factor to determine the wheel mouse scrolling speed. */
    private int wheelScrollSpeed = 10;
    private JViewport viewport;
    private JButton upButton, downButton, leftButton, rightButton;

    public JSimpleScrollPane(JComponent view) {
        this(view, AS_NEEDED, AS_NEEDED);
    }

    public JSimpleScrollPane(JComponent view, int horizontalButtonPolicy, int verticalButtonPolicy) {
        this.horizontalButtonPolicy = horizontalButtonPolicy;
        this.verticalButtonPolicy = verticalButtonPolicy;
        init();
        setViewport(new JViewport());
        getViewport().setView(view);
    }

    private class ScrollListener implements ActionListener {
        private int direction = 0;
        private int amount = unitScrollAmount;

        public void actionPerformed(ActionEvent e) {
            scroll(direction, amount);
        }
    };

    private ScrollListener scrollListener = new ScrollListener();
    private Timer scrollTimer = new Timer(scrollSpeedThrottle, scrollListener);

    private JButton createScrollButton(final int direction) {
        JButton scrollButton = new ArrowButton(direction, 1, 10);
        scrollButton.setFocusPainted(false);
        scrollButton.setFocusable(false);
        scrollButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        scrollButton.setBackground(UIManager.getColor("ScrollBar.background"));
        scrollButton.setForeground(UIManager.getColor("ScrollBar.thumb").darker());
        scrollButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scroll(direction, blockScrollAmount);
            }
        });
        scrollButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                scrollListener.direction = direction;
                scrollTimer.start();
            }

            public void mouseReleased(MouseEvent e) {
                scrollTimer.stop();
            }
        });
        return scrollButton;
    }

    private void init() {
        setLayout(new BorderLayout());
        upButton = createScrollButton(SwingConstants.NORTH);
        downButton = createScrollButton(SwingConstants.SOUTH);
        leftButton = createScrollButton(SwingConstants.WEST);
        rightButton = createScrollButton(SwingConstants.EAST);
        add(upButton, BorderLayout.NORTH);
        add(downButton, BorderLayout.SOUTH);
        add(leftButton, BorderLayout.WEST);
        add(rightButton, BorderLayout.EAST);
    }

    private void scroll(int direction, int amount) {
        int x = (int)viewport.getViewPosition().getX();
        int y = (int)viewport.getViewPosition().getY();
        switch(direction) {
            case SwingConstants.NORTH:
                y-=amount;
                break;
            case SwingConstants.SOUTH:
                y+=amount;
                break;
            case SwingConstants.WEST:
                x-=amount;
                break;
            case SwingConstants.EAST:
                x+=amount;
                break;
        }
        if (x < 0) x = 0;
        if (x > (int)(viewport.getViewSize().getWidth() - viewport.getExtentSize().getWidth()))
            x = (int)(viewport.getViewSize().getWidth() - viewport.getExtentSize().getWidth());
        if (y < 0) y = 0;
        if (y > (int)(viewport.getViewSize().getHeight() - viewport.getExtentSize().getHeight()))
            y = (int)(viewport.getViewSize().getHeight() - viewport.getExtentSize().getHeight());
        viewport.setViewPosition(new Point(x, y));
    }

    public int getHorizontalButtonPolicy() {
        return horizontalButtonPolicy;
    }

    public void setHorizontalButtonPolicy(int horizontalButtonPolicy) {
        this.horizontalButtonPolicy = horizontalButtonPolicy;
        leftButton.setVisible(horizontalButtonPolicy == ALWAYS);
        rightButton.setVisible(horizontalButtonPolicy == ALWAYS);
        updateButtonsVisibility();
    }

    public int getVerticalButtonPolicy() {
        return verticalButtonPolicy;
    }

    public void setVerticalButtonPolicy(int verticalButtonPolicy) {
        this.verticalButtonPolicy = verticalButtonPolicy;
        upButton.setVisible(verticalButtonPolicy == ALWAYS);
        downButton.setVisible(verticalButtonPolicy == ALWAYS);
        updateButtonsVisibility();
    }

    public JViewport getViewport() {
        return viewport;
    }

    public void setViewport(JViewport viewport) {
        if (this.viewport != null)
            this.viewport.removeChangeListener(viewportChangeListener);
        if (this.viewport != null)
            this.viewport.removeMouseWheelListener(viewportMouseWheelListener);
        this.viewport = viewport;
        add(viewport, BorderLayout.CENTER);
        viewport.addChangeListener(viewportChangeListener);
        viewport.addMouseWheelListener(viewportMouseWheelListener);
        updateButtonsVisibility();
    }

    private void updateButtonsVisibility() {
        boolean verticalButtonsVisible = viewport.getExtentSize().getHeight() < viewport.getViewSize().getHeight()
                && verticalButtonPolicy != NEVER;
        upButton.setVisible(verticalButtonsVisible);
        downButton.setVisible(verticalButtonsVisible);
        boolean horizontalButtonsVisible = viewport.getExtentSize().getWidth() < viewport.getViewSize().getWidth()
                && horizontalButtonPolicy != NEVER;
        leftButton.setVisible(horizontalButtonsVisible);
        rightButton.setVisible(horizontalButtonsVisible);
    }

    private ViewportChangeListener viewportChangeListener = new ViewportChangeListener();

    private class ViewportChangeListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            updateButtonsVisibility();
        }
    }

    private ViewportMouseWheelListener viewportMouseWheelListener = new ViewportMouseWheelListener();

    private class ViewportMouseWheelListener implements MouseWheelListener {
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getScrollType() == MouseWheelEvent.WHEEL_BLOCK_SCROLL) {
                scroll(e.getScrollAmount() < 0 ? SwingConstants.NORTH : SwingConstants.SOUTH,
                        Math.abs(e.getScrollAmount()) * wheelScrollSpeed);
            }
            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                scroll(e.getUnitsToScroll() < 0 ? SwingConstants.NORTH : SwingConstants.SOUTH,
                        Math.abs(e.getUnitsToScroll()) * wheelScrollSpeed);
            }
        }
    }

}
