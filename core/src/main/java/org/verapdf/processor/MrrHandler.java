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

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.verapdf.ReleaseDetails;
import org.verapdf.component.AuditDuration;
import org.verapdf.component.AuditDurationImpl;
import org.verapdf.core.VeraPDFException;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.processor.reports.BatchSummary;
import org.verapdf.processor.reports.MetadataFixerReport;
import org.verapdf.processor.reports.Reports;
import org.verapdf.processor.reports.ValidationDetails;
import org.verapdf.processor.reports.ValidationReport;
import org.verapdf.report.FeaturesReport;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 9 Nov 2016:06:43:57
 */

final class MrrHandler extends AbstractXmlHandler {
    private final static String STATEMENT_PREFIX = "PDF file is ";
    private final static String NOT_INSERT = "not ";
    private final static String STATEMENT_SUFFIX = "compliant with Validation Profile requirements.";
    private final static String COMPLIANT_STATEMENT = STATEMENT_PREFIX
            + STATEMENT_SUFFIX;
    private final static String NONCOMPLIANT_STATEMENT = STATEMENT_PREFIX
            + NOT_INSERT + STATEMENT_SUFFIX;
	private final static String report = "report"; //$NON-NLS-1$
	private final static String jobEleName = "job"; //$NON-NLS-1$
	private final static String jobsEleName = jobEleName + "s"; //$NON-NLS-1$
	private final static String procTimeEleName = "processingTime"; //$NON-NLS-1$
	private final static String buildInfoEleName = "buildInformation"; //$NON-NLS-1$
	private final static String itemDetailsEleName = "itemDetails"; //$NON-NLS-1$
	private final static String releaseDetailsEleName = "releaseDetails"; //$NON-NLS-1$
	private final static String taskResultEleName = "taskResult"; //$NON-NLS-1$
	private final static String batchSummaryEleName = "batchSummary"; //$NON-NLS-1$
	private final static String fixerRepEleName = "fixerReport"; //$NON-NLS-1$
	private final static String featuresRepEleName = "featuresReport"; //$NON-NLS-1$
	private final static String validationRepEleName = "validationReport"; //$NON-NLS-1$
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
	private MrrHandler(Writer dest, boolean logPassed) throws VeraPDFException {
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
			this.writer.writeStartElement(report);
			addReleaseDetails();
			this.writer.writeStartElement(jobsEleName);
			this.writer.flush();
		} catch (XMLStreamException excep) {
			throw wrapStreamException(excep);
		}
	}

	private void addReleaseDetails() throws XMLStreamException, VeraPDFException {
		this.writer.writeStartElement(buildInfoEleName);
		for (ReleaseDetails details : ReleaseDetails.getDetails()) {
			this.serialseElement(details, releaseDetailsEleName, true, true);
		}
		this.writer.writeEndElement();
	}

	@Override
	void resultStart(ProcessorResult result) throws VeraPDFException {
		try {
			this.writer.writeStartElement(jobEleName);
			this.serialseElement(result.getProcessedItem(), itemDetailsEleName, true, true);
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
		this.serialseElement(taskResult, taskResultEleName, true, true);
	}

	@Override
	void pdfEncrypted(TaskResult taskResult) throws VeraPDFException {
		this.serialseElement(taskResult, taskResultEleName, true, true);
	}

	@Override
	void validationSuccess(TaskResult taskResult, ValidationResult result) throws VeraPDFException {
		ValidationDetails details = Reports.fromValues(result, this.logPassed);
		ValidationReport valRep = Reports.createValidationReport(details, result.getProfileDetails().getName(), getStatement(result.isCompliant()), result.isCompliant());
		this.serialseElement(valRep, validationRepEleName, true, true);
	}

	@Override
	void validationFailure(TaskResult taskResult) throws VeraPDFException {
		this.serialseElement(taskResult, taskResultEleName, true, true);
	}

	@Override
	void featureSuccess(TaskResult taskResult, FeaturesReport featRep) throws VeraPDFException {
		this.serialseElement(featRep, featuresRepEleName, true, true);
	}

	@Override
	void featureFailure(TaskResult taskResult) throws VeraPDFException {
		this.serialseElement(taskResult, taskResultEleName, true, true);
	}

	@Override
	void fixerSuccess(TaskResult taskResult, MetadataFixerResult fixerResult) throws VeraPDFException {
		MetadataFixerReport mfRep = Reports.fromValues(fixerResult);
		this.serialseElement(mfRep, fixerRepEleName, true, true);
	}

	@Override
	void fixerFailure(TaskResult taskResult) throws VeraPDFException {
		this.serialseElement(taskResult, taskResultEleName, true, true);
	}

	@Override
	void resultEnd(ProcessorResult result) throws VeraPDFException {
		AuditDuration duration = AuditDurationImpl.sumDuration(getDurations(result));
		this.serialseElement(duration, procTimeEleName, true, true);
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
			this.serialseElement(summary, batchSummaryEleName, true, true);
			newLine(this.writer);
			// closes report element
			this.writer.writeEndElement();
			this.writer.flush();
			endDoc(this.writer);
		} catch (XMLStreamException excep) {
			throw wrapStreamException(excep);
		}
		this.close();
	}

	private static Collection<AuditDuration> getDurations(ProcessorResult result) {
		EnumMap<TaskType, TaskResult> results = result.getResults();
		if(results != null) {
			Collection<AuditDuration> res = new ArrayList<>();
			for (TaskType type : results.keySet()) {
				if (results.get(type).getDuration() != null) {
					res.add(results.get(type).getDuration());
				}
			}
			return res;
		}
		return Collections.emptyList();
	}

	protected static VeraPDFException wrapStreamException(final JAXBException excep, final String typePart) {
		return new VeraPDFException(String.format(unmarshalErrMessage, typePart), excep);
	}

	static BatchProcessingHandler newInstance(final Writer dest, final boolean logPassed) throws VeraPDFException {
		return new MrrHandler(dest, logPassed);
	}
	
    private static String getStatement(boolean status) {
        return status ? COMPLIANT_STATEMENT : NONCOMPLIANT_STATEMENT;
    }

}
