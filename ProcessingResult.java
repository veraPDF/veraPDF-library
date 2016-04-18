package org.verapdf.processor;

import org.verapdf.processor.config.Config;
import org.verapdf.report.ItemDetails;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Instance of this class contains result of
 * {@link org.verapdf.processor.ProcessorImpl#validate(InputStream, ItemDetails, Config, OutputStream)}
 * work.
 *
 * @author Sergey Shemyakov
 */
public class ProcessingResult {
	private ProcessingResult.ValidationSummary validationSummary;
	private ProcessingResult.MetadataFixingSummary metadataFixerSummary;
	private ProcessingResult.FeaturesSummary featuresSummary;
	private ProcessingResult.ReportSummary reportSummary;
	private ArrayList<String> errorMessages;

	ProcessingResult(Config config) {
		this.validationSummary = config.getProcessingType().isValidating() ?
				ValidationSummary.FILE_VALID : ValidationSummary.VALIDATION_DISABLED;
		this.metadataFixerSummary = config.isFixMetadata() ?
				MetadataFixingSummary.FIXING_SUCCEED : MetadataFixingSummary.FIXING_DISABLED;
		this.featuresSummary = config.getProcessingType().isFeatures() ?
				FeaturesSummary.FEATURES_SUCCEED : FeaturesSummary.FEATURES_DISABLED;
		this.reportSummary = ReportSummary.REPORT_SECCEED;
		this.errorMessages = new ArrayList<>();
	}

	/**
	 * @return summary of validation
	 */
	public ValidationSummary getValidationSummary() {
		return validationSummary;
	}

	/**
	 * @return summary of metadata fixing
	 */
	public MetadataFixingSummary getMetadataFixerSummary() {
		return metadataFixerSummary;
	}

	/**
	 * @return summary of feature extracting
	 */
	public FeaturesSummary getFeaturesSummary() {
		return featuresSummary;
	}

	/**
	 * @return summary of report generating
	 */
	public ReportSummary getReportSummary() {
		return reportSummary;
	}

	/**
	 * @return ArrayList of messages of exceptions that occurred during
	 * {@link org.verapdf.processor.ProcessorImpl#validate(InputStream, ItemDetails, Config, OutputStream)}
	 */
	public ArrayList<String> getErrorMessages() {
		return errorMessages;
	}

	void setValidationSummary(ValidationSummary validationSummary) {
		this.validationSummary = validationSummary;
	}

	void setReportSummary(ReportSummary reportSummary) {
		this.reportSummary = reportSummary;
	}

	void setMetadataFixerSummary(MetadataFixingSummary metadataFixerSummary) {
		this.metadataFixerSummary = metadataFixerSummary;
	}

	void setFeaturesSummary(FeaturesSummary featuresSummary) {
		this.featuresSummary = featuresSummary;
	}

	void addErrorMessage(String message) {
		errorMessages.add(message);
	}

	public enum ValidationSummary {
		FILE_VALID,
		FILE_NOT_VALID,
		ERROR_IN_VALIDATION,
		VALIDATION_DISABLED
	}

	public enum MetadataFixingSummary {
		FIXING_SUCCEED,
		ERROR_IN_FIXING,
		FIXING_DISABLED
	}

	public enum FeaturesSummary {
		FEATURES_SUCCEED,
		ERROR_IN_FEATURES,
		FEATURES_DISABLED
	}

	public enum ReportSummary {
		REPORT_SECCEED,
		ERROR_IN_REPORT
	}
}
