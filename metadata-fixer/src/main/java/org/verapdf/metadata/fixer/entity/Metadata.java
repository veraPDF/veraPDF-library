package org.verapdf.metadata.fixer.entity;

import org.verapdf.metadata.fixer.MetadataFixerResultImpl;
import org.verapdf.metadata.fixer.schemas.AdobePDF;
import org.verapdf.metadata.fixer.schemas.DublinCore;
import org.verapdf.metadata.fixer.schemas.XMPBasic;
import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * Current interface provide necessary behavior of pdf metadata
 * for {@link org.verapdf.metadata.fixer.MetadataFixer}
 *
 * @author Evgeniy Muravitskiy
 */
public interface Metadata {

	/**
	 * Remove filters from stream and add required fields to stream dictionary
	 *
	 * @param report report applied changes
	 */
	void checkMetadataStream(MetadataFixerResultImpl report);

	/**
	 * Remove identification schema if {@code MetadataFixer}
	 * can not repair document to valid PDF/A Document.
	 *
	 * @param result report applied changes
	 */
	void removePDFIdentificationSchema(MetadataFixerResultImpl result, PDFAFlavour flavour);

	/**
	 * Add PDF/A identification schema if {@code MetadataFixer}
	 * be able to repair document to valid PDF/A document.
	 *
	 * @param report  report applied changes
	 * @param flavour the checked flavour
	 */
	void addPDFIdentificationSchema(MetadataFixerResultImpl report, PDFAFlavour flavour);

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

	boolean isNeedToBeUpdated();

	void setNeedToBeUpdated(boolean needToBeUpdated);

	/**
	 * Update metadata stream from applied changes. If metadata was not
	 * changed than no updates applied
	 *
	 * @throws Exception problems with stream update
	 */
	void updateMetadataStream() throws Exception;

}
