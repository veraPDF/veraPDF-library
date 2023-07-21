/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
/**
 * 
 */
package org.verapdf.processor;

import java.io.File;
import java.util.List;

import org.verapdf.core.VeraPDFException;
import org.verapdf.processor.reports.BatchSummary;

/**
 * The veraPDF batch processor, used to process multiple files.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 8 Nov 2016:22:55:35
 */
public interface BatchProcessor extends Processor {
	/**
	 * Process a list of PDF files
	 * 
	 * @param toProcess
	 *            a {@link List} of {@link File}s to process
	 * @param resultHandler
	 *            the {@link BatchProcessingHandler} that will be used to
	 *            process the results
	 * @return a {@link BatchSummary} that reports the details of the batch
	 *         process
	 * @throws VeraPDFException
	 *             when an error occurs during processing.
	 */
	public BatchSummary process(List<? extends File> toProcess, BatchProcessingHandler resultHandler)
			throws VeraPDFException;

	/**
	 * Process all .pdf files in a directory, optionally recursively.
	 * 
	 * @param toProcess
	 *            a {@link File} that denotes a directory to process
	 * @param recurse
	 *            set {@code true} to recurse into sub-directories, false if
	 *            recursion not required.
	 * @param resultHandler
	 *            the {@link BatchProcessingHandler} that will be used to
	 *            process the results
	 * @return a {@link BatchSummary} that reports the details of the batch
	 *         process
	 * @throws VeraPDFException
	 *             when an error occurs during processing.
	 */
	public BatchSummary process(File toProcess, boolean recurse, BatchProcessingHandler resultHandler)
			throws VeraPDFException;
}
