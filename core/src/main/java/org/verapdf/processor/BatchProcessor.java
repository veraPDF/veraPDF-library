/**
 * 
 */
package org.verapdf.processor;

import java.io.File;
import java.util.List;

import org.verapdf.core.VeraPDFException;
import org.verapdf.processor.reports.BatchSummary;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 8 Nov 2016:22:55:35
 */

public interface BatchProcessor extends Processor {
	public BatchSummary process(List<? extends File> toProcess, BatchProcessingHandler resutlHandler) throws VeraPDFException;

	public BatchSummary process(File toProcess, boolean recurse, BatchProcessingHandler resutlHandler) throws VeraPDFException;
}
