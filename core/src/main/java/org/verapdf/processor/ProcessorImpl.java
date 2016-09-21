package org.verapdf.processor;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.verapdf.core.ModelParsingException;
import org.verapdf.core.ValidationException;
import org.verapdf.features.FeaturesExtractor;
import org.verapdf.features.config.FeaturesConfig;
import org.verapdf.features.tools.FeaturesCollection;
import org.verapdf.metadata.fixer.impl.MetadataFixerImpl;
import org.verapdf.metadata.fixer.impl.pb.FixerConfigImpl;
import org.verapdf.metadata.fixer.utils.FileGenerator;
import org.verapdf.metadata.fixer.utils.FixerConfig;
import org.verapdf.model.ModelParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.RuleId;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.pdfa.validators.Validators;
import org.verapdf.processor.config.Config;
import org.verapdf.processor.config.FormatOption;
import org.verapdf.processor.config.ProcessingType;
import org.verapdf.report.*;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Class is implementation of {@link Processor} interface
 *
 * @author Sergey Shemyakov
 */
public class ProcessorImpl implements Processor {

	private static final Logger LOGGER = Logger.getLogger(ProcessorImpl.class);

	private ProcessingResult processingResult;
	private String parsingErrorMessage;
	private Throwable validationExeption;
	private Throwable metadataFixingExeption;
	private Throwable featuresExeption;

	@Override
	public ProcessingResult validate(InputStream pdfFileStream, ItemDetails fileDetails, Config config,
			OutputStream reportOutputStream) {
		long startTimeOfProcessing = System.currentTimeMillis();
		checkArguments(pdfFileStream, fileDetails, config, reportOutputStream);
		ValidationResult validationResult = null;
		MetadataFixerResult fixerResult = null;
		FeaturesCollection featuresCollection = null;
		this.processingResult = new ProcessingResult(config);
		this.parsingErrorMessage = null;
		ValidationProfile validationProfile = profileFromConfig(config);
		PDFAFlavour currentFlavour = validationProfile == null ? config.getFlavour()
				: validationProfile.getPDFAFlavour();

		ProcessingType processingType = config.getProcessingType();
		try (ModelParser parser = ModelParser.createModelWithFlavour(pdfFileStream, currentFlavour)) {
			if (processingType.isValidating()) {
				try {
					if (validationProfile == null) {
						validationProfile = profileFromFlavour(parser.getFlavour());
					}
					validationResult = startValidation(validationProfile, parser, config);
				} catch (Throwable e) {
					LOGGER.debug("Error in validation", e);
					setUnsuccessfulValidation(e);
					this.processingResult.addErrorMessage(e.getMessage());
				}
				if (config.isFixMetadata() && validationResult != null) {
					try {
						fixerResult = fixMetadata(validationResult, parser, fileDetails.getName(), config);
					} catch (Throwable e) {
						LOGGER.debug("Error in metadata fixing", e);
						setUnsuccessfulMetadataFixing(e);
						this.processingResult.addErrorMessage(e.getMessage());
					}
				}
			}
			if (processingType.isFeatures()) {
				try {
					featuresCollection = extractFeatures(parser, config);
				} catch (Throwable e) {
					LOGGER.debug("Error in features collecting", e);
					setUnsuccessfulFeatureExtracting(e);
					this.processingResult.addErrorMessage(e.getMessage());
				}
			}
		} catch (ModelParsingException e) {
			if (e.getCause() instanceof InvalidPasswordException) {
				this.parsingErrorMessage = "ERROR: " + fileDetails.getName() + " is an encrypted PDF file.";
			} else {
				this.parsingErrorMessage = "ERROR: " + fileDetails.getName() + " is not a PDF format file.";
			}
			LOGGER.debug(this.parsingErrorMessage, e);
			setUnsuccessfulValidation(e);
			setUnsuccessfulMetadataFixing(e);
			setUnsuccessfulFeatureExtracting(e);
		}

		long endTimeOfProcessing = System.currentTimeMillis();
		writeReport(config, validationResult, fileDetails, reportOutputStream, validationProfile, fixerResult,
				featuresCollection, endTimeOfProcessing - startTimeOfProcessing);

		return this.processingResult;
	}

	private static void checkArguments(InputStream pdfFileStream, ItemDetails fileDetails, Config config,
			OutputStream reportOutputStream) {
		if (pdfFileStream == null) {
			throw new IllegalArgumentException("PDF file stream cannot be null");
		}
		if (config == null) {
			throw new IllegalArgumentException("Config cannot be null");
		}
		if (reportOutputStream == null) {
			throw new IllegalArgumentException("Output stream for report cannot be null");
		}
		if (config.getProcessingType().isValidating() && config.getFlavour() == PDFAFlavour.NO_FLAVOUR
				&& config.getValidationProfile().toString().equals("")) {
			throw new IllegalArgumentException("Validation cannot be started with no chosen validation profile");
		}
		if (fileDetails == null) {
			throw new IllegalArgumentException("Item details cannot be null");
		}
	}

