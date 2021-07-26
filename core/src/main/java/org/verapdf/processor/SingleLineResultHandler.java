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
package org.verapdf.processor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.verapdf.core.VeraPDFException;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.profiles.RuleId;
import org.verapdf.processor.reports.BatchSummary;
import org.verapdf.processor.reports.ItemDetails;
import org.verapdf.report.FeaturesReport;

/**
 * This result handler outputs validation summaries for documents in single line
 * and outputs only basic information.
 *
 * @author Sergey Shemyakov
 */
class SingleLineResultHandler extends AbstractBatchHandler {
	private static final String pass = "PASS "; //$NON-NLS-1$
	private static final String fail = "FAIL "; //$NON-NLS-1$
	private static final String ioExcepMess = "IOException caught when writing to output stream"; //$NON-NLS-1$
	private static final String parseExcepMessTmpl = "%s does not appear to be a valid PDF file and could not be parsed.";
	private static final String pdfEncryptMessTmpl = "%s appears to be an encrypted PDF file and could not be processed.";
	private static final String ruleMessTmpl = "  %s%s-%d\n"; //$NON-NLS-1$
	private OutputStream outputStream;
	private final boolean isVerbose;
	private final boolean logSuccess;
	private ItemDetails item;

	private SingleLineResultHandler(OutputStream outputStream) {
		this(outputStream, false);
	}

	private SingleLineResultHandler(OutputStream outputStream, boolean isVerbose) {
		this(outputStream, isVerbose, false);
	}

	private SingleLineResultHandler(OutputStream outputStream, boolean isVerbose, boolean logSuccess) {
		super();
		this.outputStream = outputStream;
		this.isVerbose = isVerbose;
		this.logSuccess = logSuccess;
	}

	@Override
	public void handleBatchStart(ProcessorConfig config) {
		// Do nothing here
	}

	@Override
	void resultStart(ProcessorResult result) {
		this.item = result.getProcessedItem();
	}

	@Override
	void parsingSuccess(final TaskResult taskResult) {
		// No need to report parsing success
	}

	@Override
	void parsingFailure(final TaskResult taskResult) throws VeraPDFException {
		try {
			this.outputStream.write(String.format(parseExcepMessTmpl, this.item.getName()).getBytes());
		} catch (IOException excep) {
			throw new VeraPDFException(ioExcepMess, excep);
		}
	}

	@Override
	void pdfEncrypted(final TaskResult taskResult) throws VeraPDFException {
		try {
			this.outputStream.write(String.format(pdfEncryptMessTmpl, this.item.getName()).getBytes());
		} catch (IOException excep) {
			throw new VeraPDFException(ioExcepMess, excep);
		}
	}

	@Override
	void validationSuccess(final TaskResult taskResult, final ValidationResult validationResult)
			throws VeraPDFException {
		String reportSummary = (validationResult.isCompliant() ? pass : fail) + this.item.getName() + "\n"; //$NON-NLS-1$
		try {
			this.outputStream.write(reportSummary.getBytes());
			if (this.isVerbose || this.logSuccess) {
				processRules(validationResult);
			}
		} catch (IOException excep) {
			throw new VeraPDFException(ioExcepMess, excep);
		}
	}

	@Override
	void validationFailure(final TaskResult taskResult) throws VeraPDFException {
		String reportSummary = "ERROR " + this.item.getName() + " " + taskResult.getType().fullName() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		try {
			this.outputStream.write(reportSummary.getBytes());
		} catch (IOException excep) {
			throw new VeraPDFException(ioExcepMess, excep);
		}
	}

	@Override
	void featureSuccess(final TaskResult taskResult, final FeaturesReport featuresReport) {
		// Not supporting feature extraction in text mode
	}

	@Override
	void featureFailure(final TaskResult taskResult) {
		// Not supporting feature extraction in text mode
	}

	@Override
	void fixerSuccess(final TaskResult taskResult, final MetadataFixerResult fixerResult) {
		// Not supporting metadata fixing in text mode
	}

	@Override
	void fixerFailure(final TaskResult taskResult) {
		// Not supporting metadata fixing in text mode
	}

	@Override
	void resultEnd(ProcessorResult result) {
		// Do nothing here
	}

	@Override
	public void handleBatchEnd(BatchSummary summary) {
		// Do nothing here
	}

	@Override
	public void close() throws IOException {
		if (this.outputStream != System.out) {
			this.outputStream.close();
		}
	}

	private void processRules(final ValidationResult validationResult) throws IOException {
		Set<RuleId> passedRules = new HashSet<>();
		Set<RuleId> failedRules = new HashSet<>();
		for (TestAssertion assertion : validationResult.getTestAssertions()) {
			if (assertion.getStatus() == TestAssertion.Status.FAILED) {
				if (this.isVerbose) {
					failedRules.add(assertion.getRuleId());
				}
			} else if (this.logSuccess) {
				passedRules.add(assertion.getRuleId());
			}
		}
		if (this.isVerbose) {
			this.outputRules(failedRules, fail);
		}
		if (this.logSuccess) {
			this.outputRules(passedRules, pass);
		}
	}

	private void outputRules(final Set<RuleId> rules, final String messStart) throws IOException {
		for (RuleId id : rules) {
			this.outputStream.write(String
					.format(ruleMessTmpl, messStart, id.getClause(), Integer.valueOf(id.getTestNumber())).getBytes());
		}
	}

	static BatchProcessingHandler newInstance(final OutputStream outputStream) {
		return new SingleLineResultHandler(outputStream);
	}

	static BatchProcessingHandler newInstance(OutputStream outputStream, final boolean verbose) {
		return new SingleLineResultHandler(outputStream, verbose);
	}

	static BatchProcessingHandler newInstance(OutputStream outputStream, final boolean verbose, final boolean logSuccess) {
		return new SingleLineResultHandler(outputStream, verbose, logSuccess);
	}
}
