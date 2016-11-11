/**
 * 
 */
package org.verapdf.processor;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.verapdf.ReleaseDetails;
import org.verapdf.component.ComponentDetails;
import org.verapdf.core.VeraPDFException;
import org.verapdf.processor.ProcessorFactory.BatchSummariser;
import org.verapdf.processor.reports.BatchSummary;

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
	 *      org.verapdf.processor.ProcessorResultHandler)
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
		this.summariser = new ProcessorFactory.BatchSummariser();
		this.handler = resultHandler;
	}

	protected void processResult(ProcessorResult result) throws VeraPDFException {
		this.summariser.addProcessingResult(result);
		this.handler.handleResult(result);
	}

	private BatchSummary finishBatch() throws VeraPDFException {
		BatchSummary summary = this.summariser.summarise();
		this.handler.handleBatchEnd(summary);
		return summary;
	}
}
