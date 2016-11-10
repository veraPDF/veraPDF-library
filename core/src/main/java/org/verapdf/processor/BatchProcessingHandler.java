/**
 * 
 */
package org.verapdf.processor;

import java.io.Closeable;

import org.verapdf.core.VeraPDFException;
import org.verapdf.processor.reports.BatchSummary;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 8 Nov 2016:22:54:02
 */

public interface BatchProcessingHandler extends Closeable {
	public void handleBatchStart() throws VeraPDFException;
	public void handleResult(ProcessorResult result) throws VeraPDFException;
	public void handleBatchEnd(BatchSummary summary) throws VeraPDFException;
}
