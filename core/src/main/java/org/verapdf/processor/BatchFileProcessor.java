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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.verapdf.core.VeraPDFException;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 8 Nov 2016:23:45:29
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
	 * @see org.verapdf.processor.AbstractBatchProcessor#processContainer(java.io.File,
	 *      boolean)
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
			if (item.isHidden() || !item.canRead())
				continue;
			if (item.isDirectory()) {
				processDir(item, recurse);
			} else if (item.isFile() && isPdf(item.getName())) {
				processItem(item);
			}
		}
	}

	private static boolean isPdf(final String name) {
		if (!name.endsWith(".pdf")) //$NON-NLS-1$
			return name.endsWith(".PDF"); //$NON-NLS-1$
		return true;
	}

	private void processItem(final File item) throws VeraPDFException {
		ProcessorResult result = this.processor.process(item);
		this.processResult(result);
	}

	private static String badItemMessage(final File item, final boolean isDir) {
		String itemType = isDir ? "directory" : "file"; //$NON-NLS-1$ //$NON-NLS-2$
		if (item == null)
			return "Null " + itemType + " item passed for processing."; //$NON-NLS-1$ //$NON-NLS-2$
		final String rootMessage = "Couldn't process: " + item.getAbsolutePath() + " is not a "; //$NON-NLS-1$ //$NON-NLS-2$
		final String messageTrail = (item.canRead()) ? itemType + "." : "readable " + itemType + "."; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return rootMessage + messageTrail;
	}
}
