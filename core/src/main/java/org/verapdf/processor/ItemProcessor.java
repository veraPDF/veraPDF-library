package org.verapdf.processor;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

import org.verapdf.component.Component;
import org.verapdf.report.ItemDetails;
import org.verapdf.report.MachineReadableReport;

/**
 * Processor encapsulates all validation processes: validation, metadata fixes
 * and feature extracting.
 *
 * @author Sergey Shemyakov
 */
public interface VeraProcessor extends Component {

	/**
	 * Method performs pdf validation with given options
	 *
	 * @param pdfFile
	 *            input stream, containing file to be validated
	 * @param fileDetails
	 *            details about file to be validated
	 * @param config
	 *            settings used in validation
	 * @param report
	 *            output stream, in which report will be written
	 */
	public ProcessorResult process(ItemDetails fileDetails, InputStream toProcess);

	public Set<ProcessorResult> process(Set<File> toProcess);
	
	public MachineReadableReport processBatch(Set<File> toProcess); 

	public ProcessorConfig getConfig();
}
