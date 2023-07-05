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

import org.verapdf.ReleaseDetails;
import org.verapdf.component.ComponentDetails;
import org.verapdf.component.Components;
import org.verapdf.core.EncryptedPdfException;
import org.verapdf.core.ModelParsingException;
import org.verapdf.core.ValidationException;
import org.verapdf.core.VeraPDFException;
import org.verapdf.core.utils.FileOutputMapper;
import org.verapdf.core.utils.FileOutputMappers;
import org.verapdf.features.AbstractFeaturesExtractor;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureExtractorConfig;
import org.verapdf.metadata.fixer.FixerFactory;
import org.verapdf.pdfa.*;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.MetadataFixerResultImpl;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.profiles.Profiles;
import org.verapdf.pdfa.validation.validators.ValidatorConfig;
import org.verapdf.processor.plugins.PluginsCollectionConfig;
import org.verapdf.processor.reports.ItemDetails;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class is implementation of {@link Processor} interface
 *
 * @author Sergey Shemyakov
 */
final class ProcessorImpl implements ItemProcessor {
	private static final FileOutputMapper defautMdFixMapper = FileOutputMappers
			.verSibFiles(FixerFactory.defaultConfig().getFixesPrefix());
	private static final ComponentDetails defaultDetails = Components
			.libraryDetails(URI.create("http://pdfa.verapdf.org/processors#default"), "VeraPDF Processor"); //$NON-NLS-1$//$NON-NLS-2$
	private static final Logger logger = Logger.getLogger(ProcessorImpl.class.getCanonicalName());
	private static VeraPDFFoundry foundry = Foundries.defaultInstance();

	private final ProcessorConfig processorConfig;
	private final ComponentDetails details;
	private final FileOutputMapper mdFixMapper;

	private final List<String> errors = new ArrayList<>();
	private final EnumMap<TaskType, TaskResult> taskResults = new EnumMap<>(TaskType.class);
	private ValidationResult validationResult = ValidationResults.defaultResult();
	private FeatureExtractionResult featureResult = new FeatureExtractionResult();
	private MetadataFixerResult fixerResult = new MetadataFixerResultImpl.Builder().build();

	private ProcessorImpl(final ProcessorConfig config, final ComponentDetails details,
			final FileOutputMapper mdFixMapper) {
		super();
		this.processorConfig = config;
		this.details = details;
		this.mdFixMapper = mdFixMapper;
	}

	@Override
	public ComponentDetails getDetails() {
		return this.details;
	}

	@Override
	public ProcessorConfig getConfig() {
		return this.processorConfig;
	}

	private void initialise() {
		this.errors.clear();
		this.taskResults.clear();
		this.validationResult = ValidationResults.defaultResult();
		this.featureResult = new FeatureExtractionResult();
		this.fixerResult = new MetadataFixerResultImpl.Builder().build();
	}

