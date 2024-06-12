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
package org.verapdf.metadata.fixer.entity;

import java.io.OutputStream;

import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.MetadataFixerResult;

/**
 * Current interface provide necessary behavior of pdf document
 * for {@link org.verapdf.pdfa.MetadataFixer}
 *
 * @author Evgeniy Muravitskiy
 */
public interface PDFDocument {

	/**
	 * Return pdf document metadata representation. Must return null
	 * if and only if handler having problems with metadata obtain
	 * (exceptions, for example). If metadata is not present in the
	 * document ('Metadata' key in catalog not present or empty) it`s
	 * must be added.
	 *
	 * @return metadata representation or null
	 * @see Metadata
	 */
	Metadata getMetadata();

	/**
	 * Return pdf document information dictionary representation.
	 * Must be not null (empty, for example).
	 *
	 * @return information dictionary representation
	 * @see InfoDictionary
	 */
	InfoDictionary getInfoDictionary();

	boolean isNeedToBeUpdated();

	/**
	 * Incremental save of pdf document. Document must be saved if and
	 * only if metadata or information dictionary of document was changed.
	 * In {@link org.verapdf.pdfa.results.MetadataFixerResultImpl} must set 1 of 3 states:
	 * <ul>
	 * <li>
	 * {@link org.verapdf.pdfa.results.MetadataFixerResult.RepairStatus#FIX_ERROR}
	 * if got problems with document save
	 * </li>
	 * <li>
	 * {@link org.verapdf.pdfa.results.MetadataFixerResult.RepairStatus#NO_ACTION}
	 * if metadata and information dictionary was not changed
	 * </li>
	 * <li>
	 * {@link org.verapdf.pdfa.results.MetadataFixerResult.RepairStatus#SUCCESS}
	 * if document save successful
	 * </li>
	 * </ul>
	 *
	 * @param status result of {@code MetadataFixerImpl} handling
	 * @param output output stream for document save
	 * @return 
	 */
	MetadataFixerResult saveDocumentIncremental(MetadataFixerResult.RepairStatus status, OutputStream output,
												PDFAFlavour flavour);

	/**
	 * Removes filters for all metadata streams in document
	 *
	 * @return number of unfiltered streams
	 */
	int removeFiltersForAllMetadataObjects();
}
