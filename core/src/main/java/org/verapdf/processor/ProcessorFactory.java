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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;

import javax.xml.bind.JAXBException;

import org.verapdf.component.Components;
import org.verapdf.core.VeraPDFException;
import org.verapdf.core.XmlSerialiser;
import org.verapdf.features.FeatureExtractorConfig;
import org.verapdf.metadata.fixer.MetadataFixerConfig;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;
import org.verapdf.pdfa.validation.validators.ValidatorConfig;
import org.verapdf.processor.plugins.PluginsCollectionConfig;
import org.verapdf.processor.reports.BatchSummary;
import org.verapdf.processor.reports.Reports;
import org.verapdf.processor.reports.Summarisers;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 30 Oct 2016:22:18:23
 */

public final class ProcessorFactory {
	private ProcessorFactory() {

	}

	public static ProcessorConfig defaultConfig() {
		return ProcessorConfigImpl.defaultInstance();
	}

	public static ProcessorConfig fromValues(final ValidatorConfig config, final FeatureExtractorConfig featureConfig,
			final PluginsCollectionConfig pluginsCollectionConfig, final MetadataFixerConfig fixerConfig,
			final EnumSet<TaskType> tasks) {
		return ProcessorConfigImpl.fromValues(config, featureConfig, pluginsCollectionConfig, fixerConfig, tasks);
	}

	public static ProcessorConfig fromValues(final ValidatorConfig config, final FeatureExtractorConfig featureConfig,
			final PluginsCollectionConfig pluginsCollectionConfig, final MetadataFixerConfig fixerConfig,
			final EnumSet<TaskType> tasks, final String mdFolder) {
		return ProcessorConfigImpl.fromValues(config, featureConfig, pluginsCollectionConfig, fixerConfig, tasks,
				mdFolder);
	}

	public static ProcessorConfig fromValues(final ValidatorConfig config, final FeatureExtractorConfig featureConfig,
			final PluginsCollectionConfig pluginsCollectionConfig, final MetadataFixerConfig fixerConfig,
			final EnumSet<TaskType> tasks, ValidationProfile customProfile) {
		return ProcessorConfigImpl.fromValues(config, featureConfig, pluginsCollectionConfig, fixerConfig, tasks,
				customProfile);
	}

	public static ProcessorConfig fromValues(final ValidatorConfig config, final FeatureExtractorConfig featureConfig,
			final PluginsCollectionConfig pluginsCollectionConfig, final MetadataFixerConfig fixerConfig,
			final EnumSet<TaskType> tasks, ValidationProfile customProfile, final String mdFolder) {
		return ProcessorConfigImpl.fromValues(config, featureConfig, pluginsCollectionConfig, fixerConfig, tasks,
				customProfile, mdFolder);
	}

	public static void configToXml(final ProcessorConfig toConvert, final OutputStream stream, boolean format)
			throws JAXBException {
		XmlSerialiser.toXml(toConvert, stream, format, false);
	}

	public static ProcessorConfig configFromXml(final InputStream source) throws JAXBException {
		return XmlSerialiser.typeFromXml(ProcessorConfigImpl.class, source);
	}

	public static final ItemProcessor createProcessor(final ProcessorConfig config) {
		return ProcessorImpl.newProcessor(config);
	}

	public static final BatchProcessor fileBatchProcessor(final ProcessorConfig config) {
		return new BatchFileProcessor(createProcessor(config));
	}

	public static final BatchProcessingHandler rawResultHandler() throws VeraPDFException {
		return rawResultHandler(new PrintWriter(System.out));
	}

	public static final BatchProcessingHandler rawResultHandler(final Writer dest) throws VeraPDFException {
		return RawResultHandler.newInstance(dest);
	}

	public static final BatchProcessingHandler getHandler(FormatOption option, boolean isVerbose,
			boolean logPassed) throws VeraPDFException {
		return getHandler(option, isVerbose, System.out, logPassed);
	}

	public static final BatchProcessingHandler getHandler(FormatOption option, boolean isVerbose,
			OutputStream reportStream, boolean logPassed) throws VeraPDFException {
		return getHandler(option, isVerbose, reportStream, logPassed, "");
	}

