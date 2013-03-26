package net.sf.jcommon.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A class to provide two buttons (<b>OK</b> and <b>Cancel</b> on a dialog.
 * The buttons must be put on the dialog by the class that extends this class.
 */
@SuppressWarnings("serial")
public class OKCancelDialog extends JDialog {
    public static final int PADDING = 3;

    /**
     * the approve and cancel buttons
     */
    protected JButton okButton
    ,
    cancelButton;

    /**
     * The panel containing the buttons.
     */
    protected JPanel buttonsPanel;

    /**
     * If false the dialog was canceled
     */
    private boolean canceled = true;

    /**
     * Stats if the dialog is newly started. When a new dialog is shown the data and selection in
     * the dialog is reseted.
     */
    private boolean isNew = true;

    /**
     * Escape listener
     */
    private KeyListener escapeKL = new KeyAdapter() {
        public void keyPressed(KeyEvent ev) {
            if (ev.getKeyCode() == KeyEvent.VK_ESCAPE) {
                cancel();
            }
            if (ev.getKeyCode() == KeyEvent.VK_ENTER) {
//                Object source = ev.getSource();
//               if ((source != OKCancelDialog.this)
//                    && (source != OKCancelDialog.this.getRootPane())
//                    && (!(source instanceof Component)
//                        || (((Component)source).getParent() != OKCancelDialog.this.getContentPane())) )
                ok();
            }
        }
    };

    /**
     * Container listener to add/remove to a new component and all his
     * subcomponents this listener and the escape listener.
     */
    private ContainerListener escapeCL = new ContainerListener() {
        public void componentAdded(ContainerEvent e) {
            addEscapeKL(e.getChild());
        }

        public void componentRemoved(ContainerEvent e) {
            removeEscapeKL(e.getChild());
        }
    };


    /**
     * Adds to a new component and all his subcomponents (if any)
     * a listener to invoke this method on newly added components
     * and the escape listener.
     */
    private void addEscapeKL(Component c) {
        c.addKeyListener(escapeKL);
        if (c instanceof Container) {
            Container x = (Container) c;
            x.addContainerListener(escapeCL);
            int n = x.getComponentCount();
            for (int i = 0; i < n; i++) {
                addEscapeKL(x.getComponent(i));
            }
        }
    }

    /**
     * Removes from a component and all his subcomponents (if any)
     * the listener to invoke this method on newly added components
     * and the escape listener.
     */
    private void removeEscapeKL(Component c) {
        c.removeKeyListener(escapeKL);
        if (c instanceof Container) {
            Container x = (Container) c;
            x.removeContainerListener(escapeCL);
            int n = x.getComponentCount();
            for (int i = 0; i < n; i++) {
                removeEscapeKL(x.getComponent(i));
            }
        }
    }

    /**
     * Creates a new approve dialog
     */
    public OKCancelDialog() {
        init();
    }

    /**
     * Creates a new approve dialog
     */
    public OKCancelDialog(Frame owner) {
        super(owner);
        init();
    }

    /**
     * Creates a new approve dialog
     */
    public OKCancelDialog(Dialog owner) {
        super(owner);
        init();
    }

    /**
     * Creates a new approve dialog
     */
    public OKCancelDialog(Frame owner, String title) {
        super(owner, title);
        init();
    }

    /**
     * Creates a new approve dialog
     */
    public OKCancelDialog(Dialog owner, String title) {
        super(owner, title);
        init();
    }

    /**
     * Initializes the dialog
     */
    private void init() {
        // the OK button
        okButton = new JButton(UIUtils.getUIString("OKCancelDialog.okButtonText", "OK"));
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                ok();
            }
        });
//        getRootPane().setDefaultButton(okButton);
        // the cancel button
        cancelButton = new JButton(UIUtils.getUIString("OKCancelDialog.cancelButtonText", "Cancel"));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                cancel();
            }
        });
        // the buttons panel
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(Box.createGlue());
        buttonsPanel.add(okButton);
        buttonsPanel.add(Box.createHorizontalStrut(PADDING));
        buttonsPanel.add(cancelButton);


        setModal(true);

        JPanel newContent = new JPanel();
        newContent.setLayout(new BorderLayout(PADDING, PADDING));
        newContent.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING,
                PADDING, PADDING));
        newContent.add(buttonsPanel, BorderLayout.SOUTH);
        setContentPane(newContent);

        this.getContentPane().addKeyListener(escapeKL);
        this.getContentPane().addContainerListener(escapeCL);
        this.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                if (isNew) {
                    resetFocus();
                    isNew = false;
                }
            }
        });
    }

    /**
     * Returns true if the dialog was canceled
     */
    public boolean isCanceled() {
        return canceled;
    }

    public JButton getOKButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    protected void cancel() {
        canceled = true;
        setVisible(false);
    }

    protected void ok() {
        canceled = false;
        setVisible(false);
    }

    public void setVisible(boolean b) {
        if (b) {
            canceled = true;
            setLocationRelativeTo(getParent());
            super.setVisible(true);
        } else {
            if (canceled || validateUserInput()) {
                isNew = true;
                super.setVisible(false);
            } else {
                canceled = true;
            }
        }
    }

    /**
     * Validates the user input for this dialog. This must be overwritten in the subclass.
     *
     * @return if the user input is valid
     */
    protected boolean validateUserInput() {
        return true;
    }

    /**
     * Resets the dialog. This is used to prepare the dialog for the next time when it will be shown.
     */
    protected void resetFocus() {
    }
}