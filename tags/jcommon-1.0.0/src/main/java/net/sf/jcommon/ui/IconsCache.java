package net.sf.jcommon.ui;

import javax.swing.*;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.net.URL;
import java.awt.*;

/**
 * @author Adrian BER
 */
public class IconsCache {

    /** The class used to load the icons. */
    private Class<?> resourceClass;

    /** The resource path relative to the resource class. */
    private String resourcePath;

    /** The icons cache as pairs (icon name, icon). */
    private Map<String, Icon> icons = new HashMap<String, Icon>();

    /** The cursors cache as pairs (cursor name, icon). */
    private Map<String, Cursor> cursors = new HashMap<String, Cursor>();

    public IconsCache(Class<?> resourceClass) {
        this(resourceClass, "img");
    }

    public IconsCache(Class<?> resourceClass, String resourcePath) {
        this.resourceClass = resourceClass;
        if (resourcePath == null || resourcePath.trim().length() == 0)
            this.resourcePath = "";
        else
            this.resourcePath = resourcePath.trim() + (resourcePath.endsWith("/") ? "" : "/");
    }

    public Image getImage(String name) {
        Icon icon = getIcon(name);
        return (icon instanceof ImageIcon ? ((ImageIcon)icon).getImage() : null);
    }

    /**
     * Returns the icon with the specified name.
     *
     * @param name the icon name. The icon should be an image file (.png, .gif) located in the
     * <code>img</code> folder. The name shouldn't contain the extension or the img dir.
     * The name could be a list image files separated by <code>&amp;</code> and the
     * returned icon will be a compound icon of all the image files.
     * @return the icon
     */
    public Icon getIcon(String name) {
        Icon icon = icons.get(name);
        if (icon == null) {
            icon = createIcon(name);
            if (icon != null)
                icons.put(name, icon);
        }
        return icon;
    }

    /**
     * Creates an icon with the specified name.
     *
     * @param name the icon name. The icon should be an image file (.png, .gif)
     * located in the <code>resourcePath</code> folder.
     * The name shouldn't contain the extension or the img dir.
     * The name could be a list image files separated by <code>&amp;</code>
     * and the resulting icon will be a compound icon of all the image files.
     * @return the newly created icon
     */
    private Icon createIcon(String name) {
        if (name.indexOf("&") >= 0) {
            ArrayList<Icon> icons = new ArrayList<Icon>();
            StringTokenizer st = new StringTokenizer(name, "&", false);
            while (st.hasMoreTokens()) {
                icons.add(getIcon(st.nextToken()));
            }
            return new ComposedIcon(icons.toArray(new Icon[icons.size()]));
        }
        URL url;
        String name2 = name.toLowerCase();
        if (name2.endsWith(".png") || name2.endsWith(".gif")
                || name2.endsWith(".jpg") || name2.endsWith(".jpeg")
                ) {
            url = resourceClass.getResource(resourcePath + name);
        } else {
            url = resourceClass.getResource(resourcePath + name + ".png");
            if (url == null)
                url = resourceClass.getResource(resourcePath + name + ".gif");
            if (url == null)
                url = resourceClass.getResource(resourcePath + name + ".jpg");
        }
        return url != null ? new ImageIcon(url) : null;
    }

    private Cursor createCursor(String name) {
        return Toolkit.getDefaultToolkit()
                .createCustomCursor(getImage(name), new Point(0, 0), name);
    }

    /**
     * Returns a custom cursor for the specified name.
     * @param name the name of the cursor
     * @return the cursor
     */
    public Cursor getCursor(String name) {
        Cursor cursor = cursors.get(name);
        if (cursor == null) {
            cursor = createCursor(name);
            cursors.put(name, cursor);
        }
        return cursor;
    }

}
