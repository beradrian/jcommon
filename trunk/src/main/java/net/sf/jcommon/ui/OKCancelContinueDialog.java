package net.sf.jcommon.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Extends functionality of OKCancelDialog with a 'SaveAndContinue' command.
 */
@SuppressWarnings("serial")
public class OKCancelContinueDialog extends OKCancelDialog {

    private boolean canContinue;

    private JButton okAndContinueButton;

    public OKCancelContinueDialog(Frame owner, String title) {
        super(owner, title);
        init();
    }

    public OKCancelContinueDialog(Dialog owner, String title) {
        super(owner, title);
        init();
    }

    private void init() {
        okAndContinueButton = new JButton(UIUtils
                .getUIString("OKCancelContinueDialog.continueButtonText", "OK & Continue"));
        okAndContinueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                okAndContinue();
            }
        });

        buttonsPanel.add(Box.createHorizontalStrut(PADDING));
        buttonsPanel.add(okAndContinueButton);
    }

    protected void okAndContinue() {
        canContinue = true;
        super.ok();
    }

    public JButton getOkAndContinueButton() {
        return okAndContinueButton;
    }

    public void setVisible(boolean visible) {
        if (visible)
            canContinue = false;
        super.setVisible(visible);
    }

    public boolean canContinue() {
        return canContinue;
    }

}
