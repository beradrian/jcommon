package net.sf.jcommon.util;

import java.util.regex.*;
import java.util.*;

/**
 * A calendar used for comparing with a date but ignoring some fields. For
 * example you can compare only the weekday.
 */
@SuppressWarnings("serial")
public class PatternGregorianCalendar extends GregorianCalendar
        implements Comparable<Calendar> {

    /** the value used to match any field value */
    public static final int ANY = -1;
    private static final int maxFields = 10;

    /** the fields that must be ignored */
    private int[] ignoreFields = new int[maxFields];
    /** the number fields that must be ignored */
    private int ignoreFieldsCount = 0;

    /** Creates a new calendar with the current date */
    public PatternGregorianCalendar() {
    }

    /** Creates a new calendar.
     * @param year the year
     * @param month the month
     * @param day the day of the month
     * @param hour the hour
     * @param minute the minute
     * @param second the second
     */
    public PatternGregorianCalendar(int year, int month, int day,
                                    int hour, int minute, int second) {
        this(year, month, day, -1, hour, minute, second);
    }

    /** Creates a new calendar.
     * @param year the year
     * @param month the month
     * @param day the day of the month
     * @param weekday the day of the week
     * @param hour the hour
     * @param minute the minute
     * @param second the second
     */
    public PatternGregorianCalendar(int year, int month, int day, int weekday,
                                    int hour, int minute, int second) {
        set(YEAR, year);
        set(MONTH, month);
        set(DAY_OF_MONTH, day);
        set(DAY_OF_WEEK, weekday);
        set(HOUR_OF_DAY, hour);
        set(MINUTE, minute);
        set(SECOND, second);
    }

    /** Returns true if the given field should be ignored when comparing with another date.
     * @param field the field to be ignored
     * @return true if the given field should be ignored when comparing with another date, false otherwise
     */
    private boolean isIgnoreField(int field) {
        for (int i = 0; i < ignoreFieldsCount; i++)
            if (ignoreFields[i] == field)
                return true;
        return false;
    }

    /** Sets the given field for ignoring it when comparing with another date.
     * @param field the field to be ignored
     */
    private void addIgnoreField(int field) {
        if (!isIgnoreField(field))
            ignoreFields[ignoreFieldsCount++] = field;
    }

    /** Sets the given field for not ignoring it when comparing with another date.
     * @param field the field to be ignored
     */
    private void removeIgnoreField(int field) {
        for (int i = 0; i < ignoreFieldsCount; i++)
            if (ignoreFields[i] == field) {
                ignoreFields[i] = ignoreFields[--ignoreFieldsCount];
                break;
            }
    }

    /** Compares the given field of this and that calendar.
     * @param that the calendar to compare with
     * @param field the field to compare
     * @return a positive value if this calendar field is bigger, a negative if the other's field is bigger
     * and 0 if are equal
     */
    private int compareFieldTo(Calendar that, int field) {
        if (!isIgnoreField(field)
                && (!(that instanceof PatternGregorianCalendar)
                || !((PatternGregorianCalendar) that).isIgnoreField(field)))
            return get(field) - that.get(field);
        else
            return 0;
    }

    /** Compares this calendar with the given date.
     * @return a positive value if this calendar field is in the future, a negative if in past
     * and 0 if in present
     */
    public int compareToCurrentDate() {
        return compareTo(new GregorianCalendar());
    }

    /** Compares this calendar date with the given date.
     * @param date the date to compare with
     * @return a positive value if this is bigger than the given date, a negative value if smaller and 0 if equal
     */
    public int compareTo(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return compareTo(calendar);
    }

    /** Compares this calendar date with another calendar date.
     * @param that the calendar to compare with
     * @return a positive value if this is bigger than the given date, a negative value if smaller and 0 if equal
     */
    public int compareTo(Calendar that) {
        int retval = 0;
        retval = compareFieldTo(that, YEAR);
        retval = (retval != 0 ? retval : compareFieldTo(that, MONTH));
        retval = (retval != 0 ? retval : compareFieldTo(that, DAY_OF_MONTH));
        retval = (retval != 0 ? retval : compareFieldTo(that, DAY_OF_WEEK));
        retval = (retval != 0 ? retval : compareFieldTo(that, HOUR_OF_DAY));
        retval = (retval != 0 ? retval : compareFieldTo(that, MINUTE));
        retval = (retval != 0 ? retval : compareFieldTo(that, SECOND));
        return retval;
    }

    public void set(int field, int value) {
        if (value == ANY)
            addIgnoreField(field);
        else {
            removeIgnoreField(field);
            super.set(field, value);
        }
    }

    public int get(int field) {
        if (isIgnoreField(field))
            return ANY;
        else
            return super.get(field);
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        if (!isIgnoreField(YEAR))
            s.append(get(YEAR));
        else
            s.append("xxxx");
        s.append('/');
        if (!isIgnoreField(MONTH)) {
            if (get(MONTH) < 10) s.append('0');
            s.append(get(MONTH));
        }
        else
            s.append("xx");
        s.append('/');
        if (!isIgnoreField(DAY_OF_MONTH)) {
            if (get(DAY_OF_MONTH) < 10) s.append('0');
            s.append(get(DAY_OF_MONTH));
        }
        else
            s.append("xx");
        if (!isIgnoreField(DAY_OF_WEEK)) {
            s.append('(');
            s.append(get(DAY_OF_WEEK));
            s.append(") ");
        }
        else
            s.append(' ');
        if (!isIgnoreField(HOUR_OF_DAY)) {
            if (get(HOUR_OF_DAY) < 10) s.append('0');
            s.append(get(HOUR_OF_DAY));
        }
        else
            s.append("xx");
        s.append(':');
        if (!isIgnoreField(MINUTE)) {
            if (get(MINUTE) < 10) s.append('0');
            s.append(get(MINUTE));
        }
        else
            s.append("xx");
        s.append(':');
        if (!isIgnoreField(SECOND)) {
            if (get(SECOND) < 10) s.append('0');
            s.append(get(SECOND));
        }
        else
            s.append("xx");
        return s.toString();
    }

    /** Pattern for matching calendar format without weekday. */
    private static Pattern patt1 = Pattern.compile("([0-9]{4}+|xxxx)/([0-9]{2}+|xx)/([0-9]{2}+|xx)\\s+([0-9]{2}+|xx):([0-9]{2}+|xx):([0-9]{2}+|xx)");
    /** Pattern for matching calendar format with weekday. */
    private static Pattern patt2 = Pattern.compile("([0-9]{4}+|xxxx)/([0-9]{2}+|xx)/([0-9]{2}+|xx)([(]([1-7]|x)[)])\\s+([0-9]{2}+|xx):([0-9]{2}+|xx):([0-9]{2}+|xx)");

    /** Creates a new calendar formatted as in the given string or null
     * if the string isn't in the right format.
     * @param s the formatted string
     * @return the resulting calendar
     */
    public static PatternGregorianCalendar parse(CharSequence s) {
        boolean form1 = false, form2 = false;
        synchronized (patt1) {
            form1 = patt1.matcher(s).matches();
            if (!form1)
                form2 = patt2.matcher(s).matches();
        }
        if (form1) {
            int year, month, day, hour, minute, second;
            try {
                year = Integer.parseInt(s.subSequence(0, 4).toString());
            }
            catch (NumberFormatException exc) {
                year = -1;
            }
            try {
                month = Integer.parseInt(s.subSequence(5, 7).toString());
            }
            catch (NumberFormatException exc) {
                month = -1;
            }
            try {
                day = Integer.parseInt(s.subSequence(8, 10).toString());
            }
            catch (NumberFormatException exc) {
                day = -1;
            }
            try {
                hour = Integer.parseInt(s.subSequence(11, 13).toString());
            }
            catch (NumberFormatException exc) {
                hour = -1;
            }
            try {
                minute = Integer.parseInt(s.subSequence(14, 16).toString());
            }
            catch (NumberFormatException exc) {
                minute = -1;
            }
            try {
                second = Integer.parseInt(s.subSequence(17, 19).toString());
            }
            catch (NumberFormatException exc) {
                second = -1;
            }
            return new PatternGregorianCalendar(year, month, day, hour, minute, second);
        }
        else if (form2) {
            int year, month, day, weekday, hour, minute, second;
            try {
                year = Integer.parseInt(s.subSequence(0, 4).toString());
            }
            catch (NumberFormatException exc) {
                year = -1;
            }
            try {
                month = Integer.parseInt(s.subSequence(5, 7).toString());
            }
            catch (NumberFormatException exc) {
                month = -1;
            }
            try {
                day = Integer.parseInt(s.subSequence(8, 10).toString());
            }
            catch (NumberFormatException exc) {
                day = -1;
            }
            try {
                weekday = Integer.parseInt(s.subSequence(11, 13).toString());
            }
            catch (NumberFormatException exc) {
                weekday = -1;
            }
            try {
                hour = Integer.parseInt(s.subSequence(15, 17).toString());
            }
            catch (NumberFormatException exc) {
                hour = -1;
            }
            try {
                minute = Integer.parseInt(s.subSequence(18, 20).toString());
            }
            catch (NumberFormatException exc) {
                minute = -1;
            }
            try {
                second = Integer.parseInt(s.subSequence(21, 23).toString());
            }
            catch (NumberFormatException exc) {
                second = -1;
            }
            return new PatternGregorianCalendar(year, month, day, weekday, hour, minute, second);
        }
        return null;
    }

    /** Creates a new interval of two calendars formatted as in the given string
     * or null if the string isn't in the right format.
     * @param s the formatted string
     * @return the resulting interval
     */
    public static Interval<? extends Calendar> parseInterval(String s) {
        StringTokenizer st = new StringTokenizer(s, "->", false);
        PatternGregorianCalendar cal1 = null, cal2 = null;
        if (st.hasMoreTokens()) {
            cal1 = parse(st.nextToken().trim());
        }
        if ((cal1 != null) && (st.hasMoreTokens())) {
            cal2 = parse(st.nextToken().trim());
        }
        if ((cal1 != null) && (cal2 != null))
            return new Interval<Calendar>(cal1, cal2);
        else
            return null;
    }

}