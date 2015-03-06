package net.sf.jcommon.geo.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.format.Formatter;

/**
 * Formats a date using a i18n message source from where it retrieves the date format based on the locale.
 * The date format is stored as any message and the default key name is {@code date.format}.
 */
public class DateFormatter implements Formatter<Date> {

	/** From where to retrieve the i18n date format. */
    private MessageSource messageSource;
    /** The name of the message key in the i18n messages. */
    private String dateFormatMessageKey;

    /**
     * @param messageSource From where to retrieve the i18n date format.
     */
    public DateFormatter(MessageSource messageSource) {
		this(messageSource, "date.format");
	}

    /**
     * @param messageSource From where to retrieve the i18n date format.
     * @param messageKey the name of the message key in the i18n messages.
     */
	public DateFormatter(MessageSource messageSource, String messageKey) {
		this.messageSource = messageSource;
		this.dateFormatMessageKey = messageKey;
	}

    public Date parse(final String text, final Locale locale) throws ParseException {
        final SimpleDateFormat dateFormat = createDateFormat(locale);
        return dateFormat.parse(text);
    }

    public String print(final Date object, final Locale locale) {
        final SimpleDateFormat dateFormat = createDateFormat(locale);
        return dateFormat.format(object);
    }

    private SimpleDateFormat createDateFormat(final Locale locale) {
        SimpleDateFormat dateFormat;
        try {
        	dateFormat = new SimpleDateFormat(this.messageSource.getMessage(dateFormatMessageKey, null, locale), locale);
        } catch (NoSuchMessageException exc) {
        	dateFormat = new SimpleDateFormat();
        }
        dateFormat.setLenient(false);
        return dateFormat;
    }

}