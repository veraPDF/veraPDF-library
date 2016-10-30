package org.verapdf.processor;

import java.util.EnumMap;
import java.util.EnumSet;

import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;

public interface ProcessorResult {

	/**
	 * @return the results
	 */
	public EnumMap<TaskType, TaskResult> getResults();

	public EnumSet<TaskType> getTaskTypes();
	public TaskResult getResultForTask(TaskType taskType);
	public ValidationResult getValidationResult();
	public FeatureExtractionResult getFeaturesResult();
	public MetadataFixerResult getFixerResult();
}