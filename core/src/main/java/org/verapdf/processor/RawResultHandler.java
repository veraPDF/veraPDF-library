/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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

import java.io.Writer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLStreamException;

import org.verapdf.core.VeraPDFException;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.processor.reports.BatchSummary;
import org.verapdf.report.FeaturesReport;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 9 Nov 2016:00:44:27
 */

final class RawResultHandler extends AbstractXmlHandler {
	private static final Logger logger = Logger.getLogger(RawResultHandler.class.getCanonicalName());
	private static final String rawEleName = "rawResults"; //$NON-NLS-1$
	private static final String configEleName = "config"; //$NON-NLS-1$
	private static final String itemEleName = "item"; //$NON-NLS-1$
	private static final String summaryName = "summary"; //$NON-NLS-1$
	private final boolean format;
	private final boolean fragment;

	/**
	 * @param dest
	 * @throws VeraPDFException
	 */
	private RawResultHandler(final Writer dest) throws VeraPDFException {
		this(dest, true, true);
	}

	/**
	 * @param dest
	 * @throws VeraPDFException
	 */
	private RawResultHandler(final Writer dest, final boolean format, final boolean fragment) throws VeraPDFException {
		super(dest);
		this.format = format;
		this.fragment = fragment;
	}

	/**
	 * @see org.verapdf.processor.BatchProcessingHandler#handleBatchStart(ProcessorConfig) ()
	 */
	@Override
	public void handleBatchStart(ProcessorConfig procConfig) throws VeraPDFException {
		try {
			startDoc(this.writer);
			this.writer.writeStartElement(rawEleName);
			this.serializeElement(procConfig, configEleName, this.format, this.fragment);
		} catch (XMLStreamException excep) {
			logger.log(Level.WARNING, String.format(strmExcpMessTmpl, writingMessage), excep);
			throw wrapStreamException(excep, rawEleName);
		}
	}

	@Override
	void resultStart(ProcessorResult result) throws VeraPDFException {
		this.serializeElement(result.getProcessedItem(), itemEleName, this.format, this.fragment);
	}

	@Override
	void parsingSuccess(TaskResult taskResult) {
		// No action for successful parsing
	}

	@Override
	void parsingFailure(TaskResult taskResult) throws VeraPDFException {
		this.serializeElement(taskResult, TASK_EXCEPTION, this.format, this.fragment);
	}

	@Override
	void pdfEncrypted(TaskResult taskResult) throws VeraPDFException {
		this.serializeElement(taskResult, TASK_EXCEPTION, this.format, this.fragment);
	}

	@Override
	void validationSuccess(TaskResult taskResult, List<ValidationResult> validationResults) throws VeraPDFException {
		for (ValidationResult result : validationResults) {
			this.serializeElement(result, VALIDATION_RESULT, this.format, this.fragment);
		}
	}

	@Override
	void validationFailure(TaskResult taskResult) throws VeraPDFException {
		this.serializeElement(taskResult, TASK_EXCEPTION, this.format, this.fragment);
	}

	@Override
	void featureSuccess(TaskResult taskResult, FeaturesReport featuresReport) throws VeraPDFException {
		this.serializeElement(featuresReport, FEATURES_REPORT, this.format, this.fragment);
	}

	@Override
	void featureFailure(TaskResult taskResult) throws VeraPDFException {
		this.serializeElement(taskResult, TASK_EXCEPTION, this.format, this.fragment);
	}

	@Override
	void fixerSuccess(TaskResult taskResult, MetadataFixerResult fixerResult) throws VeraPDFException {
		this.serializeElement(fixerResult, FIXER_RESULT, this.format, this.fragment);
	}

	@Override
	void fixerFailure(TaskResult taskResult) throws VeraPDFException {
		this.serializeElement(taskResult, TASK_EXCEPTION, this.format, this.fragment);
	}

	@Override
	void resultEnd(ProcessorResult result, Boolean isLogsEnabled) {
		// Nothing to do here
	}

	/**
	 * @see org.verapdf.processor.BatchProcessingHandler#handleBatchEnd(BatchSummary)
	 */
	@Override
	public void handleBatchEnd(BatchSummary summary) throws VeraPDFException {
		this.serializeElement(summary, summaryName, this.format, this.fragment);
		try {
			this.writer.writeEndElement();
			this.writer.flush();
			endDoc(this.writer);
		} catch (XMLStreamException excep) {
			logger.log(Level.WARNING, String.format(strmExcpMessTmpl, writingMessage), excep);
			throw wrapStreamException(excep, rawEleName);
		}
		this.close();
	}

	static final BatchProcessingHandler newInstance(final Writer dest) throws VeraPDFException {
		return new RawResultHandler(dest);
	}

	static final BatchProcessingHandler newInstance(final Writer dest, final boolean format, final boolean fragment)
			throws VeraPDFException {
		return new RawResultHandler(dest, format, fragment);
	}
}
