package net.sf.jcommon.util;

import java.util.Date;

/**
 * Utility methods for Date objects.
 */
public class DateUtils {
    public enum Period {
    	
    	SECOND(1000l), MINUTE(1000l * 60), HOUR(1000l * 3600), DAY(1000l * 3600 * 24),
    	YEAR(1000l * 3600 * 24 * 365 + 1000 * 3600 * 6);
    	
    	private long ms;
    	
    	Period(long ms) {
    		this.ms = ms;
    	}
    	
    	public long getMilliSeconds() {
    		return ms;
    	}
    }
    
    /** Use only the static methods. */
    private DateUtils() {}

    public static long difference(Date date1, Date date2, Period scope) {
        return (date1.getTime() / scope.getMilliSeconds()) - (date2.getTime() / scope.getMilliSeconds());
    }

    public static long differenceInDays(Date date1, Date date2) {
        return difference(date1, date2, Period.DAY);
    }

    public static int differenceInYears(Date date1, Date date2) {
        return (int)difference(date1, date2, Period.YEAR);
    }

    public static boolean isDateAfterToday(Date dateToCheck) {
        return differenceInDays(dateToCheck, new Date()) >= 1;
    }

    public static boolean sameDay(Date date1, Date date2) {
        return differenceInDays(date1, date2) == 0;        
    }
}