	private static boolean logPassed(final Config config) {
		return (config.getReportType() != FormatOption.XML) || config.isShowPassedRules();
	}

	ValidationProfile profileFromConfig(final Config config) {
		try {
			if (config.getValidationProfile().toString().equals("")) {
				return null;
			}
			ValidationProfile profile = profileFromFile(config.getValidationProfile().toFile());
			return profile;
		} catch (JAXBException e) {
			LOGGER.debug("Error in parsing profile XML", e);
			this.processingResult.addErrorMessage("Error in parsing profile from XML: " + e.getMessage());
			setUnsuccessfulValidation(e);
			setUnsuccessfulMetadataFixing(e);
			return Profiles.defaultProfile();
		} catch (IOException e) {
			LOGGER.debug("Error in reading profile from disc", e);
			this.processingResult.addErrorMessage("Error in reading profile from disc: " + e.getMessage());
			setUnsuccessfulValidation(e);
			setUnsuccessfulMetadataFixing(e);
			return Profiles.defaultProfile();
		}
	}

	private static ValidationProfile profileFromFile(final File profileFile) throws JAXBException, IOException {
		try (InputStream is = new FileInputStream(profileFile)) {
			ValidationProfile profile = Profiles.profileFromXml(is);
			is.close();
			// TODO: why should we check this?
			if ("sha-1 hash code".equals(profile.getHexSha1Digest())) {
				return Profiles.defaultProfile();
			}
			return profile;
		}
	}

	private ValidationResult startValidation(ValidationProfile validationProfile, ModelParser parser, Config config) {
		PDFAValidator validator = Validators.createValidator(validationProfile, logPassed(config),
				config.getMaxNumberOfFailedChecks());
		ValidationResult validationResult = validate(validator, parser);
		return validationResult;
	}

	private MetadataFixerResult fixMetadata(ValidationResult info, ModelParser parser, String fileName, Config config) {
		try {
			FixerConfig fixerConfig = FixerConfigImpl.getFixerConfig(parser.getPDDocument(), info);
			Path path = config.getFixMetadataFolder();
			File tempFile = File.createTempFile("fixedTempFile", ".pdf");
			tempFile.deleteOnExit();
			try (OutputStream tempOutput = new BufferedOutputStream(new FileOutputStream(tempFile))) {
				MetadataFixerResult fixerResult = MetadataFixerImpl.fixMetadata(tempOutput, fixerConfig);
				MetadataFixerResult.RepairStatus repairStatus = fixerResult.getRepairStatus();
				if (repairStatus == MetadataFixerResult.RepairStatus.SUCCESS
						|| repairStatus == MetadataFixerResult.RepairStatus.ID_REMOVED) {
					File resFile;
					boolean flag = true;
					while (flag) {
						if (!path.toString().trim().isEmpty()) {
							resFile = FileGenerator.createOutputFile(path.toFile(), new File(fileName).getName(),
									config.getMetadataFixerPrefix());
						} else {
							resFile = FileGenerator.createOutputFile(new File(fileName),
									config.getMetadataFixerPrefix());
						}
						Files.copy(tempFile.toPath(), resFile.toPath());
						flag = false;
					}
				}
				return fixerResult;
			}
		} catch (IOException e) {
			LOGGER.debug("Error in fixing metadata", e);
			setUnsuccessfulMetadataFixing(e);
			this.processingResult.addErrorMessage("Error in fixing metadata: " + e.getMessage());
			return null;
		}
	}

	private FeaturesCollection extractFeatures(ModelParser parser, Config config)
			throws FileNotFoundException, JAXBException {
		FeaturesConfig featuresConfig = getFeaturesConfig(config);
		List<FeaturesExtractor> extractors = FeaturesPluginsLoader.loadExtractors(config.getPluginsConfigFilePath(),
				this.processingResult);
		FeaturesCollection featuresCollection = parser.getFeatures(featuresConfig, extractors);
		return featuresCollection;
	}

	private static FeaturesConfig getFeaturesConfig(Config config) throws FileNotFoundException, JAXBException {
		Path featuresConfigFilePath = config.getFeaturesConfigFilePath();
		FeaturesConfig featuresConfig = new FeaturesConfig.Builder().build();
		if (!featuresConfigFilePath.toString().isEmpty()) {
			File featuresConfigFile = featuresConfigFilePath.toFile();
			if (!featuresConfigFile.isFile()) throw new FileNotFoundException("File: " + featuresConfigFilePath + " could not be found.");
			if (featuresConfigFile.exists() && featuresConfigFile.canRead()) {
				try (FileInputStream fis = new FileInputStream(featuresConfigFile)) {
					featuresConfig = FeaturesConfig.fromXml(fis);
					return featuresConfig;
				} catch (IOException excep) {
					LOGGER.warn("Problem when closing config file: " + featuresConfigFilePath, excep);
				}
			}
		}

		return featuresConfig;
	}

