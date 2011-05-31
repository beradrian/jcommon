package net.sf.jcommon.ui;

import java.awt.*;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import javax.swing.*;

/**
 * An icon to clone a screen portion under a given Component.
 *
 * @author Adrian Ber
 */
public class ScreenShotIcon implements Icon {

    private float filterFactor = -0.5f;
    private BufferedImage sshot;

    public ScreenShotIcon(Component cloneComponent) {
        Window w = (Window)SwingUtilities.getAncestorOfClass(Window.class, cloneComponent);
        w.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                capture(e.getComponent());
                e.getComponent().repaint();
            }

            public void mouseMoved(MouseEvent e) {
            }
        });
    }

    private void capture(Component c) {
        try {
            Robot robot = new Robot();
            Point p = c.getLocationOnScreen();
            Window w = (Window)SwingUtilities.getAncestorOfClass(Window.class, c);
            Point location = w.getLocation();
            w.setLocation(15000, 0);
            sshot = robot.createScreenCapture(new Rectangle(p.x , p.y,
                    c.getWidth(), c.getHeight()));
            w.setLocation(location);
            RescaleOp op = new RescaleOp(1 + filterFactor, 0, null);
            sshot = op.filter(sshot, null);

        } catch (AWTException exc) {
        }
    }

    /**
     * Implements the {@link javax.swing.Icon} interface
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (sshot == null)
            capture(c);
        g.drawImage(sshot.getSubimage(x, y, sshot.getWidth() - x, sshot.getHeight() - y), x, y, c);
    }

    /**
     * Implements the {@link javax.swing.Icon} interface
     */
    public int getIconWidth() {
        return sshot.getWidth();
    }

    /**
     * Implements the {@link javax.swing.Icon} interface
     */
    public int getIconHeight() {
        return sshot.getHeight();
    }
}