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

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.verapdf.core.VeraPDFException;

import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.processor.reports.BatchSummary;
import org.verapdf.report.FeaturesReport;

import java.io.IOException;
import java.io.OutputStream;


public class JsonHandler extends AbstractBatchHandler {

	private final OutputStream stream;
	private final ObjectMapper objectMapper;

	protected JsonHandler(OutputStream stream, boolean logPassed) {
		this.stream = stream;
		this.objectMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule("Serializer", new Version(2, 1,
				3, null, null, null));

		ProcessorResultSerializer resultSerializer = new ProcessorResultSerializer(ProcessorResult.class, logPassed);
		module.addSerializer(ProcessorResult.class, resultSerializer);

		objectMapper.registerModule(module);
	}

	@Override
	public void handleBatchStart(ProcessorConfig config) throws VeraPDFException {
		// Nothing to do here
	}

	@Override
	public void handleBatchEnd(BatchSummary summary) throws VeraPDFException {
		// Nothing to do here
	}

	@Override
	void resultStart(ProcessorResult result) throws VeraPDFException {
		// Nothing to do here
	}

	@Override
	void parsingSuccess(TaskResult taskResult) throws VeraPDFException {
		// Nothing to do here
	}

	@Override
	void parsingFailure(TaskResult taskResult) throws VeraPDFException {
		// Nothing to do here
	}

	@Override
	void pdfEncrypted(TaskResult taskResult) throws VeraPDFException {
		// Nothing to do here
	}

	@Override
	void validationSuccess(TaskResult taskResult, ValidationResult validationResult) throws VeraPDFException {
		// Nothing to do here
	}

	@Override
	void validationFailure(TaskResult taskResult) throws VeraPDFException {
		// Nothing to do here
	}

	@Override
	void featureSuccess(TaskResult taskResult, FeaturesReport featuresReport) throws VeraPDFException {
		// Nothing to do here
	}

	@Override
	void featureFailure(TaskResult taskResult) throws VeraPDFException {
		// Nothing to do here
	}

	@Override
	void fixerSuccess(TaskResult taskResult, MetadataFixerResult fixerResult) throws VeraPDFException {
		// Nothing to do here
	}

	@Override
	void fixerFailure(TaskResult taskResult) throws VeraPDFException {
		// Nothing to do here
	}

	@Override
	void resultEnd(ProcessorResult result, Boolean isLogsEnabled) throws VeraPDFException {
		try {
			objectMapper.writeValue(stream, result);
		} catch (IOException e) {
			throw new VeraPDFException(e.getMessage(), e);
		}
	}

	@Override
	public void close() throws IOException {
		stream.close();
	}
}
