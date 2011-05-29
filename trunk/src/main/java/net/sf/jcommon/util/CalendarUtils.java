package net.sf.jcommon.util;

import java.util.Calendar;

/**
 * Calendar utiliy methods. Use only static methods.
 * @author Adrian BER
 */
public class CalendarUtils {

    private CalendarUtils() {}

    /** Returns the difference in days between the two calendars.
     * The time is ignored when comparing.
     * Example: differenceInDays(25/09/2004 14:09:14, 30/09/2004 10:23:41) = -5.
     * @param a the first calendar date
     * @param b the second calendar date
     * @return the difference in days between two calendar dates
     */
    public static int differenceInDays(Calendar a, Calendar b) {
        a.set(Calendar.HOUR_OF_DAY, 0);
        a.set(Calendar.MINUTE, 0);
        a.set(Calendar.SECOND, 0);
        a.set(Calendar.MILLISECOND, 0);
        b.set(Calendar.HOUR_OF_DAY, 0);
        b.set(Calendar.MINUTE, 0);
        b.set(Calendar.SECOND, 0);
        b.set(Calendar.MILLISECOND, 0);
        return (int) ((a.getTime().getTime() - b.getTime().getTime())
                /(1000*60*60*24));
    }

    /** Returns true if the given calendars are in the same day.
     * @param a the first calendar date
     * @param b the second calendar date
     * @return true if the given calendars are in the same day
     */
    public static boolean sameDay(Calendar a, Calendar b) {
        return (a.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH))
                && (a.get(Calendar.MONTH) == b.get(Calendar.MONTH))
                && (a.get(Calendar.YEAR) == b.get(Calendar.YEAR));
    }
}
