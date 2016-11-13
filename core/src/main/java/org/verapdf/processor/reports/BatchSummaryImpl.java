/**
 * 
 */
package org.verapdf.processor.reports;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.verapdf.component.AuditDuration;
import org.verapdf.component.Components;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 2 Nov 2016:11:43:50
 */
@XmlRootElement(name = "summary")
final class BatchSummaryImpl implements BatchSummary {
	@XmlElement
	private final AuditDuration duration;
	@XmlAttribute
	private final int jobs;
	@XmlAttribute
	private final int failedJobs;
	@XmlAttribute
	private final int valid;
	@XmlAttribute
	private final int inValid;
	@XmlAttribute
	private final int validExcep;
	@XmlAttribute
	private final int features;

	private BatchSummaryImpl() {
		this(Components.defaultDuration(), 0, 0, 0, 0, 0, 0);
	}

	/**
	 * @param duration
	 * @param jobs
	 * @param failedJobs
	 */
	private BatchSummaryImpl(final AuditDuration duration, final int jobs, final int failedJobs, final int valid,
			final int inValid, final int validExcep, final int features) {
		super();
		this.duration = duration;
		this.jobs = jobs;
		this.failedJobs = failedJobs;
		this.valid = valid;
		this.inValid = inValid;
		this.validExcep = validExcep;
		this.features = features;
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

	static class Adapter extends XmlAdapter<BatchSummaryImpl, BatchSummary> {
		@Override
		public BatchSummary unmarshal(BatchSummaryImpl summary) {
			return summary;
		}

		@Override
		public BatchSummaryImpl marshal(BatchSummary summary) {
			return (BatchSummaryImpl) summary;
		}
	}

	static BatchSummary fromValues(final AuditDuration duration, final int jobs, final int failedJobs, final int valid,
			final int inValid, final int validExcep, final int features) {
		return new BatchSummaryImpl(duration, jobs, failedJobs, valid, inValid, validExcep, features);
	}

	@Override
	public int getValidPdfaCount() {
		return this.valid;
	}

	@Override
	public int getInvalidPdfaCount() {
		return this.inValid;
	}

	@Override
	public int getValidationExceptionCount() {
		return this.validExcep;
	}

	@Override
	public int getFeatureCount() {
		return this.features;
	}
}