	@Override
	public ProcessorResult process(File toProcess) throws VeraPDFException {
		this.initialise();
		if (toProcess == null) {
			throw new IllegalArgumentException("PDF file cannot be null");
		}
		ItemDetails fileDetails = ItemDetails.fromFile(toProcess);
		Components.Timer parseTimer = Components.Timer.start();
		TaskType task = null;
		String password = this.processorConfig.getValidatorConfig().getPassword();
		PDFAFlavour flavour = this.hasCustomProfile() ? this.processorConfig.getCustomProfile().getPDFAFlavour() :
				(this.isAuto() ? PDFAFlavour.NO_FLAVOUR : this.valConf().getFlavour());
		try (PDFAParser parser = foundry.createParser(toProcess, flavour, this.valConf().getDefaultFlavour(), password)) {
			for (TaskType t : this.getConfig().getTasks()) {
				task = t;
				switch (task) {
					case VALIDATE:
						validate(parser);
						break;
					case FIX_METADATA:
						fixMetadata(parser, fileDetails.getName());
						break;
					case EXTRACT_FEATURES:
						extractFeatures(parser);
						break;
					default:
						break;
				}
			}
		} catch (EncryptedPdfException e) {
			logger.log(Level.WARNING, fileDetails.getName() + " appears to be an encrypted PDF."); //$NON-NLS-1$
			logger.log(Level.FINE, "Exception details:", e); //$NON-NLS-1$
			return ProcessorResultImpl.encryptedResult(fileDetails,
					TaskResultImpl.fromValues(TaskType.PARSE, parseTimer.stop(), e));
		} catch (ModelParsingException e) {
			logger.log(Level.WARNING, fileDetails.getName() + " doesn't appear to be a valid PDF."); //$NON-NLS-1$
			logger.log(Level.FINE, "Exception details:", e); //$NON-NLS-1$
			return ProcessorResultImpl.invalidPdfResult(fileDetails,
					TaskResultImpl.fromValues(TaskType.PARSE, parseTimer.stop(), e));
		} catch (OutOfMemoryError e) {
			logger.log(Level.WARNING, "OutOfMemory caught when validating item"); //$NON-NLS-1$
			logger.log(Level.FINE, "Exception details:", e); //$NON-NLS-1$
			return ProcessorResultImpl.outOfMemoryResult(fileDetails,
					TaskResultImpl.fromValues(task, parseTimer.stop(), new VeraPDFException("OutOfMemory caught when validating item", e)));
		} catch (IOException excep) {
			logger.log(Level.FINER, "Problem closing PDF Stream", excep); //$NON-NLS-1$
		} catch (Exception e) {
			logger.log(Level.WARNING, fileDetails.getName() + " doesn't appear to be a valid PDF."); //$NON-NLS-1$
			logger.log(Level.FINE, "Exception details:", e); //$NON-NLS-1$
			return ProcessorResultImpl.veraExceptionResult(fileDetails,
			       TaskResultImpl.fromValues(TaskType.PARSE, parseTimer.stop(),
			       new VeraPDFException("Caught unexpected exception during parsing", e))); //$NON-NLS-1$
		}
		return ProcessorResultImpl.fromValues(fileDetails, this.taskResults, this.validationResult, this.featureResult,
				this.fixerResult);
	}

	@Override
	public ProcessorResult process(ItemDetails fileDetails, InputStream pdfFileStream) {
		this.initialise();
		checkArguments(pdfFileStream, fileDetails, this.processorConfig);
		Components.Timer parseTimer = Components.Timer.start();
		String password = this.processorConfig.getValidatorConfig().getPassword();
		TaskType task = null;
		PDFAFlavour flavour = this.hasCustomProfile() ? this.processorConfig.getCustomProfile().getPDFAFlavour() :
				(this.isAuto() ? PDFAFlavour.NO_FLAVOUR : this.valConf().getFlavour());
		try (PDFAParser parser = foundry.createParser(pdfFileStream, flavour, this.valConf().getDefaultFlavour(), password)) {
			for (TaskType t : this.getConfig().getTasks()) {
				task = t;
				switch (task) {
				case VALIDATE:
					validate(parser);
					break;
				case FIX_METADATA:
					fixMetadata(parser, fileDetails.getName());
					break;
				case EXTRACT_FEATURES:
					extractFeatures(parser);
					break;
				default:
					break;

				}
			}
		} catch (EncryptedPdfException e) {
			logger.log(Level.WARNING, fileDetails.getName() + " appears to be an encrypted PDF."); //$NON-NLS-1$
			logger.log(Level.FINE, "Exception details:", e); //$NON-NLS-1$
			return ProcessorResultImpl.encryptedResult(fileDetails,
					TaskResultImpl.fromValues(TaskType.PARSE, parseTimer.stop(), e));
		} catch (ModelParsingException e) {
			logger.log(Level.WARNING, fileDetails.getName() + " doesn't appear to be a valid PDF."); //$NON-NLS-1$
			logger.log(Level.FINE, "Exception details:", e); //$NON-NLS-1$
			return ProcessorResultImpl.invalidPdfResult(fileDetails,
					TaskResultImpl.fromValues(TaskType.PARSE, parseTimer.stop(), e));
		} catch (OutOfMemoryError e) {
			logger.log(Level.WARNING, "OutOfMemory caught when validating item"); //$NON-NLS-1$
			logger.log(Level.FINE, "Exception details:", e); //$NON-NLS-1$
			return ProcessorResultImpl.outOfMemoryResult(fileDetails,
					TaskResultImpl.fromValues(task, parseTimer.stop(), new VeraPDFException("OutOfMemory caught when validating item", e)));
		} catch (IOException excep) {
			logger.log(Level.FINER, "Problem closing PDF Stream", excep); //$NON-NLS-1$
		}  catch (Exception e) {
			logger.log(Level.WARNING, fileDetails.getName() + " doesn't appear to be a valid PDF."); //$NON-NLS-1$
			logger.log(Level.FINE, "Exception details:", e); //$NON-NLS-1$
			return ProcessorResultImpl.veraExceptionResult(fileDetails,
			       TaskResultImpl.fromValues(TaskType.PARSE, parseTimer.stop(),
			       new VeraPDFException("Caught unexpected exception during parsing", e))); //$NON-NLS-1$
		}
		return ProcessorResultImpl.fromValues(fileDetails, this.taskResults, this.validationResult, this.featureResult,
				this.fixerResult);
	}

