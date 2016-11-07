/**
 * 
 */
package org.verapdf.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.verapdf.ReleaseDetails;
import org.verapdf.component.ComponentDetails;
import org.verapdf.core.VeraPDFException;

/**
 * Abstract base class that defines the workflow of a streaming processor but
 * leaves the streaming of results to the implementer.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 6 Nov 2016:19:39:00
 */

public abstract class AbstractStreamingProcessor implements StreamingProcessor {
	private final static int indentSize = 2;
	private static final Logger logger = Logger.getLogger(AbstractStreamingProcessor.class.getCanonicalName());

	protected final ItemProcessor processor;
	private int indent = 0;

	protected AbstractStreamingProcessor(ItemProcessor processor) {
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
		// TODO Auto-generated method stub
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
	public BatchSummary processDirectory(File toProcess, OutputStream dest, boolean recurse) throws VeraPDFException {
		Writer osw = new OutputStreamWriter(dest);
		return processDirectory(toProcess, osw, recurse);
	}

	@Override
	public BatchSummary processDirectory(File toProcess, Writer dest, boolean recurse) throws VeraPDFException {
		this.processDirectory(toProcess, recurse);
		return null;
	}

	/**
	 * @see org.verapdf.processor.StreamingProcessor#process(java.util.List,
	 *      java.io.OutputStream)
	 */
	@Override
	public BatchSummary process(List<? extends File> toProcess, OutputStream dest) throws VeraPDFException {
		Writer osw = new OutputStreamWriter(dest);
		return this.process(toProcess, osw);
	}

	@Override
	public BatchSummary process(List<? extends File> toProcess, Writer dest) throws VeraPDFException {
		this.processItems(toProcess);
		return null;
	}

	// Jobs list wrapper, calls the recursive processDir() below
	protected void processDirectory(File root, boolean recurse) throws VeraPDFException {
		if (root == null || !root.isDirectory() || !root.canRead()) {
			logger.log(Level.SEVERE, badItemMessage(root, true));
		} else {
			processDir(root, recurse);
		}
	}

	private void processDir(File root, boolean recurse) throws VeraPDFException {
		for (File item : root.listFiles()) {
			if (item.isDirectory() && !item.isHidden() && item.canRead()) {
				processDir(item, recurse);
			} else {
				processItem(item);
			}
		}
	}

	protected void processItems(List<? extends File> items) throws VeraPDFException {
		for (File item : items) {
			if (item == null || !item.isFile() || !item.canRead()) {
				logger.log(Level.SEVERE, badItemMessage(item, false));
			} else {
				processItem(item);
			}
		}
	}

	private void processItem(File item) throws VeraPDFException {
		try {
			streamResult(this.processor.process(item));
		} catch (FileNotFoundException excep) {
			// Should really never happen after defensive file check in process
			// items
			logger.log(Level.SEVERE, badItemMessage(item, false), excep);
		}
	}

	abstract protected void streamResult(ProcessorResult result) throws VeraPDFException;

	protected static String badItemMessage(File item, boolean isDir) {
		String itemType = isDir ? "directory" : "file";
		if (item == null)
			return "Null " + itemType + " item passed for processing.";
		final String rootMessage = "Couldn't process: " + item.getAbsolutePath() + " is not a ";
		final String messageTrail = (item.canRead()) ? itemType + "." : "readable " + itemType + ".";
		return rootMessage + messageTrail;
	}

	protected int indent() {
		return this.indent = this.indent + indentSize;
	}

	protected int outdent() {
		if (this.indent >= indentSize)
			return this.indent = this.indent - indentSize;
		return this.indent = 0;
	}

}
