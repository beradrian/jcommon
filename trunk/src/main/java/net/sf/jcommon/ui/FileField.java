package net.sf.jcommon.ui;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ChangeEvent;
import java.util.Collection;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.io.File;

/** An UI component for choosing a file. */
@SuppressWarnings("serial")
public class FileField extends JPanel {
    private JTextField fileTextField;
    private JFileChooser fileChooser;
    private String dialogTitle;
    private int dialogType;
    private Collection<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
    private JButton browseButton;

    private JFileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setAcceptAllFileFilterUsed(true);
            fileChooser.setDialogTitle(dialogTitle);
            fileChooser.setDialogType(dialogType);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        }
        return fileChooser;
    }

    public FileField(String dialogTitle, int dialogType) {
        this.dialogTitle = dialogTitle;
        this.dialogType = dialogType;
        init();
    }

    private void init() {
        fileTextField = new JTextField(30);
        fileTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                fireChangeListeners();
            }

            public void removeUpdate(DocumentEvent e) {
                fireChangeListeners();
            }

            public void changedUpdate(DocumentEvent e) {
                fireChangeListeners();
            }
        });
        browseButton = new JButton("...");
        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browse();
            }
        });

        setLayout(new BorderLayout());
        add(fileTextField);
        add(browseButton, BorderLayout.EAST);
    }

    protected void fireChangeListeners() {
        ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener l : changeListeners) {
            l.stateChanged(event);
        }
    }

    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeListeners.remove(listener);
    }

    private void browse() {
        getFileChooser();
        File selectedFile = new File(fileTextField.getText());
        if (selectedFile.exists()) {
            fileChooser.setCurrentDirectory(selectedFile.getParentFile());
            fileChooser.setSelectedFile(selectedFile);
        }

        int retval;
        if (dialogType == JFileChooser.OPEN_DIALOG) {
            retval = fileChooser.showOpenDialog(fileTextField);
        } else {
            retval = fileChooser.showSaveDialog(fileTextField);
        }
        if (retval == JFileChooser.APPROVE_OPTION) {
            fileTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    public void requestFocus() {
        fileTextField.requestFocus();
    }

    public String getPath() {
        return fileTextField.getText();
    }

    public void setPath(String path) {
        fileTextField.setText(path);
    }

    public int getFileSelectionMode() {
        return getFileChooser().getFileSelectionMode();
    }

    public void setFileSelectionMode(int mode) {
        getFileChooser().setFileSelectionMode(mode);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        fileTextField.setEnabled(enabled);
        browseButton.setEnabled(enabled);
    }
}