	private boolean isAuto() {
		return (this.valConf().getFlavour() == PDFAFlavour.NO_FLAVOUR)
				&& (this.processorConfig.getCustomProfile() == Profiles.defaultProfile());
	}

	private static void checkArguments(InputStream pdfFileStream, ItemDetails fileDetails, ProcessorConfig config) {
		if (pdfFileStream == null) {
			throw new IllegalArgumentException("PDF file stream cannot be null"); //$NON-NLS-1$
		}
		if (config == null) {
			throw new IllegalArgumentException("Config cannot be null"); //$NON-NLS-1$
		}
		// FIXME FAST
		if (config.hasTask(TaskType.VALIDATE) && config.getValidatorConfig().getFlavour() == PDFAFlavour.NO_FLAVOUR
				&& config.getValidatorConfig().toString().equals("")) { //$NON-NLS-1$
			throw new IllegalArgumentException("Validation cannot be started with no chosen validation profile"); //$NON-NLS-1$
		}
		if (fileDetails == null) {
			throw new IllegalArgumentException("Item details cannot be null"); //$NON-NLS-1$
		}
	}

	private void validate(final PDFAParser parser) {
		TaskType type = TaskType.VALIDATE;
		Components.Timer timer = Components.Timer.start();

		try (PDFAValidator validator = validator(parser.getFlavour())) {
			this.validationResult = validator.validate(parser);
			this.taskResults.put(type, TaskResultImpl.fromValues(type, timer.stop()));
		} catch (ValidationException excep) {
			logger.log(Level.WARNING, "Exception caught when validating item", excep); //$NON-NLS-1$
			this.taskResults.put(type, TaskResultImpl.fromValues(type, timer.stop(), excep));
		} catch (IOException excep) {
			logger.log(Level.INFO, "IOException closing validator.", excep); //$NON-NLS-1$
		}
	}

	private PDFAValidator validator(PDFAFlavour parsedFlavour) {
		PDFAFlavour flavour = Foundries.defaultInstance().defaultFlavour();
		if (this.isAuto()) {
			if (parsedFlavour != PDFAFlavour.NO_FLAVOUR)
				flavour = parsedFlavour;
		} else {
			flavour = this.valConf().getFlavour();
		}
		if (this.hasCustomProfile())
			return foundry.createValidator(this.valConf(), this.processorConfig.getCustomProfile());
		return foundry.createValidator(this.valConf(), flavour);
	}

	private boolean hasCustomProfile() {
		return this.processorConfig.getCustomProfile() != Profiles.defaultProfile();
	}

