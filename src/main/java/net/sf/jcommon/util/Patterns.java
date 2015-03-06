package net.sf.jcommon.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utility methods for operating on strings.
 */
public class Patterns {

    /** Use only the static methods. */
    private Patterns() {
    }
    
	public static final Map<Pattern, String> TXT2HTML = new HashMap<Pattern, String>(),  HTML2TXT = new HashMap<Pattern, String>();
	
	static {
		TXT2HTML.put(Pattern.compile("\\A"), "<p>");
		TXT2HTML.put(Pattern.compile("\\z"), "</p>");
		TXT2HTML.put(Pattern.compile("\\n\\n"), "</p><p>");
		TXT2HTML.put(Pattern.compile("\\n"), "<br/>");
		
		HTML2TXT.put(Pattern.compile("\\A(\\s)*<p>"), "");
		HTML2TXT.put(Pattern.compile("</p>(\\s)*\\z"), "");
		HTML2TXT.put(Pattern.compile("</p>(\\s)*<p>"), "\\n\\n");
		HTML2TXT.put(Pattern.compile("<br>|<br(\\s)*/>"), "\\n");
	}
	
	public static Map<Pattern, String> compile(Map<String, String> replacements) {
		Map<Pattern, String> compiled = new HashMap<Pattern, String>();
		for (Map.Entry<String, String> replacement : replacements.entrySet()) {
			compiled.put(Pattern.compile(replacement.getKey()), replacement.getValue());
		}
		return compiled;
	}

	public static String replaceAll(String s, Map<Pattern, String> replacements) {
		for (Map.Entry<Pattern, String> replacement : replacements.entrySet()) {
			s = replacement.getKey().matcher(s).replaceAll(replacement.getValue());
		}
		return s;
	}
}
