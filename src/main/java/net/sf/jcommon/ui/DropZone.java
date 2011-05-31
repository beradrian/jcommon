package net.sf.jcommon.ui;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Collection;
import java.io.*;

/**
 * A very simple window on which you can drop files and you can process them.
 * The window can be dragged all over the screen and by default it sits on top.
 * You have to extend this class and define the method {@link #dropedFile(java.io.File)}.
 */
@SuppressWarnings("serial")
public abstract class DropZone extends JWindow implements DropTargetListener {

    private JLabel label;
    private JPopupMenu popup;

    public DropZone(String text, Icon icon) {
        init();
        if (text != null)
            label.setText(text);
        if (icon != null)
            label.setIcon(icon);
    }

    public DropZone(String text) {
        this(text, null);
    }

    public DropZone(Icon icon) {
        this(null, icon);
    }

    public DropZone() {
        this(null, null);
    }

    private void init() {
        label = new JLabel();
        label.setOpaque(false);
        label.setBackground(new Color(0, 200, 0, 100));
        label.setForeground(new Color(255, 255, 255, 100));
        label.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        getContentPane().add(label);
        setAlwaysOnTop(true);
        setDropTarget(new DropTarget(label, this));
        DragMove.install(this);
        addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3 && popup != null) {
                    popup.setInvoker(DropZone.this);
                    popup.setLocation(e.getLocationOnScreen());
                    popup.setVisible(true);
                }
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
    }

    public void dragEnter(DropTargetDragEvent dtde) {
    }

    public void dragOver(DropTargetDragEvent dtde) {
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    public void dragExit(DropTargetEvent dte) {
    }

    public void drop(DropTargetDropEvent evt) {
        Object droped = null;
        evt.acceptDrop(evt.getDropAction());
        List<DataFlavor> data = evt.getCurrentDataFlavorsAsList();
        for (DataFlavor d : data) {
            if (evt.isDataFlavorSupported(d)) {
                try {
                    droped = evt.getTransferable().getTransferData(d);
                    droped(droped);
                } catch (UnsupportedFlavorException exc) {
                } catch (IOException exc) {
                }
            }
        }
    }

    public Icon getIcon() {
        return label.getIcon();
    }

    public void setIcon(Icon icon) {
        label.setIcon(icon);
    }

    public String getText() {
        return label.getText();
    }

    public void setText(String text) {
        label.setText(text);
    }

    public JPopupMenu getPopup() {
        return popup;
    }

    public void setPopup(JPopupMenu popup) {
        this.popup = popup;
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            pack();
            setLocationRelativeTo(null);
        }
        super.setVisible(visible);
    }

    protected void droped(Object droped) {
        if (droped instanceof Collection<?>) {
            for (Object x : ((Collection<?>)droped)) {
                droped(x);
            }
        } else if (droped instanceof File) {
            dropedFile((File)droped);
        } else {
            dropedOther(droped);
        }
    }

    protected abstract void dropedFile(File file);

    protected void dropedOther(Object droped) {
    }

}
