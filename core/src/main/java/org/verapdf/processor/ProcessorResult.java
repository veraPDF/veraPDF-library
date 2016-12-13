package org.verapdf.processor;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.processor.reports.ItemDetails;
import org.verapdf.report.FeaturesReport;

@XmlJavaTypeAdapter(ProcessorResultImpl.Adapter.class)
public interface ProcessorResult {
	/**
	 * @return the {@link ItemDetails} for the item processed
	 */
	public ItemDetails getProcessedItem();

	/**
	 * @return an {@link EnumMap} of {@link TaskType}s and {@link TaskResult}s
	 *         for the {@link TaskResult}s held in this {@link ProcessorResult}
	 */
	public EnumMap<TaskType, TaskResult> getResults();

	/**
	 * @return an {@link EnumSet} of the {@link TaskType}s carried in this
	 *         result.
	 */
	public EnumSet<TaskType> getTaskTypes();

	/**
	 * @return the {@link Collection} of {@link TaskResult}s for this
	 *         {@link ProcessorResult}
	 */
	public Collection<TaskResult> getResultSet();

	/**
	 * @param taskType
	 *            the {@link TaskType} to retrieve the {@link TaskResult} for
	 * @return the {@link TaskResult} result for taskType
	 */
	public TaskResult getResultForTask(TaskType taskType);

	/**
	 * @return the {@link ValidationResult} or
	 *         {@link org.verapdf.pdfa.results.ValidationResults#defaultResult()}
	 *         if validation not performed.
	 */
	public ValidationResult getValidationResult();

	/**
	 * @return the {@link FeaturesReport}.
	 */
	public FeaturesReport getFeaturesReport();

	/**
	 * @return the {@link MetadataFixerResult}
	 */
	public MetadataFixerResult getFixerResult();

	/**
	 * @return true if the parsed file was a valid PDF, false if the PDF parser
	 *         failed to parse the document and couldn't continue to process it.
	 */
	public boolean isValidPdf();

	/**
	 * @return true if the parser detected that the PDF was encrypted and could
	 *         not continue to process it.
	 */
	public boolean isEncryptedPdf();
}