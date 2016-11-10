/**
 * 
 */
package org.verapdf.processor;

import org.verapdf.core.VeraPDFException;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.report.FeaturesReport;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 10 Nov 2016:20:54:24
 */

public abstract class AbstractBatchHandler implements BatchProcessingHandler {

	/**
	 * 
	 */
	protected AbstractBatchHandler() {
		// Nothing to do, just controlling the logic here
	}

	@Override
	public void handleResult(ProcessorResult result) throws VeraPDFException {
		if (result == null) {
			throw new VeraPDFException("Arg result is null and can not be handled.");
		}
		resultStart(result);
		processTasks(result);
		resultEnd(result);
	}

	private void processTasks(ProcessorResult result) throws VeraPDFException {
		for (TaskType taskType : result.getTaskTypes()) {
			TaskResult taskResult = result.getResultForTask(taskType);
			if (!taskResult.isExecuted())
				continue;
			switch (taskType) {
			case VALIDATE:
				if (taskResult.isSuccess())
					validationSuccess(taskResult, result.getValidationResult());
				else
					validationFailure(taskResult);
				break;

			case PARSE:
				if (taskResult.isSuccess())
					parsingSuccess(taskResult);
				else if (!result.isValidPdf())
					parsingFailure(taskResult);
				else
					pdfEncrypted(taskResult);
				break;

			case EXTRACT_FEATURES:
				if (taskResult.isSuccess())
					featureSuccess(taskResult, result.getFeaturesReport());
				else
					featureFailure(taskResult);
				break;

			case FIX_METADATA:
				if (taskResult.isSuccess())
					fixerSuccess(taskResult, result.getFixerResult());
				else
					fixerFailure(taskResult);
				break;

			default:
				break;
			}
		}
	}

	abstract void resultStart(final ProcessorResult result) throws VeraPDFException;

	abstract void parsingSuccess(final TaskResult taskResult) throws VeraPDFException;

	abstract void parsingFailure(final TaskResult taskResult) throws VeraPDFException;

	abstract void pdfEncrypted(final TaskResult taskResult) throws VeraPDFException;

	abstract void validationSuccess(final TaskResult taskResult, final ValidationResult validationResult)
			throws VeraPDFException;

	abstract void validationFailure(final TaskResult taskResult) throws VeraPDFException;

	abstract void featureSuccess(final TaskResult taskResult, final FeaturesReport featuresReport)
			throws VeraPDFException;

	abstract void featureFailure(final TaskResult taskResult) throws VeraPDFException;

	abstract void fixerSuccess(final TaskResult taskResult, final MetadataFixerResult fixerResult)
			throws VeraPDFException;

	abstract void fixerFailure(final TaskResult taskResult) throws VeraPDFException;

	abstract void resultEnd(final ProcessorResult result) throws VeraPDFException;
}
