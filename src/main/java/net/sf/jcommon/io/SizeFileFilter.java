package net.sf.jcommon.io;

import net.sf.jcommon.util.Filter;

import java.io.FileFilter;
import java.io.File;

/**
 * A file filter that accepts files which size is between a minimum and a maximum specified limit.
 *
 * @author Adrian Ber
 * @version 1.0
 */
public class SizeFileFilter implements FileFilter, Filter<File> {

    private long minimumSize = Long.MIN_VALUE, maximumSize = Long.MAX_VALUE;

    public SizeFileFilter() {
    }

    public long getMinimumSize() {
        return minimumSize;
    }

    public long getMaximumSize() {
        return maximumSize;
    }

    public void setMinimumSize(long value) {
        minimumSize = value;
    }

    public void setMaximumSize(long value) {
        maximumSize = value;
    }

    public boolean accept(File pathname) {
        long size = pathname.length();
        return (size >= minimumSize) && (size <= maximumSize);
    }

    public String toString() {
        return getClass().getName() + "(" + minimumSize + "," + maximumSize + ")";
    }
}