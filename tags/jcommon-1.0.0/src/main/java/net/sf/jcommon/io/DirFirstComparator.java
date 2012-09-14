package net.sf.jcommon.io;

import java.util.Comparator;
import java.io.File;

/**
 * A file comparator to put the directories before files and the existing files
 * before the non-existing ones.
 * @author Adrian BER
 */
public class DirFirstComparator implements Comparator<File> {

    public int compare(File f1, File f2) {
        if (f1.exists() != f2.exists())
            return f1.exists() ? -1 : 1;
        else if (f1.isDirectory() != f2.isDirectory())
            return f1.isDirectory() ? -1 : 1;
        else
            return f1.getName().compareTo(f2.getName());
    }
    
}
