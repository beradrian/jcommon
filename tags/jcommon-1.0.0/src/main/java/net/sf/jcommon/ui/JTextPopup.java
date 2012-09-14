package net.sf.jcommon.ui;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * A popup menu for a text component.
 * @author Adrian BER (adrian.ber@greefsoftware.com)
 */
public class JTextPopup extends JPopupMenu {

    /** The text component associated with this popup. */
    private JTextComponent c;

    private JMenuItem cut;
    private JMenuItem copy;
    private JMenuItem paste;
    private JMenuItem delete;
    private JMenuItem selectAll;

    public JTextPopup() {
        init();
    }

    private void init() {
        cut = new JMenuItem(UIUtils.getUIString("TextPopup.cutText", "Cut"));
        cut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.cut();
            }
        });
        copy = new JMenuItem(UIUtils.getUIString("TextPopup.copyText", "Copy"));
        copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.copy();
            }
        });
        paste = new JMenuItem(UIUtils.getUIString("TextPopup.pasteText", "Paste"));
        paste.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.paste();
            }
        });
        delete = new JMenuItem(UIUtils.getUIString("TextPopup.deleteText", "Delete"));
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    c.getDocument().remove(c.getSelectionStart(), c.getSelectionEnd() - c.getSelectionStart());
                } catch (BadLocationException exc) {}
            }
        });
        selectAll = new JMenuItem(UIUtils.getUIString("TextPopup.selectAllText", "Select All"));
        selectAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.selectAll();
            }
        });

        add(cut);
        add(copy);
        add(paste);
        add(delete);
        addSeparator();
        add(selectAll);
    }

    public void show(Component invoker, int x, int y) {
        if (invoker instanceof JTextComponent) {
            c = ((JTextComponent)invoker);
            boolean enabled = c.isEnabled();
            boolean editable = c.isEditable();
            boolean hasSelection = c.getSelectedText() != null;
            cut.setEnabled(enabled && hasSelection && editable);
            copy.setEnabled(enabled && hasSelection);
            paste.setEnabled(enabled && editable);
            delete.setEnabled(enabled && hasSelection && editable);
            paste.setEnabled(enabled && editable);
            selectAll.setEnabled(enabled);
            super.show(invoker, x, y);
        }
    }

    public void updateUI() {
        super.updateUI();
        if (cut != null) {
            cut.setText(UIUtils.getUIString("TextPopup.cutText", "Cut"));
            copy.setText(UIUtils.getUIString("TextPopup.copyText", "Copy"));
            paste.setText(UIUtils.getUIString("TextPopup.pasteText", "Paste"));
            delete.setText(UIUtils.getUIString("TextPopup.deleteText", "Delete"));
            selectAll.setText(UIUtils.getUIString("TextPopup.selectAllText", "Select All"));
        }
    }

}
