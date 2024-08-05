/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.processor;

import java.io.File;
import java.io.InputStream;

import org.verapdf.core.VeraPDFException;
import org.verapdf.processor.reports.ItemDetails;

/**
 * Processor encapsulates all validation processes: validation, metadata fixes
 * and feature extracting.
 *
 * @author Sergey Shemyakov
 */
public interface ItemProcessor extends Processor {

	/**
	 * Process an {@link InputStream} and return the {@link ProcessorResult}
	 * 
	 * @param fileDetails
	 *            the {@link ItemDetails} of the stream to process
	 * @param toProcess
	 *            an {@link InputStream} to the PDF/A to process
	 * @return the result of the process as a {@link ProcessorResult}
	 */
	public ProcessorResult process(ItemDetails fileDetails, InputStream toProcess);

	/**
	 * Process a {@link File} and return the {@link ProcessorResult}
	 * 
	 * @param toProcess
	 *            the PDF/A {@link File} to process
	 * @return the result of the process as a {@link ProcessorResult}
	 * @throws VeraPDFException
	 *             if the file cannot be found or opened.
	 */
	public ProcessorResult process(File toProcess) throws VeraPDFException;
}
