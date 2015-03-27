package net.sf.jcommon.geo.format;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import net.sf.jcommon.geo.format.DateFormatter;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.StaticMessageSource;

public class DateFormatterTest {

	private static final Locale RO = new Locale("ro");
	
	private StaticMessageSource messageSource;
	private Date d;
	
	@Before
	public void setUp() {
		messageSource = new StaticMessageSource();
		messageSource.addMessage("date.format", Locale.US, "MM/dd/yy");
		messageSource.addMessage("date.format", RO, "dd.MM.yyyy");
		
		messageSource.addMessage("date.long.format", Locale.US, "MMM d, yy");
		messageSource.addMessage("date.long.format", RO, "d MMMM yyyy");
		
		d = new GregorianCalendar(1981, 1, 13).getTime();
	}
	
	@Test
	public void testLocales() {
		DateFormatter df = new DateFormatter(messageSource);
		assertEquals("02/13/81", df.print(d , Locale.US));
		assertEquals("13.02.1981", df.print(d , RO));
	}
	
	@Test
	public void testLocaleNames() {
		DateFormatter df = new DateFormatter(messageSource, "date.long.format");
		assertEquals("Feb 13, 81", df.print(d , Locale.US));
		assertEquals("13 februarie 1981", df.print(d , RO));
	}
	
}
