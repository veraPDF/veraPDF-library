/**
 * 
 */
package org.verapdf.processor;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.verapdf.core.VeraPDFException;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 8 Nov 2016:23:45:29
 */

public final class BatchFileProcessor extends AbstractBatchProcessor {
	private static final Logger logger = Logger.getLogger(BatchFileProcessor.class.getCanonicalName());

	/**
	 * @param processor
	 */
	public BatchFileProcessor(ItemProcessor processor) {
		super(processor);
	}

	/**
	 * @see org.verapdf.processor.AbstractBatchProcessor#processContainer(java.io.File, boolean)
	 */
	@Override
	protected void processContainer(File container, boolean recurse) throws VeraPDFException {
		if (container == null || !container.isDirectory() || !container.canRead()) {
			logger.log(Level.SEVERE, badItemMessage(container, true));
		} else {
			processDir(container, recurse);
		}
	}

	/**
	 * @see org.verapdf.processor.AbstractBatchProcessor#processList(java.util.List)
	 */
	@Override
	protected void processList(List<? extends File> toProcess) throws VeraPDFException {
		for (File item : toProcess) {
			if (item == null || !item.isFile() || !item.canRead()) {
				logger.log(Level.SEVERE, badItemMessage(item, false));
			} else {
				processItem(item);
			}
		}
	}

	private void processDir(final File toProcess, final boolean recurse) throws VeraPDFException {
		for (File item : toProcess.listFiles()) {
			if (item.isDirectory() && !item.isHidden() && item.canRead()) {
				processDir(item, recurse);
			} else {
				processItem(item);
			}
		}
	}

	private void processItem(final File item) throws VeraPDFException {
		this.processResult(this.processor.process(item));
	}

	private static String badItemMessage(final File item, final boolean isDir) {
		String itemType = isDir ? "directory" : "file";
		if (item == null)
			return "Null " + itemType + " item passed for processing.";
		final String rootMessage = "Couldn't process: " + item.getAbsolutePath() + " is not a ";
		final String messageTrail = (item.canRead()) ? itemType + "." : "readable " + itemType + ".";
		return rootMessage + messageTrail;
	}
}
