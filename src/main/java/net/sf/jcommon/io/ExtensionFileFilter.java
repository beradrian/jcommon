package net.sf.jcommon.io;

import net.sf.jcommon.util.Filter;

import java.io.FileFilter;
import java.io.File;

/**
 */
public class ExtensionFileFilter implements FileFilter, Filter<File> {

    private String[] extensions;
    private boolean matchCase;

    public ExtensionFileFilter(String... extensions) {
        this(false, extensions);
    }

    public ExtensionFileFilter(boolean matchCase, String... extensions) {
        this.extensions = extensions;
        this.matchCase = matchCase;
    }

    public boolean accept(File file) {
        for (String extension : extensions) {
            if (matchCase ? file.getName().endsWith(extension)
                    : file.getName().toLowerCase().endsWith(extension.toLowerCase()))
                return true;
        }
        return false;
    }
}
