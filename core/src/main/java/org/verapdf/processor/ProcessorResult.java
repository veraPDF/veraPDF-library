package org.verapdf.processor;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.report.FeaturesReport;
import org.verapdf.report.ItemDetails;

@XmlJavaTypeAdapter(ProcessorResultImpl.Adapter.class)
public interface ProcessorResult {
	/**
	 * @return the results
	 */
	public ItemDetails getProcessedItem();
	public EnumMap<TaskType, TaskResult> getResults();
	public EnumSet<TaskType> getTaskTypes();
	public Collection<TaskResult> getResultSet();
	public TaskResult getResultForTask(TaskType taskType);
	public ValidationResult getValidationResult();
	public FeaturesReport getFeaturesReport();
	public MetadataFixerResult getFixerResult();
	public boolean isValidPdf();
	public boolean isEncryptedPdf();
}