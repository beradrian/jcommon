package net.sf.jcommon.io;

import java.io.File;
import java.util.Comparator;

/**
 * @author Adrian Ber
 * @version 1.0
 */
public class ExtensionFileComparator implements Comparator<File> {

    public int compare(File f1, File f2) {
        int idx1 = f1.getName().lastIndexOf(".");
        if (idx1 < 0)
            idx1 = f1.getName().length();
        int idx2 = f2.getName().lastIndexOf(".");
        if (idx2 < 0)
            idx2 = f2.getName().length();
        return f1.getName().substring(idx1).compareTo(f2.getName().substring(idx2));
    }
}