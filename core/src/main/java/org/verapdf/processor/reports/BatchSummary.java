package org.verapdf.processor.reports;

import org.verapdf.component.AuditDuration;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
@XmlJavaTypeAdapter(BatchSummaryImpl.Adapter.class)
public interface BatchSummary {

	/**
	 * @return the duration
	 */
	AuditDuration getDuration();

	/**
	 * @return the jobs
	 */
	int getJobs();

	/**
	 * @return the failedJobs
	 */
	int getFailedJobs();

	/**
	 * @return the validPDFAJobs
	 */
	int getValidPDFAJobs();

	/**
	 * @return the invalidPDFAJobs
	 */
	int getInvalidPDFAJobs();

	/**
	 * @return the exceptionDuringValidationJobs
	 */
	int getExceptionDuringValidationJobs();

	/**
	 * @return the featuresSuccessJobs
	 */
	int getFeaturesSuccessJobs();

	/**
	 * @return the featuresExceptionJobs
	 */
	int getFeaturesExceptionJobs();

}