package net.sf.jcommon.io;

import java.net.URISyntaxException;
import java.net.URL;
import java.io.*;

/**
 */
public class IOUtils {

    /** Use only it's static methods. */
    private IOUtils() {
    }

    public static File toFile(URL url) {
        File f = null;
        try {
          f = new File(url.toURI());
        } catch(URISyntaxException e) {
          f = new File(url.getPath());
        }
        return f;
    }

}
