package net.sf.jcommon.io;

import java.io.*;
import java.net.URL;

public class FileUtils {

    private static final int BUFFER_SIZE = 10 * 1024;

    /**
     * Use only it's static methods.
     */
    private FileUtils() {
    }

    /**
     * Returns the size of the file f if f is a file or the total size
     * of the files and subdirectories in it if f is a directory.
     *
     * @param f the file/folder to calculate the size for
     * @return the total size in bytes
     */
    public static long diskUsage(File f) {
        if (f.isDirectory()) {
            long s = 0;
            File fl[] = f.listFiles();
            int n = fl.length;
            for (int i = 0; i < n; i++) {
                s += diskUsage(fl[i]);
            }
            return s;
        } else {
            return (f.length());
        }
    }


    /** Deletes the specified directory and its content recursively.
     * @param path the directory to be deleted
     * @return if the directory was deleted or not
     */
    public static boolean deleteDirectory(File path) {
        return !path.exists() || (empty(path) && path.delete());
    }

    /** Deletes the content of specified directory recursively.
     * @param path the directory to be cleared
     * @return if the directory was cleared or not
     */
    public static boolean empty(File path) {
        boolean retval = true;
        if (path.exists()) {
            File[] files = path.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    retval &= deleteDirectory(file);
                } else {
                    retval &= file.delete();
                }
            }
        }
        return retval;
    }

    /**
     * Returns the size of the file f if f is a file or the total size
     * of the files and subdirectories in it if f is a directory.
     *
     * @param f the file/folder to calculate the size for
     * @return the total size in bytes
     */
    public static long fileCount(File f) {
        return fileCount(f, null);
    }

    /**
     * Returns the size of the file f if f is a file or the total size
     * of the files and subdirectories in it if f is a directory.
     *
     * @param f the file/folder to calculate the size for
     * @param filter the filter used to filter the files to be counted
     * @return the total size in bytes
     */
    public static long fileCount(File f, FileFilter filter) {
        if (f.isDirectory()) {
            long s = 0;
            File fl[] = filter == null ? f.listFiles() : f.listFiles(filter);
            int n = fl.length;
            for (int i = 0; i < n; i++) {
                s += fileCount(fl[i], filter);
            }
            return s;
        } else {
            return 1;
        }
    }

    /**
     * Includes a resource into a writer stream.
     *
     * @param url the URL to the resource
     * @param out the writer to which is included the resource
     * @throws java.io.IOException if an error occurs
     */
    public static void includeResource(URL url, Writer out) throws IOException {
        char[] buff = new char[BUFFER_SIZE];
        Reader in = new InputStreamReader(url.openStream());
        int readed = 0;
        do {
            readed = in.read(buff);
            if (readed > 0) {
                out.write(buff, 0, readed);
            }
        } while (readed > 0);
        in.close();
    }

    /**
     * Get the contents of an URL stream as a string.
     *
     * @param url the url
     * @return the content as string
     * @throws java.io.IOException if an error occurs
     */
    public static String getResourceAsString(URL url) throws IOException {
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        Writer out = new OutputStreamWriter(buff);
        includeResource(url, out);
        out.close();
        return new String(buff.toByteArray());
    }

}
