package net.sf.jcommon.ui;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.*;

/**
 * @author Ion BARCAN
 */
public class DynamicComposedIcon implements Icon {

    private static class Delta2D {
        private int x;
        private int y;

        public Delta2D(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    private static final Delta2D NO_DELTA = new Delta2D(0, 0);

    /** The icons that are composed in this icon. */
    private List<Icon> icons;
    private List<Delta2D> deltas;

    private int width;
    private int height;

    public DynamicComposedIcon() {
        icons = new ArrayList<Icon>();
        deltas = new ArrayList<Delta2D>();
    }

    private void resetSize() {
        width = -1;
        height = -1;
    }

    private int getDeltaX(int i) {
        return deltas.get(i).getX();
    }

    private int getDeltaY(int i) {
        return deltas.get(i).getY();
    }

    private Icon getIcon(int i) {
        return icons.get(i);
    }

    public DynamicComposedIcon(Icon... icons) {
        this.icons = Arrays.asList(icons);
        this.deltas = new ArrayList<Delta2D>(icons.length);
        for (int i = 0; i < this.icons.size(); i++) {
            deltas.add(NO_DELTA);
        }
        resetSize();
    }

    public DynamicComposedIcon(Icon[] icons, int[] deltaXs, int[] deltaYs) {
        this.icons = Arrays.asList(icons);
        this.deltas = new ArrayList<Delta2D>(icons.length);
        for (int i = 0; i < this.icons.size(); i++) {
            deltas.add(new Delta2D(deltaXs[i], deltaYs[i]));
        }
        resetSize();
    }

    public void addIcon(Icon icon) {
        icons.add(icon);
        deltas.add(NO_DELTA);
        resetSize();
    }

    public void addIcon(Icon icon, int deltaX, int deltaY) {
        icons.add(icon);
        deltas.add(new Delta2D(deltaX, deltaY));
        resetSize();
    }

    public int getIconHeight(){
        if (height < 0) {
            for (int i = 0; i < icons.size(); i++) {
                int h = getIcon(i).getIconHeight() + getDeltaY(i);
                if (h > height) {
                    height = h;
                }
            }
        }
        return height;
    }

    public int getIconWidth(){
        if (width < 0) {
            for (int i = 0; i < icons.size(); i++) {
                int w = getIcon(i).getIconWidth() + getDeltaX(i);
                if (w > width) {
                    width = w;
                }
            }
        }
        return width;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        for (int i = 0; i < icons.size(); i++) {
            getIcon(i).paintIcon(c, g, x + getDeltaX(i), y + getDeltaY(i));
        }
    }

}
