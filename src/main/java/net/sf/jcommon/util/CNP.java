package net.sf.jcommon.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * A class for validating a CNP.
 */
public class CNP {

    /** The standard length of a CNP. */
    public static final int LENGTH = 13;

    /** Returns if the given string represents a valid CNP conforming to length. */
    public static boolean validateLength(String cnp) {
        return cnp.length() == LENGTH;
    }

    private static byte[] controlValues = new byte[] {
        2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9
    };

    /** Returns if the given string represents a valid CNP conforming to the control sum. */
    public static boolean validateConsistency(String cnp) {
        if (cnp.length() != LENGTH) {
            return false;
        }
        int[] _cnp = getDigits(cnp);
        if (_cnp == null) {
            return false;
        }
        int k = getControlSum(_cnp);
        if (_cnp[LENGTH - 1] != k) {
            return false;
        }
        return true;
    }

    private static int[] getDigits(String cnp) {
        int _cnp[] = new int[LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            char c = cnp.charAt(i);
            if (!Character.isDigit(c)) {
                return null;
            }
            _cnp[i] = Character.digit(c, 10);
        }
        return _cnp;
    }

    private static int getControlSum(int[] twelveDigits) {
        int k = 0;
        for (int i = 0; i < 12; i++) {
            k += controlValues[i] * twelveDigits[i];
        }
        k %= 11;
        if (k == 10) {
            k = 1;
        }
        return k;
    }

    private static final DateFormat personalIdDateFormat = new SimpleDateFormat("yyMMdd");

    /** Returns if the given string represents a valid CNP for the given birthdate.
     * The 2nd and the 3rd digits represent the last two digits from the year birthdate,
     * the 4th and 5th represent the month and the 7th and 8th the day.
     */
    public static boolean validateBirthdate(String cnp, Date birthdate) {
        return cnp.length() > 6 && cnp.substring(1, 7).equals(personalIdDateFormat.format(birthdate));
    }

    /** Returns if the given string represents a valid CNP. */
    public static boolean validateGender(String cnp, boolean male, Date birthdate) {
        if (cnp == null || cnp.length() < 1 || !Character.isDigit(cnp.charAt(0)))
            return false;
        int g1 = Character.digit(cnp.charAt(0), 10);
        Calendar c = new GregorianCalendar();
        c.setTime(birthdate);
        int g2 = c.get(Calendar.YEAR) < 2000
                ? male ? 1 : 2
                : male ? 5 : 6;
        return g1 == g2;
    }

    public static String createValidCNP(boolean male, Date date, int departmentId, int orderId) {
        StringBuffer result = new StringBuffer("0000000000000");

        Calendar c = new GregorianCalendar();
        c.setTime(date);

        if (c.get(Calendar.YEAR) < 1900) {
            result.setCharAt(0, male ? '3' : '4');
        } else if (c.get(Calendar.YEAR) < 2000) {
            result.setCharAt(0, male ? '1' : '2');
        } else {
            result.setCharAt(0, male ? '5' : '6');
        }
        result.replace(1, 7, personalIdDateFormat.format(date));

        result.replace(7, 9, new Integer(Math.abs(departmentId) % 100).toString());
        result.replace(9, 12, new Integer(Math.abs(orderId) % 1000).toString());

        int k = getControlSum(getDigits(result.toString()));
        result.replace(12, 13, new Integer(k).toString());

        return result.toString();
    }

    private static Random random = new Random();
            
    public static String createValidCNP(boolean male, Date date) {
        
        int departmentId = randomDigit(true) * 10 + randomDigit(false);
        int orderId = randomDigit(true) * 100 + randomDigit(false) * 10 + randomDigit(false);

        return createValidCNP(male, date, departmentId, orderId);
    }

    private static int randomDigit(boolean nonZero) {
        return nonZero
                ? random.nextInt(9) + 1
                : random.nextInt(10);
    }

}
