package org.verapdf.metadata.fixer.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Evgeniy Muravitskiy
 */
public class DateConverter {

	private static final String UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
	private static SimpleDateFormat DATE_FORMAT;

	public static String toUTCString(Calendar calendar) {
		return calendar == null ? null : toUTCString(calendar.getTime());
	}

	public static String toUTCString(Date time) {
		if (time == null) {
			return null;
		}

		if (DATE_FORMAT == null) {
			DATE_FORMAT = new SimpleDateFormat(UTC_PATTERN);
			DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
		}

		return DATE_FORMAT.format(time);
	}

	public static Calendar toCalendar(String date) {
		return date == null ? null : org.apache.pdfbox.util.DateConverter.toCalendar(date);
	}

	public static String toPDFFormat(String date) {
		Calendar buffer = org.apache.pdfbox.util.DateConverter.toCalendar(date);
		return org.apache.pdfbox.util.DateConverter.toString(buffer);
	}

	public static String toPDFFormat(Calendar date) {
		return org.apache.pdfbox.util.DateConverter.toString(date);
	}

}
