package org.verapdf.component;

import java.util.Collection;
import java.util.Formatter;
import java.util.Objects;

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
public final class AuditDurationImpl implements AuditDuration {

	private static final AuditDuration defaultInstance = new AuditDurationImpl();

	@XmlAttribute
	private final long start;
	@XmlAttribute
	private final long finish;

	private AuditDurationImpl() {
		this(0, 0);
	}

	private AuditDurationImpl(final long start, final long finish) {
		this.start = start;
		this.finish = finish;
		assert (finish >= start);
	}

	@Override
	public long getStart() {
		return this.start;
	}

	@Override
	public long getFinish() {
		return this.finish;
	}

	@Override
	public long getDifference() {
		return this.finish - this.start;
	}

	@XmlValue
	@Override
	public String getDuration() {
		return getStringDuration(this.getDifference());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		AuditDurationImpl that = (AuditDurationImpl) obj;
		return start == that.start
				&& finish == that.finish;
	}

	@Override
	public int hashCode() {
		return Objects.hash(start, finish);
	}

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
			throw new IllegalArgumentException("start:" + start + " and finish:" + finish + " must be >= 0");
		}
		if (start > finish) {
			throw new IllegalArgumentException("start:" + start + " must not be > finish:" + finish);
		}
		return new AuditDurationImpl(start, finish);
	}

	public static String getStringDuration(final long difference) {
		long hours = difference / (60 * 60 * 1000);
		long minutes = difference / (60 * 1000) % 60;
		long seconds = difference / 1000 % 60;
		long millis = difference % 1000;

		try (Formatter formatter = new Formatter()) {
			formatter.format("%02d:%02d:%02d:%03d", hours, minutes, seconds, millis);
			return formatter.toString();
		}
	}

	public static long sumDuration(Collection<AuditDuration> durations) {
		long res = 0;
		if (durations != null) {
			for (AuditDuration duration : durations) {
				res += duration.getDifference();
			}
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
