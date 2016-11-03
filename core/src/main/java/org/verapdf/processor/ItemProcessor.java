package org.verapdf.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.verapdf.report.ItemDetails;

/**
 * Processor encapsulates all validation processes: validation, metadata fixes
 * and feature extracting.
 *
 * @author Sergey Shemyakov
 */
public interface ItemProcessor extends Processor {

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

	public ProcessorResult process(File toProcess) throws FileNotFoundException;
}
