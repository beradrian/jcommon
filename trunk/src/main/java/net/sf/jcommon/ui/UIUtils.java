package net.sf.jcommon.ui;

import javax.swing.*;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageReadParam;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.util.Enumeration;

/**
 * Swing utility methods.
 * @author Adrian BER
 */
public class UIUtils {

    /** Use only the static methods. */
    private UIUtils() {}

    /** Modifies the color with the specified factor.
     * @param color the color to be modified
     * @param factor the factor to modify with
     * @return the new color
     */
    public static Color modifyByFactor(Color color, double factor) {
        int r = (int) (color.getRed() * factor);
        int g = (int) (color.getGreen() * factor);
        int b = (int) (color.getBlue() * factor);
        if (r < 0) r = 0;
        if (r > 255) r = 255;
        if (g < 0) g = 0;
        if (g > 255) g = 255;
        if (b < 0) b = 0;
        if (b > 255) b = 255;
        return new Color(r, g, b);
    }

    /**
     * Loads an image from a file
     * @param imageFile the file containing the image
     * @return the image
     */
    public static ImageIcon loadImageIcon(File imageFile) {
        if (imageFile == null)
            return null;
        return new ImageIcon(imageFile.getPath());
    }

    /**
     * Loads a scaled image icon from a file.
     * @param imageFile the file containing the image
     * @param maxWidth the maximmum with of the returned image
     * @param maxHeight the maximmum height of the returned image
     * @return the image
     */
    public static ImageIcon loadImageIcon(File imageFile, int maxWidth, int maxHeight) {
        Image image = loadImage(imageFile, maxWidth, maxHeight, Image.SCALE_FAST);
        return image == null ? null : new ImageIcon(image);
    }

    /**
     * Loads a scaled image icon from an input stream.
     * @param input the file containing the image
     * @return the image
     */
    public static ImageIcon loadImageIcon(InputStream input) {
        Image image = loadImage(input);
        return image == null ? null : new ImageIcon(image);
    }

    /**
     * Loads a scaled image icon from an input stream.
     * @param input the file containing the image
     * @param maxWidth the maximmum with of the returned image
     * @param maxHeight the maximmum height of the returned image
     * @return the image
     */
    public static ImageIcon loadImageIcon(InputStream input, int maxWidth, int maxHeight) {
        Image image = loadImage(input, maxWidth, maxHeight, Image.SCALE_FAST);
        return image == null ? null : new ImageIcon(image);
    }

