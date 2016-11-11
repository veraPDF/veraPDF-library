/**
 * 
 */
package org.verapdf.processor.reports;

import java.util.List;

import org.verapdf.component.Components;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 10 Nov 2016:08:12:36
 */

public final class Reports {
	private Reports() {
		// TODO Auto-generated constructor stub
	}

	public static final BatchSummary createBatchSummary(final Components.Timer timer, final int jobs,
			final int failedJobs) {
		return BatchSummaryImpl.fromValues(timer.stop(), jobs, failedJobs);
	}

	public static final ValidationReport createValidationReport(final ValidationDetails details,
			final String profileName, final String statement, final boolean isCompliant) {
		return ValidationReportImpl.fromValues(details, profileName, statement, isCompliant);
	}

	public static final ValidationDetails fromValues(final ValidationResult result, boolean logPassedChecks,
			final int maxFailedChecks) {
		return ValidationDetailsImpl.fromValues(result, logPassedChecks, maxFailedChecks);
	}

	public static final MetadataFixerReport fromValues(final String status, final int fixCount, final List<String> fixes,
			final List<String> errors) {
		return FixerReportImpl.fromValues(status, fixCount, fixes, errors);
	}
	
	public static final MetadataFixerReport fromValues(final MetadataFixerResult fixerResult) {
		return FixerReportImpl.fromValues(fixerResult);
	}
}
