package org.verapdf.report;

import java.io.File;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.verapdf.ReleaseDetails;
import org.verapdf.component.AuditDuration;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.profiles.Profiles;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement(name = "report")
public class MachineReadableReport {

    @XmlAttribute
    private final Date creationDate;
    @XmlAttribute
    private final String processingTime;
	@XmlAttribute
	private final String version;
	@XmlAttribute
	private final Date buildDate;
    @XmlElement
    private final ItemDetails itemDetails;
    @XmlElement
    private ValidationReport validationReport;
    @XmlElement
    private MetadataFixesReport metadataFixesReport;
    @XmlElement
    private FeaturesReport pdfFeaturesReport;

    private MachineReadableReport() {

        this(ItemDetails.DEFAULT, ValidationReport.fromValues(
                Profiles.defaultProfile(), null, false, 0), "", null, null);
    }

	private MachineReadableReport(ItemDetails itemDetails,
            ValidationReport report, String processingTime,
            FeaturesReport featuresReport, MetadataFixesReport metadataFixesReport) {
        this.itemDetails = itemDetails;
        this.validationReport = report;
        this.creationDate = new Date();
        this.processingTime = processingTime;
        this.pdfFeaturesReport = featuresReport;
        this.metadataFixesReport = metadataFixesReport;

        if (!ReleaseDetails.getIds().contains("gui")) {
            ReleaseDetails.addDetailsFromResource(
                    ReleaseDetails.APPLICATION_PROPERTIES_ROOT + "app." + ReleaseDetails.PROPERTIES_EXT);
        }
		ReleaseDetails releaseDetails = ReleaseDetails.byId("gui");
		this.version = releaseDetails.getVersion();
		this.buildDate = releaseDetails.getBuildDate();
    }

    public void setErrorInValidationReport() {
        this.validationReport = ValidationReport.createErrorReport();
    }

    public void setErrorInMetadataFixerReport() {
        this.metadataFixesReport = MetadataFixesReport.createErrorReport();
    }

    public void setErrorInFeaturesReport() {
        this.pdfFeaturesReport = FeaturesReport.createErrorReport();
    }

    public void setErrorInValidationReport(String errorMessage) {
        this.validationReport = ValidationReport.createErrorReport(errorMessage);
    }

    public void setErrorInMetadataFixerReport(String errorMessage) {
        this.metadataFixesReport = MetadataFixesReport.createErrorReport(errorMessage);
    }

