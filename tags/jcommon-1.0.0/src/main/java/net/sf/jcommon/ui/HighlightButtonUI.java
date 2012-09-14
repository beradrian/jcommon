package net.sf.jcommon.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

/**
 * A button UI with three states:  normal, selected and highlighted.
 * The button is in the highlighted state when the mouse is over it.
 * Each state has a background and a border associated.
 */
public class HighlightButtonUI extends BasicButtonUI {

    private Color selectedBackground, hoverBackground, selectedBorder, hoverBorder;

    /** Constructor. */
    public HighlightButtonUI() {
    }

    /**
     * Constructor.
     * @param selectedBackground the background for the selected state of the button
     * @param selectedBorder the border for the selected state of the button
     * @param hoverBackground the background for the highlighted state of the button
     * @param hoverBorder the border for the highlighted state of the button
     */
    public HighlightButtonUI(Color selectedBackground, Color selectedBorder, Color hoverBackground, Color hoverBorder) {
        this.selectedBackground = selectedBackground;
        this.hoverBackground = hoverBackground;
        this.selectedBorder = selectedBorder;
        this.hoverBorder = hoverBorder;
    }

    public Color getSelectedBackground() {
        return selectedBackground;
    }

    public void setSelectedBackground(Color selectedBackground) {
        this.selectedBackground = selectedBackground;
    }

    public Color getHoverBackground() {
        return hoverBackground;
    }

    public void setHoverBackground(Color hoverBackground) {
        this.hoverBackground = hoverBackground;
    }

    public Color getSelectedBorder() {
        return selectedBorder;
    }

    public void setSelectedBorder(Color selectedBorder) {
        this.selectedBorder = selectedBorder;
    }

    public Color getHoverBorder() {
        return hoverBorder;
    }

    public void setHoverBorder(Color hoverBorder) {
        this.hoverBorder = hoverBorder;
    }

    public void installUI(JComponent c) {
        super.installUI(c);
        AbstractButton button = (AbstractButton)c;
        button.setRolloverEnabled(true);
        button.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
    }

    public void paint(Graphics g, JComponent c)
    {
        AbstractButton button = (AbstractButton)c;
        if(button.getModel().isRollover() || button.getModel().isArmed()
                || button.getModel().isSelected()) {
            Color oldColor = g.getColor();

            Color selBg, hoBg, selBorder, hoBorder;

            selBg = selectedBackground != null ? selectedBackground : button.getBackground().darker();
            selBorder = selectedBorder != null ? selectedBorder : selBg.darker().darker();
            hoBg = hoverBackground != null ? hoverBackground : button.getBackground().brighter();
            hoBorder = hoverBorder != null ? hoverBorder : hoBg.darker().darker();

            g.setColor(button.getModel().isSelected()
                ? selBg : hoBg);
            g.fillRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);

            g.setColor(button.getModel().isSelected() ? selBorder : hoBorder);
            g.drawRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);

            g.setColor(oldColor);
        }
        super.paint(g, c);
    }
}
