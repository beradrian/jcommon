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
