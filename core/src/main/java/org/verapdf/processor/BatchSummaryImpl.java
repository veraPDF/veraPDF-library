/**
 * 
 */
package org.verapdf.processor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.verapdf.component.AuditDuration;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 2 Nov 2016:11:43:50
 */

final class BatchSummaryImpl implements BatchSummary {
	@XmlElement
	private final AuditDuration duration;
	@XmlAttribute
	private final int jobs;
	@XmlAttribute
	private final int failedJobs;

	/**
	 * @param duration
	 * @param jobs
	 * @param failedJobs
	 */
	private BatchSummaryImpl(AuditDuration duration, int jobs, int failedJobs) {
		super();
		this.duration = duration;
		this.jobs = jobs;
		this.failedJobs = failedJobs;
	}

	/**
	 * @return the duration
	 */
	@Override
	public AuditDuration getDuration() {
		return this.duration;
	}

	/**
	 * @return the jobs
	 */
	@Override
	public int getJobs() {
		return this.jobs;
	}

	/**
	 * @return the failedJobs
	 */
	@Override
	public int getFailedJobs() {
		return this.failedJobs;
	}
}
