package net.sf.jcommon.util.text;

import java.text.Format;
import java.text.FieldPosition;
import java.text.ParsePosition;

/**
 * This class is used to format some special escape characters like newline or slash.
 */
@SuppressWarnings("serial")
public class EscapeCharsFormat extends Format {

    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        if (obj == null)
            return null;
        String s = obj.toString();
        for (int i = pos.getBeginIndex(); i < s.length(); i++) {
            switch (s.charAt(i)) {
                case '\\':
                    toAppendTo.append("\\\\");
                    break;
                case '\n':
                    toAppendTo.append("\\n");
                    break;
                case '\t':
                    toAppendTo.append("\\t");
                    break;
                default:
                    toAppendTo.append(s.charAt(i));
            }
        }
        return toAppendTo;
    }

    public Object parseObject(String source, ParsePosition pos) {
        if (source == null)
            return null;
        StringBuffer r = new StringBuffer();
        for (int i = pos.getIndex(); i < source.length(); i++) {
            if (source.charAt(i) == '\\') {
                i++;
                if (i < source.length()) {
                    if (source.charAt(i) == '\\')
                        r.append('\\');
                    else if (source.charAt(i) == 'n')
                        r.append('\n');
                    else if (source.charAt(i) == 't')
                        r.append('\t');
                    else {
                        r.append('\\' + source.charAt(i));
                    }
                }
                else
                    r.append('\\');
            }
            else
                r.append(source.charAt(i));
        }
        pos.setIndex(source.length() - 1);
        return r.toString();
    }

}
