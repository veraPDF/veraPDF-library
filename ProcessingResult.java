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
				ValidationSummary.VALIDATION_SUCCEED : ValidationSummary.NO_VALIDATION;
		this.metadataFixerSummary = config.isFixMetadata() ?
				MetadataFixingSummary.FIXING_SUCCEED : MetadataFixingSummary.NO_FIXING;
		this.featuresSummary = config.getProcessingType().isFeatures() ?
				FeaturesSummary.FEATURES_SUCCEED : FeaturesSummary.NO_FEATURES;
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

	void setErrorInValidation() {
		if(validationSummary != ValidationSummary.NO_VALIDATION) {
			validationSummary = ValidationSummary.ERROR_IN_VALIDATION;
		}
	}

	void setErrorInMetadataFixer() {
		if(metadataFixerSummary != MetadataFixingSummary.NO_FIXING) {
			metadataFixerSummary = MetadataFixingSummary.ERROR_IN_FIXING;
		}
	}

	void setErrorInFeatures() {
		if(featuresSummary != FeaturesSummary.NO_FEATURES) {
			featuresSummary = FeaturesSummary.ERROR_IN_FEATURES;
		}
	}

	public enum ValidationSummary {
		VALIDATION_SUCCEED,
		FILE_NOT_COMPLIANT,
		ERROR_IN_VALIDATION,
		NO_VALIDATION
	}

	public enum MetadataFixingSummary {
		FIXING_SUCCEED,
		ERROR_IN_FIXING,
		NO_FIXING
	}

	public enum FeaturesSummary {
		FEATURES_SUCCEED,
		ERROR_IN_FEATURES,
		NO_FEATURES
	}

	public enum ReportSummary {
		REPORT_SECCEED,
		ERROR_IN_REPORT
	}
}
