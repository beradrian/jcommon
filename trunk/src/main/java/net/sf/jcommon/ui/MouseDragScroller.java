package net.sf.jcommon.ui;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.*;

/**
 * @author Adrian BER
 */
public class MouseDragScroller implements MouseListener, MouseMotionListener {
    /** Scroll mode in which the component is moved and the viewport is fixed. */
    public static final int MOVE_COMPONENT = 1;
    /** Scroll mode in which the component is fixed and the viewport is moved over it. */
    public static final int MOVE_VIEWPORT = -1;

    /** The scroll type.
     * @see #MOVE_COMPONENT
     * @see #MOVE_VIEWPORT
     */
    private int scrollMode = MOVE_COMPONENT;

    /** The tolerance value. If the movement delta is smaller than this nothing happens. */
    private double tolerance = 0;

    /** The cursor used while scrolling. If null the cursor remains unchanged. */
    private Cursor scrollCursor;

    /** The previous point. The delta movement is made between this point and the dragging event point. */
    private Point previous;

    /** The component cursor used when not scrolling. */
    private Cursor normalCursor;

    public MouseDragScroller() {
        this(MOVE_COMPONENT);
    }

    /**
     * @param scrollMode the scroll mode
     * @see #MOVE_COMPONENT
     * @see #MOVE_VIEWPORT
     */
    public MouseDragScroller(int scrollMode) {
        this(scrollMode, Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    /**
     * @param scrollCursor the cursor used while scrolling the component
     * @param scrollMode the scroll mode
     * @see #MOVE_COMPONENT
     * @see #MOVE_VIEWPORT
     */
    public MouseDragScroller(int scrollMode, Cursor scrollCursor) {
        if (scrollMode != MOVE_COMPONENT && scrollMode != MOVE_VIEWPORT) {
            throw new IllegalArgumentException("The scroll mode can be only MOVE_COMPONENT or MOVE_VIEWPORT");
        }
        this.scrollMode = scrollMode;
        this.scrollCursor = scrollCursor;
    }

    public void mouseDragged(MouseEvent e) {
        moveTo(e.getSource(), e.getPoint());
    }

    private void moveTo(Object source, Point newPoint) {
        if (source instanceof Component
                && ((((Component)source).getParent()) instanceof JViewport)) {
            Component c = (Component) source;
            JViewport viewport = (JViewport)c.getParent();

            // calculate the delta movement
            double diffx = newPoint.getX() - previous.getX();
            double diffy = newPoint.getY() - previous.getY();
            // if smaller than tolerance ignore
            if (Math.abs(diffx) <= tolerance && Math.abs(diffy) <= tolerance)
                return;
            // calculate the new view point position
            Point viewPoint = viewport.getViewPosition();
            double x = viewPoint.getX() - scrollMode * diffx;
            double y = viewPoint.getY() - scrollMode * diffy;
            // check boundaries
            if (x < 0)
                x = 0;
            if (y < 0)
                y = 0;
            double maxx = viewport.getViewSize().getWidth()
                    - viewport.getExtentSize().getWidth();
            double maxy = viewport.getViewSize().getHeight()
                    - viewport.getExtentSize().getHeight();
            if (x > maxx)
                x = maxx;
            if (y > maxy)
                y = maxy;

            // change the view point position
            Point newViewPoint = new Point((int) x, (int) y);

            if (!viewPoint.equals(newViewPoint)) {
                viewport.setViewPosition(newViewPoint);
            }

            if (scrollMode == MOVE_VIEWPORT)
                previous = newPoint;
        }
    }

    public void mouseMoved(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        previous = e.getPoint();
        if (scrollCursor != null && e.getSource() instanceof Component) {
            normalCursor = ((Component)e.getSource()).getCursor();
            ((Component)e.getSource()).setCursor(scrollCursor);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (normalCursor != null && e.getSource() instanceof Component)
            ((Component)e.getSource()).setCursor(normalCursor);
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}
}
