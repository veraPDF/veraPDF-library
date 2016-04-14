package org.verapdf.processor;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.verapdf.core.ValidationException;
import org.verapdf.features.pb.PBFeatureParser;
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
import org.verapdf.report.*;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 	Class is implementation of {@link Processor} interface
 *
 *  @author Sergey Shemyakov
 */
public class ProcessorImpl implements Processor {

	private static final Logger LOGGER = Logger.getLogger(ProcessorImpl.class);
	private ArrayList<Exception> exceptionsInProcessing = new ArrayList<>();

	@Override
	public ProcessingResult validate(InputStream pdfFileStream, ItemDetails fileDetails,
						 Config config, OutputStream reportOutputStream) {
		ValidationResult validationResult = null;
		MetadataFixerResult fixerResult = null;
		FeaturesCollection featuresCollection = null;
		ProcessingResult processingResult = new ProcessingResult(validationResult,
				fixerResult, featuresCollection, exceptionsInProcessing);

		PDFAValidator validator;
		ValidationProfile validationProfile;

		try {
			validationProfile = profileFromConfig(config);
		} catch (JAXBException e) {
			LOGGER.error("Error in parsing profile XML", e);
			exceptionsInProcessing.add(e);
			return processingResult;
		} catch (IOException e) {
			LOGGER.error("Error in reading profile from disc", e);
			exceptionsInProcessing.add(e);
			return processingResult;
		}
		validator = (validationProfile == Profiles.defaultProfile())
				? null : Validators.createValidator(validationProfile,
				logPassed(config), config.getMaxNumberOfFailedChecks());
		long startTimeOfValidation = System.currentTimeMillis();

		try (ModelParser toValidate = new ModelParser(pdfFileStream,
				validationProfile.getPDFAFlavour())) {
			if(config.getProcessingType().isValidating()) {
				if (validator != null) {
					validationResult = validator.validate(toValidate);
					processingResult.setValidationResult(validationResult);
					if (config.isFixMetadata()) {
						fixerResult = fixMetadata(validationResult, toValidate,
								fileDetails.getName(), config);
						processingResult.setMetadataFixerResult(fixerResult);
					}
				}
			}
			if (config.getProcessingType().isFeatures()) {
				featuresCollection = PBFeatureParser
						.getFeaturesCollection(toValidate.getPDDocument());
				processingResult.setFeaturesCollection(featuresCollection);
			}
		} catch (InvalidPasswordException e) {
			LOGGER.error("Error: " + fileDetails.getName() + " is an encrypted PDF file.", e);
			exceptionsInProcessing.add(e);
			return processingResult;
		} catch (IOException e) {
			LOGGER.error("Error: " + fileDetails.getName() + " is not a PDF format file.", e);
			exceptionsInProcessing.add(e);
			return processingResult;
		} catch (ValidationException e) {
			LOGGER.error("Exception raised while validating " + fileDetails.getName(), e);
			exceptionsInProcessing.add(e);
			return processingResult;
		}
		long endTimeOfValidation = System.currentTimeMillis();

		try {
			switch (config.getReportType()) {
				case TEXT:
					if (validationResult != null) {
						String reportSummary = (validationResult.isCompliant() ?
								"PASS " : "FAIL ") + fileDetails.getName();
						reportOutputStream.write(reportSummary.getBytes());
						if (config.isVerboseCli()) {
							Set<RuleId> ruleIds = new HashSet<>();
							for (TestAssertion assertion : validationResult.getTestAssertions()) {
								if (assertion.getStatus() == TestAssertion.Status.FAILED) {
									ruleIds.add(assertion.getRuleId());
								}
							}
							for (RuleId id : ruleIds) {
								String reportRuleSummary = id.getClause() + "-" + id.getTestNumber();
								reportOutputStream.write(reportRuleSummary.getBytes());
							}
						}
					}
					break;
				case MRR:
				case HTML:
					MachineReadableReport machineReadableReport = MachineReadableReport.fromValues(
							fileDetails,
							validator == null ? Profiles.defaultProfile()
									: validator.getProfile(), validationResult,
							config.isShowPassedRules(),
							config.getMaxNumberOfDisplayedFailedChecks(),
							fixerResult, featuresCollection,
							endTimeOfValidation - startTimeOfValidation);
					if (config.getReportType() == FormatOption.MRR) {
						MachineReadableReport.toXml(machineReadableReport,
								reportOutputStream, Boolean.TRUE);
					} else if (config.getReportType() == FormatOption.HTML) {
						File tmp = File.createTempFile("verpdf", "xml");
						tmp.deleteOnExit();
						try (OutputStream os = new FileOutputStream(tmp)) {
							MachineReadableReport.toXml(
									machineReadableReport, os, Boolean.FALSE);
						}
						try (InputStream is = new FileInputStream(tmp)) {
							HTMLReport.writeHTMLReport(is,
									reportOutputStream, config.getProfileWikiPath());
						}
					}
					break;
				case XML:
					CliReport report = CliReport.fromValues(fileDetails, validationResult,
							FeaturesReport.fromValues(featuresCollection));
					CliReport.toXml(report, reportOutputStream, Boolean.TRUE);
					break;
				default:
					exceptionsInProcessing.add(new IllegalArgumentException("Invalid report type"));
					return processingResult;
			}
		} catch (IOException e) {
			LOGGER.error("Exception raised while writing report to file", e);
			exceptionsInProcessing.add(e);
			return processingResult;
		} catch (JAXBException e) {
			LOGGER.error("Exception raised while converting report to XML file", e);
			exceptionsInProcessing.add(e);
			return processingResult;
		} catch (TransformerException e) {
			LOGGER.error("Exception raised while converting MRR report into HTML", e);
			exceptionsInProcessing.add(e);
			return processingResult;
		}
		return processingResult;
	}

