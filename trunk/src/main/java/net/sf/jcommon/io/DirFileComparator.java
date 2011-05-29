package net.sf.jcommon.io;

import java.io.File;
import java.util.Comparator;

/**
 */
public class DirFileComparator implements Comparator<File> {

  public DirFileComparator() {
  }

  public int compare(File f1, File f2) {
    return ((f1.isDirectory()?2:1) - (f2.isDirectory()?2:1));
  }
}