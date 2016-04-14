package org.verapdf.processor;

import org.verapdf.features.tools.FeaturesCollection;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
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
	private ValidationResult validationResult;
	private MetadataFixerResult metadataFixerResult;
	private FeaturesCollection featuresCollection;
	private ArrayList<Exception> exceptionsInProcessing;

	ProcessingResult(ValidationResult validationResult,
							MetadataFixerResult metadataFixerResult,
							FeaturesCollection featuresCollection,
							ArrayList<Exception> exceptionsInValidation) {
		this.validationResult = validationResult;
		this.metadataFixerResult = metadataFixerResult;
		this.featuresCollection = featuresCollection;
		this.exceptionsInProcessing = exceptionsInValidation;
	}

	/**
	 * @return result of validation
	 */
	public ValidationResult getValidationResult() {
		return validationResult;
	}

	/**
	 * @return result of metadata fixing
	 */
	public MetadataFixerResult getMetadataFixerResult() {
		return metadataFixerResult;
	}

	/**
	 * @return features of file
	 */
	public FeaturesCollection getFeaturesCollection() {
		return featuresCollection;
	}

	/**
	 * @return ArrayList of exceptions that occurred during
	 * {@link org.verapdf.processor.ProcessorImpl#validate(InputStream, ItemDetails, Config, OutputStream)}
	 */
	public ArrayList<Exception> getExceptionsInProcessing() {
		return exceptionsInProcessing;
	}

	void setValidationResult(ValidationResult validationResult) {
		this.validationResult = validationResult;
	}

	void setMetadataFixerResult(MetadataFixerResult metadataFixerResult) {
		this.metadataFixerResult = metadataFixerResult;
	}

	void setFeaturesCollection(FeaturesCollection featuresCollection) {
		this.featuresCollection = featuresCollection;
	}
}
