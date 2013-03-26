package net.sf.jcommon.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

@SuppressWarnings("serial")
public class TransparentWindow extends JWindow implements MouseMotionListener, FocusListener {
    Graphics tempImgGraphics;
    Image backgroundImage, temporaryImage;
    Point mousePointer;

    public TransparentWindow() {
        init();
        setVisible(true);
    }

    private void init() {
        addMouseMotionListener(this);
        capture();
        addFocusListener(this);
    }

    public void focusGained(FocusEvent aFocusEvent) {
        Point aPoint = getLocation();
        setLocation(15000, 0);
        capture();
        setLocation(aPoint);
    }

    public void focusLost(FocusEvent aFocusEvent) {
    }

    public void capture() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        try {
            Robot aRobot = new Robot();
            Rectangle rect = new Rectangle(0, 0, dim.width, dim.height);
            backgroundImage = aRobot.createScreenCapture(rect);
        } catch (AWTException awte) {
            System.out.println("robot excepton occurred");
        }
    }

    public void mouseDragged(MouseEvent aMouseEvent) {
        Point aPoint = aMouseEvent.getPoint();
        int x = getX() + aPoint.x - mousePointer.x;
        int y = getY() + aPoint.y - mousePointer.y;
        setLocation(x, y);
        Graphics graphics = getGraphics();
        paint(graphics);
    }

    public void mouseMoved(MouseEvent aMouseEvent) {
        mousePointer = aMouseEvent.getPoint();
    }

    public void paint(Graphics graphics) {
        graphics.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(),
                getX(), getY(), getX() + getWidth(), getY() + getHeight(), null);
        super.paintComponents(graphics);
    }

}