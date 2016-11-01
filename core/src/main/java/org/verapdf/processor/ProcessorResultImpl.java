package org.verapdf.processor;

import java.util.EnumMap;
import java.util.EnumSet;

import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.MetadataFixerResultImpl;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;

/**
 * Instance of this class contains result of
 * {@link org.verapdf.processor.ProcessorImpl#validate(InputStream, ItemDetails, Config, OutputStream)}
 * work.
 *
 * @author Sergey Shemyakov
 */
class ProcessorResultImpl implements ProcessorResult {
	private final static ProcessorResult defaultInstance = new ProcessorResultImpl();
	private final EnumMap<TaskType, TaskResult> results;
	private final ValidationResult validationResult;
	private final FeatureExtractionResult featuresResult;
	private final MetadataFixerResult fixerResult;

	private ProcessorResultImpl() {
		this(new EnumMap<TaskType, TaskResult>(TaskType.class));
	}
	
	private ProcessorResultImpl(final EnumMap<TaskType, TaskResult> tasks) {
		this(tasks, ValidationResults.defaultResult(), new FeatureExtractionResult(), new MetadataFixerResultImpl.Builder().build());
	}
	
	private ProcessorResultImpl(final EnumMap<TaskType, TaskResult> results, final ValidationResult validationResult,
			final FeatureExtractionResult featuresResult, final MetadataFixerResult fixerResult) {
		super();
		this.results = results;
		this.validationResult = validationResult;
		this.featuresResult = featuresResult;
		this.fixerResult = fixerResult;
	}


	/**
	 * @return the results
	 */
	@Override
	public EnumMap<TaskType, TaskResult> getResults() {
		return this.results;
	}

	@Override
	public EnumSet<TaskType> getTaskTypes() {
		return EnumSet.copyOf(this.results.keySet());
	}
	
	static ProcessorResult defaultInstance() {
		return defaultInstance;
	}
	
	static ProcessorResult fromValues(final EnumMap<TaskType, TaskResult> results, final ValidationResult validationResult,
			final FeatureExtractionResult featuresResult, final MetadataFixerResult fixerResult) {
		return new ProcessorResultImpl(results, validationResult, featuresResult, fixerResult);
	}

	@Override
	public ValidationResult getValidationResult() {
		return this.validationResult;
	}

	@Override
	public FeatureExtractionResult getFeaturesResult() {
		return this.featuresResult;
	}

	@Override
	public MetadataFixerResult getFixerResult() {
		return this.fixerResult;
	}

	@Override
	public TaskResult getResultForTask(TaskType taskType) {
		return this.results.get(taskType);
	}
}
