package org.verapdf.metadata.fixer.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.verapdf.metadata.fixer.utils.MetadataFixerConstants.UTC_PATTERN;

/**
 * Utility class for converting dates to different formats
 *
 * @author Evgeniy Muravitskiy
 */
public class DateConverter {

	/**
	 * Convert {@code Calendar} object to string representation in UTC form
	 *
	 * @param calendar passed date
	 * @return string representation of passed date
	 */
	public static String toUTCString(Calendar calendar) {
		return calendar == null ? null : toUTCString(calendar.getTime());
	}

	/**
	 * Convert {@code Date} object to string representation in UTC form
	 *
	 * @param time passed date
	 * @return string representation of passed date
	 */
	public static String toUTCString(Date time) {
		if (time == null) {
			return null;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(UTC_PATTERN);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		return dateFormat.format(time);
	}

	/**
	 * Convert string date representation to string representation in UTC form.
	 * <p/>
	 * Note: current implementation is not effective
	 *
	 * @param date passed date
	 * @return UTC string representation of passed date
	 */
	public static String toUTCString(String date) {
		return toUTCString(toCalendar(date));
	}

	/**
	 * Convert string representation of date to {@code Calendar} object
	 *
	 * @param date passed string date
	 * @return {@code Calendar} date
	 */
	public static Calendar toCalendar(String date) {
		if (date == null) {
			return null;
		}

		Calendar buffer = org.apache.pdfbox.util.DateConverter.toCalendar(date);
		buffer.setTimeZone(TimeZone.getTimeZone("UTC"));
		return buffer;
	}

	/**
	 * Convert string representation of date to
	 * string representation of date in PDF format
	 *
	 * @param date passed date
	 * @return PDF string representation of passed date
	 */
	public static String toPDFFormat(String date) {
		Calendar buffer = org.apache.pdfbox.util.DateConverter.toCalendar(date);
		buffer.setTimeZone(TimeZone.getTimeZone("UTC"));
		return org.apache.pdfbox.util.DateConverter.toString(buffer);
	}

	/**
	 * Convert {@code Calendar} date to string representation of date in PDF format
	 *
	 * @param date passed date
	 * @return PDF string representation of date
	 */
	public static String toPDFFormat(Calendar date) {
		return org.apache.pdfbox.util.DateConverter.toString(date);
	}
}
