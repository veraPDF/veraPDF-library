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
/**
 * 
 */
package org.verapdf.processor.reports;

import org.verapdf.component.Components;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;

import java.util.List;

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
			final int failedJobs, final int valid,
			final int inValid, final int validExcep, final int features) {
		return BatchSummaryImpl.fromValues(timer.stop(), jobs, failedJobs, valid, inValid, validExcep, features);
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
