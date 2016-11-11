/**
 * 
 */
package org.verapdf.component;

import java.util.Formatter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 27 Oct 2016:13:03:27
 */
@XmlRootElement(name = "duration")
final class AuditDurationImpl implements AuditDuration {
	private static final AuditDuration defaultInstance = new AuditDurationImpl();
	private static final long msInSec = 1000L;
	private static final int secInMin = 60;
	private static final long msInMin = secInMin * msInSec;
	private static final int minInHour = 60;
	private static final long msInHour = msInMin * minInHour;

	@XmlAttribute
	private final long start;
	@XmlAttribute
	private final long finish;

	private AuditDurationImpl() {
		this(0, 0);
	}

	private AuditDurationImpl(final long start, final long finish) {
		super();
		this.start = start;
		this.finish = finish;
		assert (finish >= start);
	}

	/**
	 * @return the start
	 */
	@Override
	public long getStart() {
		return this.start;
	}

	/**
	 * @return the finish
	 */
	@Override
	public long getFinish() {
		return this.finish;
	}

	/**
	 * @return the finish
	 */
	@Override
	public long getDifference() {
		return this.finish - this.start;
	}

	@XmlValue
	@Override
	public String getDuration() {
		return getStringDuration(this.getDifference());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.finish ^ (this.finish >>> 32));
		result = prime * result + (int) (this.start ^ (this.start >>> 32));
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AuditDuration)) {
			return false;
		}
		AuditDuration other = (AuditDuration) obj;
		if (this.finish != other.getFinish()) {
			return false;
		}
		if (this.start != other.getStart()) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AuditDurationImpl [start=" + this.start + ", finish=" + this.finish + ", getDifference()="
				+ this.getDifference() + ", getDuration()=" + this.getDuration() + "]";
	}

	static AuditDuration defaultInstance() {
		return defaultInstance;
	}

	static AuditDuration fromValues(final long start, final long finish) {
		if (start < 0 || finish < 0) {
			throw new IllegalArgumentException("Args start:" + start + " and finish:" + finish + " must be >= 0");
		}
		if (start > finish) {
			throw new IllegalArgumentException("Args start" + start + " must be > finish:" + finish);
		}
		return new AuditDurationImpl(start, finish);
	}

	private static String getStringDuration(final long difference) {
		long diff = difference;
		Long hours = Long.valueOf(diff / msInHour);
		diff %= msInHour;

		Long mins = Long.valueOf(diff / msInMin);
		diff %= msInMin;

		Long sec = Long.valueOf(diff / msInSec);
		diff %= msInSec;

		Long ms = Long.valueOf(diff);

		String res;

		try (Formatter formatter = new Formatter()) {
			formatter.format("%02d:%02d:%02d:%03d", hours, mins, sec, ms);
			res = formatter.toString();
		}

		return res;
	}

	static class Adapter extends XmlAdapter<AuditDurationImpl, AuditDuration> {
		@Override
		public AuditDuration unmarshal(AuditDurationImpl procResultImpl) {
			return procResultImpl;
		}

		@Override
		public AuditDurationImpl marshal(AuditDuration procResult) {
			return (AuditDurationImpl) procResult;
		}
	}

}