	private static ValidationProfile profileFromFlavour(PDFAFlavour flavour) {
		try {
			ValidationProfile validationProfile = Profiles.getVeraProfileDirectory()
					.getValidationProfileByFlavour(flavour);
			return validationProfile;
		} catch (NoSuchElementException re) {
			LOGGER.warn(re);
		}
		return null;
	}

	private ValidationResult validate(PDFAValidator validator, ModelParser parser) {
		ValidationResult validationResult = null;
		try {
			validationResult = validator.validate(parser);
			if (!validationResult.isCompliant()) {
				this.processingResult.setValidationSummary(ProcessingResult.ValidationSummary.FILE_NOT_VALID);
			}
		} catch (ModelParsingException | ValidationException e) {
			LOGGER.debug("Error in validation", e);
			setUnsuccessfulValidation(e);
			setUnsuccessfulMetadataFixing(e);
			this.processingResult.addErrorMessage("Error in validation: " + e.getMessage());
		}
		return validationResult;
	}

	private void writeReport(Config config, ValidationResult validationResult, ItemDetails fileDetails,
			OutputStream reportOutputStream, ValidationProfile validationProfile, MetadataFixerResult fixerResult,
			FeaturesCollection featuresCollection, long processingTime) {
		try {
			if (config.getPolicyProfile().toString().equals("")) {
				switch (config.getReportType()) {
				case TEXT:
					writeTextReport(validationResult, fileDetails, reportOutputStream, config);
					break;
				case MRR:
				case HTML:
					writeMRR(fileDetails, validationProfile, validationResult, config, fixerResult, featuresCollection,
							processingTime, null, reportOutputStream);
					break;
				case XML:
					CliReport report = CliReport.fromValues(fileDetails, validationResult,
							FeaturesReport.fromValues(featuresCollection));
					CliReport.toXml(report, reportOutputStream, Boolean.TRUE);
					break;
				default:
					throw new IllegalStateException("Wrong or unknown report type.");
				}
			} else {
				writeMRR(fileDetails, validationProfile, validationResult, config, fixerResult, featuresCollection,
						processingTime, config.getPolicyProfile().toAbsolutePath().toString(), reportOutputStream);
			}
		} catch (IOException e) {
			LOGGER.error("Exception raised while writing report to file", e);
			this.processingResult.setReportSummary(ProcessingResult.ReportSummary.ERROR_IN_REPORT);
			this.processingResult.addErrorMessage("Error in writing report to file: " + e.getMessage());
		} catch (JAXBException e) {
			LOGGER.error("Exception raised while converting report to XML file", e);
			this.processingResult.setReportSummary(ProcessingResult.ReportSummary.ERROR_IN_REPORT);
			this.processingResult.addErrorMessage("Error in generating XML report file: " + e.getMessage());
		} catch (TransformerException e) {
			LOGGER.error("Exception raised while converting MRR report into HTML", e);
			this.processingResult.setReportSummary(ProcessingResult.ReportSummary.ERROR_IN_REPORT);
			this.processingResult.addErrorMessage("Error in converting MRR report to HTML:" + e.getMessage());
		}
	}

	private void writeTextReport(ValidationResult validationResult, ItemDetails fileDetails,
			OutputStream reportOutputStream, Config config) throws IOException {
		if (validationResult != null) {
			String reportSummary = (validationResult.isCompliant() ? "PASS " : "FAIL ") + fileDetails.getName() + "\n";
			reportOutputStream.write(reportSummary.getBytes());
			if (config.isVerboseCli()) {
				Set<RuleId> ruleIds = new HashSet<>();
				for (TestAssertion assertion : validationResult.getTestAssertions()) {
					if (assertion.getStatus() == TestAssertion.Status.FAILED) {
						ruleIds.add(assertion.getRuleId());
					}
				}
				for (RuleId id : ruleIds) {
					String reportRuleSummary = id.getClause() + "-" + id.getTestNumber() + "\n";
					reportOutputStream.write(reportRuleSummary.getBytes());
				}
			}
		} else if (this.parsingErrorMessage != null) {
			reportOutputStream.write((this.parsingErrorMessage + "\n").getBytes());
		} else if (this.validationExeption != null) {
			String reportSummary = "ERROR " + fileDetails.getName() + " " + this.validationExeption.toString() + "\n";
			reportOutputStream.write(reportSummary.getBytes());
		}
	}

