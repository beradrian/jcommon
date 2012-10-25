package net.sf.jcommon.io;

import java.io.File;

import com.google.common.base.Predicate;

/**
 * A file filter that accepts files based on the size.
 * If you want to accept files with size in a given range
 * {@code new SizeFileFilter(Range.closed(min, max))}
 */
public class SizeFileFilter extends FilePredicate {

	private Predicate<Long> sizeFilter;
	
    public SizeFileFilter(Predicate<Long> sizeFilter) {
		this.sizeFilter = sizeFilter;
	}

    public boolean apply(File pathname) {
        return sizeFilter.apply(pathname.length());
    }

}