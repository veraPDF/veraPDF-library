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
	private static final String ioExcepMess = "IOException caught when writing to output stream";
	private static final String parseExcepMessTmpl = "%s does not appear to be a valid PDF file and could not be parsed.";
	private static final String pdfEncryptMessTmpl = "%s appears to be an encrypted PDF file and could not be processed.";
	private OutputStream outputStream;
	private boolean isVerbose;
	private ItemDetails item;

	private SingleLineResultHandler(OutputStream outputStream) {
		this(outputStream, true);
	}

	private SingleLineResultHandler(OutputStream outputStream, boolean isVerbose) {
		super();
		this.outputStream = outputStream;
		this.isVerbose = isVerbose;
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
		String reportSummary = (validationResult.isCompliant() ? "PASS " : "FAIL ") + this.item.getName() + "\n";
		try {
			this.outputStream.write(reportSummary.getBytes());
			if (this.isVerbose) {
				processFiledRules(validationResult);
			}
		} catch (IOException excep) {
			throw new VeraPDFException(ioExcepMess, excep);
		}
	}

	@Override
	void validationFailure(final TaskResult taskResult) throws VeraPDFException {
		String reportSummary = "ERROR " + this.item.getName() + " " + taskResult.getType().fullName() + "\n";
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

	private void processFiledRules(ValidationResult validationResult) throws IOException {
		Set<RuleId> ruleIds = new HashSet<>();
		for (TestAssertion assertion : validationResult.getTestAssertions()) {
			if (assertion.getStatus() == TestAssertion.Status.FAILED) {
				ruleIds.add(assertion.getRuleId());
			}
		}
		for (RuleId id : ruleIds) {
			String reportRuleSummary = id.getClause() + "-" + id.getTestNumber() + "\n";
			this.outputStream.write(reportRuleSummary.getBytes());
		}
	}

	static BatchProcessingHandler newInstance(final OutputStream outputStream) {
		return new SingleLineResultHandler(outputStream);
	}

	static BatchProcessingHandler newInstance(OutputStream outputStream, final boolean verbose) {
		return new SingleLineResultHandler(outputStream, verbose);
	}
}
