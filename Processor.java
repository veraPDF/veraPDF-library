package org.verapdf.processor;

import org.verapdf.processor.config.Config;
import org.verapdf.report.ItemDetails;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Processor encapsulates all validation processes: validation, metadata
 * fixes and feature extracting.
 *
 *  @author Sergey Shemyakov
 */
public interface Processor {

	/**
	 * Method performs pdf validation with given options
	 *
	 * @param pdfFile input stream, containing file to be validated
	 * @param fileDetails details about file to be validated
	 * @param config settings used in validation
	 * @param report output stream, in which report will be written
	 */
	public ProcessingResult validate(InputStream pdfFile, ItemDetails fileDetails,
						 Config config, OutputStream report);

}
