/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
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
	private static final long msInSec = 1000L;
	private static final int secInMin = 60;
	private static final int minInHour = 60;

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
		return this.start == that.start && this.finish == that.finish;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Long.valueOf(this.start), Long.valueOf(this.finish));
	}

	@Override
	public String toString() {
		return "AuditDurationImpl [start=" + this.start + ", finish=" + this.finish + ", getDifference()=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ this.getDifference() + ", getDuration()=" + this.getDuration() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	static AuditDuration defaultInstance() {
		return defaultInstance;
	}

	static AuditDuration fromValues(final long start, final long finish) {
		if (start < 0 || finish < 0) {
			throw new IllegalArgumentException("start:" + start + " and finish:" + finish + " must be >= 0"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		if (start > finish) {
			throw new IllegalArgumentException("start:" + start + " must not be > finish:" + finish); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return new AuditDurationImpl(start, finish);
	}

	public static String getStringDuration(final long difference) {
		long hours = difference / (minInHour * secInMin * msInSec);
		long minutes = difference / (secInMin * msInSec) % minInHour;
		long seconds = difference / msInSec % secInMin;
		long millis = difference % msInSec;

		try (Formatter formatter = new Formatter()) {
			formatter.format("%02d:%02d:%02d:%03d", Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds), //$NON-NLS-1$
					Long.valueOf(millis));
			return formatter.toString();
		}
	}

	public static AuditDuration sumDuration(Collection<AuditDuration> durations) {
		long start = 0L;
		long finish = 0L;
		if (durations != null) {
			for (AuditDuration duration : durations) {
				if (start == 0 || duration.getStart() < start) {
					start = duration.getStart();
				}
				if (duration.getFinish() > finish) {
					finish = duration.getFinish();
				}
			}
		}
		return fromValues(start, finish);
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
