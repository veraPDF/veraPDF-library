/**
 * 
 */
package org.verapdf.report;

import java.util.Date;
import java.util.Formatter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 21 Sep 2016:23:00:31
 */
@XmlRootElement(name="taskDetails")
public final class TaskDetails {
	final static TaskDetails DEFAULT = new TaskDetails();
	
    private static final long MS_IN_SEC = 1000L;
    private static final int SEC_IN_MIN = 60;
    private static final long MS_IN_MIN = SEC_IN_MIN * MS_IN_SEC;
    private static final int MIN_IN_HOUR = 60;
    private static final long MS_IN_HOUR = MS_IN_MIN * MIN_IN_HOUR;

	@XmlAttribute
	private final String name;
	private final long startMillisecs;
	private final long finishMillisecs;
	
	private TaskDetails() {
		this("task", 0, 0);
	}

	private TaskDetails(final String name, final long startMillisecs, final long finishMillisecs) {
		this.name = name;
		this.startMillisecs = startMillisecs;
		this.finishMillisecs = finishMillisecs;
	}
	
	public String getName() {
		return this.name;
	}
	
	public long getDurationMillisecs() {
		return this.finishMillisecs - this.startMillisecs;
	}
	
	@XmlAttribute
	public String getStart() {
		return new Date(startMillisecs).toString();
	}
	
	@XmlAttribute
	public String getFinish() {
		return new Date(finishMillisecs).toString();
	}
	
	@XmlAttribute
	public String getDuration() {
		return getProcessingTime(this.getDurationMillisecs());
	}
	
	public static TaskDetails fromValues(final String name, final long startMillisecs, final long finishMillisecs) {
		return new TaskDetails(name, startMillisecs, finishMillisecs);
	}

	public static TaskDetails fromValues(final String name, final Date start, Date finish) {
		return new TaskDetails(name, start.getTime(), finish.getTime());
	}

	public static class TimedFactory {
		private final String name;
		private final long start;

		public TimedFactory(final String name) {
			this.name = name;
			this.start = System.currentTimeMillis();
		}
		
		public TaskDetails stop() {
			return fromValues(this.name, this.start, System.currentTimeMillis());
		}
	}
	
	private static String getProcessingTime(long processTime) {
        long processingTime = processTime;

        Long hours = Long.valueOf(processingTime / MS_IN_HOUR);
        processingTime %= MS_IN_HOUR;

        Long mins = Long.valueOf(processingTime / MS_IN_MIN);
        processingTime %= MS_IN_MIN;

        Long sec = Long.valueOf(processingTime / MS_IN_SEC);
        processingTime %= MS_IN_SEC;

        Long ms = Long.valueOf(processingTime);

        String res;

        try (Formatter formatter = new Formatter()) {
            formatter.format("%02d:", hours);
            formatter.format("%02d:", mins);
            formatter.format("%02d.", sec);
            formatter.format("%03d", ms);
            res = formatter.toString();
        }

        return res;
    }

}
