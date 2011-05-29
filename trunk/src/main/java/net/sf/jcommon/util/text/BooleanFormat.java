package net.sf.jcommon.util.text;

import java.text.Format;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.StringTokenizer;

/**
 * This class is used to format a boolean. Accepts a format pattern of the
 * following format "[label_for_false]|[label_for_true]".
 */
@SuppressWarnings("serial")
public class BooleanFormat extends Format {

    private static final String SEPARATOR = "|";
    private String pattern = "no|yes";
    private String falseLabel = "no";
    private String trueLabel = "yes";

    public BooleanFormat(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
        if (pattern != null) {
            StringTokenizer st = new StringTokenizer(pattern, SEPARATOR, false);
            int i = 0;
            while (st.hasMoreTokens()) {
                if (i == 0)
                    falseLabel = st.nextToken();
                else
                    trueLabel = st.nextToken();
                if (++i > 1)
                    break;
            }
        }
    }

    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        if (obj == null)
            return null;
        boolean value;
        if (obj instanceof Boolean)
            value = ((Boolean) obj).booleanValue();
        else
            value = obj.toString().equals("true");
        toAppendTo.append(value ? trueLabel : falseLabel);
        return toAppendTo;
    }

    public Object parseObject(String source, ParsePosition pos) {
        if (source == null)
            return null;
        pos.setIndex(source.length() - 1);
        return new Boolean(source.substring(pos.getIndex()).equals(trueLabel));
    }

}