	private void writeMRR(ItemDetails fileDetails, ValidationProfile validationProfile,
			ValidationResult validationResult, Config config, MetadataFixerResult fixerResult,
			FeaturesCollection featuresCollection, long processingTime, String policyProfile,
			OutputStream reportOutputStream) throws JAXBException, IOException, TransformerException {

		MachineReadableReport machineReadableReport = MachineReadableReport.fromValues(fileDetails, validationProfile,
				validationResult, config.isShowPassedRules(), config.getMaxNumberOfDisplayedFailedChecks(), fixerResult,
				featuresCollection, processingTime);
		if (this.processingResult.getValidationSummary() == ProcessingResult.ValidationSummary.ERROR_IN_VALIDATION) {
			if (this.parsingErrorMessage != null) {
				machineReadableReport
						.setErrorInValidationReport("Could not finish validation. " + this.parsingErrorMessage);
			} else if (this.validationExeption != null) {
				machineReadableReport.setErrorInValidationReport(
						"Could not finish validation. " + this.validationExeption.toString());
			} else {
				machineReadableReport.setErrorInValidationReport();
			}
		}
		if (this.processingResult.getMetadataFixerSummary() == ProcessingResult.MetadataFixingSummary.ERROR_IN_FIXING) {
			if (this.parsingErrorMessage != null) {
				machineReadableReport
						.setErrorInMetadataFixerReport("Could not finish metadata fixing. " + this.parsingErrorMessage);
			} else if (this.metadataFixingExeption != null) {
				machineReadableReport.setErrorInMetadataFixerReport(
						"Could not finish metadata fixing. " + this.metadataFixingExeption.toString());
			} else {
				machineReadableReport.setErrorInMetadataFixerReport();
			}
		}
		if (this.processingResult.getFeaturesSummary() == ProcessingResult.FeaturesSummary.ERROR_IN_FEATURES) {
			if (this.parsingErrorMessage != null) {
				machineReadableReport
						.setErrorInFeaturesReport("Could not finish features collecting. " + this.parsingErrorMessage);
			}
			if (this.featuresExeption != null) {
				machineReadableReport.setErrorInFeaturesReport(
						"Could not finish features collecting. " + this.featuresExeption.toString());
			} else {
				machineReadableReport.setErrorInFeaturesReport();
			}
		}
		if (policyProfile == null && config.getReportType() == FormatOption.MRR) {
			MachineReadableReport.toXml(machineReadableReport, reportOutputStream, Boolean.TRUE);
		} else {
			File tmp = File.createTempFile("verpdf", "xml");
			tmp.deleteOnExit();
			try (OutputStream os = new FileOutputStream(tmp)) {
				MachineReadableReport.toXml(machineReadableReport, os, Boolean.FALSE);
			}
			if (policyProfile != null) {
				try (InputStream is = new FileInputStream(tmp)) {
					Map<String, String> arguments = new HashMap<>();
					arguments.put("policyProfilePath", policyProfile);
					XsltTransformer.transform(is,
							ProcessorImpl.class.getClassLoader().getResourceAsStream("policy-example.xsl"),
							reportOutputStream, arguments);
				}
			} else if (config.getReportType() == FormatOption.HTML) {
				try (InputStream is = new FileInputStream(tmp)) {
					HTMLReport.writeHTMLReport(is, reportOutputStream, config.getProfileWikiPath(), true);
				}
			} else {
				throw new IllegalStateException("This method should be used only for MRR or HTML reports");
			}
		}
	}

	private void setUnsuccessfulValidation(Throwable e) {
		if (this.processingResult.getValidationSummary() != ProcessingResult.ValidationSummary.VALIDATION_DISABLED) {
			this.processingResult.setValidationSummary(ProcessingResult.ValidationSummary.ERROR_IN_VALIDATION);
			this.validationExeption = e;
		}
	}

	private void setUnsuccessfulMetadataFixing(Throwable e) {
		if (this.processingResult.getMetadataFixerSummary() != ProcessingResult.MetadataFixingSummary.FIXING_DISABLED) {
			this.processingResult.setMetadataFixerSummary(ProcessingResult.MetadataFixingSummary.ERROR_IN_FIXING);
			this.metadataFixingExeption = e;
		}
	}

	private void setUnsuccessfulFeatureExtracting(Throwable e) {
		if (this.processingResult.getFeaturesSummary() != ProcessingResult.FeaturesSummary.FEATURES_DISABLED) {
			this.processingResult.setFeaturesSummary(ProcessingResult.FeaturesSummary.ERROR_IN_FEATURES);
			this.featuresExeption = e;
		}
	}
}
