package net.sf.jcommon.ui;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/**
  * Icon that combines togheter a set of icons.
  * @author Adrian Ber
  * @version 1.0
  */
public class ComposedIcon implements Icon {
    /** The icons that are composed in this icon. */
    private Icon[] icons;

    private int width = -1;
    private int height = -1;

    /** Creates a new composed icon of a set of icons.
     * @param icons the icons
     */
    public ComposedIcon(Icon... icons) {
        this.icons = icons;
    }

    public int getIconHeight(){
        if (height < 0) {
            for (Icon icon : icons) {
                if (icon.getIconHeight() > height)
                    height = icon.getIconHeight();
            }
        }
        return height;
    }

    public int getIconWidth(){
        if (width < 0) {
            for (Icon icon : icons) {
                if (icon.getIconHeight() > width)
                    width = icon.getIconWidth();
            }
        }
        return width;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        for (Icon icon : icons) {
            icon.paintIcon(c, g, x, y);
        }
    }

}