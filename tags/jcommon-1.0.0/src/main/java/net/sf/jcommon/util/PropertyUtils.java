package net.sf.jcommon.util;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

/**
 * Utility methods to handle {@link java.util.Properties}.
 */
public class PropertyUtils {

    /** Use only the static methods. */
    private PropertyUtils() {
    }

    /** Expand all properties in this collection.
     * @param properties the properties containing the properties to be expanded and
     *      the values used for expanding.
     */
    public static void expandProperties(Properties properties) {
        for (Object o : properties.keySet()) {
            String propertyName = (String) o;
            String propertyValue = (String) properties.get(propertyName);
            properties.setProperty(propertyName, expandProperties(propertyValue, properties));
        }
    }

    /** Expand a value using the given properties.
     * @param value the value to be expanded; if it contains ${aPropertyName} the
     *      sequence is replaced with the property value.
     * @param properties the properties containing the values used for expanding.
     * @return the expanded value
     */
    public static String expandProperties(String value, Properties properties) {
        StringBuilder result = new StringBuilder();
        int prev = 0;
        int pos;
        //search for the next instance of $ from the 'prev' position
        while ((pos = value.indexOf("$", prev)) >= 0) {

            //if there was any text before this, add it as a fragment
            //TODO, this check could be modified to go if pos>prev;
            //seems like this current version could stick empty strings
            //into the list
            if (pos > 0) {
                result.append(value.substring(prev, pos));
            }
            //if we are at the end of the string, we tack on a $
            //then move past it
            if (pos == (value.length() - 1)) {
                result.append("$");
                prev = pos + 1;
            } else if (value.charAt(pos + 1) != '{') {
                //peek ahead to see if the next char is a property or not
                //not a property: insert the char as a literal
                /*
                fragments.addElement(value.substring(pos + 1, pos + 2));
                prev = pos + 2;
                */
                if (value.charAt(pos + 1) == '$') {
                    //backwards compatibility two $ map to one mode
                    result.append("$");
                    prev = pos + 2;
                } else {
                    //new behaviour: $X maps to $X for all values of X!='$'
                    result.append(value.substring(pos, pos + 2));
                    prev = pos + 2;
                }

            } else {
                //property found, extract its name or bail on a typo
                int endName = value.indexOf('}', pos);
                if (endName < 0) {
                    throw new IllegalArgumentException("Syntax error in property: " + value);
                }
                String propertyName = value.substring(pos + 2, endName);
                result.append(expandProperties(properties.getProperty(propertyName), properties));
                prev = endName + 1;
            }
        }
        //no more $ signs found
        //if there is any tail to the file, append it
        if (prev < value.length()) {
            result.append(value.substring(prev));
        }

        return result.toString();
    }

    /**
     * Converts encoded &#92;uxxxx to unicode chars
     * and changes special saved chars to their original forms
     * @param theString the string containing encoded unicode chars
     * @return the string with unicode chars
     */
    public static String convertFromEncodedUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuilder outBuffer = new StringBuilder(len);

        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f') aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    public static void copyProperties(Properties src, Map<? super String, ? super String> dest) {
        Enumeration<?> enumeration = src.propertyNames();
        while (enumeration.hasMoreElements()) {
            String propertyName = enumeration.nextElement().toString();
            dest.put(propertyName, src.getProperty(propertyName));
        }
    }

}
