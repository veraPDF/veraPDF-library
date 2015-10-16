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

	public static String toUTCString(Calendar calendar) {
		return calendar == null ? null : toUTCString(calendar.getTime());
	}

	public static String toUTCString(Date time) {
		if (time == null) {
			return null;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(UTC_PATTERN);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		return dateFormat.format(time);
	}

	public static Calendar toCalendar(String date) {
		return date == null ? null : org.apache.pdfbox.util.DateConverter.toCalendar(date);
	}

	public static String toPDFFormat(String date) {
		Calendar buffer = org.apache.pdfbox.util.DateConverter.toCalendar(date);
		return org.apache.pdfbox.util.DateConverter.toString(buffer);
	}

}
