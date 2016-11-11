package org.verapdf.processor;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.metadata.fixer.FixerFactory;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.report.FeaturesReport;
import org.verapdf.report.ItemDetails;

/**
 * Instance of this class contains result of
 * {@link org.verapdf.processor.ProcessorImpl#validate(InputStream, ItemDetails, Config, OutputStream)}
 * work.
 *
 * @author Sergey Shemyakov
 */
@XmlRootElement(name="processorResult")
class ProcessorResultImpl implements ProcessorResult {
	private final static ProcessorResult defaultInstance = new ProcessorResultImpl();
	@XmlAttribute
	private final boolean isValidPdf;
	@XmlAttribute
	private final boolean isEncryptedPdf;
	@XmlElement
	private final ItemDetails itemDetails;
	private final EnumMap<TaskType, TaskResult> taskResults;
	@XmlElement
	private final ValidationResult validationResult;
	private final FeatureExtractionResult featuresResult;
	@XmlElement
	private final MetadataFixerResult fixerResult;

	private ProcessorResultImpl() {
		this(ItemDetails.defaultInstance());
	}
	
	private ProcessorResultImpl(final ItemDetails details) {
		this(details, false, false);
	}

	private ProcessorResultImpl(final ItemDetails details, boolean isValidPdf, boolean isEncrypted) {
		this(details, isValidPdf, isEncrypted, new EnumMap<TaskType, TaskResult>(TaskType.class),
				ValidationResults.defaultResult(), new FeatureExtractionResult(),
				FixerFactory.defaultResult());
	}

	private ProcessorResultImpl(final ItemDetails details, final EnumMap<TaskType, TaskResult> results, final ValidationResult validationResult,
			final FeatureExtractionResult featuresResult, final MetadataFixerResult fixerResult) {
		this(details, true, true, results, validationResult, featuresResult, fixerResult);
	}

	private ProcessorResultImpl(final ItemDetails details, final boolean isValidPdf, final boolean isEncrypted, final EnumMap<TaskType, TaskResult> results,
			final ValidationResult validationResult, final FeatureExtractionResult featuresResult,
			final MetadataFixerResult fixerResult) {
		super();
		this.itemDetails = details;
		this.isValidPdf = isValidPdf;
		this.isEncryptedPdf = isEncrypted;
		this.taskResults = results;
		this.validationResult = validationResult;
		this.featuresResult = featuresResult;
		this.fixerResult = fixerResult;
	}

	/**
	 * @return the results
	 */
	@Override
	public EnumMap<TaskType, TaskResult> getResults() {
		return this.taskResults;
	}

	@Override
	@XmlElementWrapper(name="taskResult")
	@XmlElement(name="taskResult")
	public Collection<TaskResult> getResultSet() {
		return this.taskResults.values();
	}

	@Override
	public ItemDetails getProcessedItem() {
		return this.itemDetails;
	}

	@Override
	public EnumSet<TaskType> getTaskTypes() {
		return this.taskResults.isEmpty() ? EnumSet.noneOf(TaskType.class) : EnumSet.copyOf(this.taskResults.keySet());
	}

	static ProcessorResult defaultInstance() {
		return defaultInstance;
	}

	static ProcessorResult fromValues(final ItemDetails details, final EnumMap<TaskType, TaskResult> results,
			final ValidationResult validationResult, final FeatureExtractionResult featuresResult,
			final MetadataFixerResult fixerResult) {
		return new ProcessorResultImpl(details, true, true, results, validationResult, featuresResult, fixerResult);
	}

	static ProcessorResult invalidPdfResult(final ItemDetails details) {
		return new ProcessorResultImpl(details);
	}

	static ProcessorResult encryptedResult(final ItemDetails details) {
		return new ProcessorResultImpl(details, true, false);
	}

	@Override
	public ValidationResult getValidationResult() {
		return this.validationResult;
	}

	@Override
	@XmlElement
	public FeaturesReport getFeaturesReport() {
		return FeaturesReport.fromValues(this.featuresResult);
	}

	@Override
	public MetadataFixerResult getFixerResult() {
		return this.fixerResult;
	}

	@Override
	public TaskResult getResultForTask(TaskType taskType) {
		return this.taskResults.get(taskType);
	}

	@Override
	public boolean isValidPdf() {
		return this.isValidPdf;
	}

	@Override
	public boolean isEncryptedPdf() {
		return this.isEncryptedPdf;
	}

    static class Adapter extends
            XmlAdapter<ProcessorResultImpl, ProcessorResult> {
        @Override
        public ProcessorResult unmarshal(
        		ProcessorResultImpl procResultImpl) {
            return procResultImpl;
        }

        @Override
        public ProcessorResultImpl marshal(ProcessorResult procResult) {
            return (ProcessorResultImpl) procResult;
        }
    }
}