    /**
     * Loads a scaled image from a file.
     * @param imageFile the file containing the image
     * @param maxWidth the maximmum with of the returned image
     * @param maxHeight the maximmum height of the returned image
     * @param scaleType how to scale the image
     * @return the image
     */
    public static Image loadImage(File imageFile, int maxWidth, int maxHeight, int scaleType) {
        if (imageFile == null)
            return null;
        try {
            return loadImage(new BufferedInputStream(new FileInputStream(imageFile)), maxWidth, maxHeight, scaleType);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private static double getScaleFactor(double actualWidth, double actualHeight,
            double maxWidth, double maxHeight) {
        double factor = 1;
        if (maxWidth > 0 && maxHeight > 0) {
            factor = Math.min(actualWidth/maxWidth, actualHeight/maxHeight);
        } else {
            if (maxWidth > 0)
                factor = actualWidth/maxWidth;
            else if (maxHeight > 0)
                factor = actualHeight/maxHeight;
        }
        return factor;
    }

    /**
     * Loads a scaled image from an input stream.
     * @param input the file containing the image
     * @return the image
     */
    public static Image loadImage(InputStream input) {
        return loadImage(input, -1, -1, Image.SCALE_FAST);
    }

    /**
     * Loads a scaled image from an input stream.
     * @param input the file containing the image
     * @param maxWidth the maximmum with of the returned image
     * @param maxHeight the maximmum height of the returned image
     * @param scaleType the scaling type as defined in Image
     * @return the image
     */
    public static Image loadImage(InputStream input, int maxWidth, int maxHeight, int scaleType) {
        Image image = null;
        try {
            // create the input stream
            ImageInputStream imageInput = ImageIO.createImageInputStream(input);

            // create the reader
            ImageReader reader = ImageIO.getImageReaders(imageInput).next();
            reader.setInput(imageInput, true, true);
            int imageIndex = 0;

            // find the scale factor
            int scaleFactor = (int)getScaleFactor(reader.getWidth(imageIndex), reader.getHeight(imageIndex),
                    maxWidth, maxHeight);
            if (scaleFactor > 1) {
                if (reader.hasThumbnails(imageIndex)) {
                    // try to find the most appropriate thumbnail
                    int thumbnailCount = reader.getNumThumbnails(imageIndex);
                    int thumbnailIndex = -1;
                    int thumbnailDiffWidth = 0;
                    int thumbnailDiffHeight = 0;
                    for (int i = 0; i < thumbnailCount; i++) {
                        int tw = reader.getThumbnailWidth(imageIndex, i);
                        int th = reader.getThumbnailHeight(imageIndex, i);
                        if (tw - maxWidth <= thumbnailDiffWidth && th - maxHeight <= thumbnailDiffHeight) {
                            thumbnailIndex = i;
                            thumbnailDiffWidth = tw - maxWidth;
                            thumbnailDiffHeight = th - maxHeight;
                        }
                    }
                    if (thumbnailIndex >= 0) {
                        image = reader.readThumbnail(imageIndex, thumbnailIndex);
                    }
                }

                if (image == null) {
                    if (scaleType == Image.SCALE_FAST) {
                        // read using sumbsampling if a thumbnail wasn't find
                        ImageReadParam readParam = reader.getDefaultReadParam();
                        readParam.setSourceSubsampling(scaleFactor, scaleFactor, 0, 0);
                        image = reader.read(imageIndex, readParam);
                    } else {
                        image = reader.read(imageIndex);
                        image = image.getScaledInstance(maxWidth * scaleFactor, maxHeight * scaleFactor, scaleType);
                    }
                }
            } else {
                // if the scaling sizes are bigger than the image read the entire image
                image = reader.read(imageIndex);
            }
        } catch (IOException e) {
            image = null;
        }
        return image;
    }

    /** Scales the given image such that will not be bigger than the specified values
     * and keeps the aspect ratio.
     * @param image the image to be scaled
     * @param maxWidth the maximmum width
     * @param maxHeight the maximmum height
     * @param keepAspectRatio if true the aspect ratio of the image is maintained
     * @return the scaled image
     */
    public static Image scaleImage(Image image, int maxWidth, int maxHeight, boolean keepAspectRatio) {
        return scaleImage(image, maxWidth, maxHeight, keepAspectRatio, Image.SCALE_DEFAULT);
    }

    /** Scales the given image such that will not be bigger than the specified values
     * and keeps the aspect ratio.
     * @param image the image to be scaled
     * @param maxWidth the maximmum width
     * @param maxHeight the maximmum height
     * @param keepAspectRatio if true the aspect ratio of the image is maintained
     * @param hints hints for scaling the image
     * @return the scaled image
     */
    public static Image scaleImage(Image image, int maxWidth, int maxHeight, boolean keepAspectRatio, int hints) {
        if (!keepAspectRatio) {
            return image.getScaledInstance(maxWidth, maxHeight, hints);
        }
        double scaleFactorWidth = image.getWidth(null) / maxWidth;
        double scaleFactorHeight = image.getHeight(null) / maxHeight;
        if (scaleFactorWidth != 1 || scaleFactorHeight != 1) {
            if (scaleFactorWidth > scaleFactorHeight) {
                image = image.getScaledInstance(maxWidth, -1, hints);
            } else {
                image = image.getScaledInstance(-1, maxHeight, hints);
            }
        }
        return image;
    }

    /** Scales the given image such that will not be bigger than the specified values
     * and keeps the aspect ratio.
     * @param image the image to be scaled
     * @param maxWidth the maximmum width
     * @param maxHeight the maximmum height
     * @param keepAspectRatio if true the aspect ratio of the image is maintained
     * @return the scaled image
     */
    public static ImageIcon scaleImageIcon(ImageIcon image, int maxWidth, int maxHeight, boolean keepAspectRatio) {
        return scaleImageIcon(image, maxWidth, maxHeight, keepAspectRatio, Image.SCALE_DEFAULT);
    }

    /** Scales the given image such that will not be bigger than the specified values
     * and keeps the aspect ratio.
     * @param image the image to be scaled
     * @param maxWidth the maximmum width
     * @param maxHeight the maximmum height
     * @param keepAspectRatio if true the aspect ratio of the image is maintained
     * @param hints hints for scaling the image
     * @return the scaled image
     */
    public static ImageIcon scaleImageIcon(ImageIcon image, int maxWidth, int maxHeight, boolean keepAspectRatio,
            int hints) {
        return new ImageIcon(UIUtils.scaleImage(image.getImage(), maxWidth, maxHeight, keepAspectRatio, hints));
    }

    public static BufferedImage convertImageToBufferedImage(Image image) {
        ImageObserver observer = null;
        BufferedImage bufferedImage = new BufferedImage ( image.getWidth(observer), image.getHeight(observer),
                BufferedImage.TYPE_INT_BGR);
        bufferedImage.createGraphics().drawImage( image, 0, 0, observer);
        return bufferedImage;
    }

    public static void ensureVisible(JDialog dialog) {
        ensureVisible(dialog, true);
    }

    public static void ensureVisible(JDialog dialog, boolean center) {
        if (dialog.isVisible()) {
            dialog.repaint();
            dialog.requestFocus();
        } else {
            if (center)
                dialog.setLocationRelativeTo(dialog.getParent());
            dialog.setVisible(true);
        }
    }

    /** Returns the UI resource string.
     * @param key the resource key as it is in UIManager
     * @param defaultValue the value returned if there's no resource
     * identified by the given key.
     * @return the UI resource string
     */
    public static String getUIString(String key, String defaultValue) {
        String value = UIManager.getString(key);
        return value != null ? value : defaultValue;
    }

    /** Returns the UI resource icon.
     * @param key the resource key as it is in UIManager
     * @param defaultValue the value returned if there's no resource
     * identified by the given key.
     * @return the UI resource icon
     */
    public static Icon getUIIcon(String key, Icon defaultValue) {
        Icon value = UIManager.getIcon(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Install the header renderer for all the existing and future added columns of the table
     * @param table the table where to install the header
     * @param renderer the header renderer
     */
    public static void installHeaderRenderer(final JTable table, final TableCellRenderer renderer) {
        // set the renderer for the existing columns
        Enumeration<TableColumn> e = table.getColumnModel().getColumns();
        while (e.hasMoreElements()) {
            e.nextElement().setHeaderRenderer(renderer);
        }

        // add a listener to set the renderer to every new added column
        table.getColumnModel().addColumnModelListener(new TableColumnModelListener() {
            public void columnMarginChanged(ChangeEvent e) {
            }

            public void columnSelectionChanged(ListSelectionEvent e) {
            }

            public void columnAdded(TableColumnModelEvent e) {
                int end = e.getToIndex();
                JTable table = (JTable) e.getSource();
                for (int i = e.getFromIndex(); i < end; i++) {
                    table.getColumn(table.getColumnName(i)).setHeaderRenderer(renderer);
                }
            }

            public void columnMoved(TableColumnModelEvent e) {
            }

            public void columnRemoved(TableColumnModelEvent e) {
            }
        });

    }

}
