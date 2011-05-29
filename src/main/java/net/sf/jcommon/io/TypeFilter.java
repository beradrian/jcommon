package net.sf.jcommon.io;

import net.sf.jcommon.util.Filter;

import java.io.FileFilter;
import java.io.File;

/**
 * A filter for different types of files.
 * @author Adrian BER
 */
public class TypeFilter implements FileFilter, Filter<File> {
    /** Accepts folders. */
    public static final int DIRECTORY = 1;
    /** Accepts files. */
    public static final int FILE = 2;
    /** Accepts readonly files/folders. */
    public static final int READONLY = 4;
    /** Accepts hidden files/folders. */
    public static final int HIDDEN = 8;

    private int types = FILE & DIRECTORY;

    public TypeFilter() {
    }

    /** Creates a filter that accepts the given types of files.
     * @param types a logical combination of {@link #DIRECTORY}, {@link #FILE}, {@link #READONLY}, {@link #HIDDEN}
     */
    public TypeFilter(int types) {
        this.types = types;
    }

    public int getTypes() {
        return types;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public boolean accept(File file) {
        if (!file.exists())
            return true;
        int mask = (file.isDirectory() ? DIRECTORY : 0)
                & (file.isFile() ? FILE : 0)
                & (!file.canWrite() ? READONLY : 0)
                & (file.isHidden() ? HIDDEN : 0);
        return (mask & types) != 0;
    }
}
