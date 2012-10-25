package net.sf.jcommon.io;

import java.io.File;
import java.util.Comparator;

import net.sf.jcommon.util.Comparators;

/**
 * Any of the below comparators can be chained using {@link Comparators#chain(Comparator...)},
 * e.g. if you want files sorted by name, but directories first you will use
 * {@code Comparators.chain(FileComparators.dirFirst(), FileComparators.byName())}
 *
 */
public class FileComparators {

	private FileComparators() {
	}

	private static Comparator<File> DIR_FIRST = new Comparator<File>() {
		public int compare(File f1, File f2) {
			return ((f1.isDirectory() ? 2 : 1) - (f2.isDirectory() ? 2 : 1));
		}
	};

	public static Comparator<File> dirFirst() {
		return DIR_FIRST;
	}
	
	private static Comparator<File> EXISTING_FIRST = new Comparator<File>() {
		public int compare(File f1, File f2) {
			return ((f1.exists() ? 2 : 1) - (f2.exists() ? 2 : 1));
		}
	};

	public static Comparator<File> existingFirst() {
		return EXISTING_FIRST;
	}
	
	private static Comparator<File> NAME_COMPARATOR = new Comparator<File>() {
	    public int compare(File f1, File f2) {
            return f1.getName().compareTo(f2.getName());
	    }
	};

	public static Comparator<File> byName() {
		return NAME_COMPARATOR;
	}
	
	private static Comparator<File> FULL_PATH_COMPARATOR = new Comparator<File>() {
	    public int compare(File f1, File f2) {
            return f1.getAbsolutePath().compareTo(f2.getAbsolutePath());
	    }
	};
	
	public static Comparator<File> byFullPath() {
		return FULL_PATH_COMPARATOR;
	}
	
	private static Comparator<File> SIZE_COMPARATOR = new Comparator<File>() {
	    public int compare(File f1, File f2) {
	    	return (int)(f1.length() - f2.length());
	    }
	};

	public static Comparator<File> bySize() {
		return SIZE_COMPARATOR;
	}

	
	private static Comparator<File> LAST_MODIFIED_COMPARATOR = new Comparator<File>() {
	    public int compare(File f1, File f2) {
	    	return (int)(f1.lastModified() - f2.lastModified());
	    }
	};

	public static Comparator<File> byLastModified() {
		return LAST_MODIFIED_COMPARATOR;
	}

	private static Comparator<File> EXTENSION_COMPARATOR = new Comparator<File>() {
	    public int compare(File f1, File f2) {
	        int idx1 = f1.getName().lastIndexOf('.');
	        String ext1 = (idx1 < 0 ? "" : f1.getName().substring(idx1 + 1));
	        int idx2 = f2.getName().lastIndexOf('.');
	        String ext2 = (idx2 < 0 ? "" : f2.getName().substring(idx2 + 1));
	        return ext1.compareTo(ext2);
	    }
	};

	public static Comparator<File> byExtension() {
		return EXTENSION_COMPARATOR;
	}
	
}
