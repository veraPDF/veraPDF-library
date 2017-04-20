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

import java.io.Closeable;

import org.verapdf.core.VeraPDFException;
import org.verapdf.processor.reports.BatchSummary;

/**
 * Interface that should be implemented by developers wishing to write custom
 * result handlers for the veraPDF {@link BatchProcessor}. The interface
 * effectively defines a set of processing callbacks that are invoked by the
 * batch processor at specific points during processing.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 8 Nov 2016:22:54:02
 */
public interface BatchProcessingHandler extends Closeable {
	/**
	 * This method is called by the {@link BatchProcessor} at the start of
	 * processing. The processor passes it {@link ProcessorConfig} allowing the
	 * handler to read the config details and take action.
	 * 
	 * @param config
	 *            the {@link ProcessorConfig} supplied by the caller of the
	 *            batch process.
	 * @throws VeraPDFException
	 *             if there's a problem setting up the batch process.
	 */
	public void handleBatchStart(ProcessorConfig config) throws VeraPDFException;

	/**
	 * This method is called by the {@link BatchProcessor} after each item in
	 * the batch is processed allowing the implementor to take specific action
	 * for each item processed.
	 * 
	 * @param result
	 *            the {@link ProcessorResult} for the last item processed.
	 * @throws VeraPDFException
	 *             if there's a problem with the particular result.
	 */
	public void handleResult(ProcessorResult result) throws VeraPDFException;

	/**
	 * This method is called by the {@link BatchProcessor} at the end of the
	 * batch process and allows custom action to be taken informed by the
	 * summary of the batch process.
	 * 
	 * @param summary
	 *            the {@link BatchSummary} for the batch process just completed.
	 * @throws VeraPDFException
	 *             if there's a problem handling the {@link BatchSummary}
	 */
	public void handleBatchEnd(BatchSummary summary) throws VeraPDFException;
}
