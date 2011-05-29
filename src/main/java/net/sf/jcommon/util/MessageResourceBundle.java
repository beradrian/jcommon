package net.sf.jcommon.util;

import java.util.*;
import java.text.*;
import java.io.IOException;
import java.io.InputStream;

/**
 */
public class MessageResourceBundle extends ResourceBundle {
    private static final String PATTERN_SUFFIX = ".pattern";
    private static final String RESOURCE_SUFFIX = ".i18n";

    private String resourcePath = "res/";

    /** The resources map with key, values */
    private Map<String, Object> resources = new HashMap<String, Object>();
    /** The associated locale. */
    private Locale locale;
    /** The date format symbols used to format a date. */
    private DateFormatSymbols dateSym;
    /** The decimal format symbols used to format numbers. */
    private DecimalFormatSymbols decimalSym;

    public MessageResourceBundle(Class<?> resourceClass, Locale locale) {
        load(resourceClass, locale);
    }

    /** Loads the resources from the class resources of the given class for the given locale.
     * It tries to load a resource "res/[country]_[language].i18n" or  "res/[language].i18n"
     * @param resourceClass the class for which we look for the resources
     * @param locale the associated locale
     */
    public void load(Class<?> resourceClass, Locale locale) {
        InputStream in = resourceClass.getResourceAsStream((resourcePath
                + locale.getCountry() + "_" + locale.getLanguage() + RESOURCE_SUFFIX).toLowerCase());
        if (in == null) {
            in = resourceClass.getResourceAsStream((resourcePath
                + locale.getLanguage() + RESOURCE_SUFFIX).toLowerCase());
        }
        setLocale(locale);
        load(in);
    }

    void load(InputStream in) {
        try {
            Properties properties = new Properties();
            properties.load(in);
            PropertyUtils.copyProperties(properties, resources);
        } catch (IOException e) {
        }
    }


    public String getMessage(String key) {
        return (String) handleGetObject(key);
    }

    public String getMessage(String key, Object[] args) {
        return getMessageFormat(key).format(args);
    }

    public String getMessage(String key, Number number) {
        return getNumberFormat(key).format(number);
    }

    public String getMessage(String key, long l) {
        return getNumberFormat(key).format(l);
    }

    public String getMessage(String key, double d) {
        return getNumberFormat(key).format(d);
    }

    public String getMessage(String key, Date date) {
        return getDateFormat(key).format(date);
    }

    public MessageFormat getMessageFormat(String key) {
        MessageFormat f = (MessageFormat) handleGetObject(key);
        if (f == null) {
            String pattern = (String) handleGetObject(key + PATTERN_SUFFIX);
            if (pattern != null) {
                f = new MessageFormat(pattern, locale);
                resources.put(key, f);
            }
        }
        return f;
    }

    public NumberFormat getNumberFormat(String key) {
        NumberFormat f = (NumberFormat) handleGetObject(key);
        if (f == null) {
            String pattern = (String) handleGetObject(key + PATTERN_SUFFIX);
            if (pattern != null) {
                f = new DecimalFormat(pattern, decimalSym);
                resources.put(key, f);
            }
        }
        return f;
    }

    public DateFormat getDateFormat(String key) {
        DateFormat f = (DateFormat) handleGetObject(key);
        if (f == null) {
            String pattern = (String) handleGetObject(key + PATTERN_SUFFIX);
            if (pattern != null) {
                f = new SimpleDateFormat(pattern, dateSym);
                resources.put(key, f);
            }
        }
        return f;
    }

    private void setLocale(Locale locale) {
        this.locale = locale;
        dateSym = new DateFormatSymbols(locale);
        decimalSym = new DecimalFormatSymbols(locale);
    }

    public Locale getLocale() {
        return locale;
    }

    public DateFormatSymbols getDateFormatSymbols() {
        return dateSym;
    }

    public Set<String> getKeySet() {
        return resources.keySet();
    }

    public Iterator<String> getKeyIterator() {
        return resources.keySet().iterator();
    }

    public Enumeration<String> getKeys() {
        return new IteratorEnumeration<String>(resources.keySet().iterator());
    }

    protected Object handleGetObject(String key) {
        return resources.get(key);
    }
}
