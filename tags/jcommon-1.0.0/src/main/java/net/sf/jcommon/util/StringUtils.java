package net.sf.jcommon.util;

/**
 * Utility methods for operating on strings.
 */
public class StringUtils {

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

}
