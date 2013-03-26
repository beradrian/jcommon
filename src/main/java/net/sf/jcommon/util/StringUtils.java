package net.sf.jcommon.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods for operating on strings.
 */
public class StringUtils {

    /** Use only the static methods. */
    private StringUtils() {
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

    private static final Pattern NEWLINE_NEWLINE = Pattern.compile("\\n\\n");
    private static final Pattern NEWLINE = Pattern.compile("\\n");
    
    /**
     * Replaces newline-newline with tag P and newline with BR
     * @param text
     * @return
     */
    public static String nl2p(String text) {
    	Matcher m = NEWLINE_NEWLINE.matcher(text);
    	if (m.find()) {
    		text = "<p>" + m.replaceAll("</p><p>") + "</p>";
    	}
    	m = NEWLINE.matcher(text);
    	if (m.find()) {
    		text = m.replaceAll("<br/>");
    	}
    	return text;
    }
}
