package org.verapdf.processor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.verapdf.component.ComponentDetails;
import org.verapdf.component.Components;
import org.verapdf.core.EncryptedPdfException;
import org.verapdf.core.ModelParsingException;
import org.verapdf.core.ValidationException;
import org.verapdf.core.VeraPDFException;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureExtractorConfig;
import org.verapdf.metadata.fixer.utils.FileGenerator;
import org.verapdf.pdfa.Foundries;
import org.verapdf.pdfa.MetadataFixer;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.PDFParser;
import org.verapdf.pdfa.VeraPDFFoundry;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.MetadataFixerResultImpl;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.profiles.Profiles;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;
import org.verapdf.report.ItemDetails;

/**
 * Class is implementation of {@link Processor} interface
 *
 * @author Sergey Shemyakov
 */
class ProcessorImpl implements VeraProcessor {
	private static final ComponentDetails defaultDetails = Components
			.libraryDetails(URI.create("http://pdfa.verapdf.org/processors#default"), "VeraPDF Processor");
	private static final Logger logger = Logger.getLogger(ProcessorImpl.class.getName());
	private static VeraPDFFoundry foundry = Foundries.defaultInstance();

	private final ProcessorConfig processorConfig;
	private final ComponentDetails details;

	private final List<String> errors = new ArrayList<>();
	private final EnumMap<TaskType, TaskResult> results = new EnumMap<>(TaskType.class);
	private ValidationResult validationResult = ValidationResults.defaultResult();
	private FeatureExtractionResult featureResult = new FeatureExtractionResult();
	private MetadataFixerResult fixerResult = new MetadataFixerResultImpl.Builder().build();

	private String parsingErrorMessage;

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
		this.results.clear();
		this.validationResult = ValidationResults.defaultResult();
		this.featureResult = new FeatureExtractionResult();
		this.fixerResult = new MetadataFixerResultImpl.Builder().build();
	}

	@Override
	public ProcessorResult process(ItemDetails fileDetails, InputStream pdfFileStream) {
		this.initialise();
		checkArguments(pdfFileStream, fileDetails, this.processorConfig);
		this.parsingErrorMessage = null;
		ValidationProfile profile = Profiles.getVeraProfileDirectory()
				.getValidationProfileByFlavour(this.processorConfig.getValidatorConfig().getFlavour());
		PDFAFlavour currentFlavour = profile == Profiles.defaultProfile()
				? this.processorConfig.getValidatorConfig().getFlavour() : profile.getPDFAFlavour();

		try (PDFParser parser = foundry.createParser(pdfFileStream, currentFlavour)) {
			if (profile == Profiles.defaultProfile()) {
				profile = Profiles.getVeraProfileDirectory().getValidationProfileByFlavour(parser.getFlavour());
			}
			for (TaskType task : this.getConfig().getTasks()) {
				switch (task) {
				case VALIDATE:
					validate(parser, profile);
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
		} catch (EncryptedPdfException | ModelParsingException e) {
			this.parsingErrorMessage = (e instanceof EncryptedPdfException)
					? "ERROR: " + fileDetails.getName() + " is an encrypted PDF file."
					: "ERROR: " + fileDetails.getName() + " is not a PDF format file.";
			logger.log(Level.INFO, this.parsingErrorMessage, e);
		} catch (IOException excep) {
			logger.log(Level.FINE, "Problem closing PDF Stream", excep);
		}

		return ProcessorResultImpl.fromValues(this.results, this.validationResult, this.featureResult, this.fixerResult);
	}

	private static void checkArguments(InputStream pdfFileStream, ItemDetails fileDetails, ProcessorConfig config) {
		if (pdfFileStream == null) {
			throw new IllegalArgumentException("PDF file stream cannot be null");
		}
		if (config == null) {
			throw new IllegalArgumentException("Config cannot be null");
		}
		if (config.hasTask(TaskType.VALIDATE) && config.getValidatorConfig().getFlavour() == PDFAFlavour.NO_FLAVOUR
				&& config.getValidatorConfig().getProfile().toString().equals("")) {
			throw new IllegalArgumentException("Validation cannot be started with no chosen validation profile");
		}
		if (fileDetails == null) {
			throw new IllegalArgumentException("Item details cannot be null");
		}
	}

	private void validate(final PDFParser parser, final ValidationProfile profile) {
		Components.Timer timer = Components.Timer.start();
		PDFAValidator validator = foundry.createFailFastValidator(profile,
				this.processorConfig.getValidatorConfig().getMaxFails());
		try {
			this.validationResult = validator.validate(parser);
			this.results.put(TaskType.VALIDATE, TaskResultImpl.fromValues(timer.stop()));
		} catch (ValidationException excep) {
			this.results.put(TaskType.VALIDATE, TaskResultImpl.fromValues(timer.stop(), excep));
		}
	}

	private void fixMetadata(final PDFParser parser, final String fileName) {
		Components.Timer timer = Components.Timer.start();
		try {
			Path path = FileSystems.getDefault().getPath("");
			String prefix = this.getConfig().getFixerConfig().getFixesPrefix();
			File tempFile = File.createTempFile("fixedTempFile", ".pdf");
			tempFile.deleteOnExit();
			try (OutputStream tempOutput = new BufferedOutputStream(new FileOutputStream(tempFile))) {
				MetadataFixer fixer = foundry.newMetadataFixer();
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
			this.results.put(TaskType.FIX_METADATA, TaskResultImpl.fromValues(timer.stop()));
		} catch (IOException excep) {
			this.results.put(TaskType.FIX_METADATA, TaskResultImpl.fromValues(timer.stop(),
					new VeraPDFException("Processing exception in metadata fixer", excep)));
		}
	}

	private void extractFeatures(PDFParser parser) {
		Components.Timer timer = Components.Timer.start();
		FeatureExtractorConfig featuresConfig = this.getConfig().getFeatureConfig();
		this.featureResult = parser.getFeatures(featuresConfig);
		this.results.put(TaskType.EXTRACT_FEATURES, TaskResultImpl.fromValues(timer.stop()));
	}


	static VeraProcessor newProcessor(final ProcessorConfig config) {
		return new ProcessorImpl(config);
	}

	static VeraProcessor newProcessor(final ProcessorConfig config, final ComponentDetails details) {
		return new ProcessorImpl(config, details);
	}
}