    public void setErrorInFeaturesReport(String errorMessage) {
        this.pdfFeaturesReport = FeaturesReport.createErrorReport(errorMessage);
    }

//    private void writeMRR(ItemDetails fileDetails, ValidationProfile validationProfile,
//			ValidationResult validationResult, Config config, MetadataFixerResult fixerResult,
//			FeaturesCollection featuresCollection, TaskDetails taskDetails, String policyProfile, ProcessResult result)
//			throws JAXBException, IOException, TransformerException {
//	
//		MachineReadableReport machineReadableReport = MachineReadableReport.fromValues(fileDetails, validationProfile,
//				validationResult, config.getValidatorConfig().isRecordPasses(),
//				config.getMaxNumberOfDisplayedFailedChecks(), fixerResult, featuresCollection, taskDetails);
//		if (result.getValidationSummary() == ProcessResult.ValidationSummary.ERROR_IN_VALIDATION) {
//			if (this.parsingErrorMessage != null) {
//				machineReadableReport
//						.setErrorInValidationReport("Could not finish validation. " + this.parsingErrorMessage);
//			} else if (this.validationExeption != null) {
//				machineReadableReport.setErrorInValidationReport(
//						"Could not finish validation. " + this.validationExeption.toString());
//			} else {
//				machineReadableReport.setErrorInValidationReport();
//			}
//		}
//		if (result.getMetadataFixerSummary() == ProcessResult.MetadataFixingSummary.ERROR_IN_FIXING) {
//			if (this.parsingErrorMessage != null) {
//				machineReadableReport
//						.setErrorInMetadataFixerReport("Could not finish metadata fixing. " + this.parsingErrorMessage);
//			} else if (this.metadataFixingExeption != null) {
//				machineReadableReport.setErrorInMetadataFixerReport(
//						"Could not finish metadata fixing. " + this.metadataFixingExeption.toString());
//			} else {
//				machineReadableReport.setErrorInMetadataFixerReport();
//			}
//		}
//		if (result.getFeaturesSummary() == ProcessResult.FeaturesSummary.ERROR_IN_FEATURES) {
//			if (this.parsingErrorMessage != null) {
//				machineReadableReport
//						.setErrorInFeaturesReport("Could not finish features collecting. " + this.parsingErrorMessage);
//			}
//			if (this.featuresExeption != null) {
//				machineReadableReport.setErrorInFeaturesReport(
//						"Could not finish features collecting. " + this.featuresExeption.toString());
//			} else {
//				machineReadableReport.setErrorInFeaturesReport();
//			}
//		}
//		if (policyProfile == null && config.getReportType() == FormatOption.MRR) {
//			MachineReadableReport.toXml(machineReadableReport, System.out, Boolean.TRUE);
//		} else {
//			File tmp = File.createTempFile("verpdf", "xml");
//			tmp.deleteOnExit();
//			try (OutputStream os = new FileOutputStream(tmp)) {
//				MachineReadableReport.toXml(machineReadableReport, os, Boolean.FALSE);
//			}
//			if (policyProfile != null) {
//				try (InputStream is = new FileInputStream(tmp)) {
//					Map<String, String> arguments = new HashMap<>();
//					arguments.put("policyProfilePath", policyProfile);
//					XsltTransformer.transform(is, ProcessorImpl.class.getClassLoader()
//							.getResourceAsStream("org/verapdf/report/policy-example.xsl"), System.out, arguments);
//				}
//			} else if (config.getReportType() == FormatOption.MRR) {
//				try (InputStream is = new FileInputStream(tmp)) {
//					HTMLReport.writeHTMLReport(is, System.out, config.getProfileWikiPath(), true);
//				}
//			} else {
//				throw new IllegalStateException("This method should be used only for MRR or HTML reports");
//			}
//		}
//	}
//
//	private void writeReport(Config config, ValidationResult validationResult, ItemDetails fileDetails,
//			ValidationProfile validationProfile, MetadataFixerResult fixerResult, FeaturesCollection featuresCollection,
//			TaskDetails taskDetails, ProcessResult result) {
//		try {
//			if (config.getPolicyProfile().toString().equals("")) {
//				switch (config.getReportType()) {
//				case TEXT:
//					writeTextReport(validationResult, fileDetails, System.out, config);
//					break;
//				case MRR:
//					writeMRR(fileDetails, validationProfile, validationResult, config, fixerResult, featuresCollection,
//							taskDetails, null, result);
//					break;
//				case XML:
//					CliReport report = CliReport.fromValues(fileDetails, validationResult,
//							FeaturesReport.fromValues(featuresCollection));
//					CliReport.toXml(report, System.out, Boolean.TRUE);
//					break;
//				default:
//					throw new IllegalStateException("Wrong or unknown report type.");
//				}
//			} else {
//				writeMRR(fileDetails, validationProfile, validationResult, config, fixerResult, featuresCollection,
//						taskDetails, config.getPolicyProfile().toAbsolutePath().toString(), result);
//			}
//		} catch (IOException e) {
//			logger.log(Level.SEVERE, "Exception raised while writing report to file", e);
//			result.setReportSummary(ProcessResult.ReportSummary.ERROR_IN_REPORT);
//			result.addErrorMessage("Error in writing report to file: " + e.getMessage());
//		} catch (JAXBException e) {
//			logger.log(Level.SEVERE, "Exception raised while converting report to XML file", e);
//			result.setReportSummary(ProcessResult.ReportSummary.ERROR_IN_REPORT);
//			result.addErrorMessage("Error in generating XML report file: " + e.getMessage());
//		} catch (TransformerException e) {
//			logger.log(Level.SEVERE, "Exception raised while converting MRR report into HTML", e);
//			result.setReportSummary(ProcessResult.ReportSummary.ERROR_IN_REPORT);
//			result.addErrorMessage("Error in converting MRR report to HTML:" + e.getMessage());
//		}
//	}
//
//	private void writeTextReport(ValidationResult validationResult, ItemDetails fileDetails,
//			OutputStream reportOutputStream, Config config) throws IOException {
//		if (validationResult != null) {
//			String reportSummary = (validationResult.isCompliant() ? "PASS " : "FAIL ") + fileDetails.getName() + "\n";
//			reportOutputStream.write(reportSummary.getBytes());
//			if (config.isVerboseCli()) {
//				Set<RuleId> ruleIds = new HashSet<>();
//				for (TestAssertion assertion : validationResult.getTestAssertions()) {
//					if (assertion.getStatus() == TestAssertion.Status.FAILED) {
//						ruleIds.add(assertion.getRuleId());
//					}
//				}
//				for (RuleId id : ruleIds) {
//					String reportRuleSummary = id.getClause() + "-" + id.getTestNumber() + "\n";
//					reportOutputStream.write(reportRuleSummary.getBytes());
//				}
//			}
//		} else if (this.parsingErrorMessage != null) {
//			reportOutputStream.write((this.parsingErrorMessage + "\n").getBytes());
//		} else if (this.validationExeption != null) {
//			String reportSummary = "ERROR " + fileDetails.getName() + " " + this.validationExeption.toString() + "\n";
//			reportOutputStream.write(reportSummary.getBytes());
//		}
//	}
//
	/**
     * @param file
     * @param profile
     * @param validationResult
     * @param reportPassedChecks
     * @param maxFailuresDisplayed
     * @param fixerResult
     * @param collection
     * @param processingTime
     * @return a MachineReadableReport instance initialised from the passed
     *         values
     */
    public static MachineReadableReport fromValues(File file, ValidationProfile profile,
            ValidationResult validationResult, boolean reportPassedChecks, int maxFailuresDisplayed,
            MetadataFixerResult fixerResult, FeatureExtractionResult collection,
            AuditDuration duration) {
        return fromValues(ItemDetails.fromFile(file), profile,
                validationResult, reportPassedChecks,
                maxFailuresDisplayed, fixerResult,
                collection, duration);
    }

    /**
     * @param item
     * @param profile
     * @param validationResult
     * @param reportPassedChecks
     * @param fixerResult
     * @param collection
     * @param processingTime
     * @return a MachineReadableReport instance initialised from the passed
     *         values
     */
    public static MachineReadableReport fromValues(ItemDetails item, ValidationProfile profile,
            ValidationResult validationResult, boolean reportPassedChecks, int maxFailuresDisplayed,
            MetadataFixerResult fixerResult, FeatureExtractionResult collection, AuditDuration duration) {
        ValidationReport validationReport = null;
        if (validationResult != null) {
            validationReport = ValidationReport.fromValues(profile,
                    validationResult, reportPassedChecks, maxFailuresDisplayed);
        }
        FeaturesReport featuresReport = FeaturesReport.fromValues(collection);
        MetadataFixesReport fixesReport = MetadataFixesReport.fromValues(fixerResult);
        return new MachineReadableReport(item, validationReport,
        		duration.getDuration(), featuresReport, fixesReport);
    }
}
