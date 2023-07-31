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

import org.verapdf.ReleaseDetails;
import org.verapdf.component.ComponentDetails;
import org.verapdf.core.VeraPDFException;
import org.verapdf.processor.ProcessorFactory.BatchSummariser;
import org.verapdf.processor.reports.BatchSummary;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 8 Nov 2016:22:58:09
 */

public abstract class AbstractBatchProcessor implements BatchProcessor {
	protected final ItemProcessor processor;
	private BatchProcessingHandler handler = null;
	private BatchSummariser summariser = null;

	/**
	 * @see org.verapdf.processor.BatchProcessor#process(java.io.File, boolean,
	 *      org.verapdf.processor.BatchProcessingHandler)
	 */
	protected AbstractBatchProcessor(final ItemProcessor processor) {
		this.processor = processor;
	}

	/**
	 * @see org.verapdf.processor.Processor#getConfig()
	 */
	@Override
	public ProcessorConfig getConfig() {
		return this.processor.getConfig();
	}

	/**
	 * @see org.verapdf.processor.Processor#getDependencies()
	 */
	@Override
	public Collection<ReleaseDetails> getDependencies() {
		return this.processor.getDependencies();
	}

	/**
	 * @see org.verapdf.component.Component#getDetails()
	 */
	@Override
	public ComponentDetails getDetails() {
		return this.processor.getDetails();
	}

	/**
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		this.processor.close();
	}

	@Override
	public BatchSummary process(final File toProcess, final boolean recurse, final BatchProcessingHandler resultHandler)
			throws VeraPDFException {
		this.initialise(resultHandler);
		this.handler.handleBatchStart(this.processor.getConfig());
		this.processContainer(toProcess, recurse);
		return finishBatch();
	}

	@Override
	public BatchSummary process(final List<? extends File> toProcess, final BatchProcessingHandler resultHandler)
			throws VeraPDFException {
		this.initialise(resultHandler);
		this.handler.handleBatchStart(this.processor.getConfig());
		this.processList(toProcess);
		return finishBatch();
	}

	protected abstract void processContainer(final File container, final boolean recurse) throws VeraPDFException;

	protected abstract void processList(final List<? extends File> toProcess) throws VeraPDFException;

	private void initialise(final BatchProcessingHandler resultHandler) {
		this.summariser = new ProcessorFactory.BatchSummariser(this.getConfig());
		this.handler = resultHandler;
	}

	protected void processResult(ProcessorResult result, Boolean isLogsEnabled) throws VeraPDFException {
		this.handler.handleResult(result, isLogsEnabled);
		this.summariser.addProcessingResult(result);
	}

	private BatchSummary finishBatch() throws VeraPDFException {
		BatchSummary summary = this.summariser.summarise();
		this.handler.handleBatchEnd(summary);
		return summary;
	}
}
