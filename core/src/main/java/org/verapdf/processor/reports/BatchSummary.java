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
	
	int getValidPdfaCount();
	int getInvalidPdfaCount();

	int getValidationExceptionCount();
	int getFeatureCount();
}