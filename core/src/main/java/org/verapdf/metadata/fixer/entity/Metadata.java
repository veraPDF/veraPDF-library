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

import org.verapdf.metadata.fixer.schemas.AdobePDF;
import org.verapdf.metadata.fixer.schemas.DublinCore;
import org.verapdf.metadata.fixer.schemas.XMPBasic;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.MetadataFixerResultImpl;

/**
 * Current interface provide necessary behavior of pdf metadata
 * for {@link MetadataFixerImpl}
 *
 * @author Evgeniy Muravitskiy
 */
public interface Metadata {

	/**
	 * Add required fields to stream dictionary and add FlateDecode Filter for part 2 and 3 flavours or remove filters for part 1 flavour
	 *
	 * @param resultBuilder report applied changes
	 */
	void checkMetadataStream(MetadataFixerResultImpl.Builder resultBuilder, PDFAFlavour flavour);

	/**
	 * Remove identification schema if {@code MetadataFixerImpl}
	 * can not repair document to valid PDF/A Document.
	 *
	 * @param resultBuilder report applied changes
	 */
	void removePDFIdentificationSchema(MetadataFixerResultImpl.Builder resultBuilder, PDFAFlavour flavour);

	/**
	 * Add PDF/A identification schema if {@code MetadataFixerImpl}
	 * be able to repair document to valid PDF/A document.
	 *
	 * @param resultBuilder  report applied changes
	 * @param flavour the checked flavour
	 */
	void addPDFIdentificationSchema(MetadataFixerResultImpl.Builder resultBuilder, PDFAFlavour flavour);

	/**
	 * Get {@code DublinCore} representation. If current schema not presented
	 * in metadata but Information dictionary consist corresponding values
	 * than must return empty dublin core schema.
	 *
	 * @param info information dictionary representation of current document
	 * @return {@code DublinCore} schema or null
	 */
	DublinCore getDublinCoreSchema(InfoDictionary info);

	/**
	 * Get {@code AdobePDF} representation. If current schema not presented
	 * in metadata but Information dictionary consist corresponding values
	 * than must return empty dublin core schema.
	 *
	 * @param info information dictionary representation of current document
	 * @return {@code AdobePDF} schema or null
	 */
	AdobePDF getAdobePDFSchema(InfoDictionary info);

	/**
	 * Get {@code XMPBasic} representation. If current schema not presented
	 * in metadata but Information dictionary consist corresponding values
	 * than must return empty dublin core schema.
	 *
	 * @param info information dictionary representation of current document
	 * @return {@code XMPBasic} schema or null
	 */
	XMPBasic getXMPBasicSchema(InfoDictionary info);

	/**
	 * @return
	 */
	boolean isNeedToBeUpdated();

	/**
	 * @param needToBeUpdated
	 */
	void setNeedToBeUpdated(boolean needToBeUpdated);

	/**
	 * Update metadata stream from applied changes. If metadata was not
	 * changed than no updates applied
	 *
	 * @throws Exception problems with stream update
	 */
	void updateMetadataStream(MetadataFixerResultImpl.Builder resultBuilder, PDFAFlavour flavour) throws Exception;

}
