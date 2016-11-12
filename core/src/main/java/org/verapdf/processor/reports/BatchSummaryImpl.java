/**
 * 
 */
package org.verapdf.processor.reports;

import org.verapdf.component.AuditDuration;
import org.verapdf.component.Components;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 2 Nov 2016:11:43:50
 */
@XmlRootElement(name="summary")
final class BatchSummaryImpl implements BatchSummary {
	@XmlElement
	private final AuditDuration duration;
	@XmlAttribute
	private final int jobs;
	@XmlAttribute
	private final int failedJobs;
	@XmlAttribute
	private final int validPDFAJobs;
	@XmlAttribute
	private final int invalidPDFAJobs;
	@XmlAttribute
	private final int exceptionDuringValidationJobs;
	@XmlAttribute
	private final int featuresSuccessJobs;
	@XmlAttribute
	private final int featuresExceptionJobs;

	private BatchSummaryImpl() {
		this(Components.defaultDuration(), 0, 0, 0, 0, 0, 0, 0);
	}
	/**
	 * @param duration
	 * @param jobs
	 * @param failedJobs
	 */
	private BatchSummaryImpl(AuditDuration duration, int jobs, int failedJobs, int validPDFAJobs,
							 int invalidPDFAJobs, int exceptionDuringValidationJobs,
							 int featuresSuccessJobs, int featuresExceptionJobs) {
		super();
		this.duration = duration;
		this.jobs = jobs;
		this.failedJobs = failedJobs;
		this.validPDFAJobs = validPDFAJobs;
		this.invalidPDFAJobs = invalidPDFAJobs;
		this.exceptionDuringValidationJobs = exceptionDuringValidationJobs;
		this.featuresSuccessJobs = featuresSuccessJobs;
		this.featuresExceptionJobs = featuresExceptionJobs;
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

	/**
	 * @return the validPDFAJobs
	 */
	@Override
	public int getValidPDFAJobs() {
		return this.validPDFAJobs;
	}

	/**
	 * @return the invalidPDFAJobs
	 */
	@Override
	public int getInvalidPDFAJobs() {
		return this.invalidPDFAJobs;
	}

	/**
	 * @return the exceptionDuringValidationJobs
	 */
	@Override
	public int getExceptionDuringValidationJobs() {
		return this.exceptionDuringValidationJobs;
	}

	/**
	 * @return the featuresSuccessJobs
	 */
	@Override
	public int getFeaturesSuccessJobs() {
		return this.featuresSuccessJobs;
	}

	/**
	 * @return the featuresExceptionJobs
	 */
	@Override
	public int getFeaturesExceptionJobs() {
		return this.featuresExceptionJobs;
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

	static BatchSummary fromValues(final AuditDuration duration, final int jobs, final int failedJobs,
								   int validPDFAJobs, int invalidPDFAJobs, int exceptionDuringValidationJobs,
								   int featuresSuccessJobs, int featuresExceptionJobs) {
		return new BatchSummaryImpl(duration, jobs, failedJobs,
				validPDFAJobs, invalidPDFAJobs, exceptionDuringValidationJobs,
				featuresSuccessJobs, featuresExceptionJobs);
	}
}
