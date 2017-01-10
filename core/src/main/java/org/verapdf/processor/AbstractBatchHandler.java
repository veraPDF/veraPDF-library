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
