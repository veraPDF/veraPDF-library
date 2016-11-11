package org.verapdf.processor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.verapdf.ReleaseDetails;
import org.verapdf.component.ComponentDetails;
import org.verapdf.component.Components;
import org.verapdf.core.EncryptedPdfException;
import org.verapdf.core.ModelParsingException;
import org.verapdf.core.ValidationException;
import org.verapdf.core.VeraPDFException;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureExtractorConfig;
import org.verapdf.metadata.fixer.MetadataFixerConfig;
import org.verapdf.metadata.fixer.utils.FileGenerator;
import org.verapdf.pdfa.Foundries;
import org.verapdf.pdfa.MetadataFixer;
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.VeraPDFFoundry;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.MetadataFixerResultImpl;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.profiles.Profiles;
import org.verapdf.pdfa.validation.validators.ValidatorConfig;
import org.verapdf.report.ItemDetails;

/**
 * Class is implementation of {@link Processor} interface
 *
 * @author Sergey Shemyakov
 */
final class ProcessorImpl implements ItemProcessor {
	private static final ComponentDetails defaultDetails = Components
			.libraryDetails(URI.create("http://pdfa.verapdf.org/processors#default"), "VeraPDF Processor");
	private static final Logger logger = Logger.getLogger(ProcessorImpl.class.getCanonicalName());
	private static VeraPDFFoundry foundry = Foundries.defaultInstance();

	private final ProcessorConfig processorConfig;
	private final ComponentDetails details;

	private final List<String> errors = new ArrayList<>();
	private final EnumMap<TaskType, TaskResult> taskResults = new EnumMap<>(TaskType.class);
	private ValidationResult validationResult = ValidationResults.defaultResult();
	private FeatureExtractionResult featureResult = new FeatureExtractionResult();
	private MetadataFixerResult fixerResult = new MetadataFixerResultImpl.Builder().build();

	private ProcessorImpl(final ProcessorConfig config) {
		this(config, defaultDetails);
	}

	private ProcessorImpl(final ProcessorConfig config, final ComponentDetails details) {
		super();
		this.processorConfig = config;
		this.details = details;
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
		ProcessorResult retVal = ProcessorResultImpl.defaultInstance();
		try (InputStream fis = new FileInputStream(toProcess)) {
			retVal = this.process(ItemDetails.fromFile(toProcess), fis);
		} catch (FileNotFoundException excep) {
			throw new VeraPDFException("Couldn't find file: " + toProcess.getPath() + " to process.", excep);
		} catch (IOException excep) {
			logger.log(Level.INFO, "Problem closing file:" + toProcess, excep);
		}
		return retVal;
	}

