package net.sf.jcommon.io;

import java.io.File;

/**
 * A file filter that accepts files which size is between a minimum and a maximum specified limit.
 */
public class SizeFileFilter extends FilePredicate {

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

    public boolean apply(File pathname) {
        long size = pathname.length();
        return (size >= minimumSize) && (size <= maximumSize);
    }

    public String toString() {
        return getClass().getName() + "(" + minimumSize + "," + maximumSize + ")";
    }

}