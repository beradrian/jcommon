package net.sf.jcommon.util;

import java.util.*;
import java.io.*;

/**
 * Extended Properties class to get special values like booleans or integers and to enable saving to a file.
 */
@SuppressWarnings("serial")
public class XProperties extends Properties {

    /** If an option represents a list of options, this is the separator. */
    public static final String LIST_SEPARATOR = ";";

    /** The parent properties from where to get the value if it's not found in this collection. */
    private Properties defaults;

    /** The options given as a semicolon (or other string defined by {@link #LIST_SEPARATOR}) separated list
     * are stored here as lists.
     */
    private Map<String, List<String>> optionLists = new HashMap<String, List<String>>();

    public XProperties() {
    }

    /**
     * Constructor
     * @param defaults the parent properties from where to get the value if it's not found in this collection.
     */
    public XProperties(Properties defaults) {
        super(defaults);
        this.defaults = defaults;
    }

    /**
     * @param name the property name
     * @return returns the value as a boolean
     */
    public boolean getBoolean(String name) {
        return getBoolean(name, false);
    }

    /**
     * @param name the property name
     * @param defaultValue the value returned if the property wasn't found
     * @return returns the value as a boolean
     */
    public boolean getBoolean(String name, boolean defaultValue) {
        Object val = get(name);
        if (val == null)
            return defaultValue;
        else if (val instanceof Boolean)
            return (Boolean)val;
        else
            return "true".equalsIgnoreCase(val.toString());
    }

    /**
     * @param name the property name
     * @return the value as an integer
     */
    public int getInt(String name) {
        return getInt(name, 0);
    }

    /**
     * @param name the property name
     * @param defaultValue the value returned if the property wasn't found
     * @return the value as an integer
     */
    public int getInt(String name, int defaultValue) {
        Object val = get(name);
        if (val == null)
            return defaultValue;
        else if (val instanceof Number)
            return ((Number)val).intValue();
        else
            try {
                return Integer.parseInt(val.toString());
            } catch (NumberFormatException exc) {
                return defaultValue;
            }
    }

    /**
     * @param name the property name
     * @return the value as a double
     */
    public double getDouble(String name) {
        return getDouble(name, 0);
    }

    /**
     * @param name the property name
     * @param defaultValue the value returned if the property wasn't found
     * @return the value as a double
     */
    public double getDouble(String name, double defaultValue) {
        Object val = get(name);
        if (val == null)
            return defaultValue;
        else if (val instanceof Number)
            return ((Number)val).doubleValue();
        else
            try {
                return Double.parseDouble(val.toString());
            } catch (NumberFormatException exc) {
                return defaultValue;
            }
    }

    /**
     * @param name the property name
     * @return the value as a file
     */
    public File getFile(String name) {
        return getFile(name, null);
    }

    /**
     * @param name the property name
     * @param defaultValue the value returned if the property wasn't found
     * @return the value as a file
     */
    public File getFile(String name, File defaultValue) {
        Object val = get(name);
        if (val == null)
            return defaultValue;
        if (val instanceof File)
            return (File)val;
        else
            return new File(val.toString());
    }

    /**
     * Split the value of the given option into a list of values. The option is considered to contain a list of
     * semicolon separated values.
     * @param name the option name
     * @return a list containing the values for the given option
     */
    public List<String> getList(String name) {
        List<String> v = optionLists.get(name);
        if (v == null) {
            String s = getProperty(name);
            if (s != null) {
                v = new ArrayList<String>();
                StringTokenizer st = new StringTokenizer(s, LIST_SEPARATOR, false);
                while (st.hasMoreTokens()) {
                    v.add(st.nextToken().trim());
                }
                optionLists.put(name, v);
            }
        }
        return v;
    }

    /**
     * @param key the property key
     * @return the property value
     */
    public Object get(Object key) {
        Object x = super.get(key);
        if (x == null && defaults != null) {
            x = defaults.get(key);    
        }
        return x;
    }

    @Override
    public Object put(Object key, Object value) {
        optionLists.remove(key.toString());
        return super.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        optionLists.remove(key.toString());
        return super.remove(key);
    }

    /**
     * Loads these properties from a file
     * @param filename the file name from where to load the properties
     * @throws IOException if a read error occurs
     */
    public void load(String filename) throws IOException {
        InputStream in = new FileInputStream(filename);
        super.load(in);
        in.close();
    }

    /**
     * Loads these properties from a file
     * @param f the file name from where to load the properties
     * @throws IOException if a read error occurs
     */
    public void load(File f) throws IOException {
        InputStream in = new FileInputStream(f);
        super.load(in);
        in.close();
    }

    /**
     * Saves these properties to a file.
     * @param filename the file name where to save the properties
     * @throws IOException if a write error occurs
     */
    public void save(String filename) throws IOException {
        OutputStream out = new FileOutputStream(filename);
        save(out);
        out.close();
    }

    /**
     * Saves these properties to a file.
     * @param f the file name where to save the properties
     * @throws IOException if a write error occurs
     */
    public void save(File f) throws IOException {
        File parent = f.getParentFile();
        if (parent.exists() || parent.mkdirs()) {
            OutputStream out = new FileOutputStream(f);
            save(out);
            out.close();
        } else {
            throw new IOException("Parent directory cannot be created.");
        }
    }

    /**
     * Saves these properties to a stream.
     * @param out the stream where to save the properties
     */
    public void save(OutputStream out) {
        save(new OutputStreamWriter(out));
    }

    /**
     * Saves these properties to a stream.
     * @param writer the writer where to save the properties
     */
    public void save(Writer writer) {
        PrintWriter pw = writer instanceof PrintWriter ? (PrintWriter)writer : new PrintWriter(writer);
        Enumeration<?> e = propertyNames();
        while (e.hasMoreElements()) {
            String name = (String)e.nextElement();
            Object value = get(name);
            if (value != null)
                pw.println(name + "=" + escape(value.toString()));
        }
        pw.flush();
    }

    /**
     * Escapes the given string value.
     * @param value the string to be escaped
     * @return the escaped string
     */
    public static String escape(String value) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            switch (ch) {
                case '\n':
                    s.append("\\n");
                    break;
                case '\\':
                    s.append("\\\\");
                    break;
                default:
                    s.append(ch);
            }
        }
        return s.toString();
    }

    /** Creates, loads and returns properties from the given file.
     * @param defaults the default properties for the newly loaded properties
     * @param f the file to load the propertes from
     * @return the loaded options
     * @throws java.io.IOException if an I/O error occurs during loading
     */
    public static XProperties load(Properties defaults, File f) throws IOException {
        XProperties p = new XProperties(defaults);
        p.load(f);
        return p;
    }

}
