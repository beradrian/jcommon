package net.sf.jcommon.io;

import java.util.Comparator;
import java.io.File;

/**
 * @author Adrian BER
 */
public class FileNameComparator implements Comparator<File> {

    private boolean useFullPath;

    public FileNameComparator(boolean useFullPath) {
        this.useFullPath = useFullPath;
    }

    public int compare(File o1, File o2) {
        if (useFullPath)
            return o1.getAbsolutePath().compareTo(o2.getAbsolutePath());
        else
            return o1.getName().compareTo(o2.getName());
    }
}