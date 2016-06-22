package org.verapdf.processor;

import org.apache.log4j.Logger;
import org.verapdf.core.ModelParsingException;
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
import org.verapdf.processor.config.ProcessingType;
import org.verapdf.report.*;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Class is implementation of {@link Processor} interface
 *
 * @author Sergey Shemyakov
 */
public class ProcessorImpl implements Processor {

    private static final Logger LOGGER = Logger.getLogger(ProcessorImpl.class);

    private ProcessingResult processingResult;

    @Override
    public ProcessingResult validate(InputStream pdfFileStream,
            ItemDetails fileDetails, Config config,
            OutputStream reportOutputStream) throws ModelParsingException {
        long startTimeOfProcessing = System.currentTimeMillis();
        checkArguments(pdfFileStream, fileDetails, config, reportOutputStream);
        ValidationResult validationResult = null;
        MetadataFixerResult fixerResult = null;
        FeaturesCollection featuresCollection = null;
        this.processingResult = new ProcessingResult(config);
        ValidationProfile validationProfile = profileFromConfig(config);
        PDFAFlavour currentFlavour = validationProfile == null ? config
                .getFlavour() : validationProfile.getPDFAFlavour();

        ProcessingType processingType = config.getProcessingType();
        ModelParser parser = ModelParser.createModelWithFlavour(pdfFileStream, currentFlavour);
            if (processingType.isValidating()) {
                try {
                    validationResult = startValidation(validationProfile, parser,
                            config, fileDetails);
                } catch (Exception e) {
                    LOGGER.error("Error in validation", e);
                    setUnsuccessfulValidation();
                    this.processingResult.addErrorMessage(e.getMessage());
                }
                if (config.isFixMetadata() && validationResult != null) {
                    try {
                        fixerResult = fixMetadata(validationResult, parser,
                                fileDetails.getName(), config);
                    } catch (Exception e) {
                        LOGGER.error("Error in metadata fixing", e);
                        setUnsuccessfulMetadataFixing();
                        this.processingResult.addErrorMessage(e.getMessage());
                    }
                }
            }
            if (processingType.isFeatures()) {
                try {
                    featuresCollection = extractFeatures(parser, config);
                } catch (Exception e) {
                    LOGGER.error("Error in features collecting", e);
                    setUnsuccessfulFeatureExtracting();
                    this.processingResult.addErrorMessage(e.getMessage());
                }
            }
        parser.close();

        long endTimeOfProcessing = System.currentTimeMillis();
        writeReport(config, validationResult, fileDetails, reportOutputStream,
                validationProfile, fixerResult, featuresCollection,
                endTimeOfProcessing - startTimeOfProcessing);

        return this.processingResult;
    }

