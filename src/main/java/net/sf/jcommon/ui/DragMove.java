package net.sf.jcommon.ui;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.*;

/**
 * A mouse listener that will enable a window/frame to be moved by simply dragging it.
 * To use it simply instatiate it with the frame/window as the constructor parameter
 * <blockquote><code>new DragMove(window).</code></blockquote>
 * This will install the appropriate listener and you will be able to move the window on the screen
 * by simply dragging it.
 */
public class DragMove implements MouseListener, MouseMotionListener {

    public static DragMove install(Component c) {
        DragMove x = new DragMove();
        c.addMouseListener(x);
        c.addMouseMotionListener(x);
        return x;
    }

    private Point start;

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        start = e.getPoint();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        Point p = e.getLocationOnScreen();
        Component c = e.getComponent();
        c.setLocation((int)(p.getX() - start.getX()), (int)(p.getY() - start.getY()));
        c.repaint();
    }

    public void mouseMoved(MouseEvent e) {
    }
}
