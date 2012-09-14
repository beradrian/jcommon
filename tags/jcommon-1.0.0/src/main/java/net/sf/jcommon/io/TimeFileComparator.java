package net.sf.jcommon.io;

import java.io.File;
import java.util.Comparator;

/**
 */

public class TimeFileComparator implements Comparator<File> {

  public TimeFileComparator() {
  }

  public int compare(File f1, File f2) {
    return (int)(f1.lastModified() - f2.lastModified());
  }
}