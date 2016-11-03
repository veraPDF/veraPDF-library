package org.verapdf.processor;

import org.verapdf.component.AuditDuration;

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

}