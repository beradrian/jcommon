package net.sf.jcommon.util;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Utility methods for operating on strings.
 */
public class StringUtils {

    private static final int BUFFER_SIZE = 10 * 1024;

    /** Use only the static methods. */
    private StringUtils() {
    }

    /**
     * @param s the given string
     * @return the given string, except that the first character will be transformed into uppercase.
     */
    public static String toTitleCase(String s) {
        return (Character.toUpperCase(s.charAt(0))) + s.substring(1);
    }

    /** Transform a string like blah_blah into blahBlah.
     * @param s the string to be transformed
     * @return the new string
     */
    public static String toCamelCase(String s) {
        StringBuilder sb = new StringBuilder(s);
        for (int i = 1; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (c == '_') {
                sb.deleteCharAt(i);
                c = sb.charAt(i);
                if (c >= 'a' && c <= 'z') {
                    sb.deleteCharAt(i);
                    c += 'A' - 'a';
                    sb.insert(i, c);
                }
            } else {
                if (c >= 'A' && c <= 'Z') {
                    sb.deleteCharAt(i);
                    c += 'a' - 'A';
                    sb.insert(i, c);
                }
            }
        }
        return sb.toString();
    }
    
    /** Reads an URL as an input stream and return the contents as string.
     * @param url the url
     * @return the content to be find at the given URL
     */
    public static String toString(URL url) {
        if (url == null) return null;
        try {
            return toString(url.openStream());
        } catch (IOException e) {
            return null;
        }
    }

    /** Reads an reader and return the contents as string.
     * @param in the input stream
     * @return the content to be find in the given InputStream
     */
    public static String toString(InputStream in) {
        return toString(new InputStreamReader(in));
    }

    /** Reads an reader and return the contents as string.
     * @param in the reader
     * @return the content to be find in the given Reader
     */
    public static String toString(Reader in) {
        if (in == null) return null;
        try {
            int readed;
            char[] buff = new char[BUFFER_SIZE];
            StringBuilder aux = new StringBuilder();
            do {
                readed = in.read(buff, 0, BUFFER_SIZE);
                if (readed > 0) {
                    aux.append(buff, 0, readed);
                }
            } while (readed > 0);
            in.close();
            return aux.toString();
        } catch (IOException exc) {
            return null;
        }
    }

}