	private static boolean logPassed(final Config config) {
		return (config.getReportType() != FormatOption.XML)
				|| config.isShowPassedRules();
	}

	private static ValidationProfile profileFromConfig(final Config config)
			throws JAXBException, IOException {
		if (config.getValidationProfilePath() == null) {
			return (config.getFlavour() == PDFAFlavour.NO_FLAVOUR) ? Profiles
					.defaultProfile() : Profiles.getVeraProfileDirectory()
					.getValidationProfileByFlavour(config.getFlavour());
		}
		ValidationProfile profile = profileFromFile(
				config.getValidationProfilePath().toFile());
		return profile;
	}

	private static ValidationProfile profileFromFile(final File profileFile)
			throws JAXBException, IOException {
		ValidationProfile profile = Profiles.defaultProfile();
			InputStream is = new FileInputStream(profileFile);
			profile = Profiles.profileFromXml(is);
			if ("sha-1 hash code".equals(profile.getHexSha1Digest())) {
				return Profiles.defaultProfile();
			}
			return profile;
	}

	private MetadataFixerResult fixMetadata(ValidationResult info,
											ModelParser parser,
											String fileName,
											Config config) throws IOException {
		FixerConfig fixerConfig = FixerConfigImpl.getFixerConfig(
				parser.getPDDocument(), info);
		Path path = config.getFixMetadataPathFolder();
		File tempFile = File.createTempFile("fixedTempFile", ".pdf");
		tempFile.deleteOnExit();
		try (OutputStream tempOutput = new BufferedOutputStream(
				new FileOutputStream(tempFile))) {
			MetadataFixerResult fixerResult = MetadataFixerImpl.fixMetadata(
					tempOutput, fixerConfig);
			MetadataFixerResult.RepairStatus repairStatus = fixerResult
					.getRepairStatus();
			if (repairStatus == MetadataFixerResult.RepairStatus.SUCCESS || repairStatus == MetadataFixerResult.RepairStatus.ID_REMOVED) {
				File resFile;
				boolean flag = true;
				while (flag) {
					if (!path.toString().trim().isEmpty()) {
						resFile = FileGenerator.createOutputFile(config.getFixMetadataPathFolder().toFile(),
								fileName, config.getMetadataFixerPrefix());
					} else {
						resFile = FileGenerator.createOutputFile(new File(fileName),
								config.getMetadataFixerPrefix());
					}

					try {
						Files.copy(tempFile.toPath(), resFile.toPath());
						flag = false;
					} catch (FileAlreadyExistsException e) {
						LOGGER.error("Exception in copying temp file, file already exists", e);
						exceptionsInProcessing.add(e);
					}
				}
			}
			return fixerResult;
		}
	}
}