	@Override
	public ProcessorResult process(ItemDetails fileDetails, InputStream pdfFileStream) {
		this.initialise();
		checkArguments(pdfFileStream, fileDetails, this.processorConfig);
		try (PDFAParser parser = this.hasCustomProfile()
				? foundry.createParser(pdfFileStream, this.processorConfig.getCustomProfile().getPDFAFlavour())
				: this.isAuto() ? foundry.createParser(pdfFileStream)
						: foundry.createParser(pdfFileStream, this.valConf().getFlavour())) {
			if (!isAuto() || parser.getFlavour() != PDFAFlavour.NO_FLAVOUR) {
				for (TaskType task : this.getConfig().getTasks()) {
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
			} else {
				// DEAL WITH AUTO CASE WHEN PARSER RETURNS NO FLAVOUR
				logger.log(Level.SEVERE, "Item:" + fileDetails.getName() + " does not appear to be a valid PDF/A.");
			}
		} catch (EncryptedPdfException e) {
			logger.log(Level.WARNING, fileDetails.getName() + " appears to be an encrypted PDF.", e);
			return ProcessorResultImpl.encryptedResult(fileDetails);
		} catch (ModelParsingException e) {
			logger.log(Level.WARNING, fileDetails.getName() + " doesn't appear to be a valid PDF.", e);
			return ProcessorResultImpl.invalidPdfResult(fileDetails);
		} catch (IOException excep) {
			logger.log(Level.FINER, "Problem closing PDF Stream", excep);
		}

		return ProcessorResultImpl.fromValues(fileDetails, this.taskResults, this.validationResult, this.featureResult,
				this.fixerResult);
	}

	private boolean isAuto() {
		return (this.valConf().getFlavour() == PDFAFlavour.NO_FLAVOUR) && (this.processorConfig.getCustomProfile() == Profiles.defaultProfile());
	}

	private static void checkArguments(InputStream pdfFileStream, ItemDetails fileDetails, ProcessorConfig config) {
		if (pdfFileStream == null) {
			throw new IllegalArgumentException("PDF file stream cannot be null");
		}
		if (config == null) {
			throw new IllegalArgumentException("Config cannot be null");
		}
		// FIXME FAST
		if (config.hasTask(TaskType.VALIDATE) && config.getValidatorConfig().getFlavour() == PDFAFlavour.NO_FLAVOUR
				&& config.getValidatorConfig().toString().equals("")) {
			throw new IllegalArgumentException("Validation cannot be started with no chosen validation profile");
		}
		if (fileDetails == null) {
			throw new IllegalArgumentException("Item details cannot be null");
		}
	}

	private void validate(final PDFAParser parser) {
		TaskType type = TaskType.VALIDATE;
		Components.Timer timer = Components.Timer.start();

		try (PDFAValidator validator = this.isAuto() ? validator(parser.getFlavour()) : validator()) {
			this.validationResult = validator.validate(parser);
			this.taskResults.put(type, TaskResultImpl.fromValues(type, timer.stop()));
		} catch (ValidationException excep) {
			logger.log(Level.WARNING, "Exception caught when validaing item", excep);
			this.taskResults.put(type, TaskResultImpl.fromValues(type, timer.stop(), excep));
		} catch (IOException excep) {
			logger.log(Level.INFO, "IOException closing validator.", excep);
		}
	}

	private PDFAValidator validator() {
		return validator(this.valConf().getFlavour());
	}

	private PDFAValidator validator(PDFAFlavour flavour) {
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
		try {
			Path path = FileSystems.getDefault().getPath("");
			String prefix = this.fixConf().getFixesPrefix();
			File tempFile = File.createTempFile("fixedTempFile", ".pdf");
			tempFile.deleteOnExit();
			try (OutputStream tempOutput = new BufferedOutputStream(new FileOutputStream(tempFile))) {
				MetadataFixer fixer = foundry.createMetadataFixer();
				this.fixerResult = fixer.fixMetadata(parser, tempOutput, this.validationResult);
				MetadataFixerResult.RepairStatus repairStatus = this.fixerResult.getRepairStatus();
				if (repairStatus == MetadataFixerResult.RepairStatus.SUCCESS
						|| repairStatus == MetadataFixerResult.RepairStatus.ID_REMOVED) {
					File resFile;
					boolean flag = true;
					while (flag) {
						if (!path.toString().trim().isEmpty()) {
							resFile = FileGenerator.createOutputFile(path.toFile(), new File(fileName).getName(),
									prefix);
						} else {
							resFile = FileGenerator.createOutputFile(new File(fileName), prefix);
						}
						Files.copy(tempFile.toPath(), resFile.toPath());
						flag = false;
					}
				}
			}
			this.taskResults.put(type, TaskResultImpl.fromValues(type, timer.stop()));
		} catch (IOException excep) {
			this.taskResults.put(type, TaskResultImpl.fromValues(type, timer.stop(),
					new VeraPDFException("Processing exception in metadata fixer", excep)));
		}
	}

	private void extractFeatures(PDFAParser parser) {
		Components.Timer timer = Components.Timer.start();
		this.featureResult = parser.getFeatures(this.featConf());
		this.taskResults.put(TaskType.EXTRACT_FEATURES,
				TaskResultImpl.fromValues(TaskType.EXTRACT_FEATURES, timer.stop()));
	}

	static ItemProcessor newProcessor(final ProcessorConfig config) {
		return new ProcessorImpl(config);
	}

	static ItemProcessor newProcessor(final ProcessorConfig config, final ComponentDetails details) {
		return new ProcessorImpl(config, details);
	}

	private ValidatorConfig valConf() {
		return this.processorConfig.getValidatorConfig();
	}

	private MetadataFixerConfig fixConf() {
		return this.processorConfig.getFixerConfig();
	}

	private FeatureExtractorConfig featConf() {
		return this.processorConfig.getFeatureConfig();
	}

	@Override
	public Collection<ReleaseDetails> getDependencies() {
		return ReleaseDetails.getDetails();
	}

	@Override
	public void close() {
		/**
		 * Empty
		 */
	}
}
