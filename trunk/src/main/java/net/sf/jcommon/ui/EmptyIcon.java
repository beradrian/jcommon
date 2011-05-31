package net.sf.jcommon.ui;

import java.awt.*;
import javax.swing.*;

/**
 * An empty icon of user-defined sizes.
 * @author Adrian Ber
 * @version 1.0
 */
public class EmptyIcon implements Icon {
  /** the size of the icon */
  private int width, height;

  /** Creates a new empty icon of the given sizes
    * @param width the width of the new icon
    * @param height the height of the new icon
    */
  public EmptyIcon(int width, int height) {
    this.width = width;
    this.height = height;
  }

  /** Creates a new empty icon of 16x16 pixels size. */
  public EmptyIcon(){
    this(16,16);
  }

  /** Implements the {@link Icon} interface */
  public void paintIcon(Component c, Graphics g, int x, int y) {
  }

  /** Implements the {@link Icon} interface */
  public int getIconWidth() {
    return width;
  }

  /** Implements the {@link Icon} interface */
  public int getIconHeight() {
    return height;
  }
}

