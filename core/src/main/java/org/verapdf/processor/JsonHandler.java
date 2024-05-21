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
package org.verapdf.processor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.verapdf.ReleaseDetails;
import org.verapdf.component.AuditDuration;
import org.verapdf.component.AuditDurationImpl;
import org.verapdf.component.LogsSummary;
import org.verapdf.component.LogsSummaryImpl;
import org.verapdf.core.VeraPDFException;

import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.processor.reports.BatchSummary;
import org.verapdf.processor.reports.MetadataFixerReport;
import org.verapdf.processor.reports.Reports;
import org.verapdf.report.FeaturesNode;
import org.verapdf.report.FeaturesReport;

import java.io.IOException;
import java.io.Writer;
import java.util.Stack;

public class JsonHandler extends AbstractBatchHandler {

	private final Writer writer;
	private final ObjectMapper objectMapper;
	private final boolean logPassed;
	private final Stack<Boolean> stack = new Stack<>();

	protected JsonHandler(Writer writer, boolean logPassed) {
		this.writer = writer;
		this.objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
		this.logPassed = logPassed;
		SimpleModule module = new SimpleModule("FeaturesNodeSerializer", new Version(2, 1,
				3, null, null, null));
		module.addSerializer(FeaturesNode.class, new FeaturesNodeSerializer(FeaturesNode.class));
		module.addSerializer(VeraPDFException.class, new VeraPDFExceptionSerializer(VeraPDFException.class));
		objectMapper.registerModule(module);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	@Override
	public void handleBatchStart(ProcessorConfig config) throws VeraPDFException {
		try {
			this.startDocument();
			this.startElement(REPORT);
			addReleaseDetails();
			this.startElement(JOBS, true);
			writer.flush();
		} catch (IOException excep) {
			throw new VeraPDFException(excep.getMessage(), excep);
		}
	}

	private void addReleaseDetails() throws VeraPDFException {
		this.startElement(BUILD_INFORMATION);
		this.startElement(RELEASE_DETAILS, true);
		for (ReleaseDetails details : ReleaseDetails.getDetails()) {
			this.serializeElement(details, null);
		}
		endArray();
		endElement();
	}

	@Override
	public void handleBatchEnd(BatchSummary summary) throws VeraPDFException {
		try {
			// closes jobs element
			endArray();
			this.serializeElement(summary, BATCH_SUMMARY);
			// closes report element
			endElement();
			endElement();
			writer.flush();
			this.close();
		} catch (IOException excep) {
			throw new VeraPDFException(excep.getMessage(), excep);
		}
	}

	@Override
	void resultStart(ProcessorResult result) throws VeraPDFException {
		// Start job element
		this.startElement(null);
		this.serializeElement(result.getProcessedItem(), ITEM_DETAILS);
		try {
			writer.flush();
		} catch (IOException excep) {
			throw new VeraPDFException(excep.getMessage(), excep);
		}
	}

	@Override
	void parsingSuccess(TaskResult taskResult) throws VeraPDFException {
		// Even here we're not handling parsing success event
	}

	@Override
	void parsingFailure(TaskResult taskResult) throws VeraPDFException {
		this.serializeElement(taskResult, TASK_EXCEPTION);
	}

	@Override
	void pdfEncrypted(TaskResult taskResult) throws VeraPDFException {
		this.serializeElement(taskResult, TASK_EXCEPTION);
	}

	@Override
	void validationSuccess(TaskResult taskResult, ValidationResult validationResult) throws VeraPDFException {
		this.serializeElement(Reports.createValidationReport(validationResult, this.logPassed), VALIDATION_RESULT);
	}

	@Override
	void validationFailure(TaskResult taskResult) throws VeraPDFException {
		this.serializeElement(taskResult, TASK_EXCEPTION);
	}

	@Override
	void featureSuccess(TaskResult taskResult, FeaturesReport featRep) throws VeraPDFException {
		this.serializeElement(featRep, FEATURES_REPORT);
	}

	@Override
	void featureFailure(TaskResult taskResult) throws VeraPDFException {
		this.serializeElement(taskResult, TASK_EXCEPTION);
	}

	@Override
	void fixerSuccess(TaskResult taskResult, MetadataFixerResult fixerResult) throws VeraPDFException {
		MetadataFixerReport mfRep = Reports.fromValues(fixerResult);
		this.serializeElement(mfRep, FIXER_REPORT);
	}

	@Override
	void fixerFailure(TaskResult taskResult) throws VeraPDFException {
		this.serializeElement(taskResult, TASK_EXCEPTION);
	}

	@Override
	void resultEnd(ProcessorResult result, Boolean isLogsEnabled) throws VeraPDFException {
		AuditDuration duration = AuditDurationImpl.sumDuration(getDurations(result));
		this.serializeElement(duration, PROCESSING_TIME);
		if (isLogsEnabled) {
			LogsSummary logsSummary = LogsSummaryImpl.getSummary();
			if (logsSummary.getLogsCount() != 0) {
				this.serializeElement(logsSummary, LOGS);
			}
		}
		try {
			// End job element
			endElement();
			writer.flush();
		} catch (IOException excep) {
			throw new VeraPDFException(excep.getMessage(), excep);
		}
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

	private void startElement(String name) throws VeraPDFException {
		startElement(name, false);
	}

	private void startElement(String name, boolean isArray) throws VeraPDFException {
		try {
			if (stack.peek()) {
				writer.write(",");
			} else {
				stack.set(stack.size() - 1, true);
			}
			stack.add(false);
			if (name != null) {
				writer.write("\"");
				writer.write(name);
				writer.write("\":");
			}
			if (isArray) {
				writer.write("[");
			} else {
				writer.write("{");
			}
		} catch (IOException e) {
			throw new VeraPDFException(e.getMessage(), e);
		}
	}

	private void startDocument() throws VeraPDFException {
		stack.add(false);
		try {
			writer.write("{");
		} catch (IOException e) {
			throw new VeraPDFException(e.getMessage(), e);
		}
	}

	private void endElement() throws VeraPDFException {
		stack.pop();
		try {
			writer.write("}");
		} catch (IOException e) {
			throw new VeraPDFException(e.getMessage(), e);
		}
	}

	private void endArray() throws VeraPDFException {
		stack.pop();
		try {
			writer.write("]");
		} catch (IOException e) {
			throw new VeraPDFException(e.getMessage(), e);
		}
	}

	private void serializeElement(Object object, String name) throws VeraPDFException {
		try {
			if (stack.peek()) {
				writer.write(",");
			} else {
				stack.set(stack.size() - 1, true);
			}
			if (name != null) {
				writer.write("\"");
				writer.write(name);
				writer.write("\":");
			}
			writer.write(objectMapper.writeValueAsString(object));
		} catch (IOException e) {
			throw new VeraPDFException(e.getMessage(), e);
		}
	}
}
