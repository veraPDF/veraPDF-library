/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
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

import org.verapdf.ReleaseDetails;
import org.verapdf.component.AuditDuration;
import org.verapdf.component.AuditDurationImpl;
import org.verapdf.component.LogsSummary;
import org.verapdf.component.LogsSummaryImpl;
import org.verapdf.core.VeraPDFException;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.processor.reports.*;
import org.verapdf.report.FeaturesReport;

import jakarta.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.Writer;
import java.util.List;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 9 Nov 2016:06:43:57
 */

class MrrHandler extends AbstractXmlHandler {
	private final boolean logPassed;

	/**
	 * @param dest
	 * @throws VeraPDFException
	 */
	private MrrHandler(Writer dest) throws VeraPDFException {
		this(dest, false);
	}

	/**
	 * @param dest
	 * @param logPassed
	 * @throws VeraPDFException
	 */
	protected MrrHandler(Writer dest, boolean logPassed) throws VeraPDFException {
		super(dest);
		this.logPassed = logPassed;
	}

	/**
	 * @see org.verapdf.processor.BatchProcessingHandler#handleBatchStart(ProcessorConfig)
	 */
	@Override
	public void handleBatchStart(ProcessorConfig config) throws VeraPDFException {
		try {
			startDoc(this.writer);
			this.writer.writeStartElement(REPORT);
			addReleaseDetails();
			this.writer.writeStartElement(JOBS);
			this.writer.flush();
		} catch (XMLStreamException excep) {
			throw wrapStreamException(excep);
		}
	}

	private void addReleaseDetails() throws XMLStreamException, VeraPDFException {
		this.writer.writeStartElement(BUILD_INFORMATION);
		for (ReleaseDetails details : ReleaseDetails.getDetails()) {
			this.serializeElement(details, RELEASE_DETAILS, true, true);
		}
		this.writer.writeEndElement();
	}

	@Override
	void resultStart(ProcessorResult result) throws VeraPDFException {
		try {
			this.writer.writeStartElement(JOB);
			this.serializeElement(result.getProcessedItem(), ITEM_DETAILS, true, true);
			this.writer.flush();
		} catch (XMLStreamException excep) {
			throw wrapStreamException(excep);
		}
	}

	@Override
	void parsingSuccess(TaskResult taskResult) {
		// Even here we're not handling parsing success event
	}

	@Override
	void parsingFailure(TaskResult taskResult) throws VeraPDFException {
		this.serializeElement(taskResult, TASK_EXCEPTION, true, true);
	}

	@Override
	void pdfEncrypted(TaskResult taskResult) throws VeraPDFException {
		this.serializeElement(taskResult, TASK_EXCEPTION, true, true);
	}

	@Override
	void validationSuccess(TaskResult taskResult, List<ValidationResult> validationResults) throws VeraPDFException {
		for (ValidationResult result : validationResults) {
			this.serializeElement(Reports.createValidationReport(result, this.logPassed), VALIDATION_RESULT, true, true);
		}
	}

	@Override
	void validationFailure(TaskResult taskResult) throws VeraPDFException {
		this.serializeElement(taskResult, TASK_EXCEPTION, true, true);
	}

	@Override
	void featureSuccess(TaskResult taskResult, FeaturesReport featRep) throws VeraPDFException {
		this.serializeElement(featRep, FEATURES_REPORT, true, true);
	}

	@Override
	void featureFailure(TaskResult taskResult) throws VeraPDFException {
		this.serializeElement(taskResult, TASK_EXCEPTION, true, true);
	}

	@Override
	void fixerSuccess(TaskResult taskResult, MetadataFixerResult fixerResult) throws VeraPDFException {
		MetadataFixerReport mfRep = Reports.fromValues(fixerResult);
		this.serializeElement(mfRep, FIXER_REPORT, true, true);
	}

	@Override
	void fixerFailure(TaskResult taskResult) throws VeraPDFException {
		this.serializeElement(taskResult, TASK_EXCEPTION, true, true);
	}

	@Override
	void resultEnd(ProcessorResult result, Boolean isLogsEnabled) throws VeraPDFException {
		AuditDuration duration = AuditDurationImpl.sumDuration(getDurations(result));
		this.serializeElement(duration, PROCESSING_TIME, true, true);
		if (isLogsEnabled) {
			LogsSummary logsSummary = LogsSummaryImpl.getSummary();
			if (logsSummary.getLogsCount() != 0) {
				this.serializeElement(logsSummary, LOGS, true, true);
			}
		}
		try {
			// End job element
			this.writer.writeEndElement();
			this.writer.flush();
		} catch (XMLStreamException excep) {
			throw wrapStreamException(excep);
		}
	}

	/**
	 * @see org.verapdf.processor.BatchProcessingHandler#handleBatchEnd(org.verapdf.processor.reports.BatchSummary)
	 */
	@Override
	public void handleBatchEnd(BatchSummary summary) throws VeraPDFException {
		try {
			// closes jobs element
			this.writer.writeEndElement();
			this.serializeElement(summary, BATCH_SUMMARY, true, true);
			// closes report element
			this.writer.writeEndElement();
			this.writer.flush();
			endDoc(this.writer);
		} catch (XMLStreamException excep) {
			throw wrapStreamException(excep);
		}
		this.close();
	}

	protected static VeraPDFException wrapStreamException(final JAXBException excep, final String typePart) {
		return new VeraPDFException(String.format(unmarshalErrMessage, typePart), excep);
	}

	static BatchProcessingHandler newInstance(final Writer dest, final boolean logPassed) throws VeraPDFException {
		return new MrrHandler(dest, logPassed);
	}

}