	public static final BatchProcessingHandler getHandler(FormatOption option, boolean isVerbose,
			OutputStream reportStream, boolean logPassed, String wikiPath) throws VeraPDFException {
		if (option == null)
			throw new IllegalArgumentException("Arg option can not be null"); //$NON-NLS-1$
		if (reportStream == null)
			throw new IllegalArgumentException("Arg reportStream can not be null"); //$NON-NLS-1$
		PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(reportStream, StandardCharsets.UTF_8));
		switch (option) {
		case TEXT:
			return SingleLineResultHandler.newInstance(printWriter, isVerbose, logPassed);
		case RAW:
			return rawResultHandler(printWriter);
		case MRR:
		case XML:
			return MrrHandler.newInstance(printWriter, logPassed);
		case HTML:
			return HTMLHandler.newInstance(printWriter, wikiPath, logPassed);
		case JSON:
			return new JsonHandler(printWriter, logPassed);
		default: // should not be reached
			throw new VeraPDFException("Unknown report format option: " + option); //$NON-NLS-1$
		}
	}

	public static void resultToXml(final ProcessorResult toConvert, final OutputStream stream, boolean prettyXml)
			throws JAXBException {
		XmlSerialiser.toXml(toConvert, stream, prettyXml, false);
	}

	public static void writeSingleResultReport(final ProcessorResult toConvert,final BatchProcessingHandler tmpHandler, ProcessorConfig config) throws VeraPDFException {
		tmpHandler.handleBatchStart(config);
		tmpHandler.handleResult(toConvert, config.getValidatorConfig().isLogsEnabled());
		BatchSummariser tmpSummariser = new BatchSummariser(config);
		tmpSummariser.addProcessingResult(toConvert);
		tmpHandler.handleBatchEnd(tmpSummariser.summarise());
	}

	public static ProcessorResult resultFromXml(final InputStream source) throws JAXBException {
		return XmlSerialiser.typeFromXml(ProcessorResultImpl.class, source);
	}

	public static void taskResultToXml(final TaskResult toConvert, final OutputStream dest, boolean prettyXml)
			throws JAXBException {
		XmlSerialiser.toXml(toConvert, dest, prettyXml, true);
	}

	public static TaskResult taskResultFromXml(final InputStream source) throws JAXBException {
		return XmlSerialiser.typeFromXml(TaskResultImpl.class, source);
	}

	public static final class BatchSummariser {
		private final ProcessorConfig config;
		private int totalJobs = 0;
		private int failedToParse = 0;
		private int encrypted = 0;
		private int outOfMemory = 0;
		private int veraExceptions = 0;
		private Components.Timer timer = Components.Timer.start();
		private Summarisers.ValidationSummaryBuilder validationBuilder = new Summarisers.ValidationSummaryBuilder();
		private Summarisers.FeatureSummaryBuilder featureBuilder = new Summarisers.FeatureSummaryBuilder();
		private Summarisers.RepairSummaryBuilder repairBuilder = new Summarisers.RepairSummaryBuilder();

		public BatchSummariser(ProcessorConfig config) {
			if (config == null)
				throw new IllegalArgumentException("Argument config can not be null"); //$NON-NLS-1$
			this.config = config;
		}

		public void addProcessingResult(ProcessorResult result) {
			this.totalJobs++;
			if (!result.isPdf()) this.failedToParse++;
			if (result.isEncryptedPdf()) this.encrypted++;
			if (result.isOutOfMemory()) this.outOfMemory++;
			if (result.hasException()) this.veraExceptions++;
			if (this.config.hasTask(TaskType.VALIDATE))
				this.validationBuilder.addResult(result);
			if (this.config.hasTask(TaskType.EXTRACT_FEATURES))
				this.featureBuilder.addResult(result);
			if (this.config.hasTask(TaskType.FIX_METADATA))
				this.repairBuilder.addResult(result);
		}

		public BatchSummary summarise() {
			return Reports.createBatchSummary(this.timer, this.validationBuilder.build(), this.featureBuilder.build(),
					this.repairBuilder.build(), this.totalJobs, this.failedToParse, this.encrypted, this.outOfMemory, this.veraExceptions);
		}
	}
}
