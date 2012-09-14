package net.sf.jcommon.io;

import java.io.File;
import java.io.FileFilter;

import com.google.common.base.Predicate;

public abstract class FilePredicate implements FileFilter, Predicate<File> {

	@Override
	public boolean accept(File file) {
		return apply(file);
	}
	
}