	private void fixMetadata(final PDFAParser parser, final String fileName) {
		TaskType type = TaskType.FIX_METADATA;
		Components.Timer timer = Components.Timer.start();
		File orig = new File(fileName);
		if (!orig.isFile()) {
			// FIXME: This needs to handle the unnamed input stream case.
		}
		File fxfl;
		try {
			fxfl = this.mdFixMapper.mapFile(orig);
		} catch (VeraPDFException excep) {
			this.taskResults.put(type, TaskResultImpl.fromValues(type, timer.stop(), excep));
			return;
		}
		MetadataFixerResult.RepairStatus rpStat = MetadataFixerResult.RepairStatus.NO_ACTION;
		try (OutputStream fxos = new BufferedOutputStream(new FileOutputStream(fxfl))) {
			MetadataFixer fixer = foundry.createMetadataFixer();
			this.fixerResult = fixer.fixMetadata(parser, fxos, this.validationResult);
			rpStat = this.fixerResult.getRepairStatus();
			this.taskResults.put(type, TaskResultImpl.fromValues(type, timer.stop()));
		} catch (IOException excep) {
			this.taskResults.put(type, TaskResultImpl.fromValues(type, timer.stop(),
					new VeraPDFException("Processing exception in metadata fixer", excep))); //$NON-NLS-1$
		}

		if (rpStat != MetadataFixerResult.RepairStatus.SUCCESS && rpStat != MetadataFixerResult.RepairStatus.ID_REMOVED) {
			if (!fxfl.delete()) {
				fxfl.deleteOnExit();
			}
		}
	}

	private void extractFeatures(PDFAParser parser) {
		Components.Timer timer = Components.Timer.start();
		try {
			this.featureResult = parser.getFeatures(this.featConf(), this.getPlugins());
			this.taskResults.put(TaskType.EXTRACT_FEATURES,
					TaskResultImpl.fromValues(TaskType.EXTRACT_FEATURES, timer.stop()));
		} catch (Throwable e) {
			logger.log(Level.WARNING, "Exception caught when extracting features of item", e); //$NON-NLS-1$
			VeraPDFException veraExcep = new VeraPDFException("Exception caught when extracting features of item", e); //$NON-NLS-1$
			this.taskResults.put(TaskType.EXTRACT_FEATURES,
					TaskResultImpl.fromValues(TaskType.EXTRACT_FEATURES, timer.stop(), veraExcep));
		}
	}

	private List<AbstractFeaturesExtractor> getPlugins() {
		PluginsCollectionConfig pluginsCollectionConfig = this.processorConfig.getPluginsCollectionConfig();
		return FeaturesPluginsLoader.loadExtractors(pluginsCollectionConfig);
	}

	static ItemProcessor newProcessor(final ProcessorConfig config) {
		return newProcessor(config, defaultDetails);
	}

	static ItemProcessor newProcessor(final ProcessorConfig config, final ComponentDetails details) {
		FileOutputMapper mapper = defautMdFixMapper;
		// FIXME: this is hacky
		if (isMdFolder(config.getMetadataFolder())) {
			mapper = FileOutputMappers.verFold(config.getMetadataFolder(), config.getFixerConfig().getFixesPrefix());
		} else {
			mapper = FileOutputMappers.verSibFiles(config.getFixerConfig().getFixesPrefix());
		}
		return newProcessor(config, details, mapper);
	}

	static ItemProcessor newProcessor(final ProcessorConfig config, final ComponentDetails details,
			final FileOutputMapper mdFixMapper) {
		return new ProcessorImpl(config, details, mdFixMapper);
	}

	private ValidatorConfig valConf() {
		return this.processorConfig.getValidatorConfig();
	}

	private FeatureExtractorConfig featConf() {
		return this.processorConfig.getFeatureConfig();
	}

	@Override
	public Collection<ReleaseDetails> getDependencies() {
		return ReleaseDetails.getDetails();
	}

	static private boolean isMdFolder(final String mdFolder) {
		if (mdFolder == null) return false;
		if (mdFolder.isEmpty()) return false;
		return ! mdFolder.equals(ProcessorConfigImpl.defaultInstance().getMetadataFolder());
	}
	@Override
	public void close() {
		/**
		 * Empty
		 */
	}
}
