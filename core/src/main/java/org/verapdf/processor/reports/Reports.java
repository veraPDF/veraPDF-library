/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org> All rights
 * reserved. veraPDF Library core is free software: you can redistribute it
 * and/or modify it under the terms of either: The GNU General public license
 * GPLv3+. You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the
 * source tree. If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html. The Mozilla Public License
 * MPLv2+. You should have received a copy of the Mozilla Public License along
 * with veraPDF Library core as the LICENSE.MPL file in the root of the source
 * tree. If a copy of the MPL was not distributed with this file, you can obtain
 * one at http://mozilla.org/MPL/2.0/.
 */
/**
 * 
 */
package org.verapdf.processor.reports;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.verapdf.component.Components;
import org.verapdf.core.XmlSerialiser;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 10 Nov 2016:08:12:36
 */
public final class Reports {

	private static final String STATEMENT_PREFIX = "PDF file is ";
	private static final String NOT_INSERT = "not ";
	private static final String STATEMENT_SUFFIX = "compliant with Profile requirements.";
	private static final String COMPLIANT_STATEMENT = STATEMENT_PREFIX
			+ STATEMENT_SUFFIX;
	private static final String NONCOMPLIANT_STATEMENT = STATEMENT_PREFIX
			+ NOT_INSERT + STATEMENT_SUFFIX;

	private Reports() {
		throw new AssertionError("Should never enter Reports()."); //$NON-NLS-1$
	}

	/**
	 * @param timer
	 *            a {@link org.verapdf.component.Components.Timer} instance
	 *            which is stopped to measure the duration of the batch process
	 * @param jobs
	 *            the number of jobs in the batch
	 * @param failedJobs
	 *            the number o failed jobs in the batch
	 * @param valid
	 *            the number of valid PDF/A documents in the batch
	 * @param inValid
	 *            the number of invalid PDF/A documents in the batch
	 * @param validExcep
	 *            the number of validation jobs that threw exceptions
	 * @param features
	 *            the number of feature extraction jobs run
	 * @return a new {@link BatchSummary} instance created using the passed
	 *         values
	 */
	public static final BatchSummary createBatchSummary(final Components.Timer timer, final ValidationBatchSummary validationSummary,
			final FeaturesBatchSummary featureSummary, final MetadataRepairBatchSummary repairSummary, final int totalJobs,
														final int failedToParse, final int encrypted, final int outOfMemory, final int veraExceptions) {
		if (totalJobs < 0)  throw new IllegalArgumentException("Argument totalJobs must be >= 0"); //$NON-NLS-1$
		if (failedToParse < 0)  throw new IllegalArgumentException("Argument failedToParse must be >= 0"); //$NON-NLS-1$
		if (encrypted < 0)  throw new IllegalArgumentException("Argument encrypted must be >= 0"); //$NON-NLS-1$
		if (outOfMemory < 0)  throw new IllegalArgumentException("Argument outOfMemory must be >= 0"); //$NON-NLS-1$
		if (veraExceptions < 0)  throw new IllegalArgumentException("Argument veraExceptions must be >= 0"); //$NON-NLS-1$
		if ((failedToParse + encrypted) > totalJobs) throw new IllegalArgumentException("Argument totalJobs must be >= arguments (failedJobs + encrypted)"); //$NON-NLS-1$
		return BatchSummaryImpl.fromValues(timer.stop(), validationSummary, featureSummary, repairSummary, totalJobs, failedToParse, encrypted, outOfMemory, veraExceptions);
	}

	/**
	 * Creates a new {@link ValidationReport} instance from the passed
	 * parameters
	 * 
	 * @param details
	 *            the {@link ValidationDetails} instance to add to the report
	 * @param profileName
	 *            the name of the {@link ValidationProfile} used to validation
	 *            the PDF/A
	 * @param statement
	 *            a String statement, should indicate whether the file was valid
	 *            or invalid
	 * @param isCompliant
	 *            boolean value, set true if PDF/A document complied with the
	 *            validation profile rules
	 * @return a new {@link ValidationReport} instance
	 */
	public static final ValidationReport createValidationReport(final ValidationDetails details,
	                                                            final String profileName, final String statement,
	                                                            final boolean isCompliant, final String jobEndStatus) {
		return ValidationReportImpl.fromValues(details, profileName, statement, isCompliant, jobEndStatus);
	}

	public static final ValidationReport createValidationReport(final ValidationResult validationResult,
																final boolean logPassed) {
		ValidationDetails details = Reports.fromValues(validationResult, logPassed);
		return Reports.createValidationReport(details, validationResult.getProfileDetails().getName(),
		                                      getStatement(validationResult.isCompliant()), validationResult.isCompliant(),
		                                      validationResult.getJobEndStatus().getValue());
	}

	private static String getStatement(boolean status) {
		return status ? COMPLIANT_STATEMENT : NONCOMPLIANT_STATEMENT;
	}

	/**
	 * Deserialise a {@link ValidationReport} from an XML string
	 * 
	 * @param xmlSource
	 *            a String value which is the XML representation of the
	 *            {@link ValidationReport}
	 * @return a new {@link ValidationReport} instance deserialised from the
	 *         source string
	 * @throws JAXBException
	 *             when the passed String is not a valid
	 *             {@link ValidationReport} representation
	 */
	public static final ValidationReport validationReportFromXml(final String xmlSource) throws JAXBException {
		return XmlSerialiser.typeFromXml(ValidationReportImpl.class, xmlSource);
	}

	/**
	 * Creates a new {@link ValidationDetails} instance from the passed
	 * parameters
	 * 
	 * @param result
	 *            the {@link ValidationResult} produced by the validation task
	 * @param logPassedChecks
	 *            boolean indicating whether passed checks were logged
	 * @return a new {@link ValidationDetails} instance
	 */
	public static final ValidationDetails fromValues(final ValidationResult result, boolean logPassedChecks) {
		return ValidationDetailsImpl.fromValues(result, logPassedChecks);
	}

	public static final MetadataFixerReport fromValues(final String status, final int fixCount,
			final List<String> fixes, final List<String> errors) {
		return FixerReportImpl.fromValues(status, fixCount, fixes, errors);
	}

	public static final ValidationDetails fromValues(final ValidationResult result, boolean logPassedChecks,
													 final int maxFailedChecks) {
		return Reports.fromValues(result, logPassedChecks);
	}

	public static final MetadataFixerReport fromValues(final MetadataFixerResult fixerResult) {
		return FixerReportImpl.fromValues(fixerResult);
	}
}
