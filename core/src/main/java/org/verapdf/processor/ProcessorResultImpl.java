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
package org.verapdf.processor;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.metadata.fixer.FixerFactory;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.processor.reports.ItemDetails;
import org.verapdf.report.FeaturesReport;

/**
 * Instance of this class contains result of
 * {@link org.verapdf.processor.ProcessorImpl#validate(InputStream, ItemDetails, Config, OutputStream)}
 * work.
 *
 * @author Sergey Shemyakov
 */
@XmlRootElement(name = "processorResult")
class ProcessorResultImpl implements ProcessorResult {
	private final static ProcessorResult defaultInstance = new ProcessorResultImpl();
	@XmlAttribute
	private final boolean isPdf;
	@XmlAttribute
	private final boolean isEncryptedPdf;
	@XmlAttribute
	private final boolean isOutOfMemory;
	@XmlAttribute
	private final boolean hasException;
	@XmlElement
	private final ItemDetails itemDetails;
	private final EnumMap<TaskType, TaskResult> taskResults;
	@XmlElement
	private final ValidationResult validationResult;
	private final FeatureExtractionResult featuresResult;
	@XmlElement
	private final MetadataFixerResult fixerResult;

	private ProcessorResultImpl() {
		this(ItemDetails.defaultInstance(), TaskResultImpl.defaultInstance());
	}

	private ProcessorResultImpl(final ItemDetails details, final TaskResult result) {
		this(details, false, false, false, result);
	}

	private ProcessorResultImpl(final ItemDetails details, boolean isEncrypted, boolean isOutOfMemory, final TaskResult result) {
		this(details, true, isEncrypted, isOutOfMemory, result);
	}

	private ProcessorResultImpl(final ItemDetails details, boolean isValidPdf, boolean isEncrypted, boolean isOutOfMemory,
			final TaskResult result) {
		this(details, isValidPdf, isEncrypted, isOutOfMemory, resMap(result), ValidationResults.defaultResult(),
				new FeatureExtractionResult(), FixerFactory.defaultResult());
	}

	private ProcessorResultImpl(final ItemDetails details, final EnumMap<TaskType, TaskResult> results,
			final ValidationResult validationResult, final FeatureExtractionResult featuresResult,
			final MetadataFixerResult fixerResult) {
		this(details, true, false, false, results, validationResult, featuresResult, fixerResult);
	}

	private ProcessorResultImpl(final ItemDetails details, final boolean isPdf, final boolean isEncrypted,
			final boolean isOutOfMemory,
			final EnumMap<TaskType, TaskResult> results, final ValidationResult validationResult,
			final FeatureExtractionResult featuresResult, final MetadataFixerResult fixerResult) {
		super();
		this.itemDetails = details;
		this.isPdf = isPdf;
		this.isEncryptedPdf = isEncrypted;
		this.isOutOfMemory = isOutOfMemory;
		this.taskResults = results;
		this.hasException = !isEncryptedPdf && !this.isOutOfMemory && this.isPdf &&
				this.taskResults.values().stream().anyMatch(res -> res.getException() != null);
		this.validationResult = validationResult;
		this.featuresResult = featuresResult;
		this.fixerResult = fixerResult;
	}

	/**
	 * @return the results
	 */
	@Override
	public EnumMap<TaskType, TaskResult> getResults() {
		return this.taskResults;
	}

	@Override
	@XmlElementWrapper(name = "taskResult")
	@XmlElement(name = "taskResult")
	public Collection<TaskResult> getResultSet() {
		return this.taskResults.values();
	}

	@Override
	public ItemDetails getProcessedItem() {
		return this.itemDetails;
	}

	@Override
	public EnumSet<TaskType> getTaskTypes() {
		return this.taskResults.isEmpty() ? EnumSet.noneOf(TaskType.class) : EnumSet.copyOf(this.taskResults.keySet());
	}

	static ProcessorResult defaultInstance() {
		return defaultInstance;
	}

	static ProcessorResult fromValues(final ItemDetails details, final EnumMap<TaskType, TaskResult> results,
			final ValidationResult validationResult, final FeatureExtractionResult featuresResult,
			final MetadataFixerResult fixerResult) {
		return new ProcessorResultImpl(details, true, false, false, results, validationResult, featuresResult, fixerResult);
	}

	static ProcessorResult invalidPdfResult(final ItemDetails details, final TaskResult res) {
		return new ProcessorResultImpl(details, res);
	}

	static ProcessorResult encryptedResult(final ItemDetails details, final TaskResult res) {
		return new ProcessorResultImpl(details, true, false, res);
	}

	static ProcessorResult outOfMemoryResult(final ItemDetails details, final TaskResult res) {
		return new ProcessorResultImpl(details, false, true, res);
	}

	static ProcessorResult veraExceptionResult(final ItemDetails details, final TaskResult res) {
		return new ProcessorResultImpl(details, false, false, res);
	}

	@Override
	public ValidationResult getValidationResult() {
		return this.validationResult;
	}

	@Override
	@XmlElement
	public FeaturesReport getFeaturesReport() {
		return FeaturesReport.fromValues(this.featuresResult);
	}

	@Override
	public MetadataFixerResult getFixerResult() {
		return this.fixerResult;
	}

	@Override
	public TaskResult getResultForTask(TaskType taskType) {
		return this.taskResults.get(taskType);
	}

	@Override
	public boolean isPdf() {
		return this.isPdf;
	}

	@Override
	public boolean isEncryptedPdf() {
		return this.isEncryptedPdf;
	}

	@Override
	public boolean isOutOfMemory() {
		return this.isOutOfMemory;
	}

	@Override
	public boolean hasException() {
		return this.hasException;
	}

	static class Adapter extends XmlAdapter<ProcessorResultImpl, ProcessorResult> {
		@Override
		public ProcessorResult unmarshal(ProcessorResultImpl procResultImpl) {
			return procResultImpl;
		}

		@Override
		public ProcessorResultImpl marshal(ProcessorResult procResult) {
			return (ProcessorResultImpl) procResult;
		}
	}

	private static EnumMap<TaskType, TaskResult> resMap(TaskResult result) {
		EnumMap<TaskType, TaskResult> resultMap = new EnumMap<>(TaskType.class);
		if (result != null && result.getType() != null)
			resultMap.put(result.getType(), result);
		return resultMap;
	}
}