    private void checkArguments(InputStream pdfFileStream,
            ItemDetails fileDetails, Config config,
            OutputStream reportOutputStream) {
        if (pdfFileStream == null) {
            throw new IllegalArgumentException("PDF file stream cannot be null");
        }
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null");
        }
        if (reportOutputStream == null) {
            throw new IllegalArgumentException(
                    "Output stream for report cannot be null");
        }
        if (config.getProcessingType().isValidating()
                && config.getFlavour() == PDFAFlavour.NO_FLAVOUR
                && config.getValidationProfile().toString().equals("")) {
            throw new IllegalArgumentException(
                    "Validation cannot be started with no chosen validation profile");
        }
        if (fileDetails == null) {
            throw new IllegalArgumentException("Item details cannot be null");
        }
    }

    private static boolean logPassed(final Config config) {
        return (config.getReportType() != FormatOption.XML)
                || config.isShowPassedRules();
    }

    ValidationProfile profileFromConfig(final Config config) {
        try {
            if (config.getValidationProfile().toString().equals("")) {
                return null;
            }
            ValidationProfile profile = profileFromFile(config
                    .getValidationProfile().toFile());
            return profile;
        } catch (JAXBException e) {
            LOGGER.error("Error in parsing profile XML", e);
            this.processingResult
                    .addErrorMessage("Error in parsing profile from XML: "
                            + e.getMessage());
            setUnsuccessfulValidation();
            setUnsuccessfulMetadataFixing();
            return Profiles.defaultProfile();
        } catch (IOException e) {
            LOGGER.error("Error in reading profile from disc", e);
            this.processingResult
                    .addErrorMessage("Error in reading profile from disc: "
                            + e.getMessage());
            setUnsuccessfulValidation();
            setUnsuccessfulMetadataFixing();
            return Profiles.defaultProfile();
        }
    }

    private static ValidationProfile profileFromFile(final File profileFile)
            throws JAXBException, IOException {
        ValidationProfile profile = Profiles.defaultProfile();
        InputStream is = new FileInputStream(profileFile);
        profile = Profiles.profileFromXml(is);
        is.close();
        // TODO: why should we check this?
        if ("sha-1 hash code".equals(profile.getHexSha1Digest())) {
            return Profiles.defaultProfile();
        }
        return profile;
    }

    private ValidationResult startValidation(
            ValidationProfile validationProfile, ModelParser parser,
            Config config, ItemDetails fileDetails) {
        if (validationProfile == null) {
            validationProfile = profileFromFlavour(parser.getFlavour());
        }
        PDFAValidator validator = Validators.createValidator(validationProfile,
                logPassed(config), config.getMaxNumberOfFailedChecks());
        ValidationResult validationResult = validate(validator, parser);
        return validationResult;
    }

    private MetadataFixerResult fixMetadata(ValidationResult info,
            ModelParser parser, String fileName, Config config) {
        try {
            FixerConfig fixerConfig = FixerConfigImpl.getFixerConfig(
                    parser.getPDDocument(), info);
            Path path = config.getFixMetadataFolder();
            File tempFile = File.createTempFile("fixedTempFile", ".pdf");
            tempFile.deleteOnExit();
            try (OutputStream tempOutput = new BufferedOutputStream(
                    new FileOutputStream(tempFile))) {
                MetadataFixerResult fixerResult = MetadataFixerImpl
                        .fixMetadata(tempOutput, fixerConfig);
                MetadataFixerResult.RepairStatus repairStatus = fixerResult
                        .getRepairStatus();
                if (repairStatus == MetadataFixerResult.RepairStatus.SUCCESS
                        || repairStatus == MetadataFixerResult.RepairStatus.ID_REMOVED) {
                    File resFile;
                    boolean flag = true;
                    while (flag) {
                        if (!path.toString().trim().isEmpty()) {
                            resFile = FileGenerator.createOutputFile(
                                    path.toFile(),
                                    new File(fileName).getName(),
                                    config.getMetadataFixerPrefix());
                        } else {
                            resFile = FileGenerator.createOutputFile(new File(
                                    fileName), config.getMetadataFixerPrefix());
                        }
                        Files.copy(tempFile.toPath(), resFile.toPath());
                        flag = false;
                    }
                }
                return fixerResult;
            }
        } catch (IOException e) {
            LOGGER.error("Error in fixing metadata", e);
            setUnsuccessfulMetadataFixing();
            this.processingResult.addErrorMessage("Error in fixing metadata: "
                    + e.getMessage());
            return null;
        }
    }

    private FeaturesCollection extractFeatures(ModelParser parser, Config config) {
        FeaturesCollection featuresCollection = null;
        try {
            // TODO: should we make a plugins folder path as a additional
            // setting in the Config and let user to change it?
            String appHome = System.getProperty("app.home");
            Path pluginsPath = null;
            if (appHome != null) {
                pluginsPath = new File(appHome, "plugins").toPath();
            }
            featuresCollection = PBFeatureParser.getFeaturesCollection(
                    parser.getPDDocument(), config.isPluginsEnabled(),
                    pluginsPath);
        } catch (Exception e) {
            LOGGER.error("Error in extracting features", e);
            setUnsuccessfulFeatureExtracting();
            this.processingResult
                    .addErrorMessage("Error in feature extraction: "
                            + e.getMessage());
        }
        return featuresCollection;
    }

    private ValidationProfile profileFromFlavour(PDFAFlavour flavour) {
        ValidationProfile validationProfile = null;
        try {
            validationProfile = Profiles.getVeraProfileDirectory()
                    .getValidationProfileByFlavour(flavour);
        } catch (NoSuchElementException re) {
            LOGGER.warn(re);
        }
        return validationProfile;
    }

    private ValidationResult validate(PDFAValidator validator,
            ModelParser parser) {
        ValidationResult validationResult = null;
        try {
            validationResult = validator.validate(parser);
            if (!validationResult.isCompliant()) {
                this.processingResult
                        .setValidationSummary(ProcessingResult.ValidationSummary.FILE_NOT_VALID);
            }
        } catch (ModelParsingException | ValidationException e) {
            LOGGER.error("Error in validation", e);
            setUnsuccessfulValidation();
            setUnsuccessfulMetadataFixing();
            this.processingResult.addErrorMessage("Error in validation: "
                    + e.getMessage());
        }
        return validationResult;
    }

    private void writeReport(Config config, ValidationResult validationResult,
            ItemDetails fileDetails, OutputStream reportOutputStream,
            ValidationProfile validationProfile,
            MetadataFixerResult fixerResult,
            FeaturesCollection featuresCollection, long processingTime) {
        try {
            switch (config.getReportType()) {
            case TEXT:
                writeTextReport(validationResult, fileDetails,
                        reportOutputStream, config);
                break;
            case MRR:
            case HTML:
                writeMRR(fileDetails, validationProfile, validationResult,
                        config, fixerResult, featuresCollection,
                        processingTime, reportOutputStream);
                break;
            case XML:
                CliReport report = CliReport.fromValues(fileDetails,
                        validationResult,
                        FeaturesReport.fromValues(featuresCollection));
                CliReport.toXml(report, reportOutputStream, Boolean.TRUE);
                break;
            default:
                throw new IllegalStateException("Wrong or unknown report type.");
            }
        } catch (IOException e) {
            LOGGER.error("Exception raised while writing report to file", e);
            this.processingResult
                    .setReportSummary(ProcessingResult.ReportSummary.ERROR_IN_REPORT);
            this.processingResult
                    .addErrorMessage("Error in writing report to file: "
                            + e.getMessage());
        } catch (JAXBException e) {
            LOGGER.error(
                    "Exception raised while converting report to XML file", e);
            this.processingResult
                    .setReportSummary(ProcessingResult.ReportSummary.ERROR_IN_REPORT);
            this.processingResult
                    .addErrorMessage("Error in generating XML report file: "
                            + e.getMessage());
        } catch (TransformerException e) {
            LOGGER.error(
                    "Exception raised while converting MRR report into HTML", e);
            this.processingResult
                    .setReportSummary(ProcessingResult.ReportSummary.ERROR_IN_REPORT);
            this.processingResult
                    .addErrorMessage("Error in converting MRR report to HTML:"
                            + e.getMessage());
        }
    }

    private void writeTextReport(ValidationResult validationResult,
            ItemDetails fileDetails, OutputStream reportOutputStream,
            Config config) throws IOException {
        if (validationResult != null) {
            String reportSummary = (validationResult.isCompliant() ? "PASS "
                    : "FAIL ") + fileDetails.getName() + "\n";
            reportOutputStream.write(reportSummary.getBytes());
            if (config.isVerboseCli()) {
                Set<RuleId> ruleIds = new HashSet<>();
                for (TestAssertion assertion : validationResult
                        .getTestAssertions()) {
                    if (assertion.getStatus() == TestAssertion.Status.FAILED) {
                        ruleIds.add(assertion.getRuleId());
                    }
                }
                for (RuleId id : ruleIds) {
                    String reportRuleSummary = id.getClause() + "-"
                            + id.getTestNumber() + "\n";
                    reportOutputStream.write(reportRuleSummary.getBytes());
                }
            }
        }
    }

    private void writeMRR(ItemDetails fileDetails,
            ValidationProfile validationProfile,
            ValidationResult validationResult, Config config,
            MetadataFixerResult fixerResult,
            FeaturesCollection featuresCollection, long processingTime,
            OutputStream reportOutputStream) throws JAXBException, IOException,
            TransformerException {

        MachineReadableReport machineReadableReport = MachineReadableReport
                .fromValues(fileDetails, validationProfile, validationResult,
                        config.isShowPassedRules(),
                        config.getMaxNumberOfDisplayedFailedChecks(),
                        fixerResult, featuresCollection, processingTime);
        if (this.processingResult.getValidationSummary() == ProcessingResult.ValidationSummary.ERROR_IN_VALIDATION) {
            machineReadableReport.setErrorInValidationReport();
        }
        if (this.processingResult.getMetadataFixerSummary() == ProcessingResult.MetadataFixingSummary.ERROR_IN_FIXING) {
            machineReadableReport.setErrorInMetadataFixerReport();
        }
        if (this.processingResult.getFeaturesSummary() == ProcessingResult.FeaturesSummary.ERROR_IN_FEATURES) {
            machineReadableReport.setErrorInFeaturesReport();
        }
        if (config.getReportType() == FormatOption.MRR) {
            MachineReadableReport.toXml(machineReadableReport,
                    reportOutputStream, Boolean.TRUE);
        } else if (config.getReportType() == FormatOption.HTML) {
            File tmp = File.createTempFile("verpdf", "xml");
            tmp.deleteOnExit();
            try (OutputStream os = new FileOutputStream(tmp)) {
                MachineReadableReport.toXml(machineReadableReport, os,
                        Boolean.FALSE);
            }
            try (InputStream is = new FileInputStream(tmp)) {
                HTMLReport.writeHTMLReport(is, reportOutputStream,
                        config.getProfileWikiPath());
            }
        } else {
            throw new IllegalStateException(
                    "This method should be used only for MRR or HTML reports");
        }
    }

    private void setUnsuccessfulValidation() {
        if (this.processingResult.getValidationSummary() != ProcessingResult.ValidationSummary.VALIDATION_DISABLED) {
            this.processingResult
                    .setValidationSummary(ProcessingResult.ValidationSummary.ERROR_IN_VALIDATION);
        }
    }

    private void setUnsuccessfulMetadataFixing() {
        if (this.processingResult.getMetadataFixerSummary() != ProcessingResult.MetadataFixingSummary.FIXING_DISABLED) {
            this.processingResult
                    .setMetadataFixerSummary(ProcessingResult.MetadataFixingSummary.ERROR_IN_FIXING);
        }
    }

    private void setUnsuccessfulFeatureExtracting() {
        if (this.processingResult.getFeaturesSummary() != ProcessingResult.FeaturesSummary.FEATURES_DISABLED) {
            this.processingResult
                    .setFeaturesSummary(ProcessingResult.FeaturesSummary.ERROR_IN_FEATURES);
        }
    }

    private void setUnsuccessfulProcessing() {
        setUnsuccessfulValidation();
        setUnsuccessfulMetadataFixing();
        setUnsuccessfulFeatureExtracting();
    }
}
