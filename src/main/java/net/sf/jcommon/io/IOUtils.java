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

    /** Returns the content from the specified URL as an array of bytes.
     * @param url the URL from where to get the content
     * @return the content as an array of bytes
     */
    public static byte[] getBytes(URL url) {
        try {
            return getBytes(url.openStream());
        } catch (IOException e) {
            return null;
        }
    }

    /** Returns the content of the file with the specified name as an array of bytes.
     * @param filename the name of the file from where to get the content
     * @return the content as an array of bytes
     */
    public static byte[] getBytes(String filename) {
        try {
            return getBytes(new FileInputStream(filename));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /** Returns the content of the file as an array of bytes.
     * @param f the file from where to get the content
     * @return the content as an array of bytes
     */
    public static byte[] getBytes(File f) {
        try {
            return getBytes(new FileInputStream(f));
        }
        catch (FileNotFoundException exc) {
            return null;
        }
    }

    /** Returns the content of the input stream as an array of bytes.
     * @param is the input stream from where to get the content
     * @return the content as an array of bytes
     */
    public static byte[] getBytes(InputStream is) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int x;
            while ((x = is.read()) != -1) {
                bos.write(x);
            }
            return bos.toByteArray();
        } catch(IOException exc) { return null;}
    }

}
