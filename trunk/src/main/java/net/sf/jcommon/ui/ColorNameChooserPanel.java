package net.sf.jcommon.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.colorchooser.*;

/**
 * This is a panel from where you can choose a color based on it's HTML name.
 * @author Adrian Ber
 * @see HTMLColor
 */
public class ColorNameChooserPanel extends AbstractColorChooserPanel {

    /** The name of this color chooser panel; will appear as the tab name
     *  in the color chooser
     */
    private static final String TITLE = "Names";
    /** The name displayed for a color without a HTML name */
    private static final String CUSTOM = "<Custom>";

    /** The combo box filled with the HTML color names */
    private JComboBox colorsComboBox = new JComboBox();

    /** Creates a new color chooser panel. */
    public ColorNameChooserPanel() {
        buildChooser();
    }

    public Icon getSmallDisplayIcon() {
        return null;
    }

    public void updateChooser() {
        if (parent != null) {
            String x = HTMLColor.valueOf(getColorFromModel()).name();
            if (x == null) x = CUSTOM;
            colorsComboBox.setSelectedItem(x);
        }
    }

    public String getDisplayName() {
        return TITLE;
    }

    public Icon getLargeDisplayIcon() {
        return null;
    }

    /** Initializes this color chooser components. */
    protected void buildChooser() {
        setLayout(new BorderLayout());
        colorsComboBox.addItem(CUSTOM);
        for (HTMLColor color : HTMLColor.values()) {
            colorsComboBox.addItem(color);
        }
        colorsComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if (parent == null) return;
                Object name = colorsComboBox.getSelectedItem();
                if ((name != null) && (!name.toString().equals(CUSTOM))) {
                    parent.setColor(HTMLColor.forName(name.toString()));
                }
            }
        });
        add(new JLabel("HTML Color:"), BorderLayout.WEST);
        add(colorsComboBox, BorderLayout.CENTER);
    }

    /** the color chooser in which is included this panel. */
    private JColorChooser parent = null;

    public void installChooserPanel(JColorChooser enclosingChooser) {
        parent = enclosingChooser;
        super.installChooserPanel(enclosingChooser);
    }

}