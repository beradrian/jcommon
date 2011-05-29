package net.sf.jcommon.util.text;

import java.text.Format;
import java.text.FieldPosition;
import java.text.ParsePosition;

/**
 * This class is used to format a null. The pattern is what replaces a null value.
 */
@SuppressWarnings("serial")
public class NullFormat extends Format {

    private String pattern = "";

    public NullFormat(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        toAppendTo.append(obj == null ? pattern : obj.toString());
        return toAppendTo;
    }

    public Object parseObject(String source, ParsePosition pos) {
        if (source == null)
            return null;
        pos.setIndex(source.length() - 1);
        source = source.substring(pos.getIndex());
        return (source.equals("null") ? null : source);
    }

}
