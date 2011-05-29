package net.sf.jcommon.util;

import static org.junit.Assert.*;

import org.junit.*;
import java.util.GregorianCalendar;

/**
 * @author Adrian BER
 */
public class DateUtilsTestCase {

    @Test public void testDifferenceInDays() {
        assertEquals(3, DateUtils.differenceInDays(new GregorianCalendar(2005, 5, 6, 11, 23, 5).getTime(),
                new GregorianCalendar(2005, 5, 3, 17, 37, 8).getTime()));
        assertEquals(-5, DateUtils.differenceInDays(new GregorianCalendar(2005, 5, 5, 16, 23, 5).getTime(),
                new GregorianCalendar(2005, 5, 10, 10, 37, 8).getTime()));
        assertEquals(0, DateUtils.differenceInDays(new GregorianCalendar(2005, 5, 5, 16, 23, 5).getTime(),
                new GregorianCalendar(2005, 5, 5, 10, 37, 8).getTime()));
        assertEquals(0, DateUtils.differenceInDays(new GregorianCalendar(2005, 5, 5, 14, 23, 5).getTime(),
                new GregorianCalendar(2005, 5, 5, 18, 37, 8).getTime()));
    }

    @Test public void testDifferenceInYears() {
        assertEquals(27, DateUtils.differenceInYears(new GregorianCalendar(2005, 5, 6, 11, 23, 5).getTime(),
                new GregorianCalendar(1978, 3, 11, 11, 23, 8).getTime()));
        assertEquals(27, DateUtils.differenceInYears(new GregorianCalendar(2005, 3, 11, 10, 23, 5).getTime(),
                new GregorianCalendar(1978, 3, 11, 11, 23, 8).getTime()));
        assertEquals(0, DateUtils.differenceInYears(new GregorianCalendar(1978, 5, 11, 10, 23, 5).getTime(),
                new GregorianCalendar(1978, 3, 11, 11, 23, 8).getTime()));
        assertEquals(1, DateUtils.differenceInYears(new GregorianCalendar(1981, 1, 11, 10, 23, 5).getTime(),
                new GregorianCalendar(1980, 1, 11, 11, 23, 8).getTime()));
    }
}
