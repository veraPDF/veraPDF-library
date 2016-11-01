package org.verapdf.processor;

import java.io.InputStream;

import org.verapdf.component.Component;
import org.verapdf.report.ItemDetails;

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

	public ProcessorConfig getConfig();
}
