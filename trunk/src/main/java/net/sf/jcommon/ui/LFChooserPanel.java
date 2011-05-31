package net.sf.jcommon.ui;

import java.awt.*;
import javax.swing.*;

/**
 * A panel from where you can change the look&amp;feel.
 * For changing the look&amp;feel you must call
 * {@link #installSelectedLookAndFeel}. To see if the user has chosen a new
 * look&amp;feel different from the current one you can call
 * {@link #needsUpdate}.
 *
 * @author Adrian Ber &lt;beradrian@yahoo.com&gt;
 * @version 1.0
 */
public class LFChooserPanel extends JPanel {

    private JComboBox lfComboBox = new JComboBox();
    private UIManager.LookAndFeelInfo[] lfs;

    public LFChooserPanel() {
        lfs = UIManager.getInstalledLookAndFeels();
        String selLf = UIManager.getLookAndFeel().getName();
        for (int i = 0; i < lfs.length; i++) {
            lfComboBox.addItem(lfs[i].getName());
            if (selLf.equals(lfs[i].getName())) {
                lfComboBox.setSelectedIndex(i);
            }
        }

        setLayout(new GridBagLayout());
        JLabel lfLabel = new JLabel("Look&Feel");
        add(lfLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        add(lfComboBox, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    }

    /**
     * If the user has chosen a different look&amp;feel from the current one
     * this method returns true.
     * @return if the selected LnF has changed
     */
    public boolean needsUpdate() {
        return !UIManager.getLookAndFeel().getClass().getName()
                .equals(lfs[lfComboBox.getSelectedIndex()].getClassName());
    }

    /**
     * Sets as the system look and feel the look and feel chosen with this panel.
     * @param comp the component for which to set LnF
     */
    public void installSelectedLookAndFeel(Component comp) {
        try {
            UIManager.setLookAndFeel(lfs[lfComboBox.getSelectedIndex()].getClassName());
            SwingUtilities.updateComponentTreeUI(comp);
        }
        catch (ClassNotFoundException exc) {
        }
        catch (UnsupportedLookAndFeelException exc) {
        }
        catch (IllegalAccessException exc) {
        }
        catch (InstantiationException exc) {
        }
    }

}