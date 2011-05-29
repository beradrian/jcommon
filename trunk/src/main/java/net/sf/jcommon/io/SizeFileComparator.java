package net.sf.jcommon.io;

import java.io.File;
import java.util.Comparator;

/**
 *
 */

public class SizeFileComparator implements Comparator<File> {

    public SizeFileComparator() {
    }

    public int compare(File f1, File f2) {
        return (int) (f1.length() - f2.length());
    }

}